/*
 * ImageIdentifier.java
 *
 * Created on November 14, 2002, 4:18 PM
 *
 * $Id: ImageIdentifier.java,v 1.1 2002/11/25 13:26:23 erichson Exp $
 *
 * $Log: ImageIdentifier.java,v $
 * Revision 1.1  2002/11/25 13:26:23  erichson
 * First check-in
 *
 */

package medview.datahandling.images;

import medview.datahandling.examination.*;

/**
 * Class which uniquely identifies an image belonging to an examination
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 */
public class ImageIdentifier {
    
    private final ExaminationIdentifier examinationIdentifier;
    private final String imageName;
    
    /** Creates a new instance of ImageIdentifier */
    public ImageIdentifier(ExaminationIdentifier examinationIdentifier,String imageName) {
        //this.patientIdentifier = patientIdentifier;
        this.examinationIdentifier = examinationIdentifier;
        this.imageName = imageName;
    }
    
    public ExaminationIdentifier getExaminationIdentifier() {
        return examinationIdentifier;
    }
    
    public String getName() {
        return imageName;
    }
    
}
