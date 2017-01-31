/*
 * $Id: TabPanel.java,v 1.1 2003/11/11 00:18:20 oloft Exp $
 *
 * Created on June 21, 2001, 8:07 PM
 *
 * Contains editables.
 *
 */

package medview.formeditor.components;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import medview.formeditor.exceptions.*;
import medview.formeditor.interfaces.*;
import medview.formeditor.models.*;
import medview.formeditor.tools.*;



public class TabPanel extends JPanel implements FocusListener, ChangeListener, ActionListener {
    
    private static final boolean debug = true;
    
    private CategoryModel model;
    private ValueInputComponent selectedInput;
    private Vector inputComponents;
    // private Box box;
    private ValueTabbedPane parentTabbedPane;
    
    //private ChangeListener changeListener;
    private Vector changeListeners;
    private Vector actionListeners;
    
    private boolean designable;
    private JScrollPane scrollPane;
    
    /** Creates new TabPanel */
    public TabPanel(CategoryModel catModel, ValueTabbedPane in_parentTabbedPane,boolean pDesignable ) {
        //super(BoxLayout.Y_AXIS);
        super();
        
        setRequestFocusEnabled(false);
        
        inputComponents = new Vector();
        designable = pDesignable;
        parentTabbedPane = in_parentTabbedPane;
        
        this.setLayout(new BorderLayout());
        
        setModel(catModel);
        resetSelectedInput();
        
        // setBorder(BorderFactory.createEtchedBorder());                
    }
    public TabPanel(CategoryModel catModel, ValueTabbedPane in_parentTabbedPane ) {
        this(catModel, in_parentTabbedPane,false);
    }
    
    public void addActionListener(ActionListener newListener) {
        if (actionListeners == null) {
            actionListeners = new Vector();
        }
        
        if (! actionListeners.contains(newListener)) {
            actionListeners.add(newListener);
        }
    }
    
    public void addChangeListener(ChangeListener newListener) {
        if (changeListeners == null) {
            changeListeners = new Vector();
        }
        
        if (! changeListeners.contains(newListener)) {
            changeListeners.add(newListener);
        }
        
        //changeListener = newListener;
    }
    
    public void resetAllComponents() {
        ValueInputComponent[] inputs = getInputComponents();
        for (int i = 0; i < inputs.length; i++) {
            inputs[i].resetContents();
        }
    }
    
    
    /**
     * Fire an action event (presumably to the parent ValueTabbedPane of this TabPanel)
     */
    public void fireActionEvent(ActionEvent ev) {
        // Propagate ev to ALL actionlisteners!
        for (Iterator it = actionListeners.iterator(); it.hasNext();) {
            ActionListener nextListener = (ActionListener) it.next();
            nextListener.actionPerformed(ev);
        }
    }
    
    public void fireStateChanged() {
        
        if (changeListeners == null) {
            //System.err.println("Changelisteners were null, not firing...");
        } else if (changeListeners.size() == 0) {
            //System.err.println("Zero changelisteners, not firing...");
        } else {
            
            //System.out.println("TabPanel Firing stateChanged to [" + changeListeners.size() + " changeListeners] => want listPanel to update.");
            
            for (Iterator it = changeListeners.iterator(); it.hasNext();) {
                ChangeListener nextListener = (ChangeListener) it.next();
                
                nextListener.stateChanged(new ChangeEvent(this));
                
            }
        }
    }
    
    /**
     * Method for handling when one of the NamedTextFields gain focus
     */
    public void focusGained(FocusEvent e) {
        
        
        Object objSource = e.getSource();
        
        //System.out.println("focus gained by " + objSource);
        
        if (objSource instanceof ValueInputComponent ) {
            ValueInputComponent sourceInput = (ValueInputComponent) objSource;
            
            if (sourceInput != selectedInput) {
                // We switched the focus to another input component
                
                String inpName;
                if (sourceInput == null) inpName = "null";
                else inpName = sourceInput.getInputModel().getName();
                
                //System.out.println("Setting selected input to " + inpName);
                
                setSelectedInputComponent(sourceInput);
            }
            
            // if it is the same field as before, do nothing
            
        } else {
            System.err.println("Serious bug: objSource in focusGained not ValueInputComponent!");
        }
    }
    
    public void focusLost(FocusEvent e) {}
    
    public ValueInputComponent[] getInputComponents() {
        
        ValueInputComponent[] inputs = new ValueInputComponent[inputComponents.size()];
        inputs = (ValueInputComponent[]) inputComponents.toArray(inputs);
        return inputs;
    }
    
