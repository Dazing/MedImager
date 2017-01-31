/*
 * $Id: TranslatorHandler.java,v 1.36 2010/06/28 07:12:39 oloft Exp $
 *
 * Created on den 26 juli 2002, 10:42
 *
 */

package medview.medrecords.components;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.text.*;

import medview.common.actions.*;
import medview.common.generator.*;

import medview.datahandling.*;
import medview.datahandling.examination.*;

import medview.medrecords.data.*;
import medview.medrecords.exceptions.*;

import medview.summarycreator.view.*;

import misc.foundation.*;
import misc.foundation.io.*;

import se.chalmers.cs.medview.docgen.*;
import se.chalmers.cs.medview.docgen.template.*;
import se.chalmers.cs.medview.docgen.translator.*;

/**
 * Component to show Translators and Previews in windows
 * (call showPreview or showTranslator methods).
 *
 * @author  nader, some reworking by Nils, Fredrik
 */
public class TranslatorHandler implements MedViewLanguageConstants
{
	// MEMBERS

	private static TranslatorHandler instance;

	private static MedViewDataHandler mVDH = MedViewDataHandler.instance();

	private static PreferencesModel prefs = PreferencesModel.instance();


	private JFrame previewFrame = null;

	private JDialog translatorDialog = null;

	private JTextPane previewTextPane = null;


	private String templateLocation = null;

	private String translatorLocation = null;

	private TemplateModel cachedTemplateModel = null;

	private TranslatorModel cachedTranslatorModel = null;


	// CONSTRUCTORS

	private TranslatorHandler() // defeat instantiation
	{
	}

	public static TranslatorHandler instance()
	{
		if (instance == null)
		{
			instance = new TranslatorHandler();
		}

		return instance;
	}


	// SETTING THE TEMPLATE AND TRANSLATOR TO USE

	public void setTemplateLocation(String loc)
	{
		if ((this.templateLocation != null) && !this.templateLocation.equalsIgnoreCase(loc))
		{
			clearTemplateLocation();
		}

		this.templateLocation = loc;
	}

	public void setTranslatorLocation(String loc)
	{
		if ((this.translatorLocation != null) && !this.translatorLocation.equalsIgnoreCase(loc))
		{
			clearTranslatorLocation();
		}

		this.translatorLocation = loc;
	}

	public String getTemplateLocation() // might be null
	{
		return this.templateLocation;
	}

	public String getTranslatorLocation() // might be null
	{
		return this.translatorLocation;
	}

	public boolean isTemplateLocationSet()
	{
		return (templateLocation != null);
	}

	public boolean isTranslatorLocationSet()
	{
		return (translatorLocation != null);
	}

	public void clearTemplateLocation()
	{
		this.templateLocation = null;

		this.cachedTemplateModel = null;
	}

	public void clearTranslatorLocation()
	{
		this.translatorLocation = null;

		this.cachedTranslatorModel = null;
	}

	private TemplateModel getTemplate() throws CouldNotObtainTemplateException
	{
		if (!isTemplateLocationSet())
		{
			throw new CouldNotObtainTemplateException(mVDH.getLanguageString(

				ERROR_NO_TEMPLATE_SET));
		}
		else if (cachedTemplateModel != null)
		{
			return cachedTemplateModel;
		}
		else
		{
			try
			{
				cachedTemplateModel = mVDH.loadTemplate(templateLocation);

				return cachedTemplateModel;
			}
			catch (se.chalmers.cs.medview.docgen.misc.CouldNotLoadException exc)
			{
				exc.printStackTrace();

				cachedTemplateModel = null;

				throw new CouldNotObtainTemplateException(exc);
			}
		}
	}


	// OBTAIN TRANSLATOR TO USE

	private TranslatorModel getTranslator() throws CouldNotObtainTranslatorException
	{
		if (!isTranslatorLocationSet())
		{
			throw new CouldNotObtainTranslatorException(mVDH.getLanguageString(

				ERROR_NO_TRANSLATOR_SET));
		}
		else if (cachedTranslatorModel != null)
		{
			return cachedTranslatorModel;
		}
		else
		{
			try
			{
				cachedTranslatorModel = mVDH.loadTranslator(translatorLocation);

				return cachedTranslatorModel;
			}
			catch (se.chalmers.cs.medview.docgen.misc.CouldNotLoadException exc)
			{
				exc.printStackTrace();

				cachedTranslatorModel = null;

				throw new CouldNotObtainTranslatorException(exc);
			}
		}
	}


