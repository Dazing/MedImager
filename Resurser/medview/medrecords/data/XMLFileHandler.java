/*
 *
 * Created on July 24, 2001, 4:52 PM
 *
 * $Id: XMLFileHandler.java,v 1.8 2007/10/17 15:17:04 it2aran Exp $
 *
 */

package medview.medrecords.data;

import java.io.*;
import java.util.*;

import medview.medrecords.models.*;
import medview.medrecords.interfaces.*;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;
/**
 *
 * @author  nils
 * @version
 */
public class XMLFileHandler  {
    
    static final boolean debug = false;
    
    /**
     * Creates new XMLFileHandler
     * @param treeFileDirectory the directory to store the tree files in
     */
    public XMLFileHandler() {}
    
    /**
     * Store the examination in the data base
     */
    public void saveExamination(File xmlFile, ExaminationModel examination) throws IOException {
        
        FileOutputStream fos = new FileOutputStream(xmlFile);
        //PrintStream ps = new PrintStream(fos);
        OutputStreamWriter ps = new OutputStreamWriter(fos, "ISO-8859-1"); // Olof 14/9
        //ps.println("Test xmlfile");
        ps.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n");
        // Start writing
        ps.write("<EXAMINATION>\n");
        
        CategoryModel[] categories = examination.getCategories();
        //System.out.println("Categories: " + categories.length);
        for (int cat = 0; cat < categories.length; cat++) {
            //System.out.println("Category " + cat + ": " + categories[cat].getTitle());
            InputModel[] inputs = categories[cat].getInputs();
            ps.write("<CATEGORY>\n"); // New category: Write category tag
            ps.write("<NAME>" + categories[cat].getTitle() + "</NAME>\n"); // Write category name
            //System.out.println("Fields: " + inputs.length);
            for (int input = 0; input < inputs.length; input++) {
                
                ps.write("<INPUT>\n"); // New Input: Write starting tag
                ps.write("<TYPE>");
                switch(inputs[input].getType()) {
                    case InputModel.INPUT_TYPE_TEXTAREA:
                        ps.write("TEXTAREA");
                        break;
                   /* case InputModel.TYPE_PICTURECHOOSER:
                        ps.print("PICTURECHOOSER");
                        break;*/
                    default:
                        ps.write("UNKNOWN");
                        break;
                } // end switch
                ps.write("</TYPE>\n");
                ps.write("<NAME>" + inputs[input].getName() + "</NAME>\n");  // Write field name
                ps.write("<DESCRIPTION>" + inputs[input].getDescription() + "</DESCRIPTION>\n");
                
                if (inputs[input].getType() == InputModel.INPUT_TYPE_TEXTAREA) {
                    FieldModel fm = (FieldModel) inputs[input];
                    ps.write("<FREETEXT>");
                    if (fm.isEditable()) {
                        ps.write("TRUE");
                    } else {
                        ps.write("FALSE");
                    }
                    ps.write("</FREETEXT>\n");
                    ps.write("<TRANSLATE_ABLE>"); // nader 18/4
                    ps.write( (fm.isTranslateAble() ?"TRUE" :"FALSE") );
                    ps.write("</TRANSLATE_ABLE>\n");
                    
                    ps.write("<REQUIRED>"); // nader 18/4
                    ps.write( (fm.isRequired() ?"TRUE" :"FALSE") );
                    ps.write("</REQUIRED>\n");
                    
                    ps.write("<FIELDTYPE>");
                    switch (fm.getFieldType()) {
                        case FieldModel.FIELD_TYPE_INTERVAL:
                            ps.write("INTERVAL");
                            break;
                            case FieldModel.FIELD_TYPE_QUESTION:
                            ps.write("QUESTION");
                            break;
                        case FieldModel.FIELD_TYPE_SINGLE:
                            ps.write("SINGLE");
                            break;
                        case FieldModel.FIELD_TYPE_MULTI:
                            ps.write("MULTI");
                            break;
                        case FieldModel.FIELD_TYPE_NOTE:
                            ps.write("NOTE");
                            break;
                        case FieldModel.FIELD_TYPE_IDENTIFICATION:
                            ps.write("IDENTIFICATION");
                            break;
                        default:
                            ps.write("UNKNOWN");
                            break;
                    }
                    ps.write("</FIELDTYPE>\n");
                }
                // Write all the values
                String[] values = inputs[input].getValues();
                
                //System.out.println("Values: " + values.length);
                for (int val = 0; val < values.length; val++) {
                    ps.write("<VALUE>" + values[val] + "</VALUE>\n"); // Write out a value for this input
                } // end for;
                ps.write("</INPUT>\n");
                
            } // End input loop
            ps.write("</CATEGORY>\n");
        }
        // Finish
        ps.write("</EXAMINATION>\n");
        ps.flush();
        //System.out.println("Saveexamination done");
    }
    
