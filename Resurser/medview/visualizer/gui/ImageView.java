/*
 * ImageView.java
 *
 * Created on October 15, 2002, 1:46 PM
 *
 * $Id: ImageView.java,v 1.12 2005/01/26 13:08:44 erichson Exp $
 *
 * $Log: ImageView.java,v $
 * Revision 1.12  2005/01/26 13:08:44  erichson
 * Removed junk at end of file
 *
 * Revision 1.10  2004/10/08 15:58:50  erichson
 * Now filters out examinations without images, and provides a notice in the status bar
 * that it was done.
 *
 * Revision 1.9  2004/10/08 15:07:55  erichson
 * For some reason this view had its own dataset instead of using the one in View superclass. Fixed.
 *
 * Revision 1.8  2002/12/19 08:46:27  zachrisg
 * Removed the aggregation controls since they have no use in this view.
 *
 * Revision 1.7  2002/12/05 11:58:28  erichson
 * removed selectionChanged; put updateSelection() in validateView() instead
 *
 * Revision 1.6  2002/12/04 14:52:38  erichson
 * Fixed viewTransferHandler assignment
 *
 * Revision 1.5  2002/11/29 15:33:28  erichson
 * added selectionEvent handling
 *
 * Revision 1.4  2002/11/21 15:47:36  erichson
 * Removed most of updateView: Moved its functionality to ImageViewPanel
 *
 * Revision 1.3  2002/11/14 16:01:00  zachrisg
 * Added support for session loading/saving of aggregations.
 *
 * Revision 1.2  2002/10/31 16:00:55  erichson
 * Major functionality overhaul: Removed most functionality and moved to imageViewPanel, DataSource etc
 *
 * Revision 1.1  2002/10/17 11:45:49  erichson
 * First check-in. The class seems to work, but image paths need to be adjusted to use it right now.
 *
 */

package medview.visualizer.gui;

import java.io.IOException;
import java.util.*;

import medview.datahandling.*;
import medview.datahandling.aggregation.*;
import medview.datahandling.examination.*;
import medview.datahandling.images.*;

import medview.visualizer.data.*; // ExaminationDataSet, Element...
import medview.visualizer.dnd.*; // TransferHandler...
import medview.visualizer.event.*; // SelectionEvent...

/** 
 * a View for images
 * @author Nils Erichson <d97nix@dtek.chalmers.se> 
 * @version 0.1
 */
public class ImageView extends View implements MedViewLanguageConstants {
        
    private ImageViewPanel imagePanel;
    private ViewTransferHandler viewTransferHandler;
    
    /** Creates a new instance of ImageView
     * @param examinationDataSet the examination data set to use to create the view
     */
    public ImageView(ExaminationDataSet examinationDataSet) {
        super(examinationDataSet, false); // sets dataset etc     
                
        // create the panel
        viewTransferHandler = new ViewTransferHandler(this);                        
        imagePanel = new ImageViewPanel(this, viewTransferHandler);        
                               
        imagePanel.setTransferHandler(viewTransferHandler);        
        setViewComponent(imagePanel);        
        updateView();
        System.out.println("updateview done");
    }
    
    /** Returns the name of the type of view.
     *
     * @return The view type name.
     */
    protected String getViewName() {
        return "Photo view";
    }
    
    /** Marks the view as invalid.
     * Called when an element has been selected or deselected.
     */
    protected void invalidateView() {
    }
        
    /** Updates everything that needs to know which terms exist.
     */
    public void updateTermChoosers(boolean chosenTermsChanged, boolean allTermsChanged) 
    { // Ignore, this view has no term choosers 
        
    }    
    
     /*
     * Called after a change in data has been made and checks if the selection for this view has changed.
     * If so is the case then recreate internal data structures and repaint the view.
     */    
    public void validateView() {
        
        if (dataSetHasChanged) {
            updateView();
        } else if (selectionHasChanged) { // unneccesary if updateView was run            
            imagePanel.updateSelection();
        }
        super.validateView();  
        //updateView();
    }

    private ExaminationDataSet filterOutExaminationsWithoutImages(ExaminationDataSet eds)
    {        
        Vector v = new Vector(); // Store OK elements here
        ExaminationDataElement[] allElements = eds.getElements();
        for (int i = 0; i < allElements.length; i++)
        {            
            try {
                ExaminationIdentifier examinationIdentifier =            
                    allElements[i].getExaminationIdentifier();
            
                ExaminationImage[] examinationImages = 
                    allElements[i].getDataSource().getImages(examinationIdentifier);

                if (examinationImages.length > 0) // This element has images
                {
                    v.add(allElements[i]);
                }
            } catch (IOException ioe)
            {
                ApplicationManager.errorMessage("Error when filtering out examinations with images: IOException: " + ioe.getMessage());
            } catch (NoSuchExaminationException nsee)
            {
                ApplicationManager.errorMessage("Error when filtering out examinations with images: NoSuchExaminationException: " + nsee.getMessage());
            }
        }
        
        // Convert vector to array
        ExaminationDataElement[] newElements =
            new ExaminationDataElement[v.size()];
        
        newElements = (ExaminationDataElement[]) v.toArray(newElements);
        
        // Make dataset out of array
        ExaminationDataSet newDataSet = new ExaminationDataSet(newElements);
        return newDataSet;
    }
    
    /** We override setExaminationDataSet in view to filter out examinetions without images */
    public void setExaminationDataSet(ExaminationDataSet eds) {
        int edsCount = eds.getElementCount();
        super.setExaminationDataSet(filterOutExaminationsWithoutImages(eds));
        int newCount = getExaminationDataSet().getElementCount();
        int difference = edsCount - newCount;
        if (difference > 0) {
            setStatusBarText("Selected: " + getTotalSelectionString(false)
              + ". (" + difference + " examinations skipped because they lack images)");
        }
    }
    
    private void updateView() {
        // get the examination elements
        ExaminationDataElement[] elements = getExaminationDataSet().getElements();                
               
        System.out.println("Displaying images for " + elements.length + " examinations");
        imagePanel.displayImages(elements);       
        System.out.println("updateView done");
    }
}
/*                                                                                                   
 **/