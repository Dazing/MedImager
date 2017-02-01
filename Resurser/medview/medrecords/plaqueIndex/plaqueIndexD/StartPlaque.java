/*
 * StartPlaque.java
 *
 * Created on den 10 februari 2003, 17:29
 *
 * $Log: StartPlaque.java,v $
 * Revision 1.7  2006/09/13 22:00:07  oloft
 * Added Open functionality
 *
 * Revision 1.6  2005/04/26 13:48:27  erichson
 * Added NumberActions for shift+(0-5).
 *
 * Revision 1.5  2005/04/26 12:57:12  erichson
 * Merging COPENHAGEN_DEVELOPMENT branch.
 *
 * Revision 1.4.2.7  2005/04/26 12:37:40  erichson
 * Fix to make the plaque input get keyboard focus properly
 *
 * Revision 1.4.2.6  2005/04/26 11:25:45  erichson
 * removed 1 leftover println
 *
 * Revision 1.4.2.5  2005/04/26 10:39:40  erichson
 * Code cleanup while finishing up
 *
 * Revision 1.4.2.4  2005/04/25 11:16:50  erichson
 * Enter key now moves forward.
 *
 * Revision 1.4.2.3  2005/04/20 15:09:34  erichson
 * Changed backspace handling (was same as delete before)
 *
 * Revision 1.4.2.2  2005/03/25 18:46:02  erichson
 * Moved keyboard handling here (from plaquePanelD).
 *
 * Revision 1.4.2.1  2005/03/25 13:35:22  erichson
 * Fixed for strange alignment discrepancy between över/underkaeben.
 *
 * Revision 1.4  2004/12/08 14:42:58  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.3  2004/01/06 21:34:01  oloft
 * Enaabled detailed term names
 *
 * Revision 1.2  2003/08/06 22:46:29  erichson
 * Just changed some variable names // NE
 *
 */

package medview.medrecords.plaqueIndex.plaqueIndexD;

import java.awt.*;
import javax.swing.*;

import java.awt.event.*;
import javax.swing.event.*;


import medview.datahandling.examination.tree.*;
/**
 * Base component of the PlaqueInput.
 * Provides a tabbedPane with Overkaebe and Underkaebe, and keyboard access to them. // NE
 *
 * @author  nader
 */
public class StartPlaque extends JTabbedPane 
{
    
    private JTabbedPane mParentTabbedPane;
        
    /** Creates a new instance of StartPlaque */
    public StartPlaque() 
    {
        super();
        // TabPanel tabPanel = new TabPanel(aCatModel,this,designable); /
        
        PlaquePanelD overPanel = new PlaquePanelD("OVERKAEBEN",false,this);
        PlaquePanelD underPanel = new PlaquePanelD("UNDERKAEBEN",true,this);                
        
        /**
         * Note:
         * For some reason the first tab that gets added, gets a misaligned layout compared to subsequent 
         * tabs. I'm not sure what causes this, but my workaround is to add a temporary tab,
         * add the real tabs, then remove the temp tab. It's silly, but it works. // NE
         */        
        
        addTab("Temp", new PlaquePanelD("Temp",true,this));                 
        addTab("OVERK\u00C6BEN",  overPanel); // second kaeben goes into tree-file
        addTab("UNDERK\u00C6BEN", underPanel); // second kaeben goes into tree-file
        remove(0); // see above // NE
                
        /*java.awt.Font aFont = this.getFont();
        java.awt.Font bFont = aFont.deriveFont(java.awt.Font.BOLD);
        java.awt.Font cFont = bFont.deriveFont(18);
        this.setFont(cFont);*/
        
        registerKeyboardActions();
        
    }
    
    /**
     * Constructor which makes StartPlaque listen to its JTabbedPane ancestor, and
     * request focus when it is changed.
     *
     * Used to keep keyboard focus in the MedRecords tabbedpane.
     */
    public StartPlaque(JTabbedPane ancestor)
    {
        this();
        mParentTabbedPane = ancestor;
        ancestor.addChangeListener(new ChangeListener() 
        {
            public void stateChanged(ChangeEvent e)
            {
                if (mParentTabbedPane.getSelectedComponent() == StartPlaque.this)
                {                    
                    // When the plaque input tab is selected, request focus to the current plaque input
                    getSelectedComponent().requestFocus();                    
                }
            }
        });
    }
    
    
    /**
     * Registers what happens when number keys are pressed
     */
    
    private class NumberAction extends AbstractAction
    {
        private final int number;
        private final String name;
        
        public NumberAction(int number)
        {
            super();
            this.number = number;            
            this.name = "NUMBERACTION_" + number;
        }
        
        public void actionPerformed(ActionEvent ae)
        {
            keyboardInput(number);
        }
        
        public String getName() { return name; }        
        
    }
    
