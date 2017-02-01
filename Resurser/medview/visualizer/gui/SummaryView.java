/*
 * SummaryView.java
 *
 * Created on November 4, 2002, 9:26 AM
 *
 * $Id: SummaryView.java,v 1.15 2005/06/30 10:40:19 erichson Exp $
 *
 * $Log: SummaryView.java,v $
 * Revision 1.15  2005/06/30 10:40:19  erichson
 * Summary generation uses MedViewGeneratorEngineBuilder.
 *
 * Revision 1.14  2005/06/09 08:56:21  erichson
 * Updated to interface with the new se.chalmers.docgen package.
 *
 * Revision 1.13  2005/01/26 13:13:46  erichson
 * Fixed junk at end of file
 *
 * Revision 1.11  2004/11/16 10:25:21  erichson
 * Added revalidate since summary lagged one click "behind"
 *
 * Revision 1.10  2004/11/13 10:59:09  erichson
 * Thread naming
 *
 * Revision 1.9  2002/12/19 09:51:24  zachrisg
 * Examinations are now sorted by date.
 *
 * Revision 1.8  2002/12/16 11:12:07  zachrisg
 * Removed the tabs.
 *
 * Revision 1.7  2002/12/06 15:12:10  zachrisg
 * Moved summary generation to a new thread.
 *
 * Revision 1.6  2002/11/28 13:20:19  zachrisg
 * Removed bug that throwed an exception when the template was changed.
 *
 * Revision 1.5  2002/11/26 13:16:21  zachrisg
 * Now uses new template and translator handling.
 *
 * Revision 1.4  2002/11/18 16:02:25  zachrisg
 * Improved performance a bit and allowed for multiple selection and selection of
 * patients.
 *
 * Revision 1.3  2002/11/18 15:15:09  zachrisg
 * It is now possible to completely remove the statusbar.
 *
 * Revision 1.2  2002/11/14 16:01:03  zachrisg
 * Added support for session loading/saving of aggregations.
 *
 * Revision 1.1  2002/11/06 09:25:42  zachrisg
 * First check in.
 *
 */

package medview.visualizer.gui;

import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.tree.*;

import misc.foundation.*;
import misc.foundation.io.*;

import medview.common.generator.*;
/*import medview.common.template.*;
import medview.common.translator.*;*/
import medview.datahandling.*;
import medview.datahandling.aggregation.*;
import medview.datahandling.examination.*;
import medview.visualizer.data.*;
import medview.visualizer.dnd.*;
import medview.visualizer.event.*;

import se.chalmers.cs.medview.docgen.*;
import se.chalmers.cs.medview.docgen.template.*;
import se.chalmers.cs.medview.docgen.translator.*;


