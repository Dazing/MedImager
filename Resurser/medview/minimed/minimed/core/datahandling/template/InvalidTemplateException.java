package minimed.core.datahandling.template;


/**
 * Exception thrown when a template is invalid.
 *
 * @author Nils Erichson
 */
public class InvalidTemplateException extends Exception {
    private String templateLocation;
    
    /** 
     * Creates a new instance of InvalidTemplateException 
     */
    public InvalidTemplateException(String templateLocation, String message) {
        super(message);
        this.templateLocation = templateLocation;
    }    
    
    public String getTemplateLocation() {
        return templateLocation;
    }
}
