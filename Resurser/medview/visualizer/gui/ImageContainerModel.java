/*
 * ImageContainerModel.java
 *
 * Created on den 28 november 2002, 15:19
 *
 * $Id: ImageContainerModel.java,v 1.3 2002/12/06 14:43:59 erichson Exp $
 *
 * $Log: ImageContainerModel.java,v $
 * Revision 1.3  2002/12/06 14:43:59  erichson
 * Cleaned up and added some javadoc
 *
 */

package medview.visualizer.gui;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

import medview.datahandling.examination.*;
import medview.datahandling.images.*;

import medview.visualizer.data.*;

/**
 * Model class for image containers
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 */
public class ImageContainerModel {
    
    /* Fields */
    
    //private final String imagePath;
    private ImageIcon imageIcon = null;
    private final ExaminationDataElement examinationDataElement;
    private final ExaminationImage examinationImage;
    private String patientIdentifier = "P-code unknown";
    private String examinationTime = "Time unknown";
    
    /** Thumbnail icon height */
    public static final int ICON_HEIGHT = 90;
    /** Thumbnail icon width */
    public static final int ICON_WIDTH = 120;
    
    /* Constructors */
    
    public ImageContainerModel( ExaminationImage image, ExaminationDataElement elem) {
     
        examinationImage = image;
        
        // Create image icon from the thumbnail of this image
        
        this.examinationDataElement = elem;
        try {
            ExaminationIdentifier id = examinationDataElement.getExaminationIdentifier();
            patientIdentifier = id.getPcode();
            examinationTime = id.getStringRepresentation();
        } catch (java.io.IOException ioe) {
            System.err.println("ImageContainerModel constructor: Could not get ExaminationIdentifier because of IOException: " + ioe.getMessage());
        };
    }
    
    public ExaminationImage getExaminationImage() {
        return examinationImage;
    }
    
    public ExaminationDataElement getExaminationElement() {
        return examinationDataElement;
    }
    
    public String getPatientIdentifier() {
        return patientIdentifier;
    }
    
    public String getExaminationTime() {
        return examinationTime;
    }
    
    /**
     * Gets a thumbnail Icon for this image
     */
    public ImageIcon getImageIcon() throws java.io.IOException {
        if (imageIcon == null) {
            java.awt.Image thumbnail = examinationImage.getThumbnail();
            ImageIcon thumbIcon = createImageIcon(thumbnail);
            this.imageIcon = thumbIcon;
        }
        
        if (imageIcon == null)
            throw new java.io.IOException("getImageIcon: Could not load thumbnail: imageIcon null");
        else
            return imageIcon;
    }
               
    /** 
     * Creates a thumbnail icon from an image (scales it etc)
     */
    private ImageIcon createImageIcon(Image im) {
        ApplicationManager AM = ApplicationManager.getInstance();
        /*
        System.out.println("image createImage");
        AM.startTimer();
        
        System.out.println("image getScaledInstance took " + AM.stopTimer());
        AM.startTimer();*/
        im = im.getScaledInstance(ICON_WIDTH, ICON_HEIGHT, Image.SCALE_DEFAULT);
        /*System.out.println("image scaling took " + AM.stopTimer());
        AM.startTimer();*/
        ImageIcon icon = new ImageIcon(im);
        // System.out.println("new ImageIcon took " + AM.stopTimer());
        return icon;
    }    
}
