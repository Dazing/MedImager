/*
 *
 * $Id: MedFormTemplateReader.java,v 1.11 2010/07/01 07:37:14 oloft Exp $
 *
 * $Log: MedFormTemplateReader.java,v $
 * Revision 1.11  2010/07/01 07:37:14  oloft
 * MR 4.5, minor edits
 *
 * Revision 1.10  2010/06/28 07:12:39  oloft
 * MR 4.5 dev
 *
 * Revision 1.9  2008/07/28 06:56:51  it2aran
 * * Package now includes
 * 	termdefinitions
 * 	termvalues
 * 	database
 * 	template
 * 	translator
 * and can be changed withour restarting (both in MSummary and MRecords
 * * removed more termdefinitions checks (the bug that slowed down MRecords) in MedSummary which should make it load faster
 *
 * Revision 1.8  2008/01/31 13:23:27  it2aran
 * Cariesdata handler that retrieves caries data from an external database
 * Some bugfixes
 *
 * Revision 1.7  2007/10/17 15:17:04  it2aran
 * Medrecords 4.3 Beta 1
 *
 * Revision 1.6  2006/11/15 16:39:03  oloft
 * Minor fixes
 *
 * Revision 1.5  2006/11/03 22:37:01  oloft
 * Not alla attibutes, e.g., freetext were set
 *
 * Revision 1.4  2005/08/23 13:38:45  erichson
 * Changed translation of TEXT to NOTE, and separated exception handling when preset value fetching fails
 *
 * Revision 1.3  2005/08/23 09:24:39  erichson
 * Updated which MR inputs correspond to mForm input types
 *
 * Revision 1.2  2005/07/18 13:35:16  erichson
 * Updated.
 *
 * Revision 1.1  2005/07/06 12:04:47  erichson
 * First check-in
 *
 *
 */

package medview.medrecords.data;

import java.io.*;
import javax.xml.parsers.*;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.w3c.dom.*;

import medview.medrecords.models.*;

/**
* Parses an XML Template file (MedForm format) into an ExaminationModel object.
 *
 * @author Nils Erichson
 */
