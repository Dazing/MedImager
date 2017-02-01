/*
 * ImageViewPanel.java
 *
 * $Id: ImageViewPanel.java,v 1.11 2003/07/02 10:39:13 erichson Exp $
 *
 * $Log: ImageViewPanel.java,v $
 * Revision 1.11  2003/07/02 10:39:13  erichson
 * Removed println's, redirected to errorMessage instead
 *
 * Revision 1.10  2003/06/24 18:14:51  erichson
 * More informative error messages when IOexception is caught
 *
 * Revision 1.9  2002/12/06 15:12:22  erichson
 * Added "displaying images done" message.
 *
 * Revision 1.8  2002/12/06 13:30:20  zachrisg
 * DataManager.validateViews() is now called after the user has deselected
 * all elements by clicking on the panel between images.
 *
 * Revision 1.7  2002/12/05 12:00:10  erichson
 * setSelected() removed - replaced call with updateBorder() instead
 *
 * Revision 1.6  2002/12/04 14:50:16  erichson
 * Updated mouse and drag listeners (merged them into MouseInputAdapter) for drag support
 *
 * Revision 1.5  2002/12/03 14:50:05  erichson
 * Added new statusbar and some starting drag support
 *
 * Revision 1.4  2002/11/29 15:26:43  erichson
 * Rewrote imagemodel/container caching, moved inner classes out, added selection handling
 *
 * Revision 1.3  2002/11/21 15:49:50  erichson
 * Rewrote displayImages to use the new image handling
 *
 * Revision 1.2  2002/11/08 11:05:53  erichson
 * Changed imagePaths handling
 *
 * Revision 1.1  2002/10/31 15:57:24  erichson
 * First check-in.
 *
 *
 * --------------------------------
 * Modified from MedSummaryImagePanel by Fredrik Lindahl. Forked at revision:
 * MedSummaryImagePanel.java,v 1.5 2002/10/12 14:11:03 lindahlf Exp 
 * --------------------------------
 */

package medview.visualizer.gui;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import misc.gui.*;

import medview.common.dialogs.*;

import medview.datahandling.*;
import medview.datahandling.examination.*; // ExaminationIdentifier etc
import medview.datahandling.images.*; // ImageIdentifier

import medview.visualizer.data.*; // ExaminationDataElement etc

/**
 * A panel containing images, to be used as a component in a View.
 * @author Nils Erichson <d97nix@dtek.chalmers.se>, based on MedSummaryImagePanel by Fredrik Lindahl
 * @version 1.0
 */

public class ImageViewPanel extends JPanel {
    
    /* Fields */
    
    private MedViewDataHandler mVDH;    
    private JPanel imagePanel;    
    
    private ExaminationDataElement[] elements = null;
    
    private Component parent;    
    private JScrollPane jSP;
    private HashMap oldExaminationToContainerVectorMap, currentExaminationToContainerVectorMap;
    private int imHeight, imWidth;    
    
    private MouseInputAdapter dragMouseListener;    
    private TransferHandler transferHandler = null;
        
    
    /* Constructors */
    
    /** Creates an ImageViewPanel with a preset parent component
     * @param parent the Component which should be the panel's parent
     */    
    public ImageViewPanel(Component parent, TransferHandler th) {
        this.mVDH = MedViewDataHandler.instance();
        
        dragMouseListener = new DragMouseListener();                             
        
        currentExaminationToContainerVectorMap = new HashMap();        
        oldExaminationToContainerVectorMap = new HashMap();
        transferHandler = th;
        
        this.parent = parent;
        
        initDims();        
        initPanel();
        
                /* NOTE
                 * ====
                 * The 'parent' reference is necessary for
                 * the action to view examination images, so
                 * the dialog can be set up to appear centered
                 * over the parent. */ // <- THIS SHOULD BE CHANGED TO MEDIATOR
    }
    
    
    /* Methods */    
    
    /**
     * Removes all images from the panel and repaints
     */
    public void clearImages() {
        imagePanel.removeAll();
        imagePanel.setPreferredSize(calcPrefSize(0));
        jSP.setPreferredSize(calcPrefSize(0));
        imagePanel.revalidate();
        imagePanel.repaint();
    }
    
    /**
     * Makes the panel display the images for a set of examinationDataElements.
     * @param elements the ExaminationDataElements for the examinations whose images to display
     */
    public void displayImages(ExaminationDataElement[] elements ) // previously took ImageContainerModels as parameter
    {                       
        ApplicationManager.getInstance().setStatusBarText("Displaying images...");
        this.elements = elements;
        imagePanel.removeAll();
        int nrOfImages = 0;
        // Move current imageset to old
        oldExaminationToContainerVectorMap = currentExaminationToContainerVectorMap;
        currentExaminationToContainerVectorMap.clear();
        
        // get/create ImageContainerModels for all elements        
        
        //for (Iterator elementIterator = Arrays.asList(elements).iterator(); elementIterator.hasNext();) {
        for (int i = 0; i < elements.length; i++) {
            ExaminationDataElement nextElement = elements[i];
            ApplicationManager.getInstance().setStatusBarProgress(1,i+1,elements.length);
            //ExaminationDataElement nextElement = (ExaminationDataElement) elementIterator.next();
            try {
                ExaminationIdentifier examinationIdentifier = nextElement.getExaminationIdentifier();
                // Try go get old set of containers
                Vector containerVector = (Vector) oldExaminationToContainerVectorMap.get(examinationIdentifier);
                if (containerVector == null) {
                    // Containers did not exist, we need to create new                
                    containerVector = new Vector();
                    
                    ExaminationImage[] examinationImages = nextElement.getDataSource().getImages(examinationIdentifier);
                    
                    for (Iterator imageIterator = Arrays.asList(examinationImages).iterator(); imageIterator.hasNext();) {
                        ExaminationImage nextImage = (ExaminationImage) imageIterator.next();
                        ImageContainerModel imageContainerModel = new ImageContainerModel(nextImage,nextElement);
                        ImageContainerButton imageContainer = new ImageContainerButton(imageContainerModel);
                        imageContainer.setTransferHandler(transferHandler);
                        
                        containerVector.add(imageContainer);
                        imageContainer.addMouseListener(dragMouseListener);
                        imageContainer.addMouseMotionListener(dragMouseListener);
                        imagePanel.add(imageContainer);
                        
                        nrOfImages++;
                    }                                        
                }
                // We now have a containerVector, store it in the new
                currentExaminationToContainerVectorMap.put(examinationIdentifier,containerVector);
            } catch (java.io.IOException ioe) {
                try {
                    ApplicationManager.getInstance().errorMessage("displayImages: Skipped element " + nextElement.getExaminationIdentifier().toString() + " because IOException: " + ioe.getMessage());
                } catch (java.io.IOException ioe2) {
                    ApplicationManager.getInstance().errorMessage("displayImages: Skipped element (which could not be identified because of additional IOException: " + ioe2.getMessage() + ") - reason: " + ioe.getMessage());
                }
            } catch (NoSuchExaminationException nsee) {
                try {
                    ApplicationManager.getInstance().errorMessage("displayImages: Skipped element " + nextElement.getExaminationIdentifier().toString() + " because NoSuchExaminationException: " + nsee.getMessage());
                } catch (java.io.IOException ioe2) {
                    ApplicationManager.getInstance().errorMessage("displayImages: Skipped element (which could not be identified because of IOException: " + ioe2.getMessage() + ") because of NoSuchExaminationException: " + nsee.getMessage());
                }
            }
                
        }
        // Image container checking done, cut loose the old hashmap so that old imageContainers get garbage collected
        oldExaminationToContainerVectorMap = null;
                                        
        //System.out.println("nr of images: " + nrOfImages);
        
        //ImageContainerModel[] currImodels = new ImageContainerModel[nrOfImages];
        //currImodels = (ImageContainerModel[]) ImageContainerModels.toArray(currImodels);
                        
                
        Dimension prefSize = calcPrefSize(nrOfImages);
        
        
        Dimension imagePanelDim = imagePanel.getPreferredSize();
        
        imagePanel.setPreferredSize(prefSize);                
        
        Dimension imagePanelDim2 = imagePanel.getPreferredSize();
        
        //System.out.println("Calced = " + prefSize + ", Pref1 = " + imagePanelDim + ", Pref2 = " + imagePanelDim2);
                
        
        jSP.setPreferredSize(prefSize);
                        
        
        
        imagePanel.revalidate();        
        imagePanel.repaint();
        
        ApplicationManager.getInstance().setStatusBarText("Displaying images done.");
        
                /* NOTE
                 * ====
                 * Revalidation only recalculates
                 * the needed preferred sizes for
                 * the components to accomodate the
                 * eventual new contents, but if the
                 * new size needed is the same, or
                 * smaller, the revalidate method will
                 * not cause a resize() of the component
                 * and will thus the component will not
                 * repaint.
                 */
    }
    
    private Dimension calcPrefSize(int nrOfImages) {
                /* NOTE
                 * ====
                 * The scrollbar's width can
                 * vary if the user has chosen
                 * another look and feel, thus
                 * it is instantiated here to
                 * find out a scrollbars width.
                 */
        
        JScrollBar dummySB = new JScrollBar(JScrollBar.VERTICAL);
        
        Dimension prefDim = dummySB.getPreferredSize();
        
        int sBWidth = prefDim.width;                
        //int availableWidth = jSP.getWidth();   // How wide is the scrollPane?
        int availableWidth = jSP.getViewport().getWidth();
        //System.out.println("debug: Available viewport width = " + availableWidth);
        int imPerRow = availableWidth / imWidth;  // Images per row       
        //System.out.println("Images per row: " + imPerRow);
        int rowsReq = -1;
        
        if (nrOfImages == 0) { // No images -> only one row
            rowsReq = 1;
        }
        else { // calculate number of rows
            rowsReq = (nrOfImages / (imPerRow + 1)) + 1;
        }        
                
        int prefWidth = -1; 
        
        if (rowsReq == 1) {
            prefWidth = jSP.getWidth();
        } else {
            prefWidth = jSP.getWidth() - sBWidth;
        }
        
        int prefHeight = rowsReq * imHeight;
                
        return new Dimension(prefWidth, prefHeight);
    }                            
    
    
    // Set this object's imHeight and imWidth
    private void initDims() {
        JLabel dummyLabel = new JLabel();
        
        // Set the dummyLabel's border
        Border outer = BorderFactory.createEmptyBorder(5,5,5,5);        
        Border inner = BorderFactory.createEtchedBorder();        
        dummyLabel.setBorder(BorderFactory.createCompoundBorder(outer, inner));
        
        Border dummyBorder = dummyLabel.getBorder();
        
        Insets bInsets = dummyBorder.getBorderInsets(dummyLabel);
        
        int borderHeight = bInsets.top + bInsets.bottom;        
        int borderWidth = bInsets.left + bInsets.right;
        
        int iconHeight = ImageContainerModel.ICON_HEIGHT;        
        int iconWidth = ImageContainerModel.ICON_WIDTH;
        
        this.imHeight = iconHeight + borderHeight;        
        this.imWidth = iconWidth + borderWidth;
    }
    
    // Set layout and add panel in scrollpane to this
    private void initPanel() {
        setLayout(new BorderLayout());
        
        // Init image JPanel
        imagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));        
        imagePanel.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));        
        imagePanel.setOpaque(false);
        
        imagePanel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                // System.out.println("inner imagePanel got mousepress");
                DataManager.getInstance().deselectAllElements();
                DataManager.getInstance().validateViews();
            }
        });
        
        
        // Init scrollpane for the ImagePanel
        jSP = new JScrollPane(imagePanel);        
        jSP.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));        
        jSP.setOpaque(false);
        
        add(jSP, BorderLayout.CENTER);
    }
    
    public void updateSelection() {
        
        for (int i = 0; i < elements.length; i++) {
            try {
                ExaminationIdentifier nextExamination = elements[i].getExaminationIdentifier();            
                Vector containers = (Vector) currentExaminationToContainerVectorMap.get(nextExamination);
                if (containers != null) {
                    for (Iterator containerIterator = containers.iterator(); containerIterator.hasNext();) {
                        ImageContainerButton nextContainer = (ImageContainerButton) containerIterator.next();
                        //nextContainer.setSelected(elements[i].isSelected());
                        nextContainer.updateBorder();
                    }
                }
            } catch (java.io.IOException ioe) { 
                ApplicationFrame.getInstance().errorMessage("WARNING: IOException in updateSelection() for element " + i + "(" + elements[i] + ")");
            }
        }
            // Get the imagecontainers belonging to this element            
    }
    
    /* Inner classes */
    
    
    /* Listen to containers for mouse events */
    
    private class DragMouseListener extends MouseInputAdapter { 
        private boolean dragActive = false; // Whether a drag is currently active
        
        public void mousePressed(MouseEvent me) {
            dragActive = false;
        }
        
        public void mouseDragged(MouseEvent me) {
            JComponent sourceComponent = (JComponent) me.getSource();            
            if (!dragActive) {
                dragActive = true;
                //System.out.println("exportAsDrag called");
                transferHandler.exportAsDrag(sourceComponent, me, TransferHandler.COPY);
            }            
        }
    }
    
}