    // Should later return ExaminationModel
    public ExaminationModel readXMLExamination(File xmlFile) throws IOException {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        Document doc = null;
        
        //System.out.println("--ReadXMLexamination began");
        
        try {
            DocumentBuilder builder = docBuilderFactory.newDocumentBuilder();
            
            if ( xmlFile.canRead()) {
                //System.out.println("Yes could read file [" + xmlFile.getPath() + "]");
            } else {
                //System.out.println("Couldnt read " + xmlFile.getAbsolutePath());
            }
            FileInputStream fis = new FileInputStream(xmlFile);
            doc = builder.parse(fis);
            
        } catch (ParserConfigurationException e) {
            System.err.println("readXMLExamination got ParserConfigurationException: " + e.getMessage());
            
            
        } catch (SAXException e) {
            System.err.println("readXMLExamination got SAXException: " + e.getMessage());
        }
        
        NodeList categoryNodeList = doc.getElementsByTagName("CATEGORY");
        //System.out.println("Got " + categoryNodeList.getLength() + " categories");
        ExaminationModel exModel = new ExaminationModel();
        
        // Make categories
        
        for (int i=0; i < categoryNodeList.getLength(); i++) {
            
            String categoryName = "NameUnknown";
            Vector inputs = new Vector();
            
            Node catNode = categoryNodeList.item(i); // Get category no. i
            
            // Read its child nodes
            
            NodeList children = catNode.getChildNodes();
            
            for (int c = 0; c < children.getLength(); c++) {
                Node child = children.item(c); // Get child no. c
                
                // It can be NAME or INPUT
                String nodename = child.getNodeName();
                //System.out.println("Child nodename: [" + nodename+"]");
                
                if (nodename.equals("NAME")) { // Set the category name
                    if (debug) System.out.println("Matched CATEGORY NAME");
                    Node ch = child.getFirstChild();
                    String value = ch.getNodeValue();
                    
                    categoryName = value;
                } else if (nodename.equals("INPUT")) {
                    if (debug) System.out.println("Matched INPUT");
                    
                    if (debug) System.out.println("Working an input");
                    
                    // Create new inputModel
                    // TYPE, NAME, DESCRIPTION, VALUE
                    
                    // Get children of this INPUT node
                    
                    NodeList inputChildren = child.getChildNodes();
                    int inputType = 0;
                    String inputName = "Name not specified";
                    String inputDesc = "Description not specified";
                    String inputComment = "";
                    String inputValue = "";
                    String chooserPath = "";
                    boolean freeText = true;
                    boolean translateAble = true;
                    boolean isRequired    = false;
                    int newFieldType = FieldModel.FIELD_TYPE_SINGLE;
                    // For through all the children of an Input
                    for (int inpC = 0; inpC < inputChildren.getLength(); inpC++){
                        
                        Node inputChildNode = inputChildren.item(inpC);
                        
                        String name = inputChildNode.getNodeName();
                        Node ch = inputChildNode.getFirstChild();
                        String childValue = "";
                        if (ch != null)
                            childValue = ch.getNodeValue();
                        if (childValue == null)
                            childValue = "";
                        
                        // TYPE, NAME, DESCRIPTION, VALUE
                        
                        if (name.equals("TYPE")) {
                            if (debug) System.out.println("Matched INPUT TYPE");
                            //String value = inputChildNode.getFirstChild().getNodeValue();
                            String value = childValue;
                            
                            if (value.equals("TEXTAREA")) {
                                if (debug) System.out.println("Matched INPUT TYPE TEXTAREA");
                                inputType = InputModel.INPUT_TYPE_TEXTAREA;
                           /* } else if (value.equals("PICTURECHOOSER")) {
                                if (debug) System.out.println("Matched INPUT TYPE PICTURECHOOSER");
                                inputType = InputModel.TYPE_PICTURECHOOSER;
                            } */
                            }
                            else {
                                //System.out.println("Warning: Did not recognize INPUT TYPE value: " + value);
                            }
                        }else if (name.equals("NAME")) {
                            inputName = childValue;
                            
                        } else if (name.equals("DESCRIPTION")) {
                            inputDesc = childValue;

                        } else if (name.equals("COMMENT")) {
                            inputComment = childValue;

                        } else if (name.equals("VALUE")) {
                            inputValue = childValue;
                            
                        } else if (name.equals("FREETEXT")) {
                            
                            if (childValue.toUpperCase().equals("FALSE"))
                                freeText = false;
                            else
                                freeText = true;
                            
                        }else if (name.equals("TRANSLATE_ABLE")) { // nader 18/4
                            if (childValue.toUpperCase().equals("FALSE"))
                                translateAble = false;
                            else
                                translateAble = true;
                            
                        }else if (name.equals("REQUIRED")) { // nader 18/4
                            if (childValue.toUpperCase().equals("FALSE"))
                                isRequired = false;
                            else
                                isRequired = true;
                            
                        }else if (name.equals("FIELDTYPE")) {
                            String fieldT = childValue.toUpperCase();
                            if (fieldT.equals("SINGLE"))
                                newFieldType = FieldModel.FIELD_TYPE_SINGLE;
                            else if (fieldT.equals("MULTI"))
                                newFieldType = FieldModel.FIELD_TYPE_MULTI;
                            else if (fieldT.equals("NOTE"))
                                newFieldType = FieldModel.FIELD_TYPE_NOTE;
                            else if (fieldT.equals("INTERVAL"))
                                newFieldType = FieldModel.FIELD_TYPE_INTERVAL;
                            else if (fieldT.equals("QUESTION"))
                                newFieldType = FieldModel.FIELD_TYPE_QUESTION;
                            else if (fieldT.equals("IDENTIFICATION")){
                                newFieldType = FieldModel.FIELD_TYPE_IDENTIFICATION;
                            }
                            else {
                                System.out.println("WARNING: FIELDTYPE unknown [" + fieldT +"], defaulting to single");
                                newFieldType = FieldModel.FIELD_TYPE_SINGLE;
                            }
                        } else if (name.equals("PATH")) { // Path for PictureChooser
                            chooserPath = childValue;
                            if (chooserPath == null) chooserPath = "";
                        }
                    }
                  /*  String[] presets =  DatahandlingHandler.getInstance().getPresets(inputName);
                    if (presets == null) {
                        System.err.println("WARNING: Could not get any presets for input [ " + inputName + "]!");
                        presets = new String[1];
                        presets[0] = "";
                    }*/
                    String[] presets = {""};//new String[1];
                   // presets[0] = "";
                    PresetModel presetModel = new PresetModel(inputName,presets);
                    switch(inputType) {
                        case InputModel.INPUT_TYPE_TEXTAREA:
                            FieldModel    fModel = new CreatorFieldModel(inputName, newFieldType, presetModel, inputDesc, inputComment, inputValue, FieldModel.DEFAULT_LENGTH);
                            
                            fModel.setEditable(freeText);
                            fModel.setTranslateAble(translateAble);   // nader 18/4
                            fModel.setRequired(isRequired);   // nader 5/8
                            inputs.add(fModel); // add to category
                            
                            break;
                            
                       /* case InputModel.INPUT_TYPE_PICTURECHOOSER:
                            PictureChooserModel pModel = new PictureChooserModel(inputName,inputDesc,chooserPath); // No path
                            inputs.add(pModel);     // add to category
                        
                            break;*/
                        default:
                            System.err.println("Note: Error: Did not recognize type of <INPUT> (inputType = " + inputType);
                            break;
                    } // end type switch
                } // end node "<INPUT>"
            } // End of loop through children of <CATEGORY> for loop
            CategoryModel catModel = new CategoryModel(categoryName); // Create the categoryModel
            
            //Add everything in the inputs vector
            for (Iterator it = inputs.iterator(); it.hasNext(); ) {
                InputModel inpModel = (InputModel) it.next();
                catModel.addInput(inpModel);
                //System.out.println("Added an input!");
            }
            inputs.clear();   // Clear inputs
            // Add the category to the ExaminationModel
            
            exModel.addCategory(catModel);
        } // Done
        //System.out.println("--ReadXMLexamination done.");
        return exModel;
    }
    
    public static void main(String[] args) {
        String filename = "/users/dtek/d97/d97nix/Java/medview/testmall.xml";
        File fileToRead = new File(filename);
        try {
            new XMLFileHandler().readXMLExamination(fileToRead);
        } catch (IOException e) {
            System.out.println("Could not read: " + e.getMessage());
        }
        
    }
}
