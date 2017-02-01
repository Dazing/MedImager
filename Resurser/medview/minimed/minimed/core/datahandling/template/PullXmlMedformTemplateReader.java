package minimed.core.datahandling.template;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Stack;
import minimed.core.MinimedConstants;
import minimed.core.models.CategoryModel;
import minimed.core.models.ExaminationModel;
import minimed.core.models.FieldModel;
import minimed.core.models.PresetModel;
import minimed.core.properties.PropertiesHandler;
import minimed.core.datahandling.DataHandlerFactory;
import minimed.core.datahandling.NoSuchTermException;
import minimed.core.datahandling.term.TermDataHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import ch.ubique.inieditor.IniEditor;

/**
 * This class provides functionality to build an Examination object from
 * an XML form. It is assumed that the form conforms to a DTD. The created
 * Examination is not yet complete, it has to be filled with term values.
 * 
 * @author Andreas Nilsson
 */
public class PullXmlMedformTemplateReader {
	
	Stack stack;
	ExaminationModel exam;
	CategoryModel currentCat;
	FieldModel currentInput;
	PresetModel currentPresets;
	TermDataHandler termHandler;
	IniEditor settings, language;
	
	/* Manages dependencies. */
	HashSet dependencyLinks = new HashSet();
	DependencyLink currentDepLink;
	HashSet inputModels = new HashSet();
	
	public PullXmlMedformTemplateReader() {
		settings = new IniEditor();
		try {
			settings.load(MinimedConstants.SETTINGS_FILE);
		} catch (IOException ioe) {
			System.out.println("No such file: " + MinimedConstants.SETTINGS_FILE);
			System.exit(1);
		}
		
		/* Load language file */
		language = new IniEditor();
		try {
			language.load(MinimedConstants.LANGUAGE_FILE);
		} catch (IOException ioe) {
			System.out.println("No such file: " + MinimedConstants.LANGUAGE_FILE);
			System.exit(1);
		}
		
		termHandler = DataHandlerFactory.instance().getDefaultTermDataHandler();
		
		PropertiesHandler propHandler = new PropertiesHandler(settings, language);
		termHandler.setTermDefinitionLocation(propHandler.getTermDefinitionsPath());
		termHandler.setTermValueLocation(propHandler.getTermValuesPath());
	}
	/**
	 * Creates an <code>ExaminationModel</code> object from the XML form provided
	 * as input. 
	 * 
	 * @param pFile an XML form. 
	 * @return a model built from the form. 
	 */
	public ExaminationModel readXMLExamination(File pFile) {
		XmlPullParserFactory factory;
		XmlPullParser parser;
		
		exam = new ExaminationModel();
		
		stack = new Stack();
		
		try {
			factory = XmlPullParserFactory.newInstance();
			parser = factory.newPullParser();
			
		} catch (XmlPullParserException e) {
			return null;
		}
		
		/* Sets the input. */
		try {
			FileReader filereader = new FileReader(pFile);
			parser.setInput(filereader);
		} catch (XmlPullParserException e) {
			return null;
		} catch (FileNotFoundException e) {
			return null;
		}
		
		
		/* Parse. */
		int eventType;
		try {
			eventType = parser.getEventType();
			
			do {
				if(eventType == XmlPullParser.START_DOCUMENT) {
					/* Do nothing. */
				} else if(eventType == XmlPullParser.END_DOCUMENT) {
					/* Do nothing. */
				} else if(eventType == XmlPullParser.START_TAG) {
					processStartElement(parser);
				} else if(eventType == XmlPullParser.END_TAG) {
					processEndElement(parser);
				} else if(eventType == XmlPullParser.TEXT) {
					processText(parser);
				}
				eventType = parser.next();
			} while (eventType != XmlPullParser.END_DOCUMENT);
			
		} catch (XmlPullParserException e1) {
			return null;
		} catch (IOException e) {
			return null;
		}			
		
		/* Link dependencies. */
		Iterator linkIterator = dependencyLinks.iterator();
		DependencyLink currentLink;
		FieldModel dependentFieldModel;
		
		while (linkIterator.hasNext()) {
			currentLink = (DependencyLink)linkIterator.next();
			dependentFieldModel = currentLink.inputmodel;
			Iterator fieldModelIterator = inputModels.iterator();
			
			/* Find dependent input model. */ 
			String depInputModelName = currentLink.depTerm;
			
			while (fieldModelIterator.hasNext()) {
				FieldModel thisFieldModel = (FieldModel)fieldModelIterator.next();
				if (thisFieldModel.getName().equalsIgnoreCase(depInputModelName)) {
					dependentFieldModel.addDepModel(currentLink.depVal, thisFieldModel);
				}
			}
			
		}
		
		Iterator fieldModelIterator = inputModels.iterator();
		while (fieldModelIterator.hasNext()) {
			FieldModel fm = (FieldModel)fieldModelIterator.next();
		}
				
		return exam;
	}
	
