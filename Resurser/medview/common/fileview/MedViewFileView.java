/*
 * MedViewFileView.java
 *
 * Created on October 30, 2002, 2:38 PM
 *
 * $Id: MedViewFileView.java,v 1.2 2002/10/31 09:36:48 erichson Exp $
 *
 * $Log: MedViewFileView.java,v $
 * Revision 1.2  2002/10/31 09:36:48  erichson
 * Fix: 1.1 mixed up mvg and mvd
 * added basic filtering support
 *
 * Revision 1.1  2002/10/31 08:55:24  erichson
 * First check-in
 *
 */

package medview.common.fileview;

import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.*;

/**
 *
 * FileView for file choosers in the medview framework. 
 * This fileview has the following attributes:
 *
 * * Makes custom icons for the standard MedView files (@see #getIcon)
 *
 * * Makes MVD and MVG directories non-traversable (@see #isTraversable) 
 *
 * @version 1.0
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 */
public class MedViewFileView extends FileView {
    
    private final java.io.FileFilter acceptableFileFilter;
    
    /**
     * Creates new MedViewFileView without restrictions on what files are selected
     */
    public MedViewFileView() {        
        super();
        acceptableFileFilter = null;
    }
    
    /**
     * Creates a new MedViewFileView with a FileFilter that restricts what files are selectable
     */
    public MedViewFileView(java.io.FileFilter acceptFileFilter) {
        super();
        this.acceptableFileFilter = acceptFileFilter;
    }
    
    /**
     * Whether a directory should be traversable or not.
     * In a medview file view, .mvd and .mvg directories may not be traversed (they are seen as document files)
     */
    
    public Boolean isTraversable(File f) {
        if (f.isDirectory()) {
            String fileName = f.getName().toLowerCase();
            if (fileName.endsWith(".mvd")) {
                return Boolean.FALSE; 
            } else if (fileName.endsWith(".mvg")) {
                return Boolean.FALSE;
            } else {
                return Boolean.TRUE;
            }
        } else { // not directory
            return Boolean.FALSE;
        }
    }

    
    /**
     * Whether a chosen file is acceptable or not (depends on the file filter used)
     */
    private boolean isAcceptable(File f) {
        if (acceptableFileFilter == null)
            return true;
        else
            return acceptableFileFilter.accept(f);
    }
    
            
    /**
     * Gets the icon for a file.
     *
     * The following files are known: 
     *
     * MVD directories - represented with a MedView icon
     *
     * .MVG direcories - containing aggregation data - represented with a funnel icon
     *
     * .tree files - represented by a tree icon
     *
     * @param f the file whose icon to fetch
     * @return a new Icon
     */
    public Icon getIcon(File f)	{
        String fileName = f.getName().toLowerCase();
        
        try {            
            if (fileName.endsWith(".mvg")) {
                return loadIcon("funnel16.png");
            } else if (fileName.endsWith(".mvd")) {
                return loadIcon("medview16.png");
            } else if (fileName.endsWith(".tree")) {
                return loadIcon("tree16.png");
            } else if (fileName.equals("forest.forest")) {           
                return loadIcon("tree16.png");
            }
            else {
                return super.getIcon(f); // fall back to default
            }
        } catch (IOException ioe) { // icon loading failed
            return super.getIcon(f); // fall back to default
        }
    }
    
    /**
     * Load and create icon
     * @param name the icon image's file name
     * @return the new icon
     */
    
    private javax.swing.ImageIcon loadIcon(String name) throws IOException {
        String resourceName = "/medview/common/resources/icons/medview/" + name;
        //System.out.println("Trying to load: " + resourceName);
        java.net.URL resource = getClass().getResource(resourceName);
        if (resource == null) {
            //System.out.println("Could not load resource " + resourceName +"!");
            throw new java.io.IOException("Resource not found (" + resourceName + ")!");
        } else {
            return new javax.swing.ImageIcon(resource);
        }
    }
    
}