    public ValueInputComponent getFirstInputComponent() {
        ValueInputComponent[] components = getInputComponents();
        if (components.length <= 0)
            return null;
        else
            return components[0];
        
    }
    
    public ValueInputComponent getNextInputComponent(ValueInputComponent vic) {
        //return getInputAfter(getSelectedInputComponent());
        return getInputAfter(vic);
    }
    
    public ValueTabbedPane getParentPane() {
        return parentTabbedPane;
    }
    
    public ValueInputComponent getInputAfter(ValueInputComponent input) {
        
        if (input == null) {
            //System.out.println("MEAN ERROR: TabPanel.getInputAfter(null)! Returning null!");
            return null;
        } else {
            // Find location of the current inputcomponent in the vector
            int index = inputComponents.indexOf(input);
            
            if ((index < 0) || (index >= (inputComponents.size()-1) )) { // Last element or worse
                // System.err.println("TabPanel.getInputAfter: no such area - nr [" + index + "] - [" + input.getInputModel().getName() +"] in vector!");
            } else {
                // System.out.println("Sought area " + input + " gave index " + index);
                ValueInputComponent nextUp = (ValueInputComponent) inputComponents.get(index + 1);
                // System.out.println("So the next one is " + (index + 1) + " ("+nextUp+")");
                return nextUp;
            }
            
            return null; // none found
        }
    }
    
    public ValueInputComponent getSelectedInputComponent() {
       // Ut.prt("Tab panel try to find selected");
        return selectedInput;
    }
    public void scrollToSelectedComponent() {
        
        if(! (selectedInput instanceof NamedTextArea) ) return;
        
        NamedTextArea   slctInput   = (NamedTextArea)selectedInput;
        JPanel          slctComp    = slctInput.getParentPanel();
        scrollToPanel(slctComp);
        
    }
    
    public void scrollToPanel(JPanel slctComp) {
        
        JViewport       viewport    = scrollPane.getViewport();// the visible view
        Rectangle       vwRect      = viewport.getBounds();
     /*
        int cX  = slctComp.getLocation().x + slctComp.getWidth() / 2; //the center of the slctComponent
        int cY  = slctComp.getLocation().y + slctComp.getHeight() / 2;
      
        int x   = cX - vwRect.width / 2;  // x,y of a rect as large as vwRect with the center of (cX,cY)
        int y   = cY - vwRect.height / 2;
      */
        int cX  = 0;
        int cY  = 0;
        
        if(slctComp != null){
            cX  = slctComp.getLocation().x ;
            cY  = slctComp.getLocation().y ;
        }
        
        int x   = cX;
        int y   = cY;
        
        x       = (x < 0? 0: x);
        y       = (y < 0? 0: y);
        
       // Ut.prt("Scrol to X =" + x + "  Y = " + y);
        
        vwRect.x = x;
        vwRect.y = y;
        
        JComponent aP = (JComponent)viewport.getView();
        aP.scrollRectToVisible(vwRect);
        if(slctComp != null && slctComp instanceof InputContainerPanel)
            ( (InputContainerPanel)slctComp).selectComponent();
        
    }
    