	// OBTAIN TRANSLATOR TERMS

	public Object[] getTerms()
	{
		try
		{
			TranslatorModel model = getTranslator(); // -> CouldNotObtainTranslatorException

			return model.getContainedTerms();
		}
		catch (CouldNotObtainTranslatorException exc)
		{
			exc.printStackTrace();

			return new Object[0];
		}
	}


	// SHOW TRANSLATOR

	public void showTranslator(Frame parentFrame, String term, String newValue) throws
            CouldNotObtainTranslatorException, TermNotInTranslatorException {
		if (translatorDialog == null)
		{
			translatorDialog = new JDialog(parentFrame, mVDH.getLanguageString(

				TITLE_TRANSLATOR_LS_PROPERTY), true); // modal = true
		}

		translatorDialog.setTitle(mVDH.getLanguageString(MedViewLanguageConstants.

			TITLE_TERM_EDITOR_LS_PROPERTY) + " " + term);

		final TranslatorModel translatorModel = getTranslator(); // -> CouldNotObtainTranslatorException

		TranslationModel translationModel = translatorModel.getTranslationModel(term);

		if (translationModel == null)
		{
			throw new TermNotInTranslatorException(mVDH.getLanguageString( // -> TermNotInTranslatorException

				ERROR_TERM_NOT_FOUND_IN_TRANSLATOR));
		}
		else
		{
			translationModel.addValue(newValue);
		}

		//int type = translationModel.getType();

		String typeDesc = translationModel.getTypeDescriptor();

		// create temporary copy of the translation model for use in the editor

		final TranslationModel tempTranslationModel = (TranslationModel) translationModel.clone();

		AbstractTableTermView termView = (AbstractTableTermView) TermViewFactory.instance().createTermView(typeDesc);

		termView.setModel(tempTranslationModel);

		// create the button panel

		JPanel buttonPanel = new JPanel();

		buttonPanel.setLayout(new GridLayout(1, 3, 5, 0));

		JButton translatorOKButton = new JButton(mVDH.getLanguageString(

			MedViewLanguageConstants.BUTTON_OK_LS_PROPERTY));

		JButton translatorCancelButton = new JButton(mVDH.getLanguageString(

			MedViewLanguageConstants.BUTTON_CANCEL_TEXT_LS_PROPERTY));

		buttonPanel.add(translatorOKButton);

		buttonPanel.add(new JLabel());

		buttonPanel.add(translatorCancelButton);

		translatorOKButton.addActionListener(new ActionListener() // ok button
		{
			public void actionPerformed(ActionEvent e)
			{
				translatorModel.removeTranslationModel(tempTranslationModel.getTerm());

				translatorModel.addTranslationModel(tempTranslationModel);

				try
				{
					mVDH.saveTranslator(translatorModel, getTranslatorLocation());
				}
				catch (se.chalmers.cs.medview.docgen.misc.CouldNotSaveException exc)
				{
					exc.printStackTrace(); // provide error dialog in future - TODO
				}

				translatorDialog.dispose();
			}
		});

		translatorCancelButton.addActionListener(new ActionListener() // cancel button
		{
			public void actionPerformed(ActionEvent e)
			{
				translatorDialog.dispose();
			}
		});

		Container contentPane = translatorDialog.getContentPane();

		contentPane.removeAll();

		contentPane.setLayout(new BorderLayout());

		contentPane.add(termView, BorderLayout.CENTER);

		contentPane.add(buttonPanel, BorderLayout.SOUTH);

		translatorDialog.pack();

		termView.selectAndEdit(newValue); // scroll to and put caret in correct editing position

		translatorDialog.show(); // blocks
	}


	// SHOW PREVIEW WINDOW

