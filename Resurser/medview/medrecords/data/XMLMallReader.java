/*
 * XMLMallReader.java
 *
 * Created on den 26 juni 2002, 14:12
 *
 * $Id: XMLMallReader.java,v 1.18 2007/10/17 15:17:04 it2aran Exp $
 *
 * $Log: XMLMallReader.java,v $
 * Revision 1.18  2007/10/17 15:17:04  it2aran
 * Medrecords 4.3 Beta 1
 *
 * Revision 1.17  2005/07/18 13:32:08  erichson
 * Removed daysAgo variable, uses MRConst instead
 *
 * Revision 1.16  2005/04/29 09:35:15  erichson
 * Added exception handling for when the XML parsing fails (previously the application would get NullPointerException and crash)
 *
 */

package medview.medrecords.data;

import java.io.*;

import java.util.*;

import javax.xml.parsers.*;

import medview.datahandling.*;

import medview.medrecords.models.*;

import org.w3c.dom.*;
import org.xml.sax.*;

/**
 *
 * @author  nader
 * @version
 */
public class XMLMallReader
{
	public XMLMallReader()
	{
	}

	public ExaminationModel readXMLExamination(File xmlFile) throws IOException, SAXException
	{
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		
		Document doc = null;

		try
		{
			DocumentBuilder builder = docBuilderFactory.newDocumentBuilder();
			
			FileInputStream fis = new FileInputStream(xmlFile);
			
			doc = builder.parse(fis); // throws SAXException
		}
		catch (ParserConfigurationException e)
		{
		    String errorString = "ParserConfigurationException: " + e.getMessage();
                    System.err.println(errorString);
                    throw new IOException(errorString);
		}		

                if (doc == null)
                {
                    throw new IOException("readXMLExamination(): XML parsing failed - document is null");
                }
                
		NodeList categoryNodeList = doc.getElementsByTagName("CATEGORY");
		
		ExaminationModel exModel = new ExaminationModel();
		
		return makeCategories(exModel, categoryNodeList);
	}

	private ExaminationModel makeCategories(ExaminationModel exModel, NodeList categoryNodeList)
	{
		MedViewDataHandler mDH = MedViewDataHandler.instance();

		for (int i = 0; i < categoryNodeList.getLength(); i++)
		{
			Vector inputs = new Vector();
			
			Node catNode = categoryNodeList.item(i);
			
			NodeList children = catNode.getChildNodes();
			
			String categoryName = makeInputModels(children, inputs);
			
			CategoryModel catModel = new CategoryModel(categoryName);

			for (Iterator it = inputs.iterator(); it.hasNext(); )
			{
				InputModel inpModel = (InputModel)it.next();
				
				catModel.addInput(inpModel);
			}
			
			inputs.clear();
			
			exModel.addCategory(catModel);
		}

		CategoryModel photoModel = createPictureCategory(mDH.getLanguageString(MedViewLanguageConstants.
		
			LABEL_SELECT_EXAMINATION_IMAGES_LS_PROPERTY));

		if (photoModel != null)
		{
			exModel.addCategory(photoModel);
		}

		return exModel;
	}

