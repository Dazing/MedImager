/*
 * ImageHandler.java
 *
 * Created on November 18, 2002, 3:19 PM
 *
 * $Id: ImageHandler.java,v 1.1 2002/11/21 15:28:00 erichson Exp $
 *
 * $Log: ImageHandler.java,v $
 * Revision 1.1  2002/11/21 15:28:00  erichson
 * First check-in
 *
 */

package medview.datahandling.images;

import java.io.*; // IOException

import medview.datahandling.examination.*;

/**
 * Interface for image handling
 *
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 */
public interface ImageHandler {
    
    
    /**
     * Get the images associated with an examination
     */
    public ImageIdentifier[] getImages(ExaminationIdentifier id) throws IOException, NoSuchExaminationException;
    
    /**
     * Get a thumbnail of an image
     */
    public java.awt.Image getThumbnail(ImageIdentifier id) throws IOException;
    
    /**
     * Get a full size image
     *
     */
    public java.awt.Image getFullImage(ImageIdentifier id) throws IOException;
    
}
