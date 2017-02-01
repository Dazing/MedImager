/*
 * $Id: MeduwebTemplateAndTranslatorDataHandler.java,v 1.1 2003/07/21 21:55:08 d99figge Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl, mods for Meduweb by Figge
 * --------------------------------
 */

package medview.meduweb.data;

import java.awt.*;
import java.io.*;
import java.text.*;
import java.util.*;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.BadLocationException;
import javax.swing.ImageIcon;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import medview.common.template.*;
import medview.common.translator.*;
import medview.datahandling.*;

import misc.foundation.*;
import misc.foundation.io.*;

import org.apache.crimson.jaxp.*;
import org.apache.xalan.processor.*;
import org.w3c.dom.*;
import org.xml.sax.*;

/**
 *
 */
public class MeduwebTemplateAndTranslatorDataHandler implements medview.meduweb.data.TemplateAndTranslatorDataHandler
{

	public String[] getSectionNames(String filePath) throws NoSuchTemplateException
	{
		return null;
	}



	public String parseIdentifier(String location)
	{
		if (location == null) { return null; }

		if (location.endsWith(fileSep))
		{
			int i = location.lastIndexOf(fileSep);

			location = location.substring(0, i);
		}

		int sLoc = location.lastIndexOf(fileSep);

		if (sLoc == -1) { return location; }

		return location.substring(sLoc + 1);
	}