	private String makeInputModels(NodeList children, Vector inputs)
	{
		String categoryName = "NameUnknown";

		for (int c = 0; c < children.getLength(); c++)
		{
			Node child = children.item(c);
			
			String nodename = child.getNodeName();

			if (nodename.equals("NAME"))
			{
				Node ch = child.getFirstChild();
				
				String value = ch.getNodeValue();
				
				categoryName = value;
			}
			else if (nodename.equals("INPUT"))
			{
				NodeList inputChildren = child.getChildNodes();
				
				int inputType = 0;
				
				String inputName = "Name not specified";
				
				String inputDesc = "Description not specified";
				
				String comment = "";

                String inputValue = "";
				
				String chooserPath = "";
				
				boolean freeText = true;
				
				boolean translateAble = true;

                boolean sorted = true;

                boolean isRequired = false;
				
				int newFieldType = FieldModel.FIELD_TYPE_SINGLE;

				for (int inpC = 0; inpC < inputChildren.getLength(); inpC++)
				{
					Node inputChildNode = inputChildren.item(inpC);
					
					String name = inputChildNode.getNodeName();
					
					Node ch = inputChildNode.getFirstChild();
					
					String childValue = "";
					
					if (ch != null)
					{
						childValue = ch.getNodeValue();
					}
					if (childValue == null)
					{
						childValue = "";
					}

					if (name.equals("TYPE"))
					{
						String value = childValue;

						if (value.equals("TEXTAREA"))
						{
							inputType = InputModel.INPUT_TYPE_TEXTAREA;
						}
					}
					else if (name.equals("NAME"))
					{
						inputName = childValue;

					}
					else if (name.equals("DESCRIPTION"))
					{
						inputDesc = childValue;

					}
					else if (name.equals("VALUE"))
					{
						inputValue = childValue;

					}
					else if (name.equals("FREETEXT"))
					{
						if (childValue.toUpperCase().equals("FALSE"))
						{
							freeText = false;
						}
						else
						{
							freeText = true;
						}

					}
					else if (name.equals("TRANSLATE_ABLE"))
					{
						if (childValue.toUpperCase().equals("FALSE"))
						{
							translateAble = false;
						}
						else
						{
							translateAble = true;
						}

					}
					else if (name.equals("REQUIRED"))
					{
						if (childValue.toUpperCase().equals("FALSE"))
						{
							isRequired = false;
						}
						else
						{
							isRequired = true;
						}

					}
					else if (name.equals("FIELDTYPE"))
					{
						String fieldT = childValue.toUpperCase();
						
						if (fieldT.equals("SINGLE"))
						{
							newFieldType = FieldModel.FIELD_TYPE_SINGLE;
						}
						else if (fieldT.equals("MULTI"))
						{
							newFieldType = FieldModel.FIELD_TYPE_MULTI;
						}
						else if (fieldT.equals("NOTE"))
						{
							newFieldType = FieldModel.FIELD_TYPE_NOTE;
						}
						else if (fieldT.equals("INTERVAL"))
						{
							newFieldType = FieldModel.FIELD_TYPE_INTERVAL;
						}
						else if (fieldT.equals("QUESTION"))
						{
							newFieldType = FieldModel.FIELD_TYPE_QUESTION;
						}
						else if (fieldT.equals("IDENTIFICATION"))
						{
							newFieldType = FieldModel.FIELD_TYPE_IDENTIFICATION;
						}
						else
						{
							System.out.println("WARNING: FIELDTYPE unknown [" + fieldT +
								
								"], defaulting to single");
								
							newFieldType = FieldModel.FIELD_TYPE_SINGLE;
						}
					}
					else if (name.equals("PATH"))
					{
					}
				}
				
				String[] presets = null;
				
				try
				{
					presets = DataHandlingExtensions.instance().getPresets(inputName);
				}
				catch (Exception exc)
				{						
					presets = new String[0];
				}
				
				PresetModel presetModel = new PresetModel(inputName, presets);
				
				switch (inputType)
				{
					case InputModel.INPUT_TYPE_TEXTAREA:
					
						FieldModel fModel = new FieldModel(inputName, newFieldType, presetModel,
							
							inputDesc, comment, "", FieldModel.DEFAULT_LENGTH);

						fModel.setEditable(freeText);
						
						fModel.setTranslateAble(translateAble);
						
						fModel.setRequired(isRequired);
						
						inputs.add(fModel);
						
						break;

					default:
						System.err.println("Note: Error: Did not recognize type of <INPUT> (inputType = " +
							
							inputType);
							
						break;
				}
			}
		}
		
		return categoryName;
	}

	private CategoryModel createPictureCategory(String mDesc)
	{
		PreferencesModel prefs = PreferencesModel.instance();

		String aPath = prefs.getImageInputLocation();
		
		int daysAgo = MRConst.MrConstPicDuration; 
		
		String catName = prefs.getImageCategoryName();
		
		String photoNod = prefs.getImageTermName();

		if (daysAgo <= 0)
		{
			return null;
		}
		
		if (aPath == null || aPath.length() == 0)
		{
			return null;
		}
		
		if (catName == null || catName.length() == 0)
		{
			catName = "Photo_Category";
		}
		
		if (photoNod == null || photoNod.length() == 0)
		{
			photoNod = "Photo";
		}
		
		CategoryModel catModel = new CategoryModel(catName);
		
		PictureChooserModel pModel = new PictureChooserModel(photoNod, mDesc, "", aPath, daysAgo);

		catModel.addInput(pModel);
		
		return catModel;
	}
}
