package medview.medimager.foundation;

import java.awt.image.*;

import java.io.*;

import java.text.*;

import java.util.*;
import java.util.zip.*;

import javax.imageio.ImageIO;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import medview.datahandling.*;
import medview.datahandling.examination.*;

import medview.medimager.model.*;

import misc.foundation.io.*;

import org.w3c.dom.*;

public class ZipFileNodeStorageHandler implements NodeStorageHandler
{
	// MEMBERS

	private static final DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);

	private static final String IMAGE_ZIP_PATH = "data/image/";

	private static final String AUDIO_ZIP_PATH = "data/audio/";

	private static final String STRUCTURE_ZIP_PATH = "structure/";

	private static final String STRUCTURE_ZIP_NAME = "structure.xml";

	private static final String IMAGE_FULL_POSTFIX = "_full.jpg";

	private static final String IMAGE_MEDIUM_POSTFIX = "_medium.jpg";

	private static final String IMAGE_THUMB_POSTFIX = "_thumb.jpg";

	private static final String AUDIO_POSTFIX = ".raw";

	private static final byte[] buffer = new byte[1024];

	private static ZipFile zipDataFile = null; // the zip data file acting as data source / image data obtainer

	// XML

	private static DocumentBuilder documentBuilder = null; // same document builder each time

	private static TransformerFactory transformerFactory = null; // new transformer at each export

	public static final String XML_DOCUMENT_BUILDER_FACTORY_IMPL = "org.apache.crimson.jaxp.DocumentBuilderFactoryImpl";

	public static final String XML_TRANSFORMER_FACTORY_IMPL = "org.apache.xalan.processor.TransformerFactoryImpl";

	private static final String ELEMENT_ALBUM_STRUCTURE = "Structure";

	private static final String ELEMENT_AUDIO_DATA = "Audio_Data";

	private static final String ELEMENT_AUDIO_PATH = "Audio_Path";

	private static final String ELEMENT_DESCRIPTION = "Description";

	private static final String ELEMENT_DOCUMENT_ROOT = "Med_Imager_Node";

	private static final String ELEMENT_EXAMINATION_DATE = "Examination_Date";

	private static final String ELEMENT_EXAMINATION_IDENTIFIER = "Examination_Identifier";

	private static final String ELEMENT_IMAGE_PATH = "Image_Path";

	private static final String ELEMENT_NODE = "Node";

	private static final String ELEMENT_NOTE = "Note";

	private static final String ELEMENT_ORIGINAL_FILE_NAME = "Original_File_Name";

	private static final String ELEMENT_PCODE = "PCode";

	private static final String ELEMENT_PID = "PID";

	private static final String ATTRIBUTE_CONTAINS_DATA = "contains_data";

	private static final String ATTRIBUTE_VERSION = "version";

	private static final String ATTRIBUTE_TYPE = "type";

	private static final String TYPE_BRANCH = "branch";

	private static final String TYPE_LEAF = "leaf";

	private static final String CURRENT_VERSION = "1.0";

	private static final String YES = "yes";

	private static final String NO = "no";


	// CONSTRUCTOR

	public ZipFileNodeStorageHandler()
	{
		try
		{
			// no need to set the XML implementations directly, a platform default is always provided

			documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

			transformerFactory = TransformerFactory.newInstance();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}


	// EXPORT ALBUM

	public void exportNode(String filePath, FolderNodeModel root) throws CouldNotExportException
	{
		try
		{
			// check if previous zip data file is being overwritten

			File oldZipDataFile = null;

			File renamedZipDataFile = null;

			if (zipDataFile != null)
			{
				if (zipDataFile.getName().equalsIgnoreCase(filePath))
				{
					zipDataFile.close();

					oldZipDataFile = new File(filePath);

					renamedZipDataFile = new File(filePath + ".tmp");	// create a temporarily file to read data from

					oldZipDataFile.renameTo(renamedZipDataFile);

					zipDataFile = new ZipFile(renamedZipDataFile);		// now, image data is read from .tmp zip file
				}
			}

			// create zip file

			FileOutputStream fout = new FileOutputStream(filePath);

			ZipOutputStream zout = new ZipOutputStream(fout);

			// store image data -> zip

			storeImageData(root, zout, 0);

			// store audio data -> zip

			storeAudioData(root, zout, 0);

			// store structure data -> zip

			storeStructureData(root, zout);

			// clean up

			zout.close();

			fout.close();

			if (renamedZipDataFile != null)								// i.e., if previous zip file is being overwritten
			{
				zipDataFile.close();

				renamedZipDataFile.renameTo(oldZipDataFile);			// restore the name of the zip data file

				zipDataFile = new ZipFile(oldZipDataFile);				// now, image data is read from file path zip file again

				renamedZipDataFile.delete();							// remove the temporary file
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();

			throw new CouldNotExportException(e.getMessage());
		}
	}

	private void storeImageData(NodeModel node, ZipOutputStream zout, int siblingCounter) throws IOException
	{
		if (node instanceof FolderNodeModel)
		{
			// folder node, add all children - keep track of each child index

			NodeModel[] children = node.getChildren();

			int subSiblingCounter = 0;

			for (int ctr=0; ctr<children.length; ctr++)
			{
				storeImageData(children[ctr], zout, subSiblingCounter++);
			}
		}
		else
		{
			// leaf node, store full image data

			String zipName = IMAGE_ZIP_PATH + node.getPathToNode() + siblingCounter + IMAGE_FULL_POSTFIX;

			ZipEntry zipEntry = new ZipEntry(zipName);

			zout.putNextEntry(zipEntry);

			byte[] imageData = ((ImageLeafNodeModel)node).getFullImageByteArray();

			ByteArrayInputStream bis = new ByteArrayInputStream(imageData);

			IOUtilities.copy(bis, zout, true, false);	// close input stream, but not output stream

			zout.closeEntry();

			// store medium image data

			zipName = IMAGE_ZIP_PATH + node.getPathToNode() + siblingCounter + IMAGE_MEDIUM_POSTFIX;

			zipEntry = new ZipEntry(zipName);

			zout.putNextEntry(zipEntry);

			imageData = ((ImageLeafNodeModel)node).getMediumImageByteArray();

			bis = new ByteArrayInputStream(imageData);

			IOUtilities.copy(bis, zout, true, false);	// close input stream, but not output stream

			zout.closeEntry();

			// store thumb image data

			zipName = IMAGE_ZIP_PATH + node.getPathToNode() + siblingCounter + IMAGE_THUMB_POSTFIX;

			zipEntry = new ZipEntry(zipName);

			zout.putNextEntry(zipEntry);

			imageData = ((ImageLeafNodeModel)node).getThumbImageByteArray();

			bis = new ByteArrayInputStream(imageData);

			IOUtilities.copy(bis, zout, true, false);	// close input stream, but not output stream

			zout.closeEntry();
		}
	}

	public void storeAudioData(NodeModel node, ZipOutputStream zout, int siblingCounter) throws IOException
	{
		if (node instanceof FolderNodeModel)
		{
			// folder node, add all children - keep track of each child index

			NodeModel[] children = node.getChildren();

			int subSiblingCounter = 0;

			for (int ctr=0; ctr<children.length; ctr++)
			{
				storeAudioData(children[ctr], zout, subSiblingCounter++);
			}
		}
		else
		{
			// leaf node, store full audio data if it exists

			AudioLeafNodeModel audioLeaf = (AudioLeafNodeModel) node;

			if (audioLeaf.containsAudioData())
			{
				String zipName = AUDIO_ZIP_PATH + node.getPathToNode() + siblingCounter + AUDIO_POSTFIX;

				zout.putNextEntry(new ZipEntry(zipName));

				ByteArrayInputStream bin = new ByteArrayInputStream(audioLeaf.getAudioData());

				IOUtilities.copy(bin, zout, true, false);

				zout.closeEntry();
			}
		}
	}

	public void storeStructureData(NodeModel modelRoot, ZipOutputStream zout) throws IOException, TransformerConfigurationException, TransformerException
	{
		// create zip entry

		String zipName = STRUCTURE_ZIP_PATH + STRUCTURE_ZIP_NAME;

		ZipEntry zipEntry = new ZipEntry(zipName);

		zout.putNextEntry(zipEntry);

		// create document root

		Document document = documentBuilder.newDocument();

		Element xmlDocumentRoot = document.createElement(ELEMENT_DOCUMENT_ROOT);

		xmlDocumentRoot.setAttribute(ATTRIBUTE_VERSION, CURRENT_VERSION);

		document.appendChild(xmlDocumentRoot);

		// create structure node

		Element xmlStructureNode = document.createElement(ELEMENT_ALBUM_STRUCTURE);

		xmlDocumentRoot.appendChild(xmlStructureNode);

		// build up structure

		buildStructure(xmlStructureNode, modelRoot, document, 0);

		// write the DOM structure to XML

		Transformer transformer = transformerFactory.newTransformer();

		DOMSource source = new DOMSource(document);

		StreamResult result = new StreamResult(zout);

		transformer.transform(source, result);

		// close zip entry

		zout.closeEntry();
	}

	private void buildStructure(Node xmlParent, NodeModel modelNode, Document document, int siblingCounter)
	{
		if (modelNode instanceof FolderNodeModel)
		{
			// branch node

			Element xmlBranchNode = document.createElement(ELEMENT_NODE);

			xmlBranchNode.setAttribute(ATTRIBUTE_TYPE, TYPE_BRANCH);

			String folderName = modelNode.getDescription();

			xmlBranchNode.appendChild(document.createCDATASection(folderName));

			xmlParent.appendChild(xmlBranchNode);

			// add all children, keep track of each child index

			NodeModel[] children = modelNode.getChildren();

			int subSiblingCounter = 0;

			for (int ctr=0; ctr<children.length; ctr++)
			{
				buildStructure(xmlBranchNode, children[ctr], document, subSiblingCounter++);
			}
		}
		else
		{
			// leaf node

			Element xmlLeafNode = document.createElement(ELEMENT_NODE);

			xmlLeafNode.setAttribute(ATTRIBUTE_TYPE, TYPE_LEAF);

			xmlParent.appendChild(xmlLeafNode);

			// description

			Element xmlDescriptionNode = document.createElement(ELEMENT_DESCRIPTION);

			String description = ((LeafNodeModel)modelNode).getDescription();

			xmlDescriptionNode.appendChild(document.createCDATASection(description));

			xmlLeafNode.appendChild(xmlDescriptionNode);

			// notes

			Element xmlNoteNode = document.createElement(ELEMENT_NOTE);

			String note = ((NoteLeafNodeModel)modelNode).getNote();

			xmlNoteNode.appendChild(document.createCDATASection(note));

			xmlLeafNode.appendChild(xmlNoteNode);

			// image path

			Element xmlImagePathNode = document.createElement(ELEMENT_IMAGE_PATH);

			String imagePath = IMAGE_ZIP_PATH + modelNode.getPathToNode() + siblingCounter;

			xmlImagePathNode.appendChild(document.createCDATASection(imagePath));

			xmlLeafNode.appendChild(xmlImagePathNode);

			// audio data

			Element xmlAudioDataNode = document.createElement(ELEMENT_AUDIO_DATA);

			xmlLeafNode.appendChild(xmlAudioDataNode);

			if (((AudioLeafNodeModel)modelNode).containsAudioData())
			{
				xmlAudioDataNode.setAttribute(ATTRIBUTE_CONTAINS_DATA, YES);

				Element xmlAudioPathNode = document.createElement(ELEMENT_AUDIO_PATH);

				String audioPath = AUDIO_ZIP_PATH + modelNode.getPathToNode() + siblingCounter;

				xmlAudioPathNode.appendChild(document.createCDATASection(audioPath));

				xmlAudioDataNode.appendChild(xmlAudioPathNode);
			}
			else
			{
				xmlAudioDataNode.setAttribute(ATTRIBUTE_CONTAINS_DATA, NO);
			}

			// original file name

			Element xmlOriginalFileNameNode = document.createElement(ELEMENT_ORIGINAL_FILE_NAME);

			String originalFileName = ((ImageLeafNodeModel)modelNode).getOriginalImageFileName();

			xmlOriginalFileNameNode.appendChild(document.createCDATASection(originalFileName));

			xmlLeafNode.appendChild(xmlOriginalFileNameNode);

			// examination identifier

			Element xmlExaminationIdentifierNode = document.createElement(ELEMENT_EXAMINATION_IDENTIFIER);

			xmlLeafNode.appendChild(xmlExaminationIdentifierNode);

			PatientIdentifier pid = ((ExaminationLeafNodeModel)modelNode).getPID();

			Element xmlPCodeNode = document.createElement(ELEMENT_PCODE);

			xmlPCodeNode.appendChild(document.createCDATASection(pid.getPCode()));

			xmlExaminationIdentifierNode.appendChild(xmlPCodeNode);

			Element xmlPIDNode = document.createElement(ELEMENT_PID);

			xmlPIDNode.appendChild(document.createCDATASection(pid.getPID()));

			xmlExaminationIdentifierNode.appendChild(xmlPIDNode);

			Element xmlExaminationDateNode = document.createElement(ELEMENT_EXAMINATION_DATE);

			long date = ((ExaminationLeafNodeModel)modelNode).getExaminationDate().getTime();

			xmlExaminationDateNode.appendChild(document.createCDATASection(date + ""));

			xmlExaminationIdentifierNode.appendChild(xmlExaminationDateNode);
		}
	}


	// IMPORT ALBUM

	public FolderNodeModel importNode(String filePath) throws CouldNotImportException
	{
		try
		{
			// close previous zip data source (if existant)

			if (zipDataFile != null)
			{
				zipDataFile.close();
			}

			// open the zip file and point zip data source to it

			zipDataFile = new ZipFile(filePath);

			// get an input stream for the structure entry

			ZipEntry structureEntry = zipDataFile.getEntry(STRUCTURE_ZIP_PATH + STRUCTURE_ZIP_NAME);

			InputStream in = zipDataFile.getInputStream(structureEntry);

			// obtain a DOM Document from the input stream

			Document document = documentBuilder.parse(in);

			Element xmlDocumentRoot = (Element) document.getFirstChild();

			Element xmlStructureNode = (Element) xmlDocumentRoot.getFirstChild();

			// obtain and create the root node model

			Element xmlAlbumNode = (Element) xmlStructureNode.getFirstChild();

			CDATASection xmlAlbumNameNode = (CDATASection) xmlAlbumNode.getFirstChild();	// note: important that CDATA added first above

			DefaultFolderNodeModel modelRoot = new DefaultFolderNodeModel(xmlAlbumNameNode.getData());

			// build the node model structure

			NodeList nodeList = xmlAlbumNode.getChildNodes();

			for (int ctr=0; ctr<nodeList.getLength(); ctr++)
			{
				if (nodeList.item(ctr) instanceof Element)
				{
					if (((Element)nodeList.item(ctr)).getTagName().equals(ELEMENT_NODE))
					{
						buildModelStructure((Element)nodeList.item(ctr), modelRoot);
					}
				}
			}

			// return the node model (note: zip file still open)

			return modelRoot;
		}
		catch (Exception e)
		{
			e.printStackTrace();

			throw new CouldNotImportException(e.getMessage());
		}
	}

	private void buildModelStructure(Element xmlNode, NodeModel modelParentNode)
	{
		String type = xmlNode.getAttribute(ATTRIBUTE_TYPE);

		if (type.equals(TYPE_BRANCH))
		{
			// reconstruct a folder node model

			String name = ((CDATASection)xmlNode.getFirstChild()).getData();		// note: important that CDATA added first above

			FolderNodeModel modelNode = new DefaultFolderNodeModel(name);

			modelParentNode.add(modelNode);

			// recursively create all child node models

			NodeList nodeList = xmlNode.getChildNodes();

			for (int ctr=0; ctr<nodeList.getLength(); ctr++)
			{
				if (nodeList.item(ctr) instanceof Element)
				{
					if (((Element)nodeList.item(ctr)).getTagName().equals(ELEMENT_NODE))
					{
						buildModelStructure((Element)nodeList.item(ctr), modelNode);
					}
				}
			}
		}
		else
		{
			// create leaf node model

			DefaultLeafNodeModel modelNode = new DefaultLeafNodeModel();

			modelParentNode.add(modelNode);

			// obtain description from xml and create node model

			NodeList nodeList = xmlNode.getChildNodes();

			for (int ctr=0; ctr<nodeList.getLength(); ctr++)
			{
				if (nodeList.item(ctr) instanceof Element)			// currently, they all are (children of a leaf that is)
				{
					Element currentElement = (Element) nodeList.item(ctr);

					String tagName = currentElement.getTagName();

					if (tagName.equals(ELEMENT_DESCRIPTION))
					{
						if (currentElement.hasChildNodes())			// if a description is available...
						{
							modelNode.setDescription(((CDATASection)currentElement.getFirstChild()).getData());
						}
					}
					else if (tagName.equals(ELEMENT_NOTE))
					{
						if (currentElement.hasChildNodes())			// if a note is available...
						{
							modelNode.setNote(((CDATASection)currentElement.getFirstChild()).getData());
						}
					}
					else if (tagName.equals(ELEMENT_IMAGE_PATH))
					{
						final String imageBasePath = ((CDATASection)currentElement.getFirstChild()).getData();

						ImageDataObtainer imageDataObtainer = new ImageDataObtainer()
						{
							public BufferedImage getFullImage( ) throws IOException
							{
								String entryName = imageBasePath + IMAGE_FULL_POSTFIX;

								return ImageIO.read(zipDataFile.getInputStream(zipDataFile.getEntry(entryName)));
							}

							public BufferedImage getMediumImage( ) throws IOException
							{
								String entryName = imageBasePath + IMAGE_MEDIUM_POSTFIX;

								return ImageIO.read(zipDataFile.getInputStream(zipDataFile.getEntry(entryName)));
							}

							public BufferedImage getThumbImage( ) throws IOException
							{
								String entryName = imageBasePath + IMAGE_THUMB_POSTFIX;

								return ImageIO.read(zipDataFile.getInputStream(zipDataFile.getEntry(entryName)));
							}

							public byte[] getFullImageByteArray( ) throws IOException
							{
								return IOUtilities.convertBufferedImageToByteArray(getFullImage(), "jpg");			// TWEAK LATER
							}

							public byte[] getMediumImageByteArray( ) throws IOException
							{
								return IOUtilities.convertBufferedImageToByteArray(getMediumImage(), "jpg");		// TWEAK LATER
							}

							public byte[] getThumbImageByteArray( ) throws IOException
							{
								return IOUtilities.convertBufferedImageToByteArray(getThumbImage(), "jpg");			// TWEAK LATER
							}
						};

						modelNode.setImageDataObtainer(imageDataObtainer);
					}
					else if (tagName.equals(ELEMENT_AUDIO_DATA))
					{
						if (currentElement.getAttribute(ATTRIBUTE_CONTAINS_DATA).equals(YES))
						{
							// the node contains audio data

							final String audioBasePath = ((CDATASection)currentElement.getFirstChild().getFirstChild()).getData();

							AudioDataObtainer audioDataObtainer = new AudioDataObtainer()
							{
								public byte[] getAudioByteArray() throws IOException
								{
									try
									{
										String entryName = audioBasePath + AUDIO_POSTFIX;

										InputStream in = zipDataFile.getInputStream(zipDataFile.getEntry(entryName));

										return IOUtilities.getBytesFromInputStream(in);
									}
									catch (Exception e)
									{
										e.printStackTrace();

										throw new IOException(e.getMessage());
									}
								}
							};

							modelNode.setAudioDataObtainer(audioDataObtainer);
						}
					}
					else if (tagName.equals(ELEMENT_ORIGINAL_FILE_NAME))
					{
						modelNode.setOriginalImageFileName(((CDATASection)currentElement.getFirstChild()).getData());
					}
					else if (tagName.equals(ELEMENT_EXAMINATION_IDENTIFIER))
					{
						NodeList eidNodeList = currentElement.getChildNodes();

						String pCode = null; String pidS = null; long eDate = -1;

						for (int ctr2=0; ctr2<eidNodeList.getLength(); ctr2++)
						{
							Element eidCurrentElement = (Element) eidNodeList.item(ctr2);

							String eidTagName = eidCurrentElement.getTagName();

							if (eidTagName.equals(ELEMENT_PCODE))
							{
								pCode = ((CDATASection)eidCurrentElement.getFirstChild()).getData();
							}
							else if (eidTagName.equals(ELEMENT_PID))
							{
								pidS = ((CDATASection)eidCurrentElement.getFirstChild()).getData();
							}
							else if (eidTagName.equals(ELEMENT_EXAMINATION_DATE))
							{
								eDate = Long.parseLong(((CDATASection)eidCurrentElement.getFirstChild()).getData());
							}
						}

						PatientIdentifier pid = new PatientIdentifier(pCode, pidS);

						ExaminationIdentifier eid = new MedViewExaminationIdentifier(pid, new Date(eDate));

						modelNode.setPID(pid);

						modelNode.setEID(eid);
					}
				}
			}
		}
	}
}