/**
 * A View that displays an examination summary.
 * 
 * @author Göran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public class SummaryView extends View implements TreeSelectionListener
    , TreeExpansionListener, TemplateChangeListener, TranslatorChangeListener {
    
    /** The patient-examination tree. */
    private JTree patientExaminationTree;
    
    /** The summary panel that contains the summary textpane. */
    private JPanel summaryPanel;

    /** The generator thread. */
    private GeneratorThread generatorThread = null;
    
    /** 
     * Creates a new instance of SummaryView.
     *
     * @param dataSet The data set.
     */
    public SummaryView(ExaminationDataSet dataSet) {
        super(dataSet, false, false);
      
        // add template and translator listeners
        ApplicationManager.getInstance().addTemplateChangeListener(this);
        ApplicationManager.getInstance().addTranslatorChangeListener(this);
        
        // create the patient-examination tree
        DefaultMutableTreeNode topNode = createPatientExaminationNodes();
        patientExaminationTree = new JTree(topNode);
        patientExaminationTree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
        patientExaminationTree.setShowsRootHandles(true);
        patientExaminationTree.setRootVisible(false);
        patientExaminationTree.addTreeSelectionListener(this);
        patientExaminationTree.addTreeExpansionListener(this);
        patientExaminationTree.setTransferHandler(null);
        
        // create the summary textpane
        JComponent summaryComponent = createSummaryComponent();
        
        // add the summary textpane to a panel
        summaryPanel = new JPanel(new BorderLayout());
        summaryPanel.add(summaryComponent, BorderLayout.CENTER);
        
        // create the main panel and add the components
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(patientExaminationTree), BorderLayout.WEST);
        panel.add(summaryPanel, BorderLayout.CENTER);
        panel.setTransferHandler(new ViewTransferHandler(this));
        
        // add the main panel to the view
        setViewComponent(panel);
    }
    
    /**
     * Creates the tree nodes with patients and examinations.
     *
     * @return The topnode of the tree component.
     */
    private DefaultMutableTreeNode createPatientExaminationNodes() {
        DefaultMutableTreeNode topNode = new DefaultMutableTreeNode();
        DefaultMutableTreeNode patientNode;
        DefaultMutableTreeNode examinationNode;

        Hashtable examinationNodeObjectSetTable = new Hashtable();
        
        ExaminationDataElement[] elements = getExaminationDataSet().getElements();
        for (int i = 0; i < elements.length; i++) {
            String[] ids;
            try 
            {
                ids = elements[i].getValues(DataManager.getInstance().getPatientIdentifierTerm(), null);
            } 
            catch (medview.datahandling.NoSuchTermException exc)
            {
                // this just shouldn't happen!
                ids = new String[0];
            }
            
            if (ids.length > 0) {
                Vector examinationNodeObjectVector = (Vector) examinationNodeObjectSetTable.get(ids[0]);
                if (examinationNodeObjectVector == null) {
                    examinationNodeObjectVector = new Vector();
                    examinationNodeObjectSetTable.put(ids[0], examinationNodeObjectVector);
                }
                try {
                    ExaminationIdentifier id = elements[i].getExaminationIdentifier();
                    ExaminationValueContainer evc = elements[i].getExaminationValueContainer();

                    // make sure the examinations get sorted by date
                    int index = 0;
                    boolean examAlreadyExists = false;
                    examLoop:for (; index < examinationNodeObjectVector.size();) {
                        ExaminationNodeObject nodeObj = (ExaminationNodeObject) examinationNodeObjectVector.get(index);
                        ExaminationIdentifier nodeId = nodeObj.getExaminationIdentifier();
                        if (nodeId.equals(id)) {
                            examAlreadyExists = true;
                            break examLoop;
                        }
                        if (nodeId.getTime().after(id.getTime())) {
                            break examLoop;
                        }
                        index++;
                    }
                    
                    if (!examAlreadyExists) {
                        ExaminationNodeObject obj = new ExaminationNodeObject(id, evc);
                        examinationNodeObjectVector.add(index, obj);
                    }
                } catch (IOException exc) {
                    // do nothing
                }

            }            
        }
        
        for (Enumeration e = examinationNodeObjectSetTable.keys(); e.hasMoreElements(); ) {
            String id = (String) e.nextElement();
            
            patientNode = new DefaultMutableTreeNode(new PatientNodeObject(id));
            
            Vector examinationNodeObjectVector = (Vector) examinationNodeObjectSetTable.get(id);
            
            for (Iterator i = examinationNodeObjectVector.iterator(); i.hasNext(); ) {
                ExaminationNodeObject nodeObject = (ExaminationNodeObject) i.next();
                examinationNode = new DefaultMutableTreeNode(nodeObject);
                
                // add the examination node
                patientNode.add(examinationNode);
            }
            
            // add the patient node
            topNode.insert(patientNode, getSuitableIndexForChild(topNode, patientNode));
        }
        
        return topNode;
    }

    /**
     * Creates the summary textpane.
     *
     * @return The summary textpane.
     */
    private JComponent createSummaryComponent() {               
        ApplicationManager.getInstance().setStatusBarText("Loading template...");
        TemplateModel templateModel = ApplicationManager.getInstance().getTemplate();
        ApplicationManager.getInstance().setStatusBarText("Loading translator...");
        TranslatorModel translatorModel = ApplicationManager.getInstance().getTranslator();
        
        boolean validTemplate = (templateModel != null);
        boolean validTranslator = (translatorModel != null);
        
        // take care of invalid translators or templates
        if ((!validTemplate) && (!validTranslator)) {
            ApplicationManager.getInstance().setStatusBarText("Please choose a valid template and translator in the settings menu!");
            JLabel errorLabel = new JLabel("Please choose a valid template and translator in the settings menu!", JLabel.CENTER);            
            return errorLabel;
        } else if ((!validTemplate) && validTranslator) {
            ApplicationManager.getInstance().setStatusBarText("Please choose a valid template in the settings menu!");
            JLabel errorLabel = new JLabel("Please choose a valid template in the settings menu!", JLabel.CENTER);
            return errorLabel;
        } else if (validTemplate && (!validTranslator)) {
            ApplicationManager.getInstance().setStatusBarText("Please choose a valid translator in the settings menu!");
            JLabel errorLabel = new JLabel("Please choose a valid translator in the settings menu!", JLabel.CENTER);
            return errorLabel;
        } 
        JLabel label = new JLabel("Choose a patient or examination in the list to generate a summary.", JLabel.CENTER);
        ApplicationManager.getInstance().setStatusBarText("Finished.");
        return label;
    }    
    
    /**
     * A patient node object.
     */
    private class PatientNodeObject {
        
        private String id;
        
        public PatientNodeObject(String id) {
            this.id = id;
        }
        
        public String toString() {
            return id;
        }
    }
    
    /**
     * An examination node.
     */    
    private class ExaminationNodeObject {
        
        private ExaminationIdentifier identifier;
        private ExaminationValueContainer evc;
        
        public ExaminationNodeObject(ExaminationIdentifier identifier, ExaminationValueContainer evc) {
            this.identifier = identifier;
            this.evc = evc;
        }

        public ExaminationValueContainer getExaminationValueContainer() {
            return evc;
        }
        
        public ExaminationIdentifier getExaminationIdentifier() {
            return identifier;
        }
        
        public boolean equals(Object otherObject) {
            if (otherObject instanceof ExaminationNodeObject) {
                return identifier.equals(((ExaminationNodeObject)otherObject).getExaminationIdentifier());
            } else {
                return false;
            }
        }
        
        public String toString() {
            return identifier.getTime().toString();
        }
    }
    
    /**
     * Returns an index suitable for insertion of a node based on sorting of the toString().
     *
     * @param parent The node that the child should be inserted into
     * @param newChild The child that should be inserted
     * @return A suitable index for the child to be inserted at.
     */
    private int getSuitableIndexForChild(DefaultMutableTreeNode parent, DefaultMutableTreeNode newChild) {
        int index = 0;
        
        for (int i = 0; i < parent.getChildCount(); i++) {
            if (newChild.toString().compareTo(parent.getChildAt(i).toString()) <= 0) {
                return index;
            } else {
                index++;
            }
        }

        return index;        
    }    
    
    /** 
     * Returns the name of the type of view.
     *
     * @return The view type name.
     */
    protected String getViewName() {
        return "Summary";
    }
    
    /** 
     * Marks the view as invalid.
     * Called when an element has been selected or deselected.
     */
    protected void invalidateView() {
    }

    /**
     * Called after a change in data has been made and checks if the selection for this view has changed.
     * If so is the case then recreate internal data structures and repaint the view.
     */    
    public void validateView() {
        super.validateView();
        patientExaminationTree.setModel(new DefaultTreeModel(createPatientExaminationNodes()));
    }
        
    /** 
     * Updates everything that needs to know which terms exist.
     */
    public void updateTermChoosers(boolean chosenTermsChanged, boolean allTermsChanged)
    {
        // Do nothing
    }
    
    /**
     * Invoked when a node in the patient-examination tree has been expanded.
     * Make sure that the tree gets resized.
     *
     * @param event The event object.
     */
    public void treeExpanded(TreeExpansionEvent event) {
        validate();
        repaint();
    }
    
    /**
     * Invoked when a node in the patient-examination tree has been collapsed.
     * Make sure that the tree gets resized.
     *
     * @param event The event object.
     */    
    public void treeCollapsed(TreeExpansionEvent event) {
        validate();
        repaint();
    }
    
    /**
     * Invoked when a node in the patient-examination tree has been selected.
     * Diplay the summary of the selected examination.
     *
     * @param event The event object.
     */
    public void valueChanged(TreeSelectionEvent event) 
    {
        // System.out.println("TreeSelectionEvent: " + event.toString());
        startGeneratorThread();
    }

    private synchronized void startGeneratorThread() {
        if (generatorThread == null) {
            generatorThread = new GeneratorThread();
            generatorThread.start();
        }
    }
    
    private class GeneratorThread extends Thread 
    {
        public GeneratorThread()
        {
            super("SummaryView-GeneratorThread");
        }
        
        public void run() {
            patientExaminationTree.setEnabled(false);
            
            TreePath treePaths[] = patientExaminationTree.getSelectionPaths();
            
            if ((treePaths == null || (treePaths.length == 0))) {
                patientExaminationTree.setEnabled(true);
                generatorThread = null;
                return;
            }
                        
            // get all selected examination node objects
            LinkedHashSet nodeObjectSet = new LinkedHashSet();
            for (int tp = 0; tp < treePaths.length; tp++) {
                DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) treePaths[tp].getLastPathComponent();
                Object object = treeNode.getUserObject();
                if (object instanceof ExaminationNodeObject) {
                    nodeObjectSet.add(object);
                } else if (object instanceof PatientNodeObject) {
                    for (Enumeration e = treeNode.children(); e.hasMoreElements(); ) {
                        DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) e.nextElement();
                        Object childObject = childNode.getUserObject();
                        if (childObject instanceof ExaminationNodeObject) {
                            nodeObjectSet.add(childObject);
                        }
                    }
                }
            }
            TemplateModel templateModel = ApplicationManager.getInstance().getTemplate();
            TranslatorModel translatorModel = ApplicationManager.getInstance().getTranslator();
            
            if ( (templateModel == null) && (translatorModel == null) ) {
                ApplicationManager.getInstance().setStatusBarText("Please choose a valid template and translator in the settings menu!");
                patientExaminationTree.setEnabled(true);
                generatorThread = null;
                return;
            } else if ( (templateModel == null) && (translatorModel != null) ) {
                ApplicationManager.getInstance().setStatusBarText("Please choose a valid template in the settings menu!");
                patientExaminationTree.setEnabled(true);
                generatorThread = null;
                return;
            } else if ( (templateModel != null) && (translatorModel == null) ) {
                ApplicationManager.getInstance().setStatusBarText("Please choose a valid translator in the settings menu!");
                patientExaminationTree.setEnabled(true);
                generatorThread = null;
                return;
            }
            
            ExaminationNodeObject[] nodeObjects = (ExaminationNodeObject[]) nodeObjectSet.toArray(new ExaminationNodeObject[nodeObjectSet.size()]);
            
            ValueContainer[] containers = new ValueContainer[nodeObjects.length];
            ExaminationIdentifier[] ids = new ExaminationIdentifier[nodeObjects.length];
            for (int i = 0; i < nodeObjects.length; i++) {
                containers[i] = MedViewGeneratorUtilities.wrapExaminationValueContainer(nodeObjects[i].getExaminationValueContainer());
                ids[i] = nodeObjects[i].getExaminationIdentifier();
            }
            
            String[] sections = templateModel.getAllContainedSections();
            
            ApplicationManager.getInstance().setStatusBarText("Generating summary...");
            
            JComponent summaryComponent;

            MedViewGeneratorEngineBuilder engineBuilder = new MedViewGeneratorEngineBuilder();
                
            try {
                engineBuilder.buildIdentifiers( ids );
                engineBuilder.buildValueContainers( containers );
                engineBuilder.buildTemplateModel( templateModel );
                engineBuilder.buildTranslatorModel( translatorModel );
                engineBuilder.buildSections(sections);
                
                GeneratorEngine engine = engineBuilder.getEngine();
                
                
                StyledDocument document = engine.generateDocument();
                    summaryComponent = new JTextPane(document);
                ((JTextPane)summaryComponent).setEditable(false);
                
                ApplicationManager.getInstance().setStatusBarText("Finished summary.");
            /* } catch (CouldNotBuildEngineException exc) {
                ApplicationManager.getInstance().setStatusBarText("Summary generation failed: Could not build generator engine!");
                summaryComponent = new JLabel("Could not generate summary.", JLabel.CENTER); */
            } catch (FurtherElementsRequiredException exc) {
                ApplicationManager.getInstance().setStatusBarText("Summary generation failed: Further elements required!");
                summaryComponent = new JLabel("Could not generate summary.", JLabel.CENTER);
            } catch (CouldNotGenerateException exc) {
                ApplicationManager.getInstance().setStatusBarText("Summary generation failed: Could not generate document!");
                summaryComponent = new JLabel("Could not generate summary.", JLabel.CENTER);
            }
            summaryComponent.setTransferHandler(null);
            summaryPanel.removeAll();
            summaryPanel.add(new JScrollPane(summaryComponent), BorderLayout.CENTER);
            summaryPanel.revalidate();
            
            patientExaminationTree.setEnabled(true);
            generatorThread = null;
        }
    }
    
    
    /**
     * Called when the View is closed.
     */
     public void cleanUp() {
         ApplicationManager.getInstance().removeTemplateChangeListener(this);
         ApplicationManager.getInstance().removeTranslatorChangeListener(this);
         super.cleanUp();
     }
     
     /** 
      * Called when the template has been changed.
      *
      * @param event The object describing the event.
      *
      */
     public void templateChanged(TemplateEvent event) {
        patientExaminationTree.setModel(new DefaultTreeModel(createPatientExaminationNodes()));
     }
     
     /** 
      * Called when the translator has been changed.
      *
      * @param event The object describing the event.
      *
      */
     public void translatorChanged(TranslatorEvent event) {
        patientExaminationTree.setModel(new DefaultTreeModel(createPatientExaminationNodes()));
     }
     
}
/*
                                                                                                   
 **/