	public void saveTemplate(TemplateModelWrapper wrapper) throws CouldNotSaveException
	{
		try
		{
			// extract data from wrapper

			TemplateModel model = wrapper.getTemplateModel();

			String assTransLoc = wrapper.getAssociatedTranslatorLocation();

			String location = wrapper.getTemplateModelLocation();

			if (model == null || location == null)
			{
				String mess = "Can not save template when the information " +

							  "contained in the wrapper object is incomplete.";

				throw new CouldNotSaveException(mess);
			}


			// create document node

			org.w3c.dom.Document documentNode = builder.newDocument();


			// create 'template model' root node

			org.w3c.dom.Element templateNode = documentNode.createElement(ELEMENT_TEMPLATE_MODEL);

			templateNode.setAttribute(ATTRIBUTE_XMLTATDH_VERSION, XMLTATDH_VERSION);

			documentNode.appendChild(templateNode);


			// create 'associated translator' node <- append to 'template model' root node

			org.w3c.dom.Element associatedTranslatorNode = documentNode.createElement(ELEMENT_ASS_TRANS);

			if (assTransLoc == null) { assTransLoc = ASS_TRANS_NOT_SET; }

			associatedTranslatorNode.setAttribute(ATTRIBUTE_LOCATION, assTransLoc);

			templateNode.appendChild(associatedTranslatorNode);


			// create 'styled document' node <- append to 'template model' root node

			org.w3c.dom.Element styledDocumentNode = documentNode.createElement(ELEMENT_STYLED_DOCUMENT);

			templateNode.appendChild(styledDocumentNode);


			// create 'styled document text' node <- append to 'styled document' node

			String documentContent = model.getDocument().getText(0, model.getDocument().getLength());

			org.w3c.dom.CDATASection styledDocumentTextNode = documentNode.createCDATASection(documentContent);

			styledDocumentNode.appendChild(styledDocumentTextNode);


			// create 'styled document section' node <- append to 'styled document' node

			javax.swing.text.Element sectionElement = model.getDocument().getDefaultRootElement();

			org.w3c.dom.Element styledDocumentSectionNode = documentNode.createElement(ELEMENT_STYLED_DOCUMENT_SECTION);

			styledDocumentNode.appendChild(styledDocumentSectionNode);


			// attach the 'paragraph' nodes to the 'styled document section' node

			org.w3c.dom.Element currParagraphNode = null;

			javax.swing.text.Element currParagraphElement = null;

			javax.swing.text.AttributeSet currAttributeSet = null;

			Enumeration currAttributeEnum = null;

			Object currAttributeName = null;

			Object currAttributeValue = null;

			org.w3c.dom.Element currContentNode = null;

			javax.swing.text.Element currContentElement = null;

			for (int ctr1=0; ctr1<sectionElement.getElementCount(); ctr1++)
			{

				// set up and attach current 'paragraph' node

				currParagraphNode = documentNode.createElement(ELEMENT_STYLED_DOCUMENT_PARAGRAPH);

				currParagraphElement = sectionElement.getElement(ctr1);

				currParagraphNode.setAttribute(ATTRIBUTE_START, currParagraphElement.getStartOffset() + "");

				currParagraphNode.setAttribute(ATTRIBUTE_END, currParagraphElement.getEndOffset() + "");

				currAttributeSet = currParagraphElement.getAttributes();

				currAttributeEnum = currAttributeSet.getAttributeNames();

				while (currAttributeEnum.hasMoreElements())
				{
					currAttributeName = currAttributeEnum.nextElement();

					currAttributeValue = currAttributeSet.getAttribute(currAttributeName);

					if (shouldAddAttributeToXML(currAttributeName))
					{
						currAttributeName = adaptObjectToXML(currAttributeName);

						currAttributeValue = adaptObjectToXML(currAttributeValue);
					}
					else { continue; }

					currParagraphNode.setAttribute(currAttributeName.toString(), currAttributeValue.toString());
				}

				styledDocumentSectionNode.appendChild(currParagraphNode);


				// set up and attach content' nodes to current 'paragraph' node

				for (int ctr2=0; ctr2<currParagraphElement.getElementCount(); ctr2++)
				{
					currContentNode = documentNode.createElement(ELEMENT_STYLED_DOCUMENT_CONTENT);

					currContentElement = currParagraphElement.getElement(ctr2);

					currContentNode.setAttribute(ATTRIBUTE_START, currContentElement.getStartOffset() + "");

					currContentNode.setAttribute(ATTRIBUTE_END, currContentElement.getEndOffset() + "");

					currAttributeSet = currContentElement.getAttributes();

					currAttributeEnum = currAttributeSet.getAttributeNames();

					while (currAttributeEnum.hasMoreElements())
					{
						currAttributeName = currAttributeEnum.nextElement();

						currAttributeValue = currAttributeSet.getAttribute(currAttributeName);

						if (shouldAddAttributeToXML(currAttributeName))
						{
							currAttributeName = adaptObjectToXML(currAttributeName);

							currAttributeValue = adaptObjectToXML(currAttributeValue);
						}
						else { continue; }

						currContentNode.setAttribute(currAttributeName.toString(), currAttributeValue.toString());
					}

					currParagraphNode.appendChild(currContentNode);
				}
			}


			// create 'sections' node and add sections

			org.w3c.dom.Element sectionsNode = documentNode.createElement(ELEMENT_SECTIONS);

			templateNode.appendChild(sectionsNode);


			// attach each section to 'sections' node

			SectionModel[] sectionModels = model.getAllSectionModels();

			SectionModel currSectionModel = null;

			org.w3c.dom.Element currSectionNode = null;

			org.w3c.dom.Element currTermNode = null;

			TermModel[] termModels = null;

			TermModel currTermModel = null;

			for (int ctr1=0; ctr1<sectionModels.length; ctr1++)
			{

				// set up and attach current 'section' node

				currSectionModel = sectionModels[ctr1];

				currSectionNode = documentNode.createElement(ELEMENT_SECTION);

				currSectionNode.setAttribute(ATTRIBUTE_NAME, currSectionModel.getName());

				currSectionNode.setAttribute(ATTRIBUTE_START, currSectionModel.getStartOffset() + "");

				currSectionNode.setAttribute(ATTRIBUTE_END, currSectionModel.getEndOffset() + "");

				sectionsNode.appendChild(currSectionNode);


				// set up and attach 'term' nodes to current 'section' node

				termModels = currSectionModel.getTermModels();

				for (int ctr2=0; ctr2<termModels.length; ctr2++)
				{
					currTermModel = termModels[ctr2];

					currTermNode = documentNode.createElement(ELEMENT_TERM);

					currTermNode.setAttribute(ATTRIBUTE_START, currTermModel.getStartOffset() + "");

					currTermNode.setAttribute(ATTRIBUTE_END, currTermModel.getEndOffset() + "");

					currSectionNode.appendChild(currTermNode);
				}
			}


			// write the DOM structure to an XML file

			Transformer transformer = transformerFactory.newTransformer();

			DOMSource source = new DOMSource(documentNode);

			StreamResult result = new StreamResult(new File(location));

			transformer.transform(source, result);
		}
		catch (javax.swing.text.BadLocationException e)
		{
			throw new CouldNotSaveException(e.getMessage());
		}
		catch (TransformerConfigurationException e)
		{
			throw new CouldNotSaveException(e.getMessage());
		}
		catch (TransformerException e)
		{
			throw new CouldNotSaveException(e.getMessage());
		}
		catch (Exception e)
		{
			throw new CouldNotSaveException(e.getMessage());
		}
	}


	
	public TemplateModelWrapper loadTemplate(String filePath) throws InvalidVersionException, CouldNotLoadException
	{
		try
		{
			// set up the returned wrapper object

			TemplateModelWrapper wrapper = new TemplateModelWrapper();

			wrapper.setTemplateModelLocation(filePath);


			// set up wrapped template model

			TemplateModel model = new TemplateModel();

			javax.swing.text.StyledDocument document = model.getDocument();

			model.setAllowsNormalEditing(true);


			// obtain document and root node

			org.w3c.dom.Document documentNode = builder.parse(new File(filePath));

			org.w3c.dom.Element templateNode = (org.w3c.dom.Element) documentNode.getFirstChild();

			String version = templateNode.getAttribute(ATTRIBUTE_XMLTATDH_VERSION);

			if (!version.equals(XMLTATDH_VERSION))
			{
				String err = "The XML template and translator datahandler has version " +

							  XMLTATDH_VERSION + ", and cannot parse the specified xml " +

							  "file (" + filePath + ") with a version number of " + version +

							  ". Make sure you have the latest version of the XML template " +

							  "and translator datahandler to be able to parse later versions " +

							  "of template or translator files.";

				throw new InvalidVersionException(err);
			}


			// obtain root node children

			org.w3c.dom.NodeList children = templateNode.getChildNodes();

			org.w3c.dom.Element associatedTranslatorNode = (org.w3c.dom.Element) children.item(0);

			//org.w3c.dom.Element styledDocumentNode = (org.w3c.dom.Element) children.item(1);
			org.w3c.dom.Node styledDocumentNode = (org.w3c.dom.Node) children.item(1);
			org.w3c.dom.Element sectionsNode = (org.w3c.dom.Element) children.item(2);


			// obtain associated translator information

			String aTL = associatedTranslatorNode.getAttribute(ATTRIBUTE_LOCATION);

			if (!aTL.equals(ASS_TRANS_NOT_SET)) { wrapper.setAssociatedTranslatorLocation(aTL); }


			// build document content
			org.w3c.dom.Node apan = styledDocumentNode.getFirstChild();

			org.w3c.dom.CDATASection styledDocumentTextNode = (org.w3c.dom.CDATASection)apan;

			String cdataContent = styledDocumentTextNode.getData();

			document.remove(0, document.getLength());

			document.insertString(0, cdataContent, null);


			// mark paragraph and content elements with attributes

			org.w3c.dom.Element styledDocumentSectionNode = (org.w3c.dom.Element) styledDocumentNode.getLastChild();

			org.w3c.dom.NodeList paragraphNodes = styledDocumentSectionNode.getChildNodes();

			org.w3c.dom.NodeList contentNodes = null;

			org.w3c.dom.NamedNodeMap currParagraphNodeAttributeMap = null;

			org.w3c.dom.NamedNodeMap currContentNodeAttributeMap = null;

			javax.swing.text.MutableAttributeSet currParagraphAttributes = null;

			javax.swing.text.MutableAttributeSet currContentAttributes = null;

			String currAttributeNodeName = null;

			org.w3c.dom.Element currParagraphNode = null;

			org.w3c.dom.Element currContentNode = null;

			org.w3c.dom.Attr currAttributeNode = null;

			int currParagraphLength = -1;

			int currParagraphStart = -1;

			int currParagraphEnd = -1;

			int currContentLength = -1;

			int currContentStart = -1;

			int currContentEnd = -1;

			for (int ctr1=0; ctr1<paragraphNodes.getLength(); ctr1++)
			{

				// set paragraph attributes from paragraph node

				currParagraphNode = (org.w3c.dom.Element) paragraphNodes.item(ctr1);

				currParagraphAttributes = new javax.swing.text.SimpleAttributeSet();

				currParagraphNodeAttributeMap = currParagraphNode.getAttributes();

				for (int ctr2=0; ctr2<currParagraphNodeAttributeMap.getLength(); ctr2++)
				{
					currAttributeNode = (org.w3c.dom.Attr) currParagraphNodeAttributeMap.item(ctr2);

					currAttributeNodeName = currAttributeNode.getName();

					if (currAttributeNodeName.equals(ATTRIBUTE_START))
					{
						currParagraphStart = Integer.parseInt(currAttributeNode.getValue());
					}
					else if (currAttributeNodeName.equals(ATTRIBUTE_END))
					{
						currParagraphEnd = Integer.parseInt(currAttributeNode.getValue());
					}
					else
					{
						insertAttributeNode(currAttributeNode, currParagraphAttributes);
					}
				}

				currParagraphLength = currParagraphEnd - currParagraphStart + 1;

				document.setParagraphAttributes(currParagraphStart, currParagraphLength, currParagraphAttributes, false);


				// set content attributes from paragraph child nodes

				contentNodes = currParagraphNode.getChildNodes();

				for (int ctr2=0; ctr2<contentNodes.getLength(); ctr2++)
				{
					currContentNode = (org.w3c.dom.Element) contentNodes.item(ctr2);

					currContentAttributes = new javax.swing.text.SimpleAttributeSet();

					currContentNodeAttributeMap = currContentNode.getAttributes();

					for (int ctr3=0; ctr3<currContentNodeAttributeMap.getLength(); ctr3++)
					{
						currAttributeNode = (Attr) currContentNodeAttributeMap.item(ctr3);

						currAttributeNodeName = currAttributeNode.getName();

						if (currAttributeNodeName.equals(ATTRIBUTE_START))
						{
							currContentStart = Integer.parseInt(currAttributeNode.getValue());
						}
						else if (currAttributeNodeName.equals(ATTRIBUTE_END))
						{
							currContentEnd = Integer.parseInt(currAttributeNode.getValue());
						}
						else
						{
							insertAttributeNode(currAttributeNode, currContentAttributes);
						}
					}

					currContentLength = currContentEnd - currContentStart;

					document.setCharacterAttributes(currContentStart, currContentLength, currContentAttributes, false);
				}
			}


			// mark sections and terms in created document

			org.w3c.dom.NodeList sectionNodeList = sectionsNode.getChildNodes();

			org.w3c.dom.NodeList termNodeList = null;

			org.w3c.dom.Element currSectionNode = null;

			org.w3c.dom.Element currTermNode = null;

			String currSectionName = null;

			int currSectionStart = -1;

			int currSectionEnd = -1;

			int currTermStart = -1;

			int currTermEnd = -1;

			int currTermLength = -1;

			for (int ctr1=0; ctr1<sectionNodeList.getLength(); ctr1++)
			{

				// mark section

				currSectionNode = (Element) sectionNodeList.item(ctr1);

				currSectionName = currSectionNode.getAttribute(ATTRIBUTE_NAME);

				currSectionStart = Integer.parseInt(currSectionNode.getAttribute(ATTRIBUTE_START));

				currSectionEnd = Integer.parseInt(currSectionNode.getAttribute(ATTRIBUTE_END));

				model.markAsSection(currSectionName, currSectionStart, currSectionEnd);


				// mark section terms

				termNodeList = currSectionNode.getChildNodes();

				for (int ctr2=0; ctr2<termNodeList.getLength(); ctr2++)
				{
					currTermNode = (org.w3c.dom.Element) termNodeList.item(ctr2);

					currTermStart = Integer.parseInt(currTermNode.getAttribute(ATTRIBUTE_START));

					currTermEnd = Integer.parseInt(currTermNode.getAttribute(ATTRIBUTE_END));

					currTermLength = currTermEnd - currTermStart + 1;

					model.markAsTerm(currTermStart, currTermLength);
				}
			}


			// disallow editing, set wrapper model, and return wrapper

			model.setAllowsNormalEditing(false);

			wrapper.setTemplateModel(model);

			return wrapper;

		}
		catch (javax.swing.text.BadLocationException e)
		{
			throw new CouldNotLoadException(e.getMessage());
		}
		catch (SAXException e)
		{
			throw new CouldNotLoadException(e.getMessage());
		}
		catch (IOException e)
		{
			throw new CouldNotLoadException(e.getMessage());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("MeduwebTemplateAndTranslatorDataHandler: " + 
				"Exception: " + e.getMessage());
			throw new CouldNotLoadException(e.getMessage());
		}
	}
	/*	public TemplateModelWrapper loadTemplate(String name)
	{
		try
		{

			// construct the model that is to be returned
			TemplateModelWrapper wrapper= new TemplateModelWrapper();
			wrapper.setTemplateModelLocation(name);

			MeduwebTemplateModel model = new MeduwebTemplateModel();

			javax.swing.text.StyledDocument document = model.getDocument();

			model.setAllowsNormalEditing(true);


			// set model descriptor
			//model.setDescriptor(name);


			// use the documentBuilder to parse in the document node
			org.w3c.dom.Document documentNode = builder.parse(
				new File(name));

			// retrieve the root element node
			org.w3c.dom.Element rootElementNode = (org.w3c.dom.Element)documentNode.getFirstChild();
			String version = rootElementNode.getAttribute(ATTRIBUTE_XMLTATDH_VERSION);


			// abort templatemodel creation if incorrect version
			if (!version.equalsIgnoreCase(XMLTATDH_VERSION))
			{
				System.err.println("XMLTATDH:Error: cannot parse version "+version+" of template "+
					"xml files with this version of the XMLTemplateAndTranslatorDataHandler (" +
					XMLTATDH_VERSION + ")");
				return null;
			}


			// retrieve the elements node (first child of root element node)
			org.w3c.dom.Element contentsNode = (org.w3c.dom.Element)rootElementNode.getFirstChild();

			// retrieve and reconstruct the document content
			org.w3c.dom.NodeList contentNodes = contentsNode.getChildNodes();
			for (int ctr1=0; ctr1<contentNodes.getLength(); ctr1++)
			{
				// currentElement is the current 'content' element in DOM
				org.w3c.dom.Element currentElement = (org.w3c.dom.Element)contentNodes.item(ctr1);


				// extract element start and end offsets
				int elementStartOffset = Integer.parseInt(currentElement.getAttribute(ATTRIBUTE_START));
				int elementEndOffset = Integer.parseInt(currentElement.getAttribute(ATTRIBUTE_END));


				// initialize placeholders for attribute and CDATA
				String cDATA = NO_NODE_CDATA;
				SimpleAttributeSet elementAttributeSet = new SimpleAttributeSet();


				/* elementChildNodes is  the list of attribute elements - and
				 * also one CDATASection element containing the content - to
				 * the current 'content' element in the DOM */
				/*org.w3c.dom.NodeList elementChildNodes = currentElement.getChildNodes();
				for (int ctr2=0; ctr2<elementChildNodes.getLength(); ctr2++)
				{
					org.w3c.dom.Node currentNode = elementChildNodes.item(ctr2);

					if (currentNode instanceof CDATASection)
						cDATA = ((CDATASection)currentNode).getData();
					else
					{
						org.w3c.dom.Element attributeElement = (org.w3c.dom.Element)currentNode;

						String xmlNameString = attributeElement.getAttribute(ATTRIBUTE_NAME);
						String xmlValueString = attributeElement.getAttribute(ATTRIBUTE_VALUE);
						styleAddXMLAttribute(xmlNameString, xmlValueString, elementAttributeSet);
					}
				}

				// insert the information into the document
				document.insertString(elementStartOffset, cDATA, elementAttributeSet);
			}


			// retrieve the paragraphs node
			org.w3c.dom.Element paragraphsNode = (org.w3c.dom.Element)contentsNode.getNextSibling();

			// retrieve and reconstruct the paragraphs settings for the document
			org.w3c.dom.NodeList paragraphNodes = paragraphsNode.getChildNodes();
			for (int ctr1=0; (ctr1<paragraphNodes.getLength())
					&& !(((org.w3c.dom.Node)paragraphNodes.item(ctr1)).getNodeType() == org.w3c.dom.Node.CDATA_SECTION_NODE); ctr1++)
			{
				// currentElement is the current 'paragraph' element in DOM
				//org.w3c.dom.Node currentNode = (org.w3c.dom.Node)paragraphNodes.item(ctr1);
				org.w3c.dom.Element currentElement = (org.w3c.dom.Element)paragraphNodes.item(ctr1);


				// extract paragraph start and end offsets
				int elementStartOffset = Integer.parseInt(currentElement.getAttribute(ATTRIBUTE_START));
				int elementEndOffset = Integer.parseInt(currentElement.getAttribute(ATTRIBUTE_END));


				// reconstruct paragraph element attribute set
				SimpleAttributeSet elementAttributeSet = new SimpleAttributeSet();
				org.w3c.dom.NodeList elementChildNodes = currentElement.getChildNodes();
				for (int ctr2=0; ctr2<elementChildNodes.getLength(); ctr2++)
				{
					org.w3c.dom.Element attributeElement = (org.w3c.dom.Element)elementChildNodes.item(ctr2);

					String xmlNameString = attributeElement.getAttribute(ATTRIBUTE_NAME);
					String xmlValueString = attributeElement.getAttribute(ATTRIBUTE_VALUE);
					styleAddXMLAttribute(xmlNameString, xmlValueString, elementAttributeSet);
				}


				// insert the information into the document
				document.setParagraphAttributes(elementStartOffset, (elementEndOffset-elementStartOffset),
					elementAttributeSet, false);
			}


			// retrieve the sections node
			org.w3c.dom.Element sectionsNode = (org.w3c.dom.Element)paragraphsNode.getNextSibling();

			System.out.println("(4) Started retrieving and constructing sections.");
			// retrieve and reconstruct the sectionmodels to be contained in model
			org.w3c.dom.NodeList sectionNodes = sectionsNode.getChildNodes();
			SectionModel sectionModel = null;
			String sectionName = null;
			org.w3c.dom.Element currentSectionNode = null;
			org.w3c.dom.NodeList termNodes = null;
			for (int ctr1=0; ctr1<sectionNodes.getLength(); ctr1++)
			{
				currentSectionNode = (org.w3c.dom.Element)sectionNodes.item(ctr1);


				// extract section name, start, and end offsets
				sectionName = currentSectionNode.getAttribute(ATTRIBUTE_NAME);
				int sectionStartOffset = Integer.parseInt(currentSectionNode.getAttribute(ATTRIBUTE_START));
				int sectionEndOffset = Integer.parseInt(currentSectionNode.getAttribute(ATTRIBUTE_END));

				System.out.println("C.");

				model.markAsSection(sectionName,sectionStartOffset, sectionEndOffset);
				System.out.println("D.");
				// add the children of the section node to the section model above
				termNodes = currentSectionNode.getChildNodes();
				org.w3c.dom.Element currentTermNode = null;
				String termName = null;
				System.out.println("4." + (ctr1+1) + " Handling terms for section" + ctr1);
				for (int ctr2=0; (ctr2<termNodes.getLength()); ctr2++)
				{
					currentTermNode = (org.w3c.dom.Element)termNodes.item(ctr2);
					System.out.println("Got term " + ctr2);
					// extract term name, start, and end offsets
					//termName = currentTermNode.getAttribute(ATTRIBUTE_NAME);
					
					System.out.println("Got attr 1 ");
					int currTermStart =
						Integer.parseInt(currentTermNode.getAttribute(ATTRIBUTE_START));
					System.out.println("Got attr 2 ");
					int currTermEnd =
						Integer.parseInt(currentTermNode.getAttribute(ATTRIBUTE_END));
					System.out.println("Got attr 3 ");
					System.out.println(currTermEnd + " " + currTermStart);
					model.markAsTerm(currTermStart, currTermEnd - currTermStart + 1);
				}
			}


			// return the model and disallow normal editing mode again
			model.setAllowsNormalEditing(false);
			System.out.println("(5) Done.");
			wrapper.setTemplateModel((TemplateModel)model);
			return wrapper;
		}
		catch (SAXException SAXE)
			{
				// something occured during the <DocumentBuilder>.parse() method call
				System.err.println("XMLTATDH:Error: SAXException (parsing error) thrown in loadTemplate()");
				System.err.println(SAXE.getMessage());
			}
		catch (IOException IOE)
			{
				// something occured during the <DocumentBuilder>.parse(new File(...)) method call
				System.err.println("XMLTATDH:Error: IOException (some IO error) thrown in loadTemplate()");
				System.err.println("XMLTATDH:Error: tried to load file: "+name);
				System.err.println(IOE.getMessage());
			}
		catch (BadLocationException bLE)
			{
				// something occured during the <Document>.insertString() method call
				System.err.println("XMLTATDH:Error: BadLocationException thrown in loadTemplate()");
				System.err.println(bLE.getMessage());
				bLE.printStackTrace();
			}
		catch (CouldNotParseException cnpe)
			{
				// something occured during the <Document>.insertString() method call
				System.err.println("XMLTATDH:Error: CouldNotParseException thrown in loadTemplate()");
				System.err.println(cnpe.getMessage());
			}

		// return null if we get to this point - something wrong happened
		System.err.println("XMLTATDH:Warning: Error occured in loadTemplate() - returning null");
		return null;
	}*/	

	private void styleAddXMLAttribute(
		String xmlNameString, String xmlValueString, SimpleAttributeSet attributeSet) 
		throws CouldNotParseException
	{
		// Color constants processing
		if (xmlNameString.equalsIgnoreCase(StyleConstants.Foreground.toString()))
		{
			Color color = parseColor(xmlValueString);
			StyleConstants.setForeground(attributeSet, color);
		}
		else if (xmlNameString.equalsIgnoreCase(StyleConstants.Background.toString()))
		{
			Color color = parseColor(xmlValueString);
			StyleConstants.setBackground(attributeSet, color);
		}


		// Font constants processing
		else if (xmlNameString.equalsIgnoreCase(StyleConstants.Bold.toString()))
		{
			StyleConstants.setBold(attributeSet, Boolean.valueOf(xmlValueString).booleanValue());
		}
		else if (xmlNameString.equalsIgnoreCase(StyleConstants.FontFamily.toString()))
		{
			StyleConstants.setFontFamily(attributeSet, xmlValueString);
		}
		else if (xmlNameString.equalsIgnoreCase(StyleConstants.Italic.toString()))
		{
			StyleConstants.setItalic(attributeSet, Boolean.valueOf(xmlValueString).booleanValue());
		}
		else if (xmlNameString.equalsIgnoreCase(StyleConstants.FontSize.toString()))
		{
			StyleConstants.setFontSize(attributeSet, Integer.parseInt(xmlValueString));
		}


		// Character constants processing
		else if (xmlNameString.equalsIgnoreCase(StyleConstants.IconAttribute.toString()))
		{
			// try to load the specified image and insert as icon attribute
			ImageIcon imageIcon = new ImageIcon(xmlValueString);
			switch (imageIcon.getImageLoadStatus())
			{
				case MediaTracker.ABORTED:
				case MediaTracker.ERRORED:
					System.err.println("XMLTATDH:Warning:Error loading logo image in template");
					break;
				default:
					StyleConstants.setIcon(attributeSet, imageIcon);
			}
		}
		else if (xmlNameString.equalsIgnoreCase(StyleConstants.StrikeThrough.toString()))
		{
			StyleConstants.setUnderline(attributeSet, Boolean.valueOf(xmlValueString).booleanValue());
		}
		else if (xmlNameString.equalsIgnoreCase(StyleConstants.Subscript.toString()))
		{
			StyleConstants.setSubscript(attributeSet, Boolean.valueOf(xmlValueString).booleanValue());
		}
		else if (xmlNameString.equalsIgnoreCase(StyleConstants.Superscript.toString()))
		{
			StyleConstants.setSuperscript(attributeSet, Boolean.valueOf(xmlValueString).booleanValue());
		}
		else if (xmlNameString.equalsIgnoreCase(StyleConstants.Underline.toString()))
		{
			StyleConstants.setUnderline(attributeSet, Boolean.valueOf(xmlValueString).booleanValue());
		}


		// Paragraph constants processing
		else if (xmlNameString.equalsIgnoreCase(StyleConstants.Alignment.toString()))
		{
			StyleConstants.setAlignment(attributeSet, Integer.parseInt(xmlValueString));
		}
	}