    /**
     * Called when inputting a number via the keyboard. // NE
     */
    private void keyboardInput(int number)
    {
        // Get the currently visible tab
        PlaquePanelD p = getActivePlaquePanel();
        if (p != null)
            p.keyboardInput(number);
        
        else
            JOptionPane.showConfirmDialog(this,"Error: No active panel!", "Error", JOptionPane.ERROR_MESSAGE);        
        
    }
    
    /**
     * Move cursor backward
     */
    private void moveBackward()
    {        
        PlaquePanelD p = getActivePlaquePanel();
        if (p != null)
            p.moveCursorBackward();        
        else
            JOptionPane.showConfirmDialog(this,"Error: No active panel!", "Error", JOptionPane.ERROR_MESSAGE);                
    }
    
    
    /**
     * Move cursor forward
     */
    private void moveForward()
    {        
        PlaquePanelD p = getActivePlaquePanel();
        if (p != null)
            p.moveCursor();        
        else
            JOptionPane.showConfirmDialog(this,"Error: No active panel!", "Error", JOptionPane.ERROR_MESSAGE);                
    }
    
    private PlaquePanelD getActivePlaquePanel()
    {
        Component c = getSelectedComponent();
        if (c instanceof PlaquePanelD)
        {
            PlaquePanelD p = (PlaquePanelD) c;
            return p;
        }            
        else
            return null;
    }
    
    private void registerKeyboardActions()
    {
        
        ActionMap actionMap = getActionMap();
        InputMap inputMap = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);        
                        
        NumberAction[] numberActions = new NumberAction[16];
        for (int i = 0; i < 16; i++)
        {
            numberActions[i] = new NumberAction(i);
            actionMap.put(numberActions[i].getName(), numberActions[i]);
            
            String keyString;
            
            if (i < 10)
                keyString = new Integer(i).toString();
            else
                keyString = "shift " + new Integer(i-10);
                                           
            inputMap.put(KeyStroke.getKeyStroke(keyString), numberActions[i].getName());                                        
        }
                       
        
        /** 
         * DEL key: Delete and move forward
         */
        AbstractAction deleteKeyAction = new AbstractAction() {
            public void actionPerformed(ActionEvent ae)
            {
                StartPlaque.this.keyboardInput(-1); // This seems to be how to do an "empty" value... since the cellRenderer renders -1 as empty... // NE
            }
        };
        
        String deleteKeyActionName = "DELETE_KEY_ACTION";
        actionMap.put(deleteKeyActionName, deleteKeyAction);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,0), deleteKeyActionName);
        
        /**
         * BACKSPACE key: Delete and move backward
         */
        
        AbstractAction backspaceAction = new AbstractAction() 
        {
            public void actionPerformed(ActionEvent ae)
            {
                StartPlaque.this.moveBackward();
            }
        };
        
        String backspaceActionName = "BACKSPACE_ACTION";
        actionMap.put(backspaceActionName, backspaceAction);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE,0), backspaceActionName);
        
        /**
         * ENTER key: move forward.
         * Note: Yes, we need to customize this both here, and in the cell editor in ThreeColTable, 
         * since there is a default action in the cell editor.
         */
        AbstractAction enterAction = new AbstractAction() 
        {
            public void actionPerformed(ActionEvent ae)
            {                
                StartPlaque.this.moveForward();
            }
        };
        
        String enterActionName = "ENTER_ACTION";
        actionMap.put(enterActionName, enterAction);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0), enterActionName);
        
        
        /* Set up the InputMap */                        
        
        // Number pad keys
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD0,0), numberActions[0].getName());
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD1,0), numberActions[1].getName());
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD2,0), numberActions[2].getName());
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD3,0), numberActions[3].getName());
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD4,0), numberActions[4].getName());
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD5,0), numberActions[5].getName());
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD6,0), numberActions[6].getName());
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD7,0), numberActions[7].getName());
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD8,0), numberActions[8].getName());
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD9,0), numberActions[9].getName());                                
    }
        
    /**
     * Get Tree for this component 
     */
    public MRTree buildTree(){
        MRTree plaqueTree = new MRTree("PlaqueIndex");
        for(int i = 0; i < this.getComponentCount(); i++){
            Component component = getComponent(i);
            if (component instanceof PlaquePanelD) {
                PlaquePanelD plaquePanel = (PlaquePanelD) component;
                MRTree subTree = plaquePanel.buildTree(new PlaqueContext()); 
                plaqueTree.addChild(subTree);
            }
        }
        return plaqueTree;
    }
	
	/**
		* Load Tree for this component 
     */
    public void loadTree(Tree t){
        for(int i = 0; i < this.getComponentCount(); i++){
            Component component = getComponent(i);
            if (component instanceof PlaquePanelD) {
                PlaquePanelD plaquePanel = (PlaquePanelD) component;
                plaquePanel.loadTree(t, new PlaqueContext()); 
            }
        }
    }        
	
}
