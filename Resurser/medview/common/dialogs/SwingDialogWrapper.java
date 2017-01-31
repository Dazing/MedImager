/*
 * @(#)SwingDialogWrapper.java
 *
 * $Id: SwingDialogWrapper.java,v 1.14 2006/04/24 14:17:02 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.dialogs;

import medview.common.filefilter.*;

import medview.datahandling.*;

import java.awt.*;
import java.awt.event.*;

import java.beans.*;

import java.io.*;

import javax.swing.*;

import misc.gui.constants.*;

class SwingDialogWrapper implements MedViewLanguageConstants, MedViewMediaConstants, MedViewDataUserProperties, GUIConstants
{
	private String fileSep = System.getProperty("file.separator");

	private MedViewDataHandler mVDH = MedViewDataHandler.instance();

	private Color lastColor;

	private JDialog colorChooserDialog;

	private static final String LAST_CHOSEN_FILE_DIRECTORY_PATH_PROPERTY = "lastChosenFileDirectoryPath";

	private static final String LAST_MVD_DIRECTORY_PATH_PROPERTY = "lastMVDDirectoryPath";

	private static final String LAST_RTF_SAVE_DIRECTORY_PATH_PROPERTY = "lastRtfSaveDirectoryPath";

	private static final String LAST_INPUT_FORM_DIRECTORY_PATH_PROPERTY = "lastInputFormDirectoryPath";

	private static final String LAST_TEMPLATE_DIRECTORY_PATH_PROPERTY = "lastTemplateDirectoryPath";

	private static final String LAST_GRAPH_TEMPLATE_DIRECTORY_PATH_PROPERTY = "lastGraphTemplateDirectoryPath";

	private static final String LAST_TERM_VALUE_DIRECTORY_PATH_PROPERTY = "lastTermValueDirectoryPath";

	private static final String LAST_TERM_DEFINITION_DIRECTORY_PATH_PROPERTY = "lastTermDefinitionDirectoryPath";

	private static final String LAST_TRANSLATOR_DIRECTORY_PATH_PROPERTY = "lastTranslatorDirectoryPath";

	private static final String LAST_HTML_SAVE_DIRECTORY_PATH_PROPERTY = "lastHtmlSaveDirectoryPath";

	private static final String LAST_IMAGE_CHOICE_DIRECTORY_PATH_PROPERTY = "lastImageChoiceDirectoryPath";

	public SwingDialogWrapper() {}

	/**
	 * Creates and shows a file chooser dialog, using the specified
	 * file filter. If the file filter is set to null, the accept
	 * all default file filter will be used. The method will return
	 * a null value if an invalid file was chosen or if the user
	 * cancelled the dialog.
	 */
	public File createAndShowChooseFileDialog(Component parent, DialogFileFilter fF)
	{
		String p = LAST_CHOSEN_FILE_DIRECTORY_PATH_PROPERTY;

		String lastDir = mVDH.getUserProperty(p);

		if (lastDir == null) { lastDir = mVDH.getUserHomeDirectory(); }

		String lS = TITLE_CHOOSE_FILE_LS_PROPERTY;

		int sM = JFileChooser.FILES_AND_DIRECTORIES;

		boolean acceptAll = (fF == null);

		File file = createAndShowFileChooserDialog(parent, lS, acceptAll, fF, sM, lastDir, null);

		if (file != null)
		{
			String path = file.getAbsolutePath();

			mVDH.setUserProperty(LAST_CHOSEN_FILE_DIRECTORY_PATH_PROPERTY, path.substring(0, path.lastIndexOf(fileSep)));
		}

		return file;
	}

	/**
	 * Creates and shows a directory choose dialog. The method will
	 * return a null value if an invalid choice was made or if the
	 * user cancelled the dialog.
	 */
	public File createAndShowChooseDirectoryDialog(Component parent)
	{
		String p = LAST_CHOSEN_FILE_DIRECTORY_PATH_PROPERTY;

		String lastDir = mVDH.getUserProperty(p);

		if (lastDir == null) { lastDir = mVDH.getUserHomeDirectory(); }

		String lS = TITLE_CHOOSE_DIRECTORY_LS_PROPERTY;

		DialogFileFilter fF = new DialogDirectoryFileFilter();

		int sM = JFileChooser.DIRECTORIES_ONLY;

		File file = createAndShowFileChooserDialog(parent, lS, false, fF, sM, lastDir, null);

		if (file != null)
		{
			mVDH.setUserProperty(LAST_CHOSEN_FILE_DIRECTORY_PATH_PROPERTY, file.getAbsolutePath());
		}

		return file;
	}

	public String createAndShowLoadMVDDialog(Component parent)
	{
		String p = LAST_MVD_DIRECTORY_PATH_PROPERTY;

		String lastDir = mVDH.getUserProperty(p);

		if (lastDir == null) { lastDir = mVDH.getUserHomeDirectory(); }

		String lS = TITLE_LOAD_MVD_LS_PROPERTY;

		int sM = JFileChooser.DIRECTORIES_ONLY;

		DialogFileFilter fF = new DialogMVDFileFilter();

		ChooserPropertyListener list = new MVDPropertyListener();

		File file = createAndShowFileChooserDialog(parent, lS, false, fF, sM, lastDir, list);

		if (file != null)
		{
			String path = file.getAbsolutePath();

			mVDH.setUserProperty(LAST_MVD_DIRECTORY_PATH_PROPERTY, path.substring(0, path.lastIndexOf(fileSep)));

			return path;
		}
		else
		{
			return null;
		}
	}

	public String createAndShowChangeInputFormDialog(Component parent)
	{
		String lastDir = mVDH.getUserProperty(LAST_INPUT_FORM_DIRECTORY_PATH_PROPERTY);

		if (lastDir == null) { lastDir = mVDH.getUserHomeDirectory(); }

		int sM = JFileChooser.FILES_ONLY;

		String lS = TITLE_LOAD_INPUT_FORM_LS_PROPERTY;

		DialogFileFilter fF = new DialogXMLFileFilter();

		File file = createAndShowFileChooserDialog(parent, lS, false, fF, sM, lastDir, null);

		if (file != null)
		{
			String path = file.getAbsolutePath();

			mVDH.setUserProperty(LAST_MVD_DIRECTORY_PATH_PROPERTY, path.substring(0, path.lastIndexOf(fileSep)));

			return path;
		}
		else
		{
			return null;
		}
	}

	public String createAndShowChangeGraphTemplateDialog(Component parent)
	{
		String lastDir = mVDH.getUserProperty(LAST_GRAPH_TEMPLATE_DIRECTORY_PATH_PROPERTY);

		if (lastDir == null) { lastDir = mVDH.getUserHomeDirectory(); }

		int sM = JFileChooser.FILES_ONLY;

		String lS = TITLE_LOAD_GRAPH_TEMPLATE_LS_PROPERTY;

		DialogFileFilter fF = new DialogXMLFileFilter();

		File file = createAndShowFileChooserDialog(parent, lS, false, fF, sM, lastDir, null);

		if (file != null)
		{
			String path = file.getAbsolutePath();

			mVDH.setUserProperty(LAST_GRAPH_TEMPLATE_DIRECTORY_PATH_PROPERTY, path.substring(0, path.lastIndexOf(fileSep)));

			return path;
		}
		else
		{
			return null;
		}
	}

	public String createAndShowLoadTemplateDialog(Component parent)
	{
		String lastDir = mVDH.getUserProperty(LAST_TEMPLATE_DIRECTORY_PATH_PROPERTY);

		if (lastDir == null)
		{
			lastDir = System.getProperty("user.home");
		}

		int sM = JFileChooser.FILES_ONLY;

		String lS = TITLE_LOAD_TEMPLATE_LS_PROPERTY;

		DialogFileFilter fF = new DialogXMLFileFilter();

		File file = createAndShowFileChooserDialog(parent, lS, false, fF, sM, lastDir, null);

		if (file != null)
		{
			String path = file.getAbsolutePath();

			mVDH.setUserProperty(LAST_TEMPLATE_DIRECTORY_PATH_PROPERTY, path.substring(0, path.lastIndexOf(fileSep)));

			return path;
		}
		else
		{
			return null;
		}
	}

	public String createAndShowChangeTermDefinitionDialog(Component parent)
	{
		String lastDir = mVDH.getUserProperty(LAST_TERM_DEFINITION_DIRECTORY_PATH_PROPERTY);

		if (lastDir == null) { lastDir = mVDH.getUserHomeDirectory(); }

		int sM = JFileChooser.FILES_ONLY;

		String lS = TITLE_CHANGE_TERM_DEFINITIONS_LS_PROPERTY;

		File file = createAndShowFileChooserDialog(parent, lS, true, null, sM, lastDir, null);

		if (file != null)
		{
			String path = file.getAbsolutePath();

			mVDH.setUserProperty(LAST_TERM_DEFINITION_DIRECTORY_PATH_PROPERTY, path.substring(0, path.lastIndexOf(fileSep)));

			return path;
		}
		else
		{
			return null;
		}
	}

	public String createAndShowChangeTermValueDialog(Component parent)
	{
		String lastDir = mVDH.getUserProperty(LAST_TERM_VALUE_DIRECTORY_PATH_PROPERTY);

		if (lastDir == null) { lastDir = mVDH.getUserHomeDirectory(); }

		int sM = JFileChooser.FILES_ONLY;

		String lS = TITLE_CHANGE_TERM_VALUES_LS_PROPERTY;

		File file = createAndShowFileChooserDialog(parent, lS, true, null, sM, lastDir, null);

		if (file != null)
		{
			String path = file.getAbsolutePath();

			mVDH.setUserProperty(LAST_TERM_VALUE_DIRECTORY_PATH_PROPERTY, path.substring(0, path.lastIndexOf(fileSep)));

			return path;
		}
		else
		{
			return null;
		}
	}

	public String createAndShowSaveTemplateDialog(Component parent)
	{
		String lastDir = mVDH.getUserProperty(LAST_TEMPLATE_DIRECTORY_PATH_PROPERTY);

		if (lastDir == null)
		{
			lastDir = System.getProperty("user.home");
		}

		int sM = JFileChooser.FILES_ONLY;

		String lS = TITLE_SAVE_TEMPLATE_AS_LS_PROPERTY;

		DialogFileFilter fF = new DialogXMLFileFilter();

		File file = createAndShowFileChooserDialog(parent, lS, false, fF, sM, lastDir, null);

		if (file != null)
		{
			String path = file.getAbsolutePath();

			mVDH.setUserProperty(LAST_TERM_VALUE_DIRECTORY_PATH_PROPERTY, path.substring(0, path.lastIndexOf(fileSep)));

			if (!(path.endsWith(".xml"))) { path += ".xml"; }

			return path;
		}
		else
		{
			return null;
		}
	}

	public String createAndShowLoadTranslatorDialog(Component parent)
	{
		String lastDir = mVDH.getUserProperty(LAST_TRANSLATOR_DIRECTORY_PATH_PROPERTY);

		if (lastDir == null)
		{
			lastDir = System.getProperty("user.home");
		}

		int sM = JFileChooser.FILES_ONLY;

		String lS = TITLE_CHANGE_TRANSLATOR_LS_PROPERTY;

		DialogFileFilter fF = new DialogXMLFileFilter();

		File file = createAndShowFileChooserDialog(parent, lS, false, fF, sM, lastDir, null);

		if (file != null)
		{
			String path = file.getAbsolutePath();

			mVDH.setUserProperty(LAST_TRANSLATOR_DIRECTORY_PATH_PROPERTY, path.substring(0, path.lastIndexOf(fileSep)));

			if (!(path.endsWith(".xml"))) { path += ".xml"; }

			return path;
		}
		else
		{
			return null;
		}
	}

	public String createAndShowSaveTranslatorDialog(Component parent)
	{
		String lastDir = mVDH.getUserProperty(LAST_TRANSLATOR_DIRECTORY_PATH_PROPERTY);

		if (lastDir == null)
		{
			lastDir = System.getProperty("user.home");
		}

		String lS = TITLE_SAVE_TRANSLATOR_AS_LS_PROPERTY;

		DialogFileFilter fF = new DialogXMLFileFilter();

		int sM = JFileChooser.FILES_ONLY;

		File file = createAndShowFileChooserDialog(parent, lS, false, fF, sM, lastDir, null);

		if (file != null)
		{
			String path = file.getAbsolutePath();

			mVDH.setUserProperty(LAST_TRANSLATOR_DIRECTORY_PATH_PROPERTY, path.substring(0, path.lastIndexOf(fileSep)));

			if (!(path.endsWith(".xml"))) { path += ".xml"; }

			return path;
		}
		else
		{
			return null;
		}

		/* NOTE: If the getDefaultTranslatorDirectory()
		 * method throws a MethodNotSupportedException,
		 * it is probably the case that the implementation
		 * class of the TemplateAndTranslatorDataHandler
		 * does not handle templates and translators via
		 * the file system (may be it uses SQL or likewise).
		 * Therefore, the various dialogs where the user can
		 * choose templates and translators are not supposed
		 * to be used for such implementations, and the methods
		 * return a null value. */
	}

	public String createAndShowChangeExaminationDirectoryDialog(Component parent)
	{
		String iDir = mVDH.getExaminationDataLocation();

		if (iDir == null) { iDir = mVDH.getUserHomeDirectory(); }

		String lS = TITLE_CHANGE_EXAMINATION_LOCATION_LS_PROPERTY;

		DialogFileFilter fF = new DialogDirectoryFileFilter();

		int sM = JFileChooser.DIRECTORIES_ONLY;

		File file = createAndShowFileChooserDialog(parent, lS, false, fF, sM, iDir, null);

		if ((file != null) && (file.isDirectory()))
		{
			return file.getAbsolutePath();
		}
		else
		{
			return null;
		}

		/* NOTE: The implementation class of the
		 * ExaminationDataHandler interface in the
		 * datahandling package may not have bothered
		 * to return a proper value from the method
		 * getExaminationDataLocation() specified in
		 * the interface. In such a case, the dialog
		 * should just not disappear, but a reasonable
		 * default is used - namely the data base
		 * location. */
	}

	public String createAndShowSaveRTFDialog(Component parent)
	{
		String iDir = mVDH.getUserProperty(LAST_RTF_SAVE_DIRECTORY_PATH_PROPERTY);

		if (iDir == null) { iDir = mVDH.getUserHomeDirectory(); }

		String lS = TITLE_SAVE_RTF_LS_PROPERTY;

		DialogFileFilter fF = new DialogRTFFileFilter();

		int sM = JFileChooser.FILES_ONLY;

		File file = createAndShowFileChooserDialog(parent, lS, false, fF, sM, iDir, null);

		if (file != null)
		{
			String path = file.getAbsolutePath();

			mVDH.setUserProperty(LAST_RTF_SAVE_DIRECTORY_PATH_PROPERTY, path.substring(0, path.lastIndexOf(fileSep)));

			if (!(path.endsWith(".rtf"))) { path += ".rtf"; }

			return path;
		}
		else
		{
			return null;
		}
	}

	public String createAndShowSaveHTMLDialog(Component parent)
	{
		String iDir = mVDH.getUserProperty(LAST_HTML_SAVE_DIRECTORY_PATH_PROPERTY);

		if (iDir == null) { iDir = mVDH.getUserHomeDirectory(); }

		String lS = TITLE_SAVE_HTML_LS_PROPERTY;

		DialogFileFilter fF = new DialogHTMLFileFilter();

		int sM = JFileChooser.FILES_ONLY;

		File file = createAndShowFileChooserDialog(parent, lS, false, fF, sM, iDir, null);

		if (file != null)
		{
			String path = file.getAbsolutePath();

			mVDH.setUserProperty(LAST_HTML_SAVE_DIRECTORY_PATH_PROPERTY, path.substring(0, path.lastIndexOf(fileSep)));

			if (!(path.endsWith(".html") || path.endsWith(".htm"))) { path += ".html"; }

			return path;
		}
		else
		{
			return null;
		}
	}

	public String createAndShowChooseImageDialog(Component parent)
	{
		String iDir = mVDH.getUserProperty(LAST_IMAGE_CHOICE_DIRECTORY_PATH_PROPERTY);

		if (iDir == null) { iDir = mVDH.getUserHomeDirectory(); }

		String lS = TITLE_CHOOSE_IMAGE_LS_PROPERTY;

		DialogFileFilter fF = new DialogImageFileFilter();

		int sM = JFileChooser.FILES_ONLY;

		File file = createAndShowFileChooserDialog(parent, lS, false, fF, sM, iDir, null);

		if (file != null)
		{
			String path = file.getAbsolutePath();

			mVDH.setUserProperty(LAST_IMAGE_CHOICE_DIRECTORY_PATH_PROPERTY, path.substring(0, path.lastIndexOf(fileSep)));

			return path;
		}
		else
		{
			return null;
		}
	}

	public Color createAndShowChooseColorDialog(Component parent)
	{
		if (colorChooserDialog == null)
		{
			final String prop = TITLE_COLOR_CHOOSER_LS_PROPERTY;

			final String title = mVDH.getLanguageString(prop);

			final JColorChooser chooser = new JColorChooser();

			ActionListener okList = new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					lastColor = chooser.getColor();

					colorChooserDialog.hide();
				}
			};

			ActionListener cancList = new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					lastColor = null;

					colorChooserDialog.hide();
				}
			};

			colorChooserDialog = JColorChooser.createDialog(parent, title, true, chooser, okList, cancList);

			int op = WindowConstants.HIDE_ON_CLOSE;

			colorChooserDialog.setDefaultCloseOperation(op);

			colorChooserDialog.pack();
		}

		colorChooserDialog.setLocationRelativeTo(parent);

		colorChooserDialog.show();

		return lastColor;
	}

	// UTILITY METHODS

	private File createAndShowFileChooserDialog(Component parent, String titleLS, boolean acceptAll,

		DialogFileFilter fileFilter, int selectionMode, String initialDir, ChooserPropertyListener prop)
	{
		JFileChooser fileChooser = null;

		if (initialDir != null)
		{
			fileChooser = new JFileChooser(initialDir);
		}
		else
		{
			fileChooser = new JFileChooser(mVDH.getUserHomeDirectory());
		}

		fileChooser.setPreferredSize(FILECHOOSER_SIZE_STANDARD);

		fileChooser.setAcceptAllFileFilterUsed(acceptAll);

		if (fileFilter != null) { fileChooser.setFileFilter(fileFilter); }

		fileChooser.setFileSelectionMode(selectionMode);

		String title = mVDH.getLanguageString(titleLS);

		if (prop != null)
		{
			prop.setChooser(fileChooser); prop.setFilter(fileFilter);

			fileChooser.addPropertyChangeListener(prop);
		}

		int state = fileChooser.showDialog(parent, title);

		if (state == JFileChooser.APPROVE_OPTION)
		{
			return fileChooser.getSelectedFile();
		}
		else
		{
			return null;
		}
	}

	private abstract class ChooserPropertyListener implements PropertyChangeListener
	{
		public void setChooser(JFileChooser chooser) { this.chooser = chooser; }

		public void setFilter(DialogFileFilter filter) { this.filter = filter; }

		protected DialogFileFilter filter;

		protected JFileChooser chooser;
	}

	private class MVDPropertyListener extends ChooserPropertyListener
	{
		public void propertyChange(PropertyChangeEvent e)
		{
			if (e.getPropertyName().equals(JFileChooser.DIRECTORY_CHANGED_PROPERTY))
			{
				File newFile = (File) e.getNewValue();

				String newDir = newFile.getPath();

				String[] extensions = filter.getExtensions();

				for (int ctr=0; ctr<extensions.length; ctr++)
				{
					if (newDir.endsWith(extensions[ctr]))
					{
						chooser.setSelectedFile(newFile);

						chooser.approveSelection();
					}
				}
			}
		}
	}
}