    // Rebuild all components from the models
    private void rebuild() {
        JPanel              fieldPanel  = new JPanel();
        GridBagLayout       gbLayout    = new GridBagLayout(); // Correct, but centers too much
        InputModel[]        inputs      = model.getInputs();
        int                 aLen        = inputs.length;
        inputComponents                 = new Vector();
        
        removeAll();           // Clear out the panel
        setName(model.getTitle());
        // if (model.getTitle().
        
        fieldPanel.setAlignmentX(0); // Align to the left
        fieldPanel.setAlignmentY(0);
        fieldPanel.setLayout(gbLayout);
        
        GridBagConstraints layOutCstrn    = new GridBagConstraints();
        layOutCstrn.anchor                = GridBagConstraints.WEST;
        layOutCstrn.gridheight            = 1 ;
        layOutCstrn.gridwidth             = GridBagConstraints.REMAINDER;
        layOutCstrn.fill                  = GridBagConstraints.HORIZONTAL;// BOTH;
        layOutCstrn.weightx               = 1;
        layOutCstrn.weighty               = 0;
        layOutCstrn.gridx                 = 0;
        
        for (int i = 0; i < aLen; i++) {
            JPanel      containerPanel  = null;
            InputModel  inputModel      = inputs[i];
            layOutCstrn.gridy           = i;
            
            if (inputModel instanceof CreatorFieldModel) {
                containerPanel = addCreatorComponent(inputModel);
            }
            else if (inputModel instanceof FieldModel) {
                containerPanel = addInputComponent(inputModel);
            }
            /* else if (inputModel instanceof PictureChooserModel) {
                containerPanel = addPictureComponent(inputModel);
            } No Pictures here OT 03-11-09 */
            else {
                JOptionPane.showMessageDialog(this,"Error: unrecognized class in TabPanel rebuild()!",
                "TabPanel.rebuild()",JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(i == aLen -1) layOutCstrn.gridheight = GridBagConstraints.RELATIVE;
            gbLayout.setConstraints(containerPanel,layOutCstrn);
            fieldPanel.add(containerPanel);
        }
        
        JLabel aFillRest = new JLabel();
        aFillRest.setAlignmentY(0);
        
        layOutCstrn.fill        = GridBagConstraints.HORIZONTAL;
        layOutCstrn.weighty     = 1;
        layOutCstrn.gridheight  = GridBagConstraints.REMAINDER;
        layOutCstrn.gridy       = aLen;
        
        gbLayout.setConstraints(aFillRest,layOutCstrn);
        fieldPanel.add(aFillRest);
        
        scrollPane = new  JScrollPane(fieldPanel);
        //scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        // scrollPane.getVerticalScrollBar().getCsetValue(5);
        scrollPane.getVerticalScrollBar().setUnitIncrement(15);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(5);
        this.add(scrollPane,BorderLayout.CENTER);
        selectedInput = null;
        validate();
    }

    /* there are no images here - =T 03-11-09
    private JPanel addPictureComponent( InputModel inputModel){
        PictureChooserModel picChooserModel = (PictureChooserModel) inputModel;
        PictureChooserInput picInput = new PictureChooserInput(picChooserModel,this);
        
        picInput.addActionListener(this);
        ValueInputComponent inPutComponent = picInput;
        
        JPanel containerPanel = new InputContainerPanel(inPutComponent);
        ((InputContainerPanel)containerPanel).setTabPanel(this);
        
        inputComponents.add(inPutComponent);
        parentTabbedPane.setPhotoPanelModel(picChooserModel);
        return containerPanel;
    }*/
    
    private JPanel addInputComponent( InputModel inputModel){
        FieldModel fieldModel = (FieldModel) inputModel;
        NamedTextArea textArea = new NamedTextArea(fieldModel,this); // This tabpanel is the parent
        
        textArea.addFocusListener(this);
        if(! fieldModel.isEditable()) textArea.setEditable(false);// setEnabled(false); // nader 16/5
        
        ValueInputComponent inPutComponent = textArea;
        JPanel containerPanel = new InputContainerPanel(inPutComponent);
        ((InputContainerPanel)containerPanel).setTabPanel(this);
        
        inputComponents.add(inPutComponent);
        return containerPanel;
    }
    
    private JPanel addCreatorComponent( InputModel inputModel){
        CreatorFieldModel fieldModel    = (CreatorFieldModel) inputModel;
        CreatorNamedTextArea textArea   = new CreatorNamedTextArea(fieldModel,this);
        textArea.addFocusListener(this);
        
        ValueInputComponent inPutComponent = textArea;
        JPanel containerPanel = new CreatorContainerPanel(inPutComponent);
        
        if(fieldModel.getFieldType()== FieldModel.TYPE_IDENTIFICATION){
            CreatorContainerPanel contPan = (CreatorContainerPanel)containerPanel;
            contPan.setIdentificationState(true);
        }
        else if(fieldModel.getFieldType()== FieldModel.TYPE_NOTE){
            CreatorContainerPanel contPan = (CreatorContainerPanel)containerPanel;
            contPan.setNoteState(true);
        }
        
        ((CreatorContainerPanel)containerPanel).setTabPanel(this);
        inputComponents.add(inPutComponent);
        return containerPanel;
    }
    
    public void resetSelectedInput() {
        
        // Call to setSelectedInputComponent will cause event to fire
        setSelectedInputComponent(getFirstInputComponent());
    }
    
    /*
     * Check to see if this is a photo tah
     */
    public boolean isPhotoTab(){
        /*
         There is no Photo Tab - OT 03-11-10
        // System.out.println("call is photo");
        if(model == null) return false;
        InputModel[] inputs = model.getInputs();
        
        if(inputs == null) return false;
        if(inputs.length < 1) return false;
        if(inputs[0] == null) return false;
        InputModel inputModel = inputs[0];
        
        if (inputModel instanceof PictureChooserModel) {
            // System.out.println("this is a pictuere");
            ValueInputComponent aCom = this.getSelectedInputComponent();
            if (aCom instanceof PictureChooserInput) {
                ((PictureChooserInput)aCom).viewPicturePath();
            }
            return true;
        }
         */
        return false;
        
    }
    
    public void setDesignable(boolean in_designable) {
        designable = in_designable;
        rebuild();
    }
    
    public void setSelectedInputComponent(ValueInputComponent input) {
        System.out.println("TabPanel Setting selected input!");
        selectedInput = input;
        // Notify the main frame, which will switch the valuepanel to the correct field.
        fireStateChanged();
        //focusSelectedInput();        
    }
    
    public void focusSelectedInput() {
        //ValueInputComponent componentToFocus = aTabPan.getFirstInputComponent();
        ValueInputComponent componentToFocus = getSelectedInputComponent();
        if (debug)
            System.out.println("requesting focus for component " + componentToFocus.getInputModel().getName());                                
        componentToFocus.requestFocus();
    }
    
    public CategoryModel getModel() {
        return model;
    }
    
    public void setModel(CategoryModel in_tabModel) {
        // Remove listeners from old model
        
        if (model != null) {
            model.removeChangeListener(this);
        }
        
        model = in_tabModel;
        rebuild();
        
        model.addChangeListener(this);
    }
    
    public void stateChanged(javax.swing.event.ChangeEvent evt) {
        Object source = evt.getSource();
        
        if (source instanceof CategoryModel) {
            rebuild(); // Rebuild GUI if category definition changes
        }
    }
    
    /*
     * The TabPanel listens to its input components for choices.
     * For example, PictureChooserInputs fire ActionEvents when pictures are chosen.
     */
    public void actionPerformed(java.awt.event.ActionEvent e) {
        //System.out.println("TabPanel got an actionPerformed!");
        Object source = e.getSource();

        /*
         There are no images - OT-03-11-10
        if (source instanceof PictureChooserInput) {
            //System.out.println("From a picturechooserinput!");
            parentTabbedPane.addPhotoPanelImage(e.getActionCommand()); // The action command contains the path to the image to be added
        }
         */
    }
    
    public void clearValues(){
        for (Iterator it = inputComponents.iterator(); it.hasNext();) {
            ValueInputComponent nextChooser = (ValueInputComponent) it.next();
            if(nextChooser instanceof NamedTextArea){
                NamedTextArea nt = (NamedTextArea) nextChooser;
                nt.setText("");
            }
        }
    }
    /**
     * Checks the input values for all ValueInputComponents in this tab. // NE
     * Returns normally if they are ok, otherwise throws ValueInputException
     * @throws ValueInputException if one or more input values is incorrect
     */ 
    public void checkInputValues() throws ValueInputException {
        for (Iterator it = inputComponents.iterator(); it.hasNext();) {
            ValueInputComponent nextChooser = (ValueInputComponent) it.next();
            if(nextChooser instanceof NamedTextArea){
                NamedTextArea textArea = (NamedTextArea) nextChooser;
                try {
                    textArea.verifyInput();
                } catch (ValueInputException vie) {
                    throw new ValueInputException("Value in input field " + nextChooser.getInputModel().getName() + " (in tabPanel " + this.getModel().getTitle() +") is incorrect: " + vie.getMessage(),nextChooser,this);
                }
                
                    
            }
        }
    }
        //return null;
    
    
    /*
     * Call from CreatorContainerPanel to avoid creation of dubel id field.
     */
    public boolean checkIdType(NamedTextArea aTxtAra){
        if(hasIdType(aTxtAra)) return false;
        return !parentTabbedPane.hasIdType(this);
    }
    /*
     * Call from CreatorContainerPanel to avoid creation of dubel id field.
     */
    public boolean hasIdType(NamedTextArea aTxtAra){
        for (Iterator it = inputComponents.iterator(); it.hasNext();) {
            ValueInputComponent nextChooser = (ValueInputComponent) it.next();
            if(nextChooser instanceof NamedTextArea){
                NamedTextArea nt = (NamedTextArea) nextChooser;
                if(nt != aTxtAra){
                    if(nt.getFieldModel().getFieldType() == FieldModel.TYPE_IDENTIFICATION)
                        return true;
                }
            }
        }
        return false;
    }
    
    
    
    
    
    
    
    
}