public class MedFormTemplateReader 
extends misc.foundation.util.xml.XMLHelper    
{
	
    private DocumentBuilder builder;
    private String fileLocation;    
    
    /** Creates a new instance of GraphXMLParser */
    public MedFormTemplateReader() throws ParserConfigurationException, SAXException
    {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        builder = dbf.newDocumentBuilder();                
    }            
    
    public ExaminationModel readXMLExamination(File file)
        throws ParserConfigurationException, IOException, SAXException, InvalidTemplateException
    {
        fileLocation = file.getPath();                
		
        Document doc = builder.parse(file);
        
        return readXMLExamination(doc);
    }
    
    public ExaminationModel readXMLExamination(Document doc)
        throws ParserConfigurationException, IOException, SAXException, InvalidTemplateException    
    {
        ExaminationModel examinationModel = new ExaminationModel();
        
        // Start at <EXAMINATION> tag        
        NodeList nList = doc.getElementsByTagName("EXAMINATION");
        
        if (nList.getLength() < 0)
        {
            throw new InvalidTemplateException(fileLocation,"Couldn't find <EXAMINATION> tag.");
        }
		
        Node examinationNode = nList.item(0);
        
        // Get children of <EXAMINATION>
        
        NodeList examinationChildren = examinationNode.getChildNodes();                                                
		
        // Loop over <CATEGORY> nodes
        for (int i = 0; i < examinationChildren.getLength(); i++)
        {
            Node thisNode = examinationChildren.item(i);
            String examinationChildName = thisNode.getNodeName();                        
            
            if (examinationChildName.equalsIgnoreCase("CATEGORY"))
            {
                processCategoryNode(thisNode, examinationModel);
            }
            
            // We don't care about anything else here
        }
		
        return examinationModel;
		
    }
    
    /**
		* Process a <CATEGORY> node
     */
    private void processCategoryNode(Node categoryNode, ExaminationModel examinationModel)
        throws InvalidTemplateException
    {
        CategoryModel catModel = new CategoryModel();
        //get attributes for <CATEGEORY>, currently only "visible"
        NamedNodeMap attrNodeMap = categoryNode.getAttributes();

        for (int i = 0; i < attrNodeMap.getLength(); i++)
        {
			
            Node thisAttr = attrNodeMap.item(i);
            String attrName = thisAttr.getNodeName();

            if (attrName.equalsIgnoreCase("visible"))
            {
                catModel.setVisible(getBooleanValue(thisAttr));
            }
        }
        NodeList categoryChildren = categoryNode.getChildNodes();
        int childCount = categoryChildren.getLength();
        
        if (childCount <= 0)
            return;
        
        for (int i = 0; i < childCount; i++)
        {
            
            Node node = categoryChildren.item(i);
            String nodeName = node.getNodeName();
			
			// Which should it be?
			// Ideally, hte catModel should be extended to have both a title and 
			// a category
			
            //if (nodeName.equalsIgnoreCase("NODE")) // category name
			if (nodeName.equalsIgnoreCase("HEADER")) // category title
            {            
                String name = getChildValue(node);
                catModel.setTitle(name);                
            }
            
            // TODO: The <HEADER> is not used in MedRecords.
            
            else if (nodeName.equalsIgnoreCase("INPUT"))
            {
                processInputNode(node, catModel);
            }
            else if (nodeName.equalsIgnoreCase("INFO"))
            {
                processInfoNode(node, catModel);
            }
            
        }
        
        examinationModel.addCategory(catModel);
    }
     /**
		* Process an <INFO node
     */
    private void processInfoNode(Node inputNode, CategoryModel catModel)
    throws InvalidTemplateException
    {
        String text = null;
        String subheader = null;
        //get the children of the info node

        NodeList inputChildren = inputNode.getChildNodes();

        int childCount = inputChildren.getLength();

        if (childCount <= 0)
            return;

        InputModel inputModel = null;
        for (int i = 0; i < childCount; i++)
        {
            Node thisChild = inputChildren.item(i);
            String childName = inputChildren.item(i).getNodeName();

            if (childName.equalsIgnoreCase("TEXT"))
            {
                text = getChildValue(thisChild);
                inputModel = new InfoModel(text,InfoModel.TEXT);
                catModel.addInput(inputModel);
            }
            else if (childName.equalsIgnoreCase("SUBHEADER"))
            {
                subheader = getChildValue(thisChild);
                inputModel = new InfoModel(subheader,InfoModel.SUBHEADER);
                catModel.addInput(inputModel);
            }

        }

    }

    /**
		* Process an <INPUT> node
     */
    private void processInputNode(Node inputNode, CategoryModel catModel)
        throws InvalidTemplateException
    {
        
        boolean freetext = false;
        boolean translatable = false;
        boolean required = false;
        boolean sorted = false;
        
        int inputType = InputModel.INPUT_TYPE_TEXTAREA;
        int fieldType = FieldModel.FIELD_TYPE_SINGLE;
        
        // First process the parameters in the <input> node!
        
        NamedNodeMap attrNodeMap = inputNode.getAttributes();
        
        for (int i = 0; i < attrNodeMap.getLength(); i++)
        {
            Node thisAttr = attrNodeMap.item(i);
            String attrName = thisAttr.getNodeName();
            
            if (attrName.equalsIgnoreCase("type"))
            {
                String typeName = thisAttr.getNodeValue();
                
                if (typeName.equalsIgnoreCase("identification"))
                {
                    fieldType = FieldModel.FIELD_TYPE_IDENTIFICATION;
					freetext = true; // mForm and FormEdit mismatch, FormEdit sets true mForm false
                }
                else if (typeName.equalsIgnoreCase("note"))
                {
                    fieldType = FieldModel.FIELD_TYPE_NOTE;
                }
                else if (typeName.equalsIgnoreCase("single"))
                {
                    fieldType = FieldModel.FIELD_TYPE_SINGLE;
                }
                else if (typeName.equalsIgnoreCase("multi"))
                {
                    fieldType = FieldModel.FIELD_TYPE_MULTI;
                }
                else if (typeName.equalsIgnoreCase("question"))
                {
                    fieldType = FieldModel.FIELD_TYPE_QUESTION;
                }
                else if (typeName.equalsIgnoreCase("interval"))
                {
                    /* TODO: Not correct - mForm type 'interval' is different */
                    fieldType = FieldModel.FIELD_TYPE_INTERVAL;
                }
                else if (typeName.equalsIgnoreCase("vas"))
                {
                    /* We don't have a VAS-type input today, but the traditional way to do it
					in MedRecords is just use a single-type input, with presets */
                    fieldType = FieldModel.FIELD_TYPE_SINGLE; 
                }
                else if (typeName.equalsIgnoreCase("text"))
                {
					// Probably the mapping most similar to text in mForm
                    fieldType = FieldModel.FIELD_TYPE_SINGLE; 
                    freetext = true;
                }
                else if (typeName.equalsIgnoreCase("image"))
                {
                    inputType = InputModel.INPUT_TYPE_PICTURECHOOSER;                    
                }
                else if (typeName.equalsIgnoreCase("mineralization"))
                {
                    inputType = InputModel.INPUT_TYPE_MINERALIZATION;
                }
                else if (typeName.equalsIgnoreCase("trauma"))
                {
                    inputType = InputModel.INPUT_TYPE_TRAUMA;
                }
                else if (typeName.equalsIgnoreCase("erosion"))
                {
                    inputType = InputModel.INPUT_TYPE_EROSION;
                }
            }
			
            else if (attrName.equalsIgnoreCase("freetext"))
            {    
                // This parameter should not override if freetext has already been set (i.e. we have the "text" type)
                
                freetext = (freetext || getBooleanValue(thisAttr));    
                
            }
            else if (attrName.equalsIgnoreCase("translatable"))
            {
                translatable = getBooleanValue(thisAttr);                
            }
            else if (attrName.equalsIgnoreCase("sorted"))
            {
                sorted = getBooleanValue(thisAttr);
            }
            else if (attrName.equalsIgnoreCase("required"))
            {
                required = getBooleanValue(thisAttr);
            }
            // TODO: Visible is ignored...
        }
		
        
        // Then the children of the <INPUT> node
        
        String term = null;
        String description = null;
        String comment = null;
        
        NodeList inputChildren = inputNode.getChildNodes();
        
        int childCount = inputChildren.getLength();
        
        if (childCount <= 0)
            return;
        
        for (int i = 0; i < childCount; i++)
        {
            Node thisChild = inputChildren.item(i);
            String childName = inputChildren.item(i).getNodeName();
            
            if (childName.equalsIgnoreCase("TERM"))
            {
                term = getChildValue(thisChild);                
            }
            else if (childName.equalsIgnoreCase("DESCRIPTION"))
            {
                description = getChildValue(thisChild);
            }
            else if (childName.equalsIgnoreCase("COMMENT"))
            {
                comment = getChildValue(thisChild);
            }
            // TODO: The <DEPENDENCY> field is not used in MedRecords yet.
        }
        
        // Presets
        String[] presets = null;
		
        try
        {
			presets = DataHandlingExtensions.instance().getPresets(term);
			// System.out.println("Got these presets for term [" + term + "]: " + Arrays.asList(presets));
        }
        catch (medview.datahandling.NoSuchTermException exc)
        {						
			System.out.println("No presets - NoSuchTermException for term [" + term + "]. Reason: " + exc.getMessage());
			presets = new String[0];
        }
        catch (IOException exc)
        {						
			System.out.println("No presets [" + term + "]. IOException: " + exc.getMessage());
			presets = new String[0];
        }
        
		
        InputModel inputModel;
        
        if (inputType == InputModel.INPUT_TYPE_PICTURECHOOSER)
        {
            inputModel = new PictureChooserModel(term, 
                                                 description,
                                                 comment,
                                                 medview.medrecords.data.PreferencesModel.instance().getImageInputLocation(), 
                                                 MRConst.MrConstPicDuration);
        }
        else if(inputType == InputModel.INPUT_TYPE_TRAUMA)
        {
            inputModel = new TraumaModel();
        }
        else if(inputType == InputModel.INPUT_TYPE_EROSION)
        {
            inputModel = new ErosionModel();
        }
        else if(inputType == InputModel.INPUT_TYPE_MINERALIZATION)
        {
            inputModel = new MineralizationModel();
        }
        else
        {        
            PresetModel presetModel = new PresetModel(term, presets,sorted);
            inputModel = new FieldModel(term, // name
										fieldType, // in_fieldType, 
										presetModel, 
										description,
                                        comment,
                                        ""); // initialText);
											 //initialLength); // Optional parameter
			// properties not set in constructor
			inputModel.setEditable(freetext);
			inputModel.setTranslateAble(translatable);
			inputModel.setRequired(required);
            inputModel.setSorted(sorted);
			
        }
        
        catModel.addInput(inputModel);
    }                    
    
    /**
		* main method for testing purposes
     */ 
    public static void main(String[] args)
    {
        
        try 
	{
		MedFormTemplateReader parser = new MedFormTemplateReader();
		ExaminationModel model = parser.readXMLExamination(new File("/home/erichson/medview/medformtemplate.xml"));
		// model.dump();
		
	} catch (SAXParseException e)
	{
		System.out.println("Saxparseexception: " + e.getMessage());
		System.out.println("At: Line: " + e.getLineNumber() + ", column: " + e.getColumnNumber());        
		System.out.println("Pub id: " + e.getPublicId() + ", system id: " + e.getSystemId());        
	} catch (Exception e)
	{
		System.out.println("exception: " + e.getClass() +": " + e.getMessage());
		e.printStackTrace();
	}
    }
}