	private boolean shouldAddAttributeToXML(Object attributeName)
	{
		String nameString = attributeName.toString();

		if (nameString.equals(ABSTRACTDOCUMENT_ELEMENT_NAME_ATTRIBUTE))
		{
			return false;
		}
		if (nameString.equals(STYLECONSTANTS_RESOLVE_ATTRIBUTE_NAME))
		{
			return false;
		}
		if (nameString.equals(STYLECONSTANTS_ICON_ATTRIBUTE_NAME))
		{
			return false;
		}
		if (nameString.equals(STYLECONSTANTS_COMPONENT_ATTRIBUTE_NAME))
		{
			return false;
		}

		return true;

		/* NOTE: the element name attribute ("$ename") is an
		 * invalid XML name for an attribute. Thus, if you do
		 * not do anything about this, the parsing / transform
		 * will fail. The $ename attribute is short for 'element
		 * name', and is checked when the getName() method of
		 * the element is called. For certain other elements, the
		 * name of the element is obtained by other means. For
		 * example - for 'leaf' elements (objects of the class
		 * AbstractDocument.LeafElement), the getName() method
		 * is overridden to first check if the attribute has
		 * been set, and if it hasn't return 'content'. */

		/* NOTE: the resolve parent attribute ("resolver") is
		 * not an invalid XML name, but it is not necessary to
		 * add this to the XML file, since when reconstructing
		 * the StyledDocument from the XML, the resolve parents
		 * will be created automatically. Thus, we ignore this
		 * attribute. */

		/* NOTE: the current version of the XML template and
		 * translator data handler does not support storing icons
		 * or components from the styled document content into
		 * the XML file. */
	}

	private Object adaptObjectToXML(Object obj)
	{
		if (obj instanceof Color)
		{
			Color color = (Color) obj;

			String R = color.getRed() + "";

			String G = color.getGreen() + "";

			String B = color.getBlue() + "";

			String A = color.getAlpha() + "";

			String[] rgba = new String[] {R, G, B, A};

			for (int ctr=0; ctr<4; ctr++)
			{
				while (rgba[ctr].length() != 3)
				{
					rgba[ctr] = "0" + rgba[ctr];
				}
			}

			return (rgba[0] + rgba[1] + rgba[2] + rgba[3]);
		}

		return obj;
	}

	private Color parseColor(String colorString) throws CouldNotParseException
	{
		int R = Integer.parseInt(colorString.substring(0, 3));

		int G = Integer.parseInt(colorString.substring(3, 6));

		int B = Integer.parseInt(colorString.substring(6, 9));

		Color color = null;

		if (colorString.length() == 9)
		{
			color = new Color(R, G, B);
		}
		else if (colorString.length() == 12)
		{
			int A = Integer.parseInt(colorString.substring(9, 12));

			color = new Color(R, G, B, A);
		}
		else
		{
			String err = "Could not parse color from " + colorString;

			throw new CouldNotParseException(err);
		}

		return color;
	}

	private void insertAttributeNode(Attr node, javax.swing.text.MutableAttributeSet attributeSet)
	{
		String name = node.getName();

		if (name.equals(STYLECONSTANTS_ALIGNMENT_ATTRIBUTE_NAME))
		{
			javax.swing.text.StyleConstants.setAlignment(attributeSet, Integer.parseInt(node.getValue()));
		}

		if (name.equals(STYLECONSTANTS_BACKGROUND_ATTRIBUTE_NAME))
		{
			try
			{
				Color color = parseColor(node.getValue());

				javax.swing.text.StyleConstants.setBackground(attributeSet, color);
			}
			catch (CouldNotParseException e)
			{
				outputErrorMessage(e.getMessage());
			}
		}

		if (name.equals(STYLECONSTANTS_BIDI_LEVEL_ATTRIBUTE_NAME))
		{
		}

		if (name.equals(STYLECONSTANTS_BOLD_ATTRIBUTE_NAME))
		{
			try
			{
				boolean bool = parseBoolean(node.getValue());

				javax.swing.text.StyleConstants.setBold(attributeSet, bool);
			}
			catch (CouldNotParseException e)
			{
				outputErrorMessage(e.getMessage());
			}
		}

		if (name.equals(STYLECONSTANTS_COMPOSED_TEXT_ATTRIBUTE_NAME))
		{
		}

		if (name.equals(STYLECONSTANTS_FIRST_LINE_INDENT_ATTRIBUTE_NAME))
		{
		}

		if (name.equals(STYLECONSTANTS_FONT_FAMILY_ATTRIBUTE_NAME))
		{
			javax.swing.text.StyleConstants.setFontFamily(attributeSet, node.getValue());
		}

		if (name.equals(STYLECONSTANTS_FONT_SIZE_ATTRIBUTE_NAME))
		{
			javax.swing.text.StyleConstants.setFontSize(attributeSet, Integer.parseInt(node.getValue()));
		}

		if (name.equals(STYLECONSTANTS_FOREGROUND_ATTRIBUTE_NAME))
		{
			try
			{
				Color color = parseColor(node.getValue());

				javax.swing.text.StyleConstants.setForeground(attributeSet, color);
			}
			catch (CouldNotParseException e)
			{
				outputErrorMessage(e.getMessage());
			}
		}

		if (name.equals(STYLECONSTANTS_ITALIC_ATTRIBUTE_NAME))
		{
			try
			{
				boolean bool = parseBoolean(node.getValue());

				javax.swing.text.StyleConstants.setItalic(attributeSet, bool);
			}
			catch (CouldNotParseException e)
			{
				outputErrorMessage(e.getMessage());
			}
		}

		if (name.equals(STYLECONSTANTS_LEFT_INDENT_ATTRIBUTE_NAME))
		{
		}

		if (name.equals(STYLECONSTANTS_LINE_SPACING_ATTRIBUTE_NAME))
		{
		}

		if (name.equals(STYLECONSTANTS_MODEL_ATTRIBUTE_NAME))
		{
		}

		if (name.equals(STYLECONSTANTS_NAME_ATTRIBUTE_NAME))
		{
		}

		if (name.equals(STYLECONSTANTS_ORIENTATION_ATTRIBUTE_NAME))
		{
		}

		if (name.equals(STYLECONSTANTS_RIGHT_INDENT_ATTRIBUTE_NAME))
		{
		}

		if (name.equals(STYLECONSTANTS_SPACE_ABOVE_ATTRIBUTE_NAME))
		{
		}

		if (name.equals(STYLECONSTANTS_SPACE_BELOW_ATTRIBUTE_NAME))
		{
		}

		if (name.equals(STYLECONSTANTS_STRIKETHROUGH_ATTRIBUTE_NAME))
		{
			try
			{
				boolean bool = parseBoolean(node.getValue());

				javax.swing.text.StyleConstants.setStrikeThrough(attributeSet, bool);
			}
			catch (CouldNotParseException e)
			{
				outputErrorMessage(e.getMessage());
			}
		}

		if (name.equals(STYLECONSTANTS_SUBSCRIPT_ATTRIBUTE_NAME))
		{
			try
			{
				boolean bool = parseBoolean(node.getValue());

				javax.swing.text.StyleConstants.setSubscript(attributeSet, bool);
			}
			catch (CouldNotParseException e)
			{
				outputErrorMessage(e.getMessage());
			}
		}

		if (name.equals(STYLECONSTANTS_SUPERSCRIPT_ATTRIBUTE_NAME))
		{
			try
			{
				boolean bool = parseBoolean(node.getValue());

				javax.swing.text.StyleConstants.setSuperscript(attributeSet, bool);
			}
			catch (CouldNotParseException e)
			{
				outputErrorMessage(e.getMessage());
			}
		}

		if (name.equals(STYLECONSTANTS_TABSET_ATTRIBUTE_NAME))
		{
		}

		if (name.equals(STYLECONSTANTS_UNDERLINE_ATTRIBUTE_NAME))
		{
			try
			{
				boolean bool = parseBoolean(node.getValue());

				javax.swing.text.StyleConstants.setUnderline(attributeSet, bool);
			}
			catch (CouldNotParseException e)
			{
				outputErrorMessage(e.getMessage());
			}
		}
	}

	private boolean parseBoolean(String booleanString) throws CouldNotParseException
	{
		if (booleanString.equalsIgnoreCase("true"))
		{
			return true;
		}
		else if (booleanString.equalsIgnoreCase("false"))
		{
			return false;
		}
		else
		{
			throw new CouldNotParseException("Could not extract boolean from " + booleanString);
		}
	}

	private void outputErrorMessage(String message)
	{
		System.err.println(message);
	}








	public void saveTranslator(medview.meduweb.data.MeduwebTranslatorModel model, String filePath)
	{
		try
		{
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();


			// create document node

			org.w3c.dom.Document documentNode = builder.newDocument();


			// create 'translator model' root node

			org.w3c.dom.Element translatorNode = documentNode.createElement(ELEMENT_TRANSLATOR_MODEL);

			translatorNode.setAttribute(ATTRIBUTE_XMLTATDH_VERSION, XMLTATDH_VERSION);

			documentNode.appendChild(translatorNode);


			// create the translation model elements and add to root node

			MeduwebTranslationModel[] translationModels = model.getTranslationModels();

			MeduwebTranslationModel currTranslationModel = null;

			for (int ctr1=0; ctr1<translationModels.length; ctr1++)
			{

				// create a translation model node and add to root node

				currTranslationModel = translationModels[ctr1];

				org.w3c.dom.Element translationModelNode = documentNode.createElement(ELEMENT_TRANSLATION_MODEL);

				translationModelNode.setAttribute(ATTRIBUTE_TERM, currTranslationModel.getTerm());

				translationModelNode.setAttribute(ATTRIBUTE_AUTO_VG, currTranslationModel.isAutoVG() + ""); // "true" or "false"

				translationModelNode.setAttribute(ATTRIBUTE_VG, currTranslationModel.getVGPolicyIdentifier());

				translationModelNode.setAttribute(ATTRIBUTE_TYPE, currTranslationModel.getTypeIdentifier());

				if (currTranslationModel instanceof MeduwebSeparatedTranslationModel)
				{
					translationModelNode.setAttribute(ATTRIBUTE_SEPARATOR, ((MeduwebSeparatedTranslationModel)currTranslationModel).getSeparator());

					translationModelNode.setAttribute(ATTRIBUTE_NTL_SEPARATOR, ((MeduwebSeparatedTranslationModel)currTranslationModel).getNTLSeparator());
				}

				translatorNode.appendChild(translationModelNode);


				// append translations of current translation to current translation model node

				org.w3c.dom.Element translationsNode = documentNode.createElement(ELEMENT_TRANSLATIONS);

				translationModelNode.appendChild(translationsNode);

				Translation[] translations = currTranslationModel.getTranslations();

				Translation currTranslation = null;

				for (int ctr2=0; ctr2<translations.length; ctr2++)
				{

					// append a translation node to current translations node

					currTranslation = translations[ctr2];

					org.w3c.dom.Element translationNode = documentNode.createElement(ELEMENT_TRANSLATION);

					translationNode.setAttribute(ATTRIBUTE_VALUE, currTranslation.getValue().toString());

					translationNode.setAttribute(ATTRIBUTE_TRANSLATION, currTranslation.getTranslation());

					translationNode.setAttribute(ATTRIBUTE_PREVIEW, currTranslation.isPreview() + ""); // "true" or "false"

					translationNode.setAttribute(ATTRIBUTE_LAST_MODIFIED, dateFormatter.format(currTranslation.getLastModified()));

					translationsNode.appendChild(translationNode);
				}
			}


			// write the DOM structure to a XML file

			Transformer transformer = transformerFactory.newTransformer();

			transformer.transform(new DOMSource(documentNode), new StreamResult(new File(filePath)));
		}

		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
		}
		catch (TransformerConfigurationException e)
		{
			e.printStackTrace();
		}
		catch (TransformerException e)
		{
			e.printStackTrace();
		}
	}





	public medview.meduweb.data.MeduwebTranslatorModel loadTranslator(String filePath) throws CouldNotLoadException
	{
		try
		{
			// obtain document node

			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

			FileInputStream fIS = new FileInputStream(filePath);

			org.w3c.dom.Document documentNode = builder.parse(fIS);


			// create translator model

			org.w3c.dom.Element translatorModelNode = documentNode.getDocumentElement();

			MeduwebTranslatorModel translatorModel = new MeduwebTranslatorModel();


			// add translation models

			org.w3c.dom.NodeList translationModelNodeList = translatorModelNode.getElementsByTagName(ELEMENT_TRANSLATION_MODEL);

			org.w3c.dom.Element translationModelNode = null;

			String term = null;

			String type = null;

			String globalType = null;
			
			for (int ctr1=0; ctr1<translationModelNodeList.getLength(); ctr1++)
			{
				translationModelNode = (org.w3c.dom.Element) translationModelNodeList.item(ctr1);

				term = translationModelNode.getAttribute(ATTRIBUTE_TERM);

				type = translationModelNode.getAttribute(ATTRIBUTE_TYPE);

				try
				{
					/* NOTE: This is where we apply our policy on what to do
					 * with 'old' translator models, that have terms with
					 * types that do not match the type existant in the global
					 * term definition location. What we do is that we create
					 * a new translation model with the term specified in the
					 * system (i.e. we let the global type take preference
					 * over the type in the model), and then ignore the rest
					 * of the DOM tree structure for that node in the model.
					 * Usually, the application loading this translator model
					 * will want to let the synchronizer check the loaded model
					 * with the system values, the synchronizer will make sure
					 * that all current values for the term are entered into
					 * the freshly created translation model.
					 */

					globalType = mVDH.getTypeDescription(term);

					if (!globalType.equals(type))
					{
						printTypeMismatch(term, type, globalType);

						translatorModel.addTranslationModel(term);

						continue;
					} 
				}
				catch (NoSuchTermException e)
				{
					/* NOTE: There can be two reasons why we have ended up here:
					 * 1) a derived term has been encountered, the derived terms
					 *    are not defined in the global term definition files and
					 *    thus the datahandler will not recognize it.
					 * 2) a term that exists in the model but not in the system
					 *    have been encountered.
					 * In both of these cases, we want to continue the processing,
					 * since the necessary information for creating a translation
					 * model exists in the XML. Therefore, we simply supress this
					 * exception, since it should happen normally for certain terms
					 * and it is not an indication of an error.
					 */
					
					if (!dTH.isDerivedTerm(term)) { printNewTermInModel(term); }
				}

				MeduwebTranslationModel translationModel = translatorModel.addTranslationModel(term, type);
		
				if (translationModel == null) { continue; } // null = type not supported

				translationModel.setAutoVG(Boolean.valueOf(translationModelNode.getAttribute(ATTRIBUTE_AUTO_VG)).booleanValue());

				translationModel.setVGPolicy(translationModelNode.getAttribute(ATTRIBUTE_VG));

				if (translationModel instanceof MeduwebSeparatedTranslationModel)
				{
					((MeduwebSeparatedTranslationModel)translationModel).setSeparator(translationModelNode.getAttribute(ATTRIBUTE_SEPARATOR));

					((MeduwebSeparatedTranslationModel)translationModel).setNTLSeparator(translationModelNode.getAttribute(ATTRIBUTE_NTL_SEPARATOR));
				}


				// add translations to translation model

				org.w3c.dom.Element translationsNode = (org.w3c.dom.Element) translationModelNode.getElementsByTagName(ELEMENT_TRANSLATIONS).item(0);

				org.w3c.dom.NodeList translationsNodeList = translationsNode.getElementsByTagName(ELEMENT_TRANSLATION);

				org.w3c.dom.Element translationNode = null;

				String value = null;

				String translation = null;

				String preview = null;

				String lastModified = null;
				
				for (int ctr2=0; ctr2<translationsNodeList.getLength(); ctr2++)
				{
					translationNode = (org.w3c.dom.Element) translationsNodeList.item(ctr2);

					value = translationNode.getAttribute(ATTRIBUTE_VALUE);

					translation = translationNode.getAttribute(ATTRIBUTE_TRANSLATION);

					preview = translationNode.getAttribute(ATTRIBUTE_PREVIEW);

					lastModified = translationNode.getAttribute(ATTRIBUTE_LAST_MODIFIED);

					translationModel.addValue(value, translation, dateFormatter.parse(lastModified));

					translationModel.setPreviewStatus(value, Boolean.valueOf(preview).booleanValue());

					/* NOTE: the polymorphic type system will take care
					 * of properly constructing a correct 'value' object
					 * from the string contained in the XML file. For
					 * instance, the string in the XML file for a value of
					 * an interval term is 'xx - xx', when adding this value
					 * to the translation model, and the translation model
					 * is an instance of IntervalTranslationModel (as it
					 * should be since the TranslationModelFactory produces
					 * such an object if the type of the term is interval),
					 * the model overrides the addValue() method to parse the
					 * string and creates a proper Interval object from it,
					 * then this object will be added to the internal hashmap
					 * of translations as an 'Interval' object. */
				}
			}
			
			return translatorModel;
		}
		catch (ParserConfigurationException e)
		{
			throw new CouldNotLoadException(e.getMessage());
		}
		catch (FileNotFoundException e)
		{
			throw new CouldNotLoadException(e.getMessage());
		}
		catch (java.text.ParseException e) // the date formatter
		{
			throw new CouldNotLoadException(e.getMessage());
		}
		catch (SAXException e)
		{
			throw new CouldNotLoadException(e.getMessage());
		}
		catch (IOException e)
		{
			throw new CouldNotLoadException(e.getMessage());
		}
		catch (Exception e)
		{
			throw new CouldNotLoadException(e.getMessage());
		}
	}

	private void printTypeMismatch(String term, String typeDesc, String gTypeDesc)
	{
		System.out.println(

			"A type mismatch was found - '" + term + "' with type '" + typeDesc + "' " +

			"did not match the global type of the term (" + gTypeDesc + "). A new model " +

			"with the global type (" + gTypeDesc + ") has been created and added to the " +

			"translator.\n");
	}

	private void printNewTermInModel(String term)
	{
		System.out.println(

			"A term existed in the translator model that does not exist in the global term " +

			"definitions - '" + term + "'. Will use the type information contained in the " +

			"model when creating and adding the translation model.\n");
	}








	public String getDefaultTemplateDirectory()
	{
		return defaultTemplateDir;
	}

	public String getDefaultTranslatorDirectory()
	{
		return defaultTranslatorDir;
	}








	private void initMembers()
	{
		dTH = MeduwebDerivedTermHandler.instance();

		mVDH = MeduwebDataHandler.instance();

		Locale locale = new Locale("sv", "SE");

		fileSep = System.getProperty("file.separator");

		int dateStyle = DateFormat.LONG; int timeStyle = DateFormat.LONG;

		dateFormatter = DateFormat.getDateTimeInstance(dateStyle, timeStyle, locale);
	}

	private void initDirStruct()
	{
		defaultTemplateDir = System.getProperty("template.dir");

		File tempFile = new File(defaultTemplateDir);

		if (!tempFile.exists()) { tempFile.mkdirs(); }

		defaultTranslatorDir = System.getProperty("translator.dir");

		tempFile = new File(defaultTranslatorDir);

		if (!tempFile.exists()) { tempFile.mkdirs(); }
	}

	private void initFactories()
	{
		System.setProperty("javax.xml.parsers.DocumentBuilderFactory", "org.apache.crimson.jaxp.DocumentBuilderFactoryImpl");

		System.setProperty("javax.xml.transform.TransformerFactory", "org.apache.xalan.processor.TransformerFactoryImpl");

		try
		{
			transformerFactory = TransformerFactory.newInstance();

			documentBuilderFactory = DocumentBuilderFactory.newInstance();
		}
		catch (Exception e) { e.printStackTrace(); }
	}

	private void initBuilder()
	{
		try
		{
			builder = documentBuilderFactory.newDocumentBuilder();
		}
		catch (ParserConfigurationException e) { e.printStackTrace(); }
	}

	private void initStaticConstants()
	{
		if (ABSTRACTDOCUMENT_ELEMENT_NAME_ATTRIBUTE == null)
		{
			ABSTRACTDOCUMENT_ELEMENT_NAME_ATTRIBUTE = javax.swing.text.AbstractDocument.ElementNameAttribute;
		}
		if (STYLECONSTANTS_COMPONENT_ELEMENT_NAME == null)
		{
			STYLECONSTANTS_COMPONENT_ELEMENT_NAME = javax.swing.text.StyleConstants.ComponentElementName;
		}
		if (STYLECONSTANTS_ICON_ELEMENT_NAME == null)
		{
			STYLECONSTANTS_ICON_ELEMENT_NAME = javax.swing.text.StyleConstants.IconElementName;
		}


		if (STYLECONSTANTS_ALIGNMENT_ATTRIBUTE_NAME == null)
		{
			STYLECONSTANTS_ALIGNMENT_ATTRIBUTE_NAME = javax.swing.text.StyleConstants.Alignment.toString();
		}
		if (STYLECONSTANTS_BACKGROUND_ATTRIBUTE_NAME == null)
		{
			STYLECONSTANTS_BACKGROUND_ATTRIBUTE_NAME = javax.swing.text.StyleConstants.Background.toString();
		}
		if (STYLECONSTANTS_BIDI_LEVEL_ATTRIBUTE_NAME == null)
		{
			STYLECONSTANTS_BIDI_LEVEL_ATTRIBUTE_NAME = javax.swing.text.StyleConstants.BidiLevel.toString();
		}
		if (STYLECONSTANTS_BOLD_ATTRIBUTE_NAME == null)
		{
			STYLECONSTANTS_BOLD_ATTRIBUTE_NAME = javax.swing.text.StyleConstants.Bold.toString();
		}
		if (STYLECONSTANTS_COMPONENT_ATTRIBUTE_NAME == null)
		{
			STYLECONSTANTS_COMPONENT_ATTRIBUTE_NAME = javax.swing.text.StyleConstants.ComponentAttribute.toString();
		}
		if (STYLECONSTANTS_COMPOSED_TEXT_ATTRIBUTE_NAME == null)
		{
			STYLECONSTANTS_COMPOSED_TEXT_ATTRIBUTE_NAME = javax.swing.text.StyleConstants.ComposedTextAttribute.toString();
		}
		if (STYLECONSTANTS_FIRST_LINE_INDENT_ATTRIBUTE_NAME == null)
		{
			STYLECONSTANTS_FIRST_LINE_INDENT_ATTRIBUTE_NAME = javax.swing.text.StyleConstants.FirstLineIndent.toString();
		}
		if (STYLECONSTANTS_FONT_FAMILY_ATTRIBUTE_NAME == null)
		{
			STYLECONSTANTS_FONT_FAMILY_ATTRIBUTE_NAME = javax.swing.text.StyleConstants.FontFamily.toString();
		}
		if (STYLECONSTANTS_FONT_SIZE_ATTRIBUTE_NAME == null)
		{
			STYLECONSTANTS_FONT_SIZE_ATTRIBUTE_NAME = javax.swing.text.StyleConstants.FontSize.toString();
		}
		if (STYLECONSTANTS_FOREGROUND_ATTRIBUTE_NAME == null)
		{
			STYLECONSTANTS_FOREGROUND_ATTRIBUTE_NAME = javax.swing.text.StyleConstants.Foreground.toString();
		}
		if (STYLECONSTANTS_ICON_ATTRIBUTE_NAME == null)
		{
			STYLECONSTANTS_ICON_ATTRIBUTE_NAME = javax.swing.text.StyleConstants.IconAttribute.toString();
		}
		if (STYLECONSTANTS_ITALIC_ATTRIBUTE_NAME == null)
		{
			STYLECONSTANTS_ITALIC_ATTRIBUTE_NAME = javax.swing.text.StyleConstants.Italic.toString();
		}
		if (STYLECONSTANTS_LEFT_INDENT_ATTRIBUTE_NAME == null)
		{
			STYLECONSTANTS_LEFT_INDENT_ATTRIBUTE_NAME = javax.swing.text.StyleConstants.LeftIndent.toString();
		}
		if (STYLECONSTANTS_LINE_SPACING_ATTRIBUTE_NAME == null)
		{
			STYLECONSTANTS_LINE_SPACING_ATTRIBUTE_NAME = javax.swing.text.StyleConstants.LineSpacing.toString();
		}
		if (STYLECONSTANTS_MODEL_ATTRIBUTE_NAME == null)
		{
			STYLECONSTANTS_MODEL_ATTRIBUTE_NAME = javax.swing.text.StyleConstants.ModelAttribute.toString();
		}
		if (STYLECONSTANTS_NAME_ATTRIBUTE_NAME == null)
		{
			STYLECONSTANTS_NAME_ATTRIBUTE_NAME = javax.swing.text.StyleConstants.NameAttribute.toString();
		}
		if (STYLECONSTANTS_ORIENTATION_ATTRIBUTE_NAME == null)
		{
			STYLECONSTANTS_ORIENTATION_ATTRIBUTE_NAME = javax.swing.text.StyleConstants.Orientation.toString();
		}
		if (STYLECONSTANTS_RESOLVE_ATTRIBUTE_NAME == null)
		{
			STYLECONSTANTS_RESOLVE_ATTRIBUTE_NAME = javax.swing.text.StyleConstants.ResolveAttribute.toString();
		}
		if (STYLECONSTANTS_RIGHT_INDENT_ATTRIBUTE_NAME == null)
		{
			STYLECONSTANTS_RIGHT_INDENT_ATTRIBUTE_NAME = javax.swing.text.StyleConstants.RightIndent.toString();
		}
		if (STYLECONSTANTS_SPACE_ABOVE_ATTRIBUTE_NAME == null)
		{
			STYLECONSTANTS_SPACE_ABOVE_ATTRIBUTE_NAME = javax.swing.text.StyleConstants.SpaceAbove.toString();
		}
		if (STYLECONSTANTS_SPACE_BELOW_ATTRIBUTE_NAME == null)
		{
			STYLECONSTANTS_SPACE_BELOW_ATTRIBUTE_NAME = javax.swing.text.StyleConstants.SpaceBelow.toString();
		}
		if (STYLECONSTANTS_STRIKETHROUGH_ATTRIBUTE_NAME == null)
		{
			STYLECONSTANTS_STRIKETHROUGH_ATTRIBUTE_NAME = javax.swing.text.StyleConstants.StrikeThrough.toString();
		}
		if (STYLECONSTANTS_SUBSCRIPT_ATTRIBUTE_NAME == null)
		{
			STYLECONSTANTS_SUBSCRIPT_ATTRIBUTE_NAME = javax.swing.text.StyleConstants.Subscript.toString();
		}
		if (STYLECONSTANTS_SUPERSCRIPT_ATTRIBUTE_NAME == null)
		{
			STYLECONSTANTS_SUPERSCRIPT_ATTRIBUTE_NAME = javax.swing.text.StyleConstants.Superscript.toString();
		}
		if (STYLECONSTANTS_TABSET_ATTRIBUTE_NAME == null)
		{
			STYLECONSTANTS_TABSET_ATTRIBUTE_NAME = javax.swing.text.StyleConstants.TabSet.toString();
		}
		if (STYLECONSTANTS_UNDERLINE_ATTRIBUTE_NAME == null)
		{
			STYLECONSTANTS_UNDERLINE_ATTRIBUTE_NAME = javax.swing.text.StyleConstants.Underline.toString();
		}
	}

	public MeduwebTemplateAndTranslatorDataHandler()
	{
		initMembers();

		initDirStruct();

		initFactories();

		initBuilder();

		initStaticConstants();
	}


	private String fileSep;

	private MeduwebDerivedTermHandler dTH;

	private MeduwebDataHandler mVDH;

	private DocumentBuilder builder;

	private DateFormat dateFormatter;

	private String defaultTemplateDir;

	private String defaultTranslatorDir;

	private TransformerFactory transformerFactory;

	private DocumentBuilderFactory documentBuilderFactory;


	private static final String VALUE_GEMEN = "gemen";

	private static final String VALUE_VERSAL = "versal";

	private static final String XMLTATDH_VERSION = "1.0";

	private static final String ASS_TRANS_NOT_SET = "not set";


	private static final String DEFAULT_TEMPLATE_SUBDIRECTORY = "templates";

	private static final String DEFAULT_TRANSLATOR_SUBDIRECTORY = "translators";

	private static final String DEFAULT_XML_SUBDIRECTORY = "xml";


	private static final String ELEMENT_CONTENT = "Content";

	private static final String ELEMENT_SECTIONS = "Sections";

	private static final String ELEMENT_SECTION = "Section";

	private static final String ELEMENT_TERM = "Term";

	private static final String ELEMENT_STYLED_DOCUMENT = "SD";

	private static final String ELEMENT_STYLED_DOCUMENT_CONTENT = "SDContent";

	private static final String ELEMENT_STYLED_DOCUMENT_PARAGRAPH = "SDParagraph";

	private static final String ELEMENT_STYLED_DOCUMENT_SECTION = "SDSection";

	private static final String ELEMENT_TEMPLATE_MODEL = "TemplateModel";

	private static final String ELEMENT_TRANSLATOR_MODEL = "TranslatorModel";
	//private static final String ELEMENT_TRANSLATOR_MODEL = "MeduwebTranslatorModel";
	private static final String ELEMENT_TRANSLATION_MODEL = "TranslationModel";
	//private static final String ELEMENT_TRANSLATION_MODEL = "MeduwebTranslationModel";
	private static final String ELEMENT_TRANSLATIONS = "Translations";

	private static final String ELEMENT_TRANSLATION = "Translation";

	private static final String ELEMENT_ASS_TRANS = "AssociatedTranslator";


	private static final String ATTRIBUTE_PATH = "path";

	private static final String ATTRIBUTE_LOCATION = "location";

	private static final String ATTRIBUTE_AUTO_VG = "autoVG";

	private static final String ATTRIBUTE_LAST_MODIFIED = "lastModified";

	private static final String ATTRIBUTE_XMLTATDH_VERSION = "XMLTATDH_version";

	private static final String ATTRIBUTE_TEMPLATE_DESCRIPTOR = "descriptor";

	private static final String ATTRIBUTE_NTL_SEPARATOR = "NTLSeparator";

	private static final String ATTRIBUTE_TRANSLATION = "translation";

	private static final String ATTRIBUTE_SEPARATOR = "separator";

	private static final String ATTRIBUTE_PREVIEW = "preview";

	private static final String ATTRIBUTE_START = "start";

	private static final String ATTRIBUTE_TERM = "term";

	private static final String ATTRIBUTE_TYPE = "type";

	private static final String ATTRIBUTE_END = "end";

	private static final String ATTRIBUTE_NAME = "name";

	private static final String ATTRIBUTE_VALUE = "value";

	private static final String ATTRIBUTE_VG = "VG";


	private static String ABSTRACTDOCUMENT_ELEMENT_NAME_ATTRIBUTE = null;

	private static String STYLECONSTANTS_COMPONENT_ELEMENT_NAME = null;

	private static String STYLECONSTANTS_ICON_ELEMENT_NAME = null;

	private static String STYLECONSTANTS_ALIGNMENT_ATTRIBUTE_NAME = null;

	private static String STYLECONSTANTS_BACKGROUND_ATTRIBUTE_NAME = null;

	private static String STYLECONSTANTS_BIDI_LEVEL_ATTRIBUTE_NAME = null;

	private static String STYLECONSTANTS_BOLD_ATTRIBUTE_NAME = null;

	private static String STYLECONSTANTS_COMPONENT_ATTRIBUTE_NAME = null;

	private static String STYLECONSTANTS_COMPOSED_TEXT_ATTRIBUTE_NAME = null;

	private static String STYLECONSTANTS_FIRST_LINE_INDENT_ATTRIBUTE_NAME = null;

	private static String STYLECONSTANTS_FONT_FAMILY_ATTRIBUTE_NAME = null;

	private static String STYLECONSTANTS_FONT_SIZE_ATTRIBUTE_NAME = null;

	private static String STYLECONSTANTS_FOREGROUND_ATTRIBUTE_NAME = null;

	private static String STYLECONSTANTS_ICON_ATTRIBUTE_NAME = null;

	private static String STYLECONSTANTS_ITALIC_ATTRIBUTE_NAME = null;

	private static String STYLECONSTANTS_LEFT_INDENT_ATTRIBUTE_NAME = null;

	private static String STYLECONSTANTS_LINE_SPACING_ATTRIBUTE_NAME = null;

	private static String STYLECONSTANTS_MODEL_ATTRIBUTE_NAME = null;

	private static String STYLECONSTANTS_NAME_ATTRIBUTE_NAME = null;

	private static String STYLECONSTANTS_ORIENTATION_ATTRIBUTE_NAME = null;

	private static String STYLECONSTANTS_RESOLVE_ATTRIBUTE_NAME = null;

	private static String STYLECONSTANTS_RIGHT_INDENT_ATTRIBUTE_NAME = null;

	private static String STYLECONSTANTS_SPACE_ABOVE_ATTRIBUTE_NAME = null;

	private static String STYLECONSTANTS_SPACE_BELOW_ATTRIBUTE_NAME = null;

	private static String STYLECONSTANTS_STRIKETHROUGH_ATTRIBUTE_NAME = null;

	private static String STYLECONSTANTS_SUBSCRIPT_ATTRIBUTE_NAME = null;

	private static String STYLECONSTANTS_SUPERSCRIPT_ATTRIBUTE_NAME = null;

	private static String STYLECONSTANTS_TABSET_ATTRIBUTE_NAME = null;

	private static String STYLECONSTANTS_UNDERLINE_ATTRIBUTE_NAME = null;

	private static final String NO_NODE_CDATA = "###Content node contained no character data###";
}