	public void showPreview(Component parent, String pCode, ValueContainer container)
		throws CouldNotGeneratePreviewException
	{
		if (previewFrame == null) // construct the preview frame at first call lazily
		{
			previewFrame = new JFrame();

			previewTextPane = new JTextPane();

			previewFrame.setSize(prefs.getPreviewWindowSize());

			JScrollPane scrollPane = new JScrollPane(previewTextPane);

			Action copyClipboardAction = new ClipboardCopyAction();

			JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

			buttonPanel.add(new JButton(copyClipboardAction));

			Container contentPane = previewFrame.getContentPane();

			contentPane.add(scrollPane, BorderLayout.CENTER);

			contentPane.add(buttonPanel, BorderLayout.SOUTH);

			previewFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

			previewFrame.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent e)
				{
					Dimension previewDim = previewFrame.getSize();

					prefs.setPreviewWindowSize(previewDim);

					previewFrame.hide();
				}
			});
			
			// Pop-up menu for common text actions in the previewTextPane
			JMenuItem menuItem;
			Action menuAction;
			JPopupMenu popup = new JPopupMenu();

			TextActionProvider textActionProvider = TextActionProvider.getInstance();
			
			//Create the popup menu.
			menuAction = textActionProvider.getCutAction();
			menuItem = new JMenuItem(menuAction);
			popup.add(menuItem);
			
			menuAction = textActionProvider.getCopyAction();
			menuItem = new JMenuItem(menuAction);
			popup.add(menuItem);
			
			menuAction = textActionProvider.getPasteAction();
			menuItem = new JMenuItem(menuAction);
			popup.add(menuItem);
			
			popup.addSeparator();
			
			menuAction = textActionProvider.getSelectAllAction();
			menuItem = new JMenuItem(menuAction);
			popup.add(menuItem);
			
			//Add listener to the text area so the popup menu can come up.
			MouseListener popupListener = new PopupListener(popup);
			previewTextPane.addMouseListener(popupListener);
		}

		StyledDocument previewDocument = getPreview(pCode, container); // -> CouldNotGeneratePreviewException

		previewTextPane.setDocument(previewDocument);

		previewFrame.setLocationRelativeTo(parent);

		previewFrame.show();
	}


	// GENERATE STYLED DOCUMENT BASED ON P-CODE AND VALUECONTAINER

	private StyledDocument getPreview(String aPcode, ValueContainer container)
		throws CouldNotGeneratePreviewException
	{
		try
		{
			ExaminationIdentifier[] ids = new ExaminationIdentifier[]

				{ new MedViewExaminationIdentifier(new PatientIdentifier(aPcode)) };

			ValueContainer[] containers = new ValueContainer[] { container };

			TemplateModel template = getTemplate(); // -> CouldNotObtainTemplateException

			TranslatorModel translator = getTranslator(); // -> CouldNotObtainTranslatorException

			// de undersökningar du vill visa i previewen.

			String[] sections = template.getAllContainedSections();

			MedViewGeneratorEngineBuilder engineBuilder = new MedViewGeneratorEngineBuilder();

			engineBuilder.buildIdentifiers(ids); // -> CouldNotBuildEngineException

			engineBuilder.buildValueContainers(containers); // -> CouldNotBuildEngineException

			engineBuilder.buildTemplateModel(template); // -> CouldNotBuildEngineException

			engineBuilder.buildTranslatorModel(translator); // -> CouldNotBuildEngineException

			engineBuilder.buildSections(sections); // -> CouldNotBuildEngineException

			GeneratorEngine engine = engineBuilder.getEngine(); // -> FurtherElementsRequiredException

			StyledDocument document = engine.generateDocument(); // -> CouldNotGenerateException

			return document;
		}
		catch (CouldNotObtainTemplateException exc)
		{
			throw new CouldNotGeneratePreviewException(exc.getMessage());
		}
		catch (CouldNotObtainTranslatorException exc)
		{
			throw new CouldNotGeneratePreviewException(exc.getMessage());
		}
		catch (FurtherElementsRequiredException exc)
		{
			throw new CouldNotGeneratePreviewException(exc.getMessage());
		}
		catch (CouldNotGenerateException exc)
		{
			throw new CouldNotGeneratePreviewException(exc.getMessage());
		}
	}


	// PRIVATE CLASS FOR SYSTEM CLIPBOARD COPY

	private class ClipboardCopyAction extends MedViewAction
	{
		public void actionPerformed(ActionEvent e)
		{
			previewTextPane.selectAll();

			previewTextPane.copy();
		}

		public ClipboardCopyAction()
		{
			super(MedViewLanguageConstants.ACTION_COPY_ENTIRE_TEXT_TO_CLIPBOARD_LS_PROPERTY,

				MedViewMediaConstants.COPY_IMAGE_ICON_24);
		}
	}
	
	// PRIVATE CLASS FOR PREVIEW POP-UP MENU

	private class PopupListener extends MouseAdapter {
        JPopupMenu popup;
		
        PopupListener(JPopupMenu popupMenu) {
            popup = popupMenu;
        }
		
        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }
		
        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }
		
        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                popup.show(e.getComponent(),
                           e.getX(), e.getY());
            }
        }
    }
}
