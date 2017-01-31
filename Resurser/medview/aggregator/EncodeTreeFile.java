package medview.aggregator;

import java.util.*;
import java.io.*;

/**
 * Title:        Aggregator
 * Description:  A medview tool for grouping of terms.
 * Copyright:    Copyright (c) 2002
 * Company:      G?teborg University
 * @author Nader Nazari
 * @version 1.0
 *
 * $Id: EncodeTreeFile.java,v 1.2 2004/02/20 14:55:29 erichson Exp $
 *
 * $Log: EncodeTreeFile.java,v $
 * Revision 1.2  2004/02/20 14:55:29  erichson
 * Updated tree file reading since former reading would fail with tree files from new MedRecords (example files from Ulf)
 *
 *
 */

public class EncodeTreeFile{
    
    public EncodeTreeFile() {
    }

    /**
     * Read a treefile to a vector
     */
    private static Vector readFile(File treeFile) {
        // Ut.prt("Making tree of " + treeFile.getName());
        Vector              lines       = new Vector();
        FileInputStream     fInStrm     = null;
        InputStreamReader   inStrmRdr   = null;
        BufferedReader      bReader     = null;
        
        try {
            fInStrm     = new FileInputStream(treeFile);
            inStrmRdr   = new InputStreamReader(fInStrm,"ISO-8859-1");
            bReader     = new BufferedReader(inStrmRdr);
        }
        catch (IOException e) {
            Ut.prt(e.getMessage()  + "  ERROR when opening file: " + treeFile.getPath());
            return null;
        }
        try {  // read the stream to a vector
            while (bReader.ready()) {
                lines.add(bReader.readLine());
            }
        }
        catch (IOException e) {
            Ut.prt(e.getMessage()  + " ERROR   when reading file: " + treeFile.getPath());
            return null;
        }
        return lines;
    }
    
    /**
     * Construct a Node Tree from a treeFile
     * @param treeFile the file to parse
     * @return the root Node of the tree
     */
    public static Node makeTree(File treeFile) {
        Vector              lines       = readFile(treeFile );
        // Loop through vector and remove empty lines
        for (Iterator it = lines.iterator(); it.hasNext();) {
            String nextLine = (String) it.next();
            if (nextLine == null)
                it.remove();
            else if (nextLine.trim().equals(""))
                it.remove();
        }
                        
        // Loop through vector of lines and  build the tree
        Iterator lineIterator = lines.iterator();
        
        int lineCount = 0;
        if(! lineIterator.hasNext() ) return null;
        
        String  line        = (String) lineIterator.next();
        Node    rootNode    = new Node(line,Node.TYPE_BRANCH);                    // build root node
        Node    parent      = rootNode;
        
        for ( ; lineIterator.hasNext(); ) {                                 /* lineiterator is already defined */
            lineCount++;
            
            //	System.out.println("Read a line");
            String fences, value;
            String readLine = (String) lineIterator.next();
            line = readLine;
            
            if(line == null || line.length() == 0) {
                Ut.prt("The read line is null, probably an extra return at the eof. Skipping.");                
            } else {
                
                char firstChar = line.charAt(0);    //Crashhhhhhh 74                            // Get the first character (L or N for leaf or node)
                int type = Node.TYPE_NONE;

                if (firstChar == 'N') {
                    type = Node.TYPE_BRANCH;                                    // Node: A newline leads to a child
                } else if (firstChar == 'L') {

                    type = Node.TYPE_LEAF;                                      // Slightly different for Leaves:
                    while (line.indexOf("#") == -1) {
                        line = line + (String) lineIterator.next();             // Read more lines until we find fences ('#')
                    }
                }

                if (type != Node.TYPE_NONE) {
                    // Got Node or Leaf, process

                    int firstFence = line.indexOf('#');
                    if (firstFence >= 0 ) {
                        value = line.substring(1,firstFence);                   // get the substring that's not fences
                        fences = line.substring(firstFence,line.length());      // get the substring that are the fences
                    } else {
                        // There are no fences on this line
                        value = line.substring(1,line.length());
                        fences = "";
                    }
                    // Non-leaves (attributes) should be lower case
                    // if (type == Node.TYPE_BRANCH) {

                    // value = value.toLowerCase();
                    //}
                    Node n = new Node(value,type);
                    n.setParent(parent);

                    parent.addChild(n);
                    parent = n;

                    // Process the fences! I.e. back up as many times as there are fences
                    for (int i = 0; i < (fences.length() ); i++) {
                        if(parent != null){
                            // Ut.prt("backing up from " + parent.getValue());                        
                            Node nextParent = parent.getParent();
                            if(nextParent == null) {
                                if (parent != rootNode) {                                
                                    Ut.prt("Parent is null which is very weird since we're not at rootNode");
                                } else { // parent == rootNode
                                    if (lineCount < lines.size() -1 ) {
                                        // Ut.prt("Parent is null, but OK since we're at the final line");
                                    } else {
                                        Ut.prt("Parent is null, but we're NOT on the final line! (" + lineCount + " of " + lines.size() + ")");
                                    }
                                }
                            } else {
                                // nextParent NOT null! so OK
                                parent = nextParent;
                            }
                        }
                    }

                } else {
                    Ut.prt("Error: Neither Leaf or Node line detected! (Line = " + line + ")");
                }
            } // end "not null or empty line" block        
        } // end line iterator loop
        return rootNode;                    
    }
} // end class
