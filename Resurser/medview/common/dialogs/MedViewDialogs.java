/*
 * @(#)MedViewDialogs.java
 *
 * $Id: MedViewDialogs.java,v 1.25 2006/04/24 14:17:01 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.dialogs;

import java.awt.*;

import java.io.*;

import javax.swing.text.*;

import medview.common.dialogs.settings.*;
import medview.common.filefilter.*;

import misc.domain.*;

import misc.foundation.*;

public class MedViewDialogs
{

	// METHODS RETURNING DIALOGS

	public MedViewDialog createAddValueDialog(Window owner)
	{
		if (owner instanceof Frame)
		{
			return new AddValueDialog((Frame)owner);
		}
		else
		{
			return new AddValueDialog((Dialog)owner);
		}
	}

	public MedViewDialog createAddIntervalDialog(Window owner)
	{
		if (owner instanceof Frame)
		{
			return new AddIntervalDialog((Frame)owner);
		}
		else
		{
			return new AddIntervalDialog((Dialog)owner);
		}
	}

	public MedViewDialog createAddSectionDialog(Window owner, String[] sections)
	{
		if (owner instanceof Frame)
		{
			return new AddSectionDialog((Frame)owner, sections);
		}
		else
		{
			return new AddSectionDialog((Dialog)owner, sections);
		}
	}

	public MedViewDialog createRenameSectionDialog(Window owner, String currName)
	{
		if (owner instanceof Frame)
		{
			return new RenameSectionDialog((Frame)owner, currName);
		}
		else
		{
			return new RenameSectionDialog((Dialog)owner, currName);
		}
	}

	public MedViewDialog createChangePCodeDialog(Window owner)
	{
		if (owner instanceof Frame)
		{
			return new ChangePCodeDialog((Frame)owner);
		}
		else
		{
			return new ChangePCodeDialog((Dialog)owner);
		}
	}

	public MedViewDialog createNewTranslatorDialog(Window owner)
	{
		if (owner instanceof Frame)
		{
			return new NewTranslatorDialog((Frame)owner);
		}
		else
		{
			return new NewTranslatorDialog((Dialog)owner);
		}
	}

	/**
	 * Constructs a splash window, will not show it.
	 * @param im The image that is to be displayed in the splash window.
	 * @param devLS The abstract language string for the developer text.
	 * @param loadLS The abstract language string for initial loading text.
	 * @param bG The background color of the text areas in the window.
	 * @param style The style of the splash window, one of:\n
	 * MedViewSplashWindow.IMAGE_LOAD_DEVELOPER_STYLE\n
	 * MedViewSplashWindow.IMAGE_LOAD_STYLE\n
	 */
	public MedViewSplashWindow createSplashWindow(Image im, String devLS, String loadLS, Color bG, int style)
	{
		return new MedViewSplashWindow(im, devLS, loadLS, bG, style);
	}

	// METHODS CREATING AND SHOWING SWING DIALOGS

	public String createAndShowLoadMVDDialog(Window owner)
	{
		return swingDialogWrapper.createAndShowLoadMVDDialog(owner);
	}

	public String createAndShowChangeInputFormDialog(Window owner)
	{
		return swingDialogWrapper.createAndShowChangeInputFormDialog(owner);
	}

	public String createAndShowLoadTemplateDialog(Window owner)
	{
		return swingDialogWrapper.createAndShowLoadTemplateDialog(owner);
	}

	public String createAndShowChangeGraphTemplateDialog(Window owner)
	{
		return swingDialogWrapper.createAndShowChangeGraphTemplateDialog(owner);
	}

	/**
	 * Creates and shows a file chooser dialog, using the default
	 * accept all file filter. The method will return a null value
	 * if an invalid file was chosen or if the user cancelled the dialog.
	 */
	public File createAndShowChooseFileDialog(Window owner)
	{
		return swingDialogWrapper.createAndShowChooseFileDialog(owner, null);
	}

	/**
	 * Creates and shows a file chooser dialog, using the specified
	 * file filter. If the file filter is set to null, the accept
	 * all default file filter will be used. The method will return
	 * a null value if an invalid file was chosen or if the user
	 * cancelled the dialog.
	 */
	public File createAndShowChooseFileDialog(Window owner, DialogFileFilter fF)
	{
		return swingDialogWrapper.createAndShowChooseFileDialog(owner, fF);
	}

	/**
	 * Creates and shows a directory choose dialog. The metod will
	 * return a null value if an invalid choice was made or if the
	 * user cancelled the dialog.
	 */
	public File createAndShowChooseDirectoryDialog(Window owner)
	{
		return swingDialogWrapper.createAndShowChooseDirectoryDialog(owner);
	}

	/**
	 * Creates and shows a term definition change dialog,
	 * centered over the specified component. If the
	 * dialog is cancelled by the user, a null value is
	 * returned.
	 */
	public String createAndShowChangeTermDefinitionDialog(Window owner)
	{
		return swingDialogWrapper.createAndShowChangeTermDefinitionDialog(owner);
	}

	/**
	 * Creates and shows a term value change dialog,
	 * centered over the specified component. If the
	 * dialog is cancelled by the user, a null value is
	 * returned.
	 */
	public String createAndShowChangeTermValueDialog(Window owner)
	{
		return swingDialogWrapper.createAndShowChangeTermValueDialog(owner);
	}

	public String createAndShowLoadTranslatorDialog(Window owner)
	{
		return swingDialogWrapper.createAndShowLoadTranslatorDialog(owner);
	}

	public String createAndShowSaveTemplateDialog(Window owner)
	{
		return swingDialogWrapper.createAndShowSaveTemplateDialog(owner);
	}

	public String createAndShowSaveTranslatorDialog(Window owner)
	{
		return swingDialogWrapper.createAndShowSaveTranslatorDialog(owner);
	}

	public String createAndShowChangeExaminationDirectoryDialog(Window owner)
	{
		return swingDialogWrapper.createAndShowChangeExaminationDirectoryDialog(owner);
	}

	public Color createAndShowChooseColorDialog(Window owner)
	{
		return swingDialogWrapper.createAndShowChooseColorDialog(owner);
	}

	public String createAndShowChooseImageDialog(Window owner)
	{
		return swingDialogWrapper.createAndShowChooseImageDialog(owner);
	}

	public String createAndShowSaveRTFDialog(Window owner)
	{
		return swingDialogWrapper.createAndShowSaveRTFDialog(owner);
	}

	public String createAndShowSaveHTMLDialog(Window owner)
	{
		return swingDialogWrapper.createAndShowSaveHTMLDialog(owner);
	}

	// METHODS CREATING AND SHOWING OTHER DIALOGS

	/**
	 * Creates and show a 'change name' dialog, centered over the
	 * specified component. The parameter 'orig' is the original
	 * text filled in the text field, and the 'desc' parameter is
	 * descriptive text placed just beneath the field (if null,
	 * this text is not included). The 'l' parameter is the maximum
	 * allowed length of input. The method returns the string entered
	 * into the field, or null if the dialog was dismissed.
	 */
	public String createAndShowChangeNameDialog(Window owner, String orig, String desc, int l)
	{
		MedViewDialog dialog;

		if (owner instanceof Frame)
		{
			dialog = new ChangeNameDialog((Frame)owner, orig, desc, l);
		}
		else
		{
			dialog = new ChangeNameDialog((Dialog)owner, orig, desc, l);
		}

		dialog.show();

		if (!dialog.wasDismissed())
		{
			return (String) dialog.getObjectData();
		}
		else
		{
			return null; // dialog was dismissed
		}
	}

	/**
	 * Creates and shows a modal 'about' dialog. The following
	 * needs to be specified in order for the dialog to be
	 * displayed:\n
	 * \n
	 * 1) The parent component (over which the dialog is centered)\n
	 * 2) The language resource string for the dialog's title\n
	 * 3) The name of your application (example "MedSummary")\n
	 * 4) The version number (as a string) of your application (example "1.0.2")\n
	 * 5) The language resource string of text contained in text area of dialog\n
	 * \n
	 */
	public void createAndShowAboutDialog(Window owner, String titLS, String aN, String vN, String txtLS)
	{
		MedViewDialog dialog;

		if (owner instanceof Frame)
		{
			dialog = new AboutDialog((Frame)owner, titLS, aN, vN, txtLS);
		}
		else
		{
			dialog = new AboutDialog((Dialog)owner, titLS, aN, vN, txtLS);
		}

		dialog.show();
	}

	public int createAndShowQuestionDialog(Window owner, int type, String message)
	{
		MedViewDialog dialog;

		if (owner instanceof Frame)
		{
			dialog = new QuestionDialog((Frame)owner, type, message);
		}
		else
		{
			dialog = new QuestionDialog((Dialog)owner, type, message);
		}

		dialog.show();

		return ( (Integer) dialog.getObjectData()).intValue();
	}

	/**
	 * Creates and shows a single non-modal image dialog, with the
	 * specified title.
	 */
	public void createAndShowImageDialog(Window owner, String title, String path)
	{
		createAndShowImageDialogs(owner, new String[] {title}, new String[] {path});
	}

	/**
	 * Creates and shows a single non-modal image dialog, with the
	 * specified title.
	 */
	public void createAndShowImageDialog(Window owner, String title, Image image)
	{
		createAndShowImageDialogs(owner, new String[] {title}, new Image[] {image});
	}

	/**
	 * Creates and shows multiple non-modal image dialogs, each with
	 * a separate title.
	 */
	public void createAndShowImageDialogs(Window owner, String[] titles, String[] paths)
	{
		int locOffs = 0;

		if (paths.length > 0)
		{
			locOffs = (int) ( ( -1.0) * ( ( ( (double) paths.length) / 2.0) * 15.0));
		}

		for (int ctr = 0; ctr < paths.length; ctr++)
		{
			MedViewDialog dialog;

			if (owner instanceof Frame)
			{
				dialog = new ImageDialog((Frame)owner, titles[ctr], paths[ctr], false); // not modal
			}
			else
			{
				dialog = new ImageDialog((Dialog)owner, titles[ctr], paths[ctr], false); // not modal
			}

			Point loc = dialog.getLocation();

			loc.setLocation(loc.x += locOffs, loc.y += locOffs);

			dialog.setLocation(loc);

			dialog.show();

			locOffs += 15;
		}
	}

	/**
	 * Creates and shows multiple non-modal image dialogs, each with
	 * a separate title.
	 */
	public void createAndShowImageDialogs(Window owner, String[] titles, Image[] images)
	{
		int locOffs = 0;

		if (images.length > 0)
		{
			locOffs = (int) ( ( -1.0) * ( ( ( (double) images.length) / 2.0) * 15.0));
		}

		for (int ctr = 0; ctr < images.length; ctr++)
		{
			MedViewDialog dialog;

			if (owner instanceof Frame)
			{
				dialog = new ImageDialog((Frame)owner, titles[ctr], images[ctr], false); // not modal
			}
			else
			{
				dialog = new ImageDialog((Dialog)owner, titles[ctr], images[ctr], false); // not modal
			}

			Point loc = dialog.getLocation();

			loc.setLocation(loc.x += locOffs, loc.y += locOffs);

			dialog.setLocation(loc);

			dialog.show();

			locOffs += 15;
		}
	}

	public void createAndShowErrorDialog(Window owner, String message)
	{
		MedViewDialog dialog;

		if (owner instanceof Frame)
		{
			dialog = new ErrorDialog((Frame)owner, message);
		}
		else
		{
			dialog = new ErrorDialog((Dialog)owner, message);
		}

		dialog.show();
	}

	public void createAndShowInfoDialog(Window owner, String message)
	{
		MedViewDialog dialog;

		if (owner instanceof Frame)
		{
			dialog = new InfoDialog((Frame)owner, message);
		}
		else
		{
			dialog = new InfoDialog((Dialog)owner, message);
		}

		dialog.show();
	}

	/**
	 * Creates and shows a single non-modal text dialog, with the
	 * specified title.
	 */
	public void createAndShowTextDialog(Window owner, StyledDocument doc, String title)
	{
		this.createAndShowTextDialogs(owner, new StyledDocument[] {doc}, new String[] {title}, false);
	}

	/**
	 * Creates and shows multiple text dialogs, each with
	 * a separate title. If the modal parameter is true,
	 * each dialog will be modal.
	 */
	public void createAndShowTextDialogs(Window owner, StyledDocument[] docs, String[] titles, boolean modal)
	{
		int locOffs = 0;

		if (docs.length > 0)
		{
			locOffs = (int) ( ( -1.0) * ( ( ( (double) docs.length) / 2.0) * 15.0));
		}

		for (int ctr = 0; ctr < docs.length; ctr++)
		{
			StyledDocument doc = docs[ctr];

			String title = titles[ctr];

			TextDialog dialog;

			if (owner instanceof Frame)
			{
				dialog = new TextDialog((Frame)owner, doc, title, modal);
			}
			else
			{
				dialog = new TextDialog((Dialog)owner, doc, title, modal);
			}

			Point loc = dialog.getLocation();

			loc.setLocation(loc.x += locOffs, loc.y += locOffs);

			dialog.setLocation(loc);

			dialog.show();

			locOffs += 15;
		}
	}

	// PROGRESS MONITORING

	/**
	 * 1) CALL THIS METHOD ON THE EVENT DISPATCH THREAD
	 *
	 * 2) THE METHOD ASSUMES YOU ARE NOTIFYING THE DIALOG PROGRESS DIALOG
	 *
	 * 3) THE METHOD PLACES THE RUNNABLE ON A NON-EDT THREAD
	 *
	 * The runner is placed on a new thread (not on the event dispatch thread),
	 * so any methods in it that update Swing components must be placed on the
	 * event dispatch thread by using the swing utilities framework. This method
	 * assumes that the specified runnable notifies the progress dialog from the
	 * dialog package.
	 *
	 * EXAMPLE:
	 *
	 * ProgressDialog not = MedViewDialogs.instance().getProgressDialog();
	 *
	 * Runnable runner = new Runnable()
	 * {
	 *     doSomething(not); // doSomething takes a ProgressNotifiable
	 * }
	 *
	 * MedViewDialogs.instance().monitorProgress(runner);
	 *
	 */
	public void monitorProgress(Runnable runner)
	{
		final Thread rT = new Thread(runner);

		rT.start();

		final Thread wT = new Thread(new Runnable()
		{
			public void run()
			{
				try
				{
					rT.join();
				}
				catch (InterruptedException e)
				{}

				progressDialog.hide();
			}
		});

		if (rT.isAlive())
		{
			wT.start();

			progressDialog.show(); // blocks until hidden

			progressDialog.hide(); // insurance of hidden
		}
	}

	/**
	 * Places the runnable on a <b>non-EDT thread</b> and starts it.
	 * Will set the NotifyingRunnable's notifiable to a progress
	 * dialog that shows progress visibly. The component is for
	 * centering the progress dialog.
	 *
	 * Returns a Thread object that can be join'ed if the caller
	 * wants to wait with further progress until the operation is
	 * completed.
	 *
	 * @param runnable NotifyingRunnable
	 * @param cC Component
	 */
	public Thread startProgressMonitoring(Window owner, NotifyingRunnable runnable)
	{
		final Thread runnerThread = new Thread(runnable);

		final ProgressDialog progressDialog;

		if (owner instanceof Frame)
		{
			progressDialog = new ProgressDialog((Frame)owner);
		}
		else
		{
			progressDialog = new ProgressDialog((Dialog)owner);
		}

		runnable.setNotifiable(progressDialog);

		progressDialog.show();

		runnerThread.start();

		Thread disposerThread = new Thread(new Runnable()
		{
			public void run()
			{
				try
				{
					runnerThread.join(); // wait for runner thread to die

					progressDialog.dispose();
				}
				catch (InterruptedException exc)
				{
					exc.printStackTrace();

					System.exit(1); // this should not happen - fatal if it does
				}
			}
		});

		disposerThread.start();

		return disposerThread;
	}

	// SINGLETON OBTAINAL AND CREATION

	/**
	 * Obtain the singleton instance of the MedViewDialogs
	 * class. The instance will be created at the first call
	 * of this method, subsequent calls will return the initially
	 * created singleton.
	 */
	public static MedViewDialogs instance()
	{
		if (instance == null)
		{
			instance = new MedViewDialogs();
		}

		return instance;
	}

	// SETTINGS RELATED

	/**
	 * Creates and shows a settings dialog, populated with the
	 * specified settings panels, and centered over the specified
	 * component.
	 * @param cC Component
	 * @param panels SettingsContentPanel[]
	 */
	public void createAndShowSettingsDialog(Frame owner, SettingsContentPanel[] panels)
	{
		this.createAndShowSettingsDialog(owner, "", panels, null);
	}

	/**
	 * Creates and shows a settings dialog, with the specified
	 * settings content panels. The first tab added will be the
	 * one initially selected.
	 * @param cC Component
	 * @param titleLS String
	 * @param panels SettingsContentPanel[]
	 */
	public void createAndShowSettingsDialog(Frame owner, String titleLS, SettingsContentPanel[] panels)
	{
		this.createAndShowSettingsDialog(owner, titleLS, panels, null);
	}

	/**
	 * This version of the method allows you to specify a tab that
	 * will be selected initially when the settings dialog is shown.
	 * @param cC Component
	 * @param titleLS String
	 * @param panels SettingsContentPanel[]
	 * @param initialTabLS String
	 */
	public void createAndShowSettingsDialog(Frame owner, String titleLS, SettingsContentPanel[] panels, String initialTabLS)
	{
		SettingsDialog settingsDialog = new SettingsDialog(owner, settingsCommandQueue);

		settingsDialog.setTitleLS(titleLS);

		for (int ctr = 0; ctr < panels.length; ctr++)
		{
			settingsDialog.addSettingsPanel(panels[ctr]);
		}

		if (initialTabLS != null)
		{
			settingsDialog.selectTabLS(initialTabLS);
		}

		settingsDialog.show(); // <- blocks until return
	}

	/**
	 * Retrieve the command queue that is used for
	 * settings dialogs. If applications wishes to
	 * attach their own settings dialogs to the
	 * dialog returned by the createAndShowSettingsDialog()
	 * method, the command queue they use needs to be
	 * this one, since it is this queue that is flushed
	 * when the user presses the 'apply' button.
	 */
	public CommandQueue getSettingsCommandQueue()
	{
		return settingsCommandQueue;
	}

	// CONSTRUCTOR AND RELATED

	private void initSimpleMembers()
	{
		swingDialogWrapper = new SwingDialogWrapper();

		settingsCommandQueue = new DefaultCommandQueue();
	}

	private MedViewDialogs()
	{
		initSimpleMembers();
	}

	private ProgressDialog progressDialog;

	private static MedViewDialogs instance;

	private CommandQueue settingsCommandQueue;

	private SwingDialogWrapper swingDialogWrapper;

}
