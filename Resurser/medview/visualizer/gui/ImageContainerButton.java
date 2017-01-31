/*
 * ImageContainerButton.java
 *
 * Created on den 28 november 2002, 15:22
 *
 * $Id: ImageContainerButton.java,v 1.8 2008/08/27 09:31:49 it2aran Exp $
 *
 * $Log: ImageContainerButton.java,v $
 * Revision 1.8  2008/08/27 09:31:49  it2aran
 * Large images wasn't scaled down
 *
 * Revision 1.7  2005/03/16 14:10:03  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.6  2004/12/08 14:49:00  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.5  2003/07/02 10:35:36  erichson
 * Passes error messages to the message panel instead of popping up a dialog now.
 *
 * Revision 1.4  2002/12/06 14:36:35  erichson
 * paintComponent no longer necessary
 *
 * Revision 1.3  2002/12/05 11:57:22  erichson
 * Sped up the selection painting
 *
 * Revision 1.2  2002/12/04 15:09:52  erichson
 * Changed mouselistener handling of selection
 *
 *
 */

package medview.visualizer.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import medview.datahandling.*;
import medview.datahandling.images.*;
import medview.datahandling.MedViewMediaConstants;
import medview.datahandling.MedViewLanguageConstants;

import medview.visualizer.data.*;
import medview.medrecords.data.PreferencesModel;
import medview.common.dialogs.MedViewDialogs; // ExaminationDataElement

/**
 *
 * @author Nils Erichson <d97nix@dtek.chalmers.se>, based on prior work by Fredrik Lindahl <d97lind@dtek.chalmers.se>
 */

public class ImageContainerButton extends JLabel  {

    /* Fields */

    private boolean selected = false;
    private ImageContainerModel model;
    private Color infoPanelColor;
    private String patientCode;
    private String examinationDate;
    private boolean shouldShowPopup;
    private Action viewDetailedAction;
    private JPopupMenu popupMenu;

    private DataElementBorder border;

    private static final MedViewDataHandler mVDH = MedViewDataHandler.instance();

    private static final DataManager DM = DataManager.getInstance();


    /* Constructors */


    public ImageContainerButton(ImageContainerModel model) {
        super();
        this.model = model;

        infoPanelColor = new Color(0,0,0,50);


        patientCode = model.getPatientIdentifier();
        examinationDate = model.getExaminationTime();

        layoutImageContainerButton();

        if (shouldShowPopup) {
            setupViewAction();
            setupPopupMenu();
            setupListeners();
        }
    }

    /* Methods */

    private void layoutImageContainerButton() {
        setLayout(new BorderLayout());

        border = new DataElementBorder(model.getExaminationElement(),4);
        setBorder(border);

        // Create info panel
        JPanel infoPanel = new JPanel(new GridLayout(2,1,1,2));
        infoPanel.setBackground(infoPanelColor);
        infoPanel.setOpaque(true);

        // Create pcode label
        JLabel pCodeLabel = new JLabel(patientCode);
        pCodeLabel.setOpaque(false);
        pCodeLabel.setForeground(Color.white);
        pCodeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        infoPanel.add(pCodeLabel);

        // Create date label
        JLabel dateLabel = new JLabel(examinationDate);
        dateLabel.setOpaque(false);
        dateLabel.setForeground(Color.white);
        dateLabel.setHorizontalAlignment(SwingConstants.CENTER);

        infoPanel.add(dateLabel);

        add(Box.createGlue(), BorderLayout.CENTER);
        add(infoPanel, BorderLayout.SOUTH);

        ImageIcon imageIcon;

        try {

            imageIcon = model.getImageIcon();

        } catch (java.io.IOException ioe) {
            String errorMessage = "Image loading failed: " + ioe.getMessage();
            // JOptionPane.showMessageDialog(getParent(),errorMessage,"Image loading exception",JOptionPane.ERROR_MESSAGE);
            ApplicationManager.getInstance().errorMessage(errorMessage);
            imageIcon = mVDH.getImageIcon(MedViewMediaConstants.NO_THUMBNAIL_IMAGE_ICON); // Default image for broken images
        }

            if (imageIcon.getImageLoadStatus() == MediaTracker.ERRORED) {
                setIcon(mVDH.getImageIcon(MedViewMediaConstants.NO_THUMBNAIL_IMAGE_ICON));            // use no image icon instead
                shouldShowPopup = false;
            } else if (imageIcon.getImageLoadStatus() == MediaTracker.COMPLETE) { // loading done, set it
                setIcon(imageIcon);
                shouldShowPopup = true;
            }

    }


    private void setupViewAction() {
        viewDetailedAction = new ViewDetailedAction();
    }

    // Create the popup menu for images.
    private void setupPopupMenu() {
        popupMenu = new JPopupMenu();
        popupMenu.add(viewDetailedAction); // the action that shows the large image
    }