	void processStartElement(XmlPullParser parser) {
		stack.push(parser.getName());
		String name = parser.getName();
				
		/* Creates a new category. */
		if (name.equalsIgnoreCase("CATEGORY")) {
			currentCat = new CategoryModel();
		}
		
		if (name.equalsIgnoreCase("INPUT")) {	
			currentPresets = new PresetModel("");
			currentInput = new FieldModel("", MinimedConstants.FIELD_TYPE_SINGLE,
					currentPresets , "", "");
			
			/* Add to list of all models (needed for dependency linking). */
			inputModels.add(currentInput);
			
			/* Fetch attributes. */
			int nAttributes = parser.getAttributeCount();
			for (int i = 0; i<nAttributes; i++) {
				String attrName = parser.getAttributeName(i);
				String attrValue = parser.getAttributeValue(i);
				
				if (attrName.equalsIgnoreCase("translatable")) {
					if (attrValue.equalsIgnoreCase("true")) {
						currentInput.setTranslateAble(true);
					} else {
						currentInput.setTranslateAble(false);
					}
				}
				
				if (attrName.equalsIgnoreCase("required")) {
					if (attrValue.equalsIgnoreCase("true")) {
						currentInput.setRequired(true);
					} else {
						currentInput.setRequired(false);
					}
				}
				
				if (attrName.equalsIgnoreCase("visible")) {
					if (attrValue.equalsIgnoreCase("true")) {
						currentInput.setVisible();
					} else {
						currentInput.setNotVisible();
					}
				}
				
				if (attrName.equalsIgnoreCase("type")) {
					
					if (attrValue.equalsIgnoreCase("identification")) {
						currentInput.setFieldType(MinimedConstants.FIELD_TYPE_IDENTIFICATION);
					} else if (attrValue.equalsIgnoreCase("text")) {
						currentInput.setFieldType(MinimedConstants.FIELD_TYPE_TEXT);
					} else if (attrValue.equalsIgnoreCase("note")) {
						currentInput.setFieldType(MinimedConstants.FIELD_TYPE_NOTE);
					} else if (attrValue.equalsIgnoreCase("single")) {
						currentInput.setFieldType(MinimedConstants.FIELD_TYPE_SINGLE);
					} else if (attrValue.equalsIgnoreCase("multi")) {
						currentInput.setFieldType(MinimedConstants.FIELD_TYPE_MULTI);
					} else if (attrValue.equalsIgnoreCase("question")) {
						currentInput.setFieldType(MinimedConstants.FIELD_TYPE_QUESTION);
					} else if (attrValue.equalsIgnoreCase("interval")) {
						currentInput.setFieldType(MinimedConstants.FIELD_TYPE_INTERVAL);
					} else if (attrValue.equalsIgnoreCase("vas")) {
						currentInput.setFieldType(MinimedConstants.FIELD_TYPE_VAS);
					}				
				}
			}
		}
		
		if (name.equalsIgnoreCase("DEPRULE")) {
			currentDepLink = new DependencyLink();
			currentDepLink.inputmodel = currentInput;
		}
	}
	
	void processEndElement(XmlPullParser parser) {
		stack.pop();
		
		String name = parser.getName();
		
		/* End of category, add it to examination list. */
		if (name.equalsIgnoreCase("CATEGORY")) {
			exam.addCategory(currentCat);
		}
		
		/* End of input, add it to current category. */
		if (name.equalsIgnoreCase("INPUT")) {
			currentCat.addInput(currentInput);
		}
		
		if (name.equalsIgnoreCase("DEPRULE")) {
			dependencyLinks.add(currentDepLink);
		}
	}
	
	void processText(XmlPullParser parser) {
		String element = (String)stack.peek();
		String text = parser.getText();
				
//		if (element.equalsIgnoreCase("NODE")) {}
		
		if (element.equalsIgnoreCase("HEADER")) {
			currentCat.setTitle(text);
		}
		
		/* Process elements in <INPUT> tag. */
		if (element.equalsIgnoreCase("TERM")) {
			currentInput.setName(text);
			
			/* Fetch preset terms */
			try {
				currentPresets.addPresets(termHandler.getValues(text));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NoSuchTermException e) {}
			
			currentInput.setPresetModel(currentPresets);
		}
		
		if (element.equalsIgnoreCase("DESCRIPTION")) {
			currentInput.setDescription(text);
		}
		
		if (element.equalsIgnoreCase("DEPVAL")) {
			currentDepLink.depVal = text.toLowerCase();
		}
		
		if (element.equalsIgnoreCase("DEPTERM")) {
			currentDepLink.depTerm = text;
		}
		
		if (element.equalsIgnoreCase("COMMENT")) {
			currentInput.setComment(text);
		}
		
//		// Process <FORMINFO> elements, i.e. contact info.
//		if (element.equalsIgnoreCase("AUTHOR")) {
//		exam.author = text;
//		}
//		
//		if (element.equalsIgnoreCase("TITLE")) {
//		exam.title = text;
//		}
//		
//		if (element.equalsIgnoreCase("DATE")) {
//		exam.date = text;
//		}
//		
//		if (element.equalsIgnoreCase("NOTICE")) {
//		exam.notice = text;
//		}
//		
//		if (element.equalsIgnoreCase("TELEPHONE")) {
//		exam.contact.telephone = text;
//		}
//		
//		if (element.equalsIgnoreCase("FAX")) {
//		exam.contact.fax = text;
//		}
//		
//		if (element.equalsIgnoreCase("EMAIL")) {
//		exam.contact.email = text;
//		}
//		
//		if (element.equalsIgnoreCase("COUNTRY")) {
//		exam.contact.country = text;
//		}
//		
//		if (element.equalsIgnoreCase("CITY")) {
//		exam.contact.city = text;
//		}
//		
//		if (element.equalsIgnoreCase("POSTCODE")) {
//		exam.contact.postcode = text;
//		}
//		
//		if (element.equalsIgnoreCase("STREET")) {
//		exam.contact.street = text;
//		}
	}
}
