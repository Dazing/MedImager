/*
 * InvalidTemplateException.java
 *
 * Created on den 7 juni 2005, 15:10
 *
 * $Id: InvalidTemplateException.java,v 1.1 2005/07/06 11:35:59 erichson Exp $
 *
 * $Log: InvalidTemplateException.java,v $
 * Revision 1.1  2005/07/06 11:35:59  erichson
 * First check-in, moved from graph subpackage
 *
 * Revision 1.2  2005/06/09 15:07:07  erichson
 * More informative InvalidTemplateException.
 *
 * Revision 1.1  2005/06/07 17:54:23  erichson
 * First check-in.
 *
 *
 */

package medview.medrecords.data;

/**
 * Exception thrown when a template is invalid.
 *
 * @author Nils Erichson
 */
public class InvalidTemplateException extends Exception
{
    private String templateLocation;
    
    /** Creates a new instance of InvalidTemplateException */
    public InvalidTemplateException(String templateLocation, String message)
    {
        super(message);
        this.templateLocation = templateLocation;
    }    
    
    public String getTemplateLocation()
    {
        return templateLocation;
    }
}