    private void setupListeners() {
        this.addMouseListener(new PopupListener());
    }

    //public void paintComponent(Graphics g) {
        //System.out.println("paintComponent called on icb");
        /*
         if (model.getExaminationElement().isSelected())
            setBorder(BorderFactory.createLineBorder(Color.RED,3));
        else
            setBorder(BorderFactory.createLineBorder(Color.BLACK,3));
        */
      //  super.paintComponent(g);
    // }

    private class PopupListener extends MouseInputAdapter {
        private boolean wasSelected = false;
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) { // doubleclick
                System.out.println("Double click on container");
                ActionEvent aE = new ActionEvent(ImageContainerButton.this, -1, null);
                viewDetailedAction.actionPerformed(aE); // call viewDetailedAction
            }
        }
        public void mousePressed(MouseEvent e) {
            if (e.isPopupTrigger()) { // right mouse click
                popupMenu.show(ImageContainerButton.this, e.getX(), e.getY());
            } else { // not right click
                ExaminationDataElement element = model.getExaminationElement();

                if (element.isSelected()) { // if it is selected, do nothing until release
                    wasSelected = true;
                } else { // if not selected, do the selection immediatly
                    if (!e.isControlDown()) { // if control is not down, deselect everything else first
                        DM.deselectAllElements();
                        DM.validateViews();
                    }
                    element.setSelected(true);
                    DM.validateViews();
                }
            }
        }

        public void mouseReleased(MouseEvent e) {
            if (wasSelected) {
                ExaminationDataElement element = model.getExaminationElement();
                if (e.isControlDown()) {
                    element.setSelected(! element.isSelected()); // Invert selection
                    DM.validateViews();
                } else {
                    DM.deselectAllElements();
                    element.setSelected(true);
                    DM.validateViews();
                }
                wasSelected = false;
            }
        }


        /*
        private void handleClick(MouseEvent e) {


            } else { // Single click
                System.out.println("Single click on container");
                ExaminationDataElement element = model.getExaminationElement();
                // Get keys pushed
                // if (e.isShiftDown() ..
                if (e.isControlDown()) {
                    element.setSelected(!element.isSelected()); // Invert selection
                } else {
                    // Control not down
                    DM.deselectAllElements();
                    element.setSelected(true);
                }
            }
        }*/
    }

    public boolean isSelected() {
        return selected;
    }

    public void updateBorder() {
        repaint();
    }

    /*
    private void updateBorder(boolean selected) {
        // setBorder(new DataElementBorder(model.getExaminationElement(),4));
        repaint();
    }*/

    /**
     * Set the state of this "button"
     * if the state changes from the current state, the border will be updated
     */
    /*
    public void setSelected(boolean newState) {
        if (selected != newState) {
            updateBorder(newState);
        }
        selected = newState;
    }*/

    /**
     * The action to view an image full-size
     */
    private class ViewDetailedAction extends AbstractAction {
            /*private String nPre = MedViewLanguageConstants.ACTION_NAME_PREFIX_LS_PROPERTY;
            private String dPre = MedViewLanguageConstants.ACTION_SHORT_DESCRIPTION_PREFIX_LS_PROPERTY;
            private String post = MedViewLanguageConstants.ACTION_VIEW_EXAMINATION_IMAGE_LS_PROPERTY;
            private String name = nPre + post;
            private String desc = dPre + post;
             */

        public void actionPerformed(ActionEvent e) {
            //MedViewDialogs mVD = MedViewDialogs.instance();
            //Component root = SwingUtilities.getRoot(ImageViewPanel.this);
            //Component parentComponent = ImageViewPanel.this;
            Component parentComponent = ImageContainerButton.this;
            String title = patientCode + " - " + examinationDate;
            //mVD.createAndShowImageDialog(root, title, model.getImagePath());

            ExaminationImage examinationImage = model.getExaminationImage();
            try
            {
                Image fullSizeImage = examinationImage.getFullImage();
        		int maxWidth = PreferencesModel.instance().getImageWindowWidth();
		
                // if the image is too big to fit on scree, scale it down
		        if (fullSizeImage.getWidth(null) > maxWidth)
                {
                    fullSizeImage = fullSizeImage.getScaledInstance(maxWidth, -1, Image.SCALE_DEFAULT);
                }

                ImageIcon imageIcon = new ImageIcon(fullSizeImage);
                JLabel imageLabel = new JLabel(imageIcon);

                // show dialog with the image
               JOptionPane.showMessageDialog(parentComponent,imageLabel,examinationImage.getName(),JOptionPane.PLAIN_MESSAGE);
 
            } catch (java.io.IOException ioe) {
                JOptionPane.showMessageDialog(parentComponent,"Image loading failed: " + ioe.getMessage(),"Image loading exception",JOptionPane.ERROR_MESSAGE);
            }
        }

        public ViewDetailedAction()
	{
        }
    }
}
