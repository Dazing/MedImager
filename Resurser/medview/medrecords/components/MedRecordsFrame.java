/*
 *
 * Created on June 13, 2001, 2:46 PM
 *
 * $Id: MedRecordsFrame.java,v 1.105 2010/07/01 17:31:04 oloft Exp $
 *
 * $Log: MedRecordsFrame.java,v $
 * Revision 1.105  2010/07/01 17:31:04  oloft
 * Added edit-buttons and made these and pda-button optional
 *
 * Revision 1.104  2010/07/01 08:10:45  oloft
 * MR 4.5, minor edits
 *
 * Revision 1.103  2010/06/28 07:12:39  oloft
 * MR 4.5 dev
 *
 * Revision 1.102  2008/08/19 11:31:33  it2aran
 * Package now also includes graph template
 *
 * Revision 1.101  2008/07/28 06:56:52  it2aran
 * * Package now includes
 * 	termdefinitions
 * 	termvalues
 * 	database
 * 	template
 * 	translator
 * and can be changed withour restarting (both in MSummary and MRecords
 * * removed more termdefinitions checks (the bug that slowed down MRecords) in MedSummary which should make it load faster
 *
 * Revision 1.100  2008/06/12 09:21:21  it2aran
 * Fixed bug:
 * -------------------------------
 * 413: Scrollar till felaktigt textfält om man sparar med felaktigt infyllt textfält.
 * 164: Tabbning mellan inputs scrollar hela formuläret så att den aktuella inputen alltid är synlig
 * Övrigt
 * ------
 * Parametrar -Xms128m -Xmx256m skickas till JVM för att tilldela mer minne så att större bilder kan hanteras
 * Mucositkomponenten helt omgjord. Utseendet passar bättre in samt att inga nollor sparas om inget är ifyllt.
 * Drag'n'drop för bilder fungerade ej och är borttaget tills vidare
 * Text i felmeddelandet vid inmatat värde utan att trycka på enter ändrat
 *
 * Revision 1.99  2008/04/24 10:07:22  it2aran
 * My CVS client is strange, so a lot of files are reported as changed in docgen, when they aren't
 *
 * Revision 1.98  2008/01/31 13:23:26  it2aran
 * Cariesdata handler that retrieves caries data from an external database
 * Some bugfixes
 *
 * Revision 1.97  2007/10/17 15:17:03  it2aran
 * Medrecords 4.3 Beta 1
 *
 * Revision 1.96  2007/04/09 13:07:29  oloft
 * Changed window title
 *
 * Revision 1.95  2007/01/04 14:31:23  oloft
 * Added scaling of full size images
 *
 * Revision 1.94  2006/11/15 16:31:00  oloft
 * Moved graph code to MedRecords.java, added GFS-fix
 *
 * Revision 1.93  2006/09/14 08:58:55  oloft
 * Fixed formatting
 *
 * Revision 1.92  2006/09/13 22:00:05  oloft
 * Added Open functionality
 *
 * Revision 1.91  2006/05/29 18:32:48  limpan
 * Added minimed code
 * ----------------------------------------------------------------------
 *
 * Revision 1.90  2006/04/24 14:17:24  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.89  2005/09/23 08:45:42  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.88  2005/07/18 13:34:07  erichson
 * Now detects and reads the new (medform) template format.
 *
 * Revision 1.87  2005/06/14 15:17:18  erichson
 * Added touchscreen data loading from medForm. //NE
 *
 * Revision 1.86  2005/06/09 14:54:48  erichson
 * Added graph functionality.
 *
 * Revision 1.85  2005/04/29 09:35:45  erichson
 * Added SAXException handling to setExaminationModel.
 *
 * Revision 1.84  2005/03/16 13:53:55  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.83  2005/02/24 16:30:48  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.82  2005/02/17 10:05:14  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.81  2005/01/30 15:19:20  lindahlf
 * T4 Integration
 *
 * Revision 1.80  2004/12/20 13:14:55  erichson
 * Added mucos to completeTreeFromInputs
 *
 * Revision 1.79  2004/12/08 14:42:51  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.78  2004/11/24 15:17:32  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.77  2004/11/19 12:32:20  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.76  2004/11/16 21:24:04  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.75  2004/11/11 22:36:20  lindahlf
 * MedServer Test Pack 2
 *
 * Revision 1.74  2004/11/04 12:04:45  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.73  2004/08/24 18:25:13  lindahlf
 * no message
 *
 * Revision 1.72  2004/08/24 17:03:19  lindahlf
 * no message
 *
 * Revision 1.71  2004/06/01 10:36:12  lindahlf
 * no message
 *
 * Revision 1.70  2004/05/18 18:21:05  lindahlf
 * Åtgärdade fel med bild-filnamn skapade utan datum
 *
 * Revision 1.69  2004/04/01 00:36:28  lindahlf
 * no message
 *
 * Revision 1.68  2004/03/10 17:04:02  lindahlf
 * Tweak av PID-map
 *
 * Revision 1.67  2004/03/08 23:58:28  lindahlf
 * no message
 *
 * Revision 1.66  2004/02/28 17:51:26  lindahlf
 * no message
 *
 * Revision 1.65  2004/02/26 12:14:05  lindahlf
 * Added Cache support to MVDHandler, and proper patient id support when invoking MR from MS
 *
 * Revision 1.64  2004/02/20 12:12:40  lindahlf
 * Valbart visa översättare vid inmatning av nytt värde
 *
 * Revision 1.63  2004/02/19 18:21:28  lindahlf
 * Major update patch 1
 *
 * Revision 1.62  2004/01/27 15:43:27  oloft
 * PID introduction
 *
 * Revision 1.61  2003/12/29 00:38:45  oloft
 * mostly layout
 *
 * Revision 1.60  2003/12/22 14:39:52  oloft
 * Skip set location at same
 *
 * Revision 1.59  2003/12/21 21:54:12  oloft
 * Changed settings, removed DataHandlingHandler
 *
 * Revision 1.58  2003/11/11 13:44:36  oloft
 * Switching mainbranch
 *
 * Revision 1.57.2.31  2003/11/11 13:00:13  oloft
 * *** empty log message ***
 *
 * Revision 1.57.2.30  2003/11/07 15:08:08  oloft
 * Version number
 *
 * Revision 1.57.2.29  2003/10/26 21:24:52  oloft
 * Menu changes. Do not attempt to use Forte GUI editor
 *
 * Revision 1.57.2.28  2003/10/26 15:27:23  oloft
 * Minoe edits
 *
 * Revision 1.57.2.27  2003/10/25 15:28:53  oloft
 * Removed forgotten comment
 *
 * Revision 1.57.2.26  2003/10/24 21:06:50  oloft
 * Debug edits
 *
 * Revision 1.57.2.25  2003/10/23 11:35:41  oloft
 * Enabled multi-document mode
 *
 * Revision 1.57.2.24  2003/10/22 20:53:23  oloft
 * 4.0b15a
 *
 * Revision 1.57.2.23  2003/10/21 23:06:33  oloft
 * Added newDocument method
 *
 * Revision 1.57.2.22  2003/10/21 06:25:50  oloft
 * *** empty log message ***
 *
 * Revision 1.57.2.21  2003/10/20 14:43:01  oloft
 * File transfer
 *
 * Revision 1.57.2.20  2003/10/19 20:28:35  oloft
 * Removed pack() in newExamination which caused strange resizing
 *
 * Revision 1.57.2.19  2003/10/18 14:50:45  oloft
 * Builds tree file with new file names
 *
 * Revision 1.57.2.18  2003/10/17 10:56:23  oloft
 * Minor refactoring
 *
 * Revision 1.57.2.17  2003/10/16 21:13:47  oloft
 * File transfer
 *
 * Revision 1.57.2.16  2003/10/13 09:00:04  oloft
 * Fixes since TranslatorHandler is now Singleton
 *
 * Revision 1.57.2.15  2003/10/08 18:32:10  oloft
 * misc fixes
 *
 * Revision 1.57.2.14  2003/10/06 09:16:24  oloft
 * interim
 *
 * Revision 1.57.2.13  2003/10/03 15:56:44  oloft
 * Only formatting
 *
 * Revision 1.57.2.12  2003/10/03 11:56:59  oloft
 * *** empty log message ***
 *
 * Revision 1.57.2.11  2003/10/02 21:09:24  oloft
 * Changed error message when values are missing
 *
 * Revision 1.57.2.10  2003/10/01 07:45:34  oloft
 * Added instance variable mExaminationDate to enable re-saving using the same date
 *
 * Revision 1.57.2.9  2003/09/30 11:54:46  oloft
 * Fixed import from touch screen
 *
 * Revision 1.57.2.8  2003/09/09 18:24:08  erichson
 * Fixed bug where images wouldn't get a .jpg extension
 *
 * Revision 1.57.2.7  2003/09/09 17:31:35  erichson
 * Reworking of saving, now saves images. Better error handling for saveDialog etc.
 *
 * Revision 1.57.2.6  2003/09/09 14:01:27  erichson
 * Updated id code handling (bug 181) and added InvalidExaminationModelException handling
 *
 * Revision 1.57.2.5  2003/09/09 10:27:30  erichson
 * Added "keepTextField" checkbox and showGraph button
 *
 * Revision 1.57.2.4  2003/09/08 16:37:25  erichson
 * Changed version to beta 10, added "save succesful" dialog, changed compileTree
 *
 * Revision 1.57.2.3  2003/09/08 13:23:13  erichson
 * Changed version to beta 9
 *
 * Revision 1.57.2.2  2003/09/03 13:28:44  erichson
 * Lagt till showTranslationEditor
 *
 * Revision 1.57.2.1  2003/08/16 15:01:06  erichson
 * Major refactoring, bugfixes and comments
 *
 * Revision 1.57  2003/08/04 11:17:32  erichson
 * Compensating for removing showTranslator from TranslatorHandler constructor
 *
 * Revision 1.56  2003/08/02 00:21:59  erichson
 * Expanded info in a language error message dialog
 *
 * Revision 1.55  2003/07/23 13:46:57  erichson
 * Updated to beta 8, resolved focus issuses (changing tabs and newExamination - bugzilla bugs 26 and  29)
 *
 * Revision 1.54  2003/07/23 00:34:10  erichson
 * changed method valueChanged() to setValueChanged() to avoid confusion with ChangeListener
 * Also fixed bug 32 (valuechanged would not reset when doing new examination)
 *
 * Revision 1.53  2003/07/22 16:48:43  erichson
 * Message dialog now shows reason why the examination cannot be saved.
 * Working version updated to beta 7.
 *
 * Revision 1.52  2003/07/22 14:21:12  erichson
 * Updating version to 4.0 Beta 6 and tagging this before committing my changes // Nils
 *
 */

package medview.medrecords.components;

import java.awt.*;
import java.awt.event.*;

import java.beans.*;

import java.io.*;

import java.util.*;
import java.sql.SQLException;

import javax.swing.*;

import medview.common.actions.*;
import medview.common.components.menu.*;
import medview.common.components.toolbar.*;
import medview.common.data.*;
import medview.common.dialogs.*;
import medview.common.generator.*;

import medview.datahandling.*;
import medview.datahandling.examination.*;
import medview.datahandling.examination.tree.*;
import medview.datahandling.images.*;

import medview.medrecords.*;
import medview.medrecords.components.inputs.*;
import medview.medrecords.data.*;
import medview.medrecords.events.*;
import medview.medrecords.exceptions.*;
import medview.medrecords.models.*;
import medview.medrecords.tools.*;

import misc.gui.*;
import misc.gui.constants.*;

/**
 */
public class MedRecordsFrame extends ApplicationFrame implements
	GUIConstants, MedViewLanguageConstants, MedViewMediaConstants {
		// convenience variables
		
		private static final MedViewDataHandler mVDH = MedViewDataHandler.instance();
		
		private static final MedViewDialogs mVD = MedViewDialogs.instance();
		
		// vector for keeping mappings of examination models
		
		private static Vector mappings = new Vector();
		
		// actions
		
		private InvokeFASSAction openFASSAction;
		private Action cutAction;
		private Action copyAction;
		private Action pasteAction;
		private Action selectAllAction;
		
		// preference listener
		
		private MedViewPreferenceListener dataPreferenceListener;
		
		// toolbar buttons
		
		private javax.swing.JButton newToolbarButton;
		
		private javax.swing.JButton saveToolbarButton;
		
		private javax.swing.JButton saveAndCloseToolbarButton;

                private javax.swing.JButton cutToolbarButton;

                private javax.swing.JButton copyToolbarButton;

                private javax.swing.JButton pasteToolbarButton;
		
		private javax.swing.JButton touchScreenToolbarButton;
		
		private javax.swing.JButton showGraphPreviewToolbarButton;
		
		private javax.swing.JButton previewToolbarButton;
		
		private javax.swing.JButton openFASSToolbarButton;
		
		private javax.swing.JButton pdaToolbarButton;
		
		// labels
		
		private javax.swing.JLabel packageDescLabel;
		
		// combo boxes
		
		private javax.swing.JComboBox packageCombo;
		
		// data components package(s)
		
		private DataComponentPackage currentPackage = null;
		
		private DataComponentPackage[] allIncludedPackages = null;
		
		// boolean flag for programmatic combo update
		
		private boolean updatingPackageCombo = false;
		
		// menus
		
		private javax.swing.JMenu fileMenu;
		
		private javax.swing.JMenu importMenu; // Submenu
		
		private javax.swing.JMenu editMenu;
		
		private javax.swing.JMenu viewMenu;
		
		private javax.swing.JMenu helpMenu;
		
		private javax.swing.JMenu lookAndFeelSubMenu;
		
		// edit menu items
		
		private javax.swing.JMenuItem newExaminationMenuItem;
		
		private javax.swing.JMenuItem openExaminationMenuItem;
		
		private javax.swing.JMenuItem importTouchScreenMenuItem;
		
		private javax.swing.JMenuItem closeMenuItem;
		
		private javax.swing.JMenuItem saveMenuItem;
		
		private javax.swing.JMenuItem saveAndCloseMenuItem;
		
		private javax.swing.JMenuItem importPDAMenuItem;
		
		private javax.swing.JMenuItem optionsMenuItem;
		
		private javax.swing.JMenuItem exitMenuItem;
		
		// edit menu items
		private javax.swing.JMenuItem cutMenuItem;
		private javax.swing.JMenuItem copyMenuItem;
		private javax.swing.JMenuItem pasteMenuItem;
		private javax.swing.JMenuItem selectAllMenuItem;
		
		// view menu items
		
		private javax.swing.JMenuItem previewMenuItem;
		
		private javax.swing.JMenuItem graphPreviewMenuItem;
		
		private javax.swing.JMenuItem openFASSMenuItem;
		
		// help menu items
		
		private javax.swing.JMenuItem aboutMenuItem;
        
		// PDA Dialog
		
		private PDADialog pdaDialog;
		
		// main panels
		
		private PhotoPanel photoPanel;
		
		private ValueTabbedPane valueTabbedPane;
		
		private PresetListPanel presetListPanel;
		
		// underlying model
		
		private ExaminationModel examinationModel = null;
		
		// translator handler (for showing translation edits / responsible for previewing)
		
		private TranslatorHandler translatorHandler = TranslatorHandler.instance();
		
		// document number counter (class)
		
		private static int docNum = 0;
		
		// frame title prefix
		
		private final String TITLE_PREFIX = mVDH.getLanguageString(TITLE_NEW_EXAMINATION_LS_PROPERTY);
		
		// examination date
		
		private Date mExaminationDate; // the date this examination was initiated - OT
		
		// variable checking if the examination has changed
		
		private boolean hasChanged = false;
		
		// This is set to the filename of the partially finished exam imported from the PDA
		// when it is imported, else it is null
		
		private ExaminationIdentifier pdaImportExamination = null;
		
		// initial patientidentifier (if constructed with one - otherwise null)
		
		private PatientIdentifier initialPID = null;
		
		
		// CONSTRUCTOR
		
		public MedRecordsFrame(ExaminationModel defaultExaminationModel) {
			this(defaultExaminationModel, null);
		}
		
		public MedRecordsFrame(ExaminationModel defaultExaminationModel, PatientIdentifier pid) {
			/* NOTE: the examination model passed to the constructor will
			 only be used if there are no included packages currently set
			 for the application. */
			
			super();
			
			// initial patient identifier is stored away
			
			this.initialPID = pid;
			
			// frame size
			
			String set = mVDH.getUserStringPreference(PreferencesModel.MEDRECORDS_SIZE_PROPERTY,
													  
													  "1024x768", PreferencesModel.class);
			
			StringTokenizer t = new StringTokenizer(set, "x");
			
			try {
				int width = Integer.parseInt(t.nextToken());
				
				int height = Integer.parseInt(t.nextToken());
				
				this.setSize(new Dimension(width, height));
			} catch (Exception e) {
				e.printStackTrace();	// should never happen
			}
			
			// setup frame icon
			
			setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			
			setIconImage(mVDH.getImage(MedViewMediaConstants.FRAME_IMAGE_ICON));
			
			// title
			
			docNum++; // static counter keeping track on document numbers
			
			setValueChanged(false);
			
			setTitle(getNewDocumentTitle());
			
			// setup actions
			
			initActions();
			
			// create and add the menus
			
			initMenus();
			
			// create components and lay them out
			
			constructGUI(defaultExaminationModel);
			
			// set initial state
			
			setExaminationDate(new Date());
			
			selectFirstInput();
			
			if (pid != null) {
				setIdentification(pid);
			}
			
			// listen for preference changes
			
			dataPreferenceListener = new PreferenceListener();
			
			mVDH.addMedViewPreferenceListener(dataPreferenceListener);
			
			// PDA Dialog
			
			pdaDialog = new PDADialog(MedRecordsFrame.this, new PDADialog.PDAEventListener() {
									  public void pdaImportEvent(EventObject e) {
									  valueTabbedPane.clearAllInputs();
									  PDADialog p = (PDADialog)(e.getSource());
									  ExaminationIdentifier exam = (ExaminationIdentifier)p.getValue();
									  readPDADataFromDB(exam);
									  }
									  }
									  );
		}
				
		private void initActions() {
			
			openFASSAction = new InvokeFASSAction();
			
			openFASSAction.setURL(PreferencesModel.instance().getFASSURL());
			
			TextActionProvider textActionProvider = TextActionProvider.getInstance();
			
			// cutAction = getActionByName(DefaultEditorKit.cutAction);
			cutAction = textActionProvider.getCutAction();
			copyAction = textActionProvider.getCopyAction();
			pasteAction = textActionProvider.getPasteAction();
			selectAllAction = textActionProvider.getSelectAllAction();
						
		}
		
		private void initMenus() {
			// file menu
			
			fileMenu = new javax.swing.JMenu();
			
			fileMenu.setText(mVDH.getLanguageString(MENU_ARCHIVE_LS_PROPERTY));
			
			fileMenu.setMnemonic(mVDH.getLanguageString(MNEMONIC_MENU_ARCHIVE_LS_PROPERTY).charAt(0));
			
			// file menu - new examination
			
			newExaminationMenuItem = new javax.swing.JMenuItem(); // new examination (menu)
			
			newExaminationMenuItem.setAccelerator(NEW_PRIMARY_KEYSTROKE);
			
			newExaminationMenuItem.setText(mVDH.getLanguageString(MENU_ITEM_FILE_NEW_EXAMINATION_LS_PROPERTY));
			
			newExaminationMenuItem.setMnemonic(mVDH.getLanguageString(MNEMONIC_MENU_ITEM_NEW_EXAMINATION_LS_PROPERTY).charAt(0));
			
			newExaminationMenuItem.addActionListener(new java.awt.event.ActionListener() {
													 public void actionPerformed(java.awt.event.ActionEvent evt) {
													 newExamination();
													 }
													 });
			
			fileMenu.add(newExaminationMenuItem); // new examination
			
			// file menu - open examination
			
			openExaminationMenuItem = new javax.swing.JMenuItem(); // open examination
			
			openExaminationMenuItem.setAccelerator(LOAD_PRIMARY_KEYSTROKE);
			
			openExaminationMenuItem.setText((mVDH.getLanguageString(MENU_ITEM_FILE_OPEN_LS_PROPERTY)));
			
			openExaminationMenuItem.setMnemonic((mVDH.getLanguageString(MNEMONIC_MENU_ITEM_OPEN_LS_PROPERTY)).charAt(0));
			
			openExaminationMenuItem.setToolTipText("Open examination");
			
			openExaminationMenuItem.addActionListener(new java.awt.event.ActionListener() {
													  public void actionPerformed(java.awt.event.ActionEvent evt) {
													  openExamination();
													  }
													  });
			
			fileMenu.add(openExaminationMenuItem); // open examination
			
			// file menu - import examination data
			importMenu = new javax.swing.JMenu();
			
			importMenu.setText(mVDH.getLanguageString(MENU_ITEM_FILE_IMPORT_LS_PROPERTY));
			importMenu.setMnemonic(mVDH.getLanguageString(MNEMONIC_MENU_ITEM_IMPORT_LS_PROPERTY).charAt(0));
			
			fileMenu.add(importMenu);
			
			// file menu - import examination
			importTouchScreenMenuItem = new javax.swing.JMenuItem(); // open examination
			
			importTouchScreenMenuItem.setAccelerator(getAcceleratorKeyStroke(KeyEvent.VK_I));
			
			importTouchScreenMenuItem.setText(mVDH.getLanguageString(MENU_ITEM_FILE_IMPORT_DATA_LS_PROPERTY));
			
			importTouchScreenMenuItem.setMnemonic(mVDH.getLanguageString(MNEMONIC_MENU_ITEM_IMPORT_DATA_LS_PROPERTY).charAt(0));
			
			importTouchScreenMenuItem.setToolTipText(mVDH.getLanguageString(TOOLTIP_IMPORT_DATA_LS_PROPERTY));
			
			importTouchScreenMenuItem.addActionListener(new java.awt.event.ActionListener() {
														public void actionPerformed(java.awt.event.ActionEvent evt) {
														importTouchScreenMenuItemActionPerformed(evt);
														}
														});
			
			importMenu.add(importTouchScreenMenuItem); // import examination
			
			// file menu - PDA
			importPDAMenuItem = new javax.swing.JMenuItem();
			
			importTouchScreenMenuItem.setAccelerator(getAcceleratorKeyStroke(KeyEvent.VK_D));
			
			importPDAMenuItem.setText(mVDH.getLanguageString(MENU_ITEM_IMPORT_PDA_LS_PROPERTY));
			
			importPDAMenuItem.setMnemonic(mVDH.getLanguageString(MNEMONIC_MENU_ITEM_IMPORT_PDA_LS_PROPERTY).charAt(0));
			
			importPDAMenuItem.addActionListener(new java.awt.event.ActionListener() {
												public void actionPerformed(java.awt.event.ActionEvent evt) {
												pdaDialog.setVisible(true);
												}
												});
			
			importMenu.add(importPDAMenuItem);
			
			// file menu - close
			
			closeMenuItem = new javax.swing.JMenuItem(); // close examination
			
			closeMenuItem.setAccelerator(CLOSE_KEYSTROKE);
			
			closeMenuItem.setText(mVDH.getLanguageString(MENU_ITEM_FILE_CLOSE_EXAMINATION_LS_PROPERTY));
			
			closeMenuItem.setMnemonic(mVDH.getLanguageString(MNEMONIC_MENU_ITEM_CLOSE_EXAMINATION_LS_PROPERTY).charAt(0));
			
			closeMenuItem.addActionListener(new java.awt.event.ActionListener() {
											public void actionPerformed(java.awt.event.ActionEvent evt) {
											close(); // will ask if save needed
											}
											});
			
			fileMenu.add(closeMenuItem); // close
			
			// file menu - separator
			
			fileMenu.addSeparator();
			
			// file menu - save
			
			saveMenuItem = new javax.swing.JMenuItem(); // save examination (menu)
			
			saveMenuItem.setAccelerator(SAVE_PRIMARY_KEYSTROKE);
			
			saveMenuItem.setText(mVDH.getLanguageString(MENU_ITEM_FILE_SAVE_LS_PROPERTY));
			
			saveMenuItem.setMnemonic(mVDH.getLanguageString(MNEMONIC_MENU_ITEM_SAVE_LS_PROPERTY).charAt(0));
			
			saveMenuItem.addActionListener(new java.awt.event.ActionListener() {
										   public void actionPerformed(java.awt.event.ActionEvent evt) {
										   save();
										   }
										   });
			
			fileMenu.add(saveMenuItem);
			
			// file menu - save and close

			saveAndCloseMenuItem = new javax.swing.JMenuItem(); //
			
			saveAndCloseMenuItem.setAccelerator(SAVE_SECONDARY_KEYSTROKE);
			
			saveAndCloseMenuItem.setText(mVDH.getLanguageString(MENU_ITEM_FILE_SAVE_AND_CLOSE_LS_PROPERTY));
			
			saveAndCloseMenuItem.setMnemonic(mVDH.getLanguageString(MNEMONIC_MENU_ITEM_SAVE_AND_CLOSE_LS_PROPERTY).charAt(0));
			
			saveAndCloseMenuItem.addActionListener(new java.awt.event.ActionListener() {
										   public void actionPerformed(java.awt.event.ActionEvent evt) {
										   saveAndClose();
										   }
										   });
			
			fileMenu.add(saveAndCloseMenuItem);
			
			// file menu - separator
			
			fileMenu.addSeparator();
			
			// file menu - options
			
			optionsMenuItem = new javax.swing.JMenuItem(); // show preferences
			
			optionsMenuItem.setText(mVDH.getLanguageString(MENU_ITEM_FILE_PREFERENCES_LS_PROPERTY));
			
			optionsMenuItem.setMnemonic(mVDH.getLanguageString(MNEMONIC_MENU_ITEM_PREFERENCES_LS_PROPERTY).charAt(0));
			
			optionsMenuItem.addActionListener(new java.awt.event.ActionListener() {
											  public void actionPerformed(java.awt.event.ActionEvent evt) {
											  MedRecords.showPreferences(MedRecordsFrame.this, null);
											  }
											  });
			
			fileMenu.add(optionsMenuItem);
			
			// file menu - separator
			
			fileMenu.addSeparator();
			
			// file menu - exit
			
			exitMenuItem = new javax.swing.JMenuItem(); // exit application
			
			exitMenuItem.setText(mVDH.getLanguageString(MENU_ITEM_FILE_EXIT_LS_PROPERTY));
			
			exitMenuItem.setMnemonic(mVDH.getLanguageString(MNEMONIC_MENU_ITEM_EXIT_LS_PROPERTY).charAt(0));
			
			exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
										   public void actionPerformed(java.awt.event.ActionEvent evt) {
										   tryExit();
										   }
										   });
			
			fileMenu.add(exitMenuItem);
			
			// edit menu;
			editMenu = new javax.swing.JMenu(); // view menu
			
			editMenu.setText(mVDH.getLanguageString(MENU_EDIT_LS_PROPERTY));
			
			editMenu.setMnemonic(mVDH.getLanguageString(MNEMONIC_MENU_EDIT_LS_PROPERTY).charAt(0));
			
			// edit menu - cut
			
			cutMenuItem = new javax.swing.JMenuItem(cutAction);
			
			cutMenuItem.setMnemonic(mVDH.getLanguageString(MNEMONIC_MENU_ITEM_CUT_LS_PROPERTY).charAt(0));
			
			editMenu.add(cutMenuItem);		
			
			// edit menu - copy
			
			copyMenuItem = new javax.swing.JMenuItem(copyAction);
			
			copyMenuItem.setMnemonic(mVDH.getLanguageString(MNEMONIC_MENU_ITEM_COPY_LS_PROPERTY).charAt(0));
			
			editMenu.add(copyMenuItem);		
			
			// edit menu - paste
			
			pasteMenuItem = new javax.swing.JMenuItem(pasteAction);
			
			pasteMenuItem.setMnemonic(mVDH.getLanguageString(MNEMONIC_MENU_ITEM_PASTE_LS_PROPERTY).charAt(0));
			
			editMenu.add(pasteMenuItem);		
			
			// edit menu - separator
			
			editMenu.addSeparator();
			
			// edit menu - select all
			
			selectAllMenuItem = new javax.swing.JMenuItem(selectAllAction);
			
			selectAllMenuItem.setMnemonic(mVDH.getLanguageString(MNEMONIC_MENU_ITEM_SELECT_ALL_LS_PROPERTY).charAt(0));
			
			editMenu.add(selectAllMenuItem);		
			
			// view menu
			
			viewMenu = new javax.swing.JMenu(); // view menu
			
			viewMenu.setText(mVDH.getLanguageString(MENU_VIEW_LS_PROPERTY));
			
			viewMenu.setMnemonic(mVDH.getLanguageString(MNEMONIC_MENU_VIEW_LS_PROPERTY).charAt(0));
			
			// view menu - open FASS
			
			openFASSMenuItem = new javax.swing.JMenuItem(openFASSAction);
			
			openFASSMenuItem.setMnemonic(mVDH.getLanguageString(MNEMONIC_MENU_ITEM_OPEN_FASS_LS_PROPERTY).charAt(0));
			
			openFASSMenuItem.setIcon(null);
			
			viewMenu.add(openFASSMenuItem);
			
			// view menu - add separator
			
			viewMenu.addSeparator();
			
			// view menu - preview
			
			previewMenuItem = new javax.swing.JMenuItem(); // preview (menu)
			
			previewMenuItem.setAccelerator(getAcceleratorKeyStroke(KeyEvent.VK_R));
			
			previewMenuItem.setText(mVDH.getLanguageString(MENU_ITEM_FILE_PREVIEW_LS_PROPERTY));
			
			previewMenuItem.setMnemonic(mVDH.getLanguageString(MNEMONIC_MENU_ITEM_PREVIEW_LS_PROPERTY).charAt(0));
			
			previewMenuItem.addActionListener(new java.awt.event.ActionListener() {
											  public void actionPerformed(java.awt.event.ActionEvent evt) {
											  tryShowPreview();
											  }
											  });
			
			viewMenu.add(previewMenuItem);
			
			// view menu - graph preview
			
			graphPreviewMenuItem = new javax.swing.JMenuItem(); // graph preview
			
			graphPreviewMenuItem.setAccelerator(getAcceleratorKeyStroke(KeyEvent.VK_G));
			
			graphPreviewMenuItem.setText(mVDH.getLanguageString(MENU_ITEM_GRAPH_PREVIEW_LS_PROPERTY));
			
			graphPreviewMenuItem.setMnemonic(mVDH.getLanguageString(MNEMONIC_MENU_ITEM_GRAPH_PREVIEW_LS_PROPERTY).charAt(0));
			
			graphPreviewMenuItem.addActionListener(new java.awt.event.ActionListener() {
												   public void actionPerformed(java.awt.event.ActionEvent evt) {
												   tryGraphPreview();
												   }
												   });
			
			viewMenu.add(graphPreviewMenuItem);
			
			// view menu - add separator
			
			viewMenu.addSeparator();
			
			// choose look-and-feel
			
			lookAndFeelSubMenu = new MedViewMenu(MENU_LOOK_AND_FEEL_LS_PROPERTY, MNEMONIC_MENU_LOOK_AND_FEEL_LS_PROPERTY);
			
			final UIManager.LookAndFeelInfo[] lAFs = UIManager.getInstalledLookAndFeels();
			
			ButtonGroup buttonGroup = new ButtonGroup();
			
			for (int ctr = 0; ctr < lAFs.length; ctr++) {
				JRadioButtonMenuItem lAFMenuItem = new JRadioButtonMenuItem(lAFs[ctr].getName(),
																	  
																	  UIManager.getLookAndFeel().getClass().getName().equals(lAFs[ctr].getClassName()));
				
				final String className = lAFs[ctr].getClassName();
				
				lAFMenuItem.addItemListener(new ItemListener() {
											public void itemStateChanged(ItemEvent e) {
											if (e.getStateChange() == ItemEvent.SELECTED) {
											try {
											UIManager.setLookAndFeel(className);
											
											mVDH.setUserStringPreference(PreferencesModel.LookAndFeel,
																		 
																		 className, PreferencesModel.class);
											} catch (Exception exc) {
											exc.printStackTrace();
											}
											}
											}
											});
				
				buttonGroup.add(lAFMenuItem);
				
				lookAndFeelSubMenu.add(lAFMenuItem);
			}
			
			viewMenu.add(lookAndFeelSubMenu);
			
			// help menu
			
			helpMenu = new javax.swing.JMenu(); // help menu
			
			helpMenu.setText(mVDH.getLanguageString(MENU_HELP_LS_PROPERTY));
			
			helpMenu.setMnemonic(mVDH.getLanguageString(MNEMONIC_MENU_HELP_LS_PROPERTY).charAt(0));
			
			// help menu - about dialog
			
			aboutMenuItem = new javax.swing.JMenuItem(); // show about dialog
			
			aboutMenuItem.setText(mVDH.getLanguageString(MENU_ITEM_HELP_ABOUT_MEDRECORDS_LS_PROPERTY));
			
			aboutMenuItem.setMnemonic(mVDH.getLanguageString(MNEMONIC_MENU_ITEM_ABOUT_LS_PROPERTY).charAt(0));
			
			aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
											public void actionPerformed(java.awt.event.ActionEvent evt) {
											MedRecords.showAboutWin();
											}
											});
			
			helpMenu.add(aboutMenuItem);
			
			// add the menus to the menu bar
			
			addToMenuBar(fileMenu);
			
			addToMenuBar(editMenu);
			
			addToMenuBar(viewMenu);
			
			addToMenuBar(helpMenu);
		}
		
		private void constructGUI(ExaminationModel defaultExaminationModel) {
			// create panels
			
			photoPanel = new PhotoPanel();
			
			photoPanel.setApplicationFrame(this);
			
			valueTabbedPane = new ValueTabbedPane();
			
			valueTabbedPane.setApplicationFrame(this);
			
			// setup data component packages
			
			synchronizePackageDataWithPrefs(); // this will set the examination model according to packages
			
			if (getCurrentPackage() == null) {
				examinationModel = defaultExaminationModel;
				
				translatorHandler.clearTemplateLocation();
				
				translatorHandler.clearTranslatorLocation();
				
				// if we get here, there are no packages currently included - set the default model
				
				try {
					valueTabbedPane.setModel(examinationModel);
				} catch (InvalidExaminationModelException ieme) {
					String m1 = "Error: Invalid examination model: " + ieme.getMessage();
					
					String m2 = "InvalidExaminationModelException";
					
					JOptionPane.showMessageDialog(this, m1, m2, JOptionPane.ERROR_MESSAGE);
				}
			}
			
			// setup preset list panel
			
			presetListPanel = new PresetListPanel();
			
			valueTabbedPane.setPresetListPanel(presetListPanel);
			
			presetListPanel.addActionListener(valueTabbedPane);
			
			// create upper panel (above photo panel in main)
			
			JSplitPane upperPanel = new JSplitPane();
			
			upperPanel.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
			
			upperPanel.setOneTouchExpandable(false);
			
			upperPanel.setResizeWeight(1);
			
			upperPanel.setContinuousLayout(false);
			
			upperPanel.setAutoscrolls(false);
			
			upperPanel.setLeftComponent(valueTabbedPane);
			
			upperPanel.setRightComponent(presetListPanel);
			
			upperPanel.setDividerSize(4);
			
			valueTabbedPane.setSplitPresets(upperPanel);
			
			// create the content panel
			
			JSplitPane contentPanel = new JSplitPane();
			
			contentPanel.setOrientation(JSplitPane.VERTICAL_SPLIT);
			
			contentPanel.setLeftComponent(upperPanel);
			
			if (photoPanel != null) {
				contentPanel.setRightComponent(photoPanel); // Photo panel is under
			} else {
				contentPanel.setRightComponent(new JPanel());
			}
			
			contentPanel.setDividerSize(4);
			
			contentPanel.setOneTouchExpandable(false);
			
			contentPanel.setResizeWeight(1);
			
			contentPanel.setContinuousLayout(true);
			
			valueTabbedPane.setSplitPhoto(contentPanel);
			
			setCenterComponent(contentPanel);
			
			// repaint and select first tab
			
			this.repaint();
			
			valueTabbedPane.gotoFirstTab();
			valueTabbedPane.tabChanged();
		}
		
		
		// AFTER SHOW (MAINSHELL OVERRIDE)
		
                @Override
		protected void afterShow() {
			super.afterShow();

			if (mVDH.getUserBooleanPreference(PreferencesModel.MEDRECORDS_MAXIMIZED_PROPERTY,
											  
											  false, PreferencesModel.class)) {
				this.setExtendedState(Frame.MAXIMIZED_BOTH);	// maximize
			}
			
			this.addComponentListener(new ComponentAdapter() {
									  public void componentResized(ComponentEvent e) {
									  boolean isMaximized = (getExtendedState() == Frame.MAXIMIZED_BOTH);
									  
									  mVDH.setUserBooleanPreference(PreferencesModel.MEDRECORDS_MAXIMIZED_PROPERTY,
																	
																	isMaximized, PreferencesModel.class);
									  
									  if (!isMaximized) {
									  Dimension currSize = MedRecordsFrame.this.getSize();
									  
									  int height = currSize.height;
									  
									  int width = currSize.width;
									  
									  mVDH.setUserStringPreference(PreferencesModel.MEDRECORDS_SIZE_PROPERTY,
																   
																   width + "x" + height, PreferencesModel.class);
									  }
									  valueTabbedPane.tabChanged();
									  }
									  
									  public void componentMoved(ComponentEvent e) {
									  Point location = MedRecordsFrame.this.getLocation();
									  
									  int x = location.x;
									  
									  int y = location.y;
									  
									  mVDH.setUserStringPreference(PreferencesModel.MEDRECORDS_LOCATION_PROPERTY,
																   
																   x + "," + y, PreferencesModel.class);
									  }

									  });
			
			valueTabbedPane.requestFocus();

		}
		
		
		// FRAME DIMENSIONS
		
                @Override
		protected int getMSWidth() {
			return 1024;
		}
		
                @Override
		protected int getMSHeight() {
			return 768;
		}
		
		
		// STATUS BAR
                @Override
		protected boolean usesStatusBar() {
			return false; // override mainshell default
		}
		
		
		// TOOLBARS
                @Override
		protected JToolBar[] getToolBars() {
			// create toolbar
			
			JToolBar toolBar = new javax.swing.JToolBar();
			
			toolBar.setRollover(true);
			
			// create and add 'new examination' button
			
			newToolbarButton = new javax.swing.JButton(); // new examination (toolbar)
			
			newToolbarButton.setIcon(mVDH.getImageIcon(NEW_IMAGE_ICON_24));
			
			newToolbarButton.setToolTipText(mVDH.getLanguageString(TOOLTIP_NEW_EXAMINATION_LS_PROPERTY));
			
			newToolbarButton.addActionListener(new java.awt.event.ActionListener() {
											   public void actionPerformed(java.awt.event.ActionEvent evt) {
											   newExamination();
											   }
											   });
			
			toolBar.add(newToolbarButton);
			
			// create and add 'save examination' button
			
			saveToolbarButton = new javax.swing.JButton(); // save examination (toolbar)
			
			saveToolbarButton.setIcon(mVDH.getImageIcon(SAVE_IMAGE_ICON_24));
			
			saveToolbarButton.setToolTipText(mVDH.getLanguageString(TOOLTIP_SAVE_EXAMINATION_LS_PROPERTY));
			
			saveToolbarButton.addActionListener(new java.awt.event.ActionListener() {
												public void actionPerformed(java.awt.event.ActionEvent evt) {
												save();
												}
												});
			
			toolBar.add(saveToolbarButton);
			
			// create and add 'save and close examination' button
			
			saveAndCloseToolbarButton = new javax.swing.JButton(); // save examination (toolbar)
			
			saveAndCloseToolbarButton.setIcon(mVDH.getImageIcon(SAVE_AND_CLOSE_IMAGE_ICON_24));
			
			saveAndCloseToolbarButton.setToolTipText(mVDH.getLanguageString(TOOLTIP_SAVE_AND_CLOSE_EXAMINATION_LS_PROPERTY));
			
			saveAndCloseToolbarButton.addActionListener(new java.awt.event.ActionListener() {
												public void actionPerformed(java.awt.event.ActionEvent evt) {
												saveAndClose();
												}
												});
			
			toolBar.add(saveAndCloseToolbarButton);
			
			// create and add 'import' button
			
			touchScreenToolbarButton = new javax.swing.JButton(); // import touch screen
			
			touchScreenToolbarButton.setIcon(mVDH.getImageIcon(IMPORT_IMAGE_ICON_24));
			
			touchScreenToolbarButton.setToolTipText(mVDH.getLanguageString(TOOLTIP_IMPORT_DATA_LS_PROPERTY));
			
			touchScreenToolbarButton.addActionListener(new java.awt.event.ActionListener() {
													   public void actionPerformed(java.awt.event.ActionEvent evt) {
													   importTouchScreenMenuItemActionPerformed(null);
													   }
													   });
			
			toolBar.add(touchScreenToolbarButton);
			
			// add separator
			
			toolBar.addSeparator();

                        // Check if edit-buttons should be displayed
                    if (PreferencesModel.instance().getShowEditButtons()) {
                        // create and add 'cut' button

                        cutToolbarButton = new javax.swing.JButton(cutAction); // open FASS in browser

                        cutToolbarButton.setText(""); // don't show text (from action)

                        cutToolbarButton.setIcon(mVDH.getImageIcon(CUT_IMAGE_ICON_24));

                        toolBar.add(cutToolbarButton);

                        // create and add 'copy' button

                        copyToolbarButton = new javax.swing.JButton(copyAction); // open FASS in browser

                        copyToolbarButton.setText(""); // don't show text (from action)

                        copyToolbarButton.setIcon(mVDH.getImageIcon(COPY_IMAGE_ICON_24));

                        toolBar.add(copyToolbarButton);

                        // create and add 'paste' button

                        pasteToolbarButton = new javax.swing.JButton(pasteAction); // open FASS in browser

                        pasteToolbarButton.setText(""); // don't show text (from action)

                        pasteToolbarButton.setIcon(mVDH.getImageIcon(PASTE_IMAGE_ICON_24));

                        toolBar.add(pasteToolbarButton);

                        // add separator

                        toolBar.addSeparator();
                    }
			// create and add 'graph preview' button
			
			showGraphPreviewToolbarButton = new JButton();
			
			showGraphPreviewToolbarButton.setIcon(mVDH.getImageIcon(SHOW_GRAPH_IMAGE_ICON_24));
			
			showGraphPreviewToolbarButton.setToolTipText(mVDH.getLanguageString(TOOLTIP_SHOW_GRAPH_LS_PROPERTY));
			
			showGraphPreviewToolbarButton.addActionListener(new java.awt.event.ActionListener() {
															public void actionPerformed(java.awt.event.ActionEvent evt) {
															tryGraphPreview();
															}
															});
			
			toolBar.add(showGraphPreviewToolbarButton);
			
			// create and add 'preview' button
			
			previewToolbarButton = new javax.swing.JButton(); // preview (toolbar)
			
			previewToolbarButton.setIcon(mVDH.getImageIcon(PREVIEW_IMAGE_ICON_24));
			
			previewToolbarButton.setToolTipText(mVDH.getLanguageString(TOOLTIP_PREVIEW_LS_PROPERTY));
			
			previewToolbarButton.addActionListener(new java.awt.event.ActionListener() {
												   public void actionPerformed(java.awt.event.ActionEvent evt) {
												   tryShowPreview();
												   }
												   });
			
			toolBar.add(previewToolbarButton);
			
			// add separator
			
			toolBar.addSeparator();
			
			// create and add 'go to FASS' button
			
			openFASSToolbarButton = new javax.swing.JButton(openFASSAction); // open FASS in browser
			
			openFASSToolbarButton.setText(""); // don't show text (from action)
			
			openFASSAction.addPropertyChangeListener(new PropertyChangeListener() {
													 public void propertyChange(PropertyChangeEvent evt) {
													 if (evt.getPropertyName().equals(Action.NAME)) {
													 openFASSToolbarButton.setText("");
													 }
													 }
													 });
			
			toolBar.add(openFASSToolbarButton);
			
			// add separator
			
			toolBar.addSeparator();

                        if (PreferencesModel.instance().getShowPDAButton()) {
			// create and add 'import from pda' button
			
			pdaToolbarButton = new javax.swing.JButton();
			
			pdaToolbarButton.setIcon(mVDH.getImageIcon(PDA_IMAGE_ICON_24));
			
			pdaToolbarButton.setToolTipText(mVDH.getLanguageString(TOOLTIP_PDA_LS_PROPERTY));
			
			pdaToolbarButton.addActionListener(new java.awt.event.ActionListener() {
											   public void actionPerformed(java.awt.event.ActionEvent evt) {
											   pdaDialog.setVisible(true);
											   }
											   });
			
			toolBar.add(pdaToolbarButton);

                        // add separator

			toolBar.addSeparator();
			}
                        
			
			
			// package description label
			
			packageDescLabel =  new JLabel(mVDH.getLanguageString(LABEL_PACKAGES_LS_PROPERTY));
			
			toolBar.add(packageDescLabel);
			
			toolBar.add(Box.createRigidArea(new Dimension(11, 11)));

			// combo box - packages
			
			packageCombo = new JComboBox();
			
			packageCombo.setPreferredSize(new Dimension(COMBOBOX_WIDTH_NORMAL, COMBOBOX_HEIGHT_NORMAL));
			
			packageCombo.addItemListener(new PackageListener());
			
			synchronizePackageComboWithPackageData(); // see utility methods...
			
			MedViewToolBarComboBoxWrapper wrapper = new MedViewToolBarComboBoxWrapper(packageCombo);
			
			toolBar.add(wrapper);
			
			// return toolbar
			
			return new JToolBar[] { toolBar };
		}
		
		
		// SETTING 'VALUE CHANGED' STATE
		
		public void inputValueChanged(InputValueChangeEvent ivce) {
			setValueChanged(true); // response to when one of the inputs' values has changed
		}
		
		public void setValueChanged(boolean newstate) {
			hasChanged = newstate;
		}
		
		public boolean isValueChanged() {
			return hasChanged;
		}
		
		
		// SETTING THE EXAMINATION MODEL (FORM)
		
		public void setExaminationModel(ExaminationModel in_examModel) throws InvalidExaminationModelException {
			examinationModel = in_examModel;
			
			valueTabbedPane.setModel(examinationModel); // -> InvalidExaminationModelException
		}
		
		/**
		 * Sets the examination model to the one read from the
		 * specified file. If this is not possible, shows an
		 * error dialog to the user and returns a false value.
		 * Successful read returns true.
		 *
		 * @param chosenFile File a file containing an
		 * examination model.
		 */
		private void setExaminationModel(File chosenFile) throws InvalidExaminationModelException {
			Enumeration enm = mappings.elements();
			
			while (enm.hasMoreElements()) {
				Mapping mapping = (Mapping) enm.nextElement();
				
				String loc = mapping.getLocation();
				
				if (loc.equalsIgnoreCase(chosenFile.getPath())) {
					this.setExaminationModel(mapping.getModel());
					
					this.setTitle(getNewDocumentTitle());
					
					return; // BREAKS EXECUTION
				}
			}
			
			// if we get here, we hadn't read the examination model before
			
			try {
				ExaminationModel newModel;
				if (isNewTemplateFormat(chosenFile)) // Determine whether the chosen file is of the new format or not
				{
					MedFormTemplateReader reader = new MedFormTemplateReader();
					newModel = reader.readXMLExamination(chosenFile);
				} else {
					XMLMallReader handler = new XMLMallReader(); // read examination model
					newModel = handler.readXMLExamination(chosenFile);
				}
				mappings.add(new Mapping(newModel, chosenFile.getPath()));
				
				this.setExaminationModel(newModel); // triggers update of form in real-time
				
				this.setTitle(getNewDocumentTitle());
			} catch (IOException e) {
				e.printStackTrace();
				
				throw new InvalidExaminationModelException("Cannot read specified form! Error: " + e.getMessage());
			} catch (InvalidTemplateException e) {
				e.printStackTrace();
				throw new InvalidExaminationModelException("Cannot parse the specified form! Invalid template error: " + e.getMessage());
			} catch (org.xml.sax.SAXException e) {
				e.printStackTrace();
				throw new InvalidExaminationModelException("Could not parse the XML file! Are translator/template set correctly?\nError: " + e.getMessage());
			} catch (javax.xml.parsers.ParserConfigurationException e) {
				e.printStackTrace();
				throw new InvalidExaminationModelException("Could not parse the XML file! ParserConfigurationException: " + e.getMessage());
			}
		}
		
		private boolean isNewTemplateFormat(File chosenFile) throws FileNotFoundException, IOException {
			BufferedReader br = new BufferedReader(new FileReader(chosenFile));
			StringBuffer sb = new StringBuffer();
			String nextLine;
			do
			{
				nextLine = br.readLine();
				if (nextLine != null) {
					sb.append(nextLine);
					sb.append("\n");
				}
			} while (nextLine != null);
			
			if (sb.toString().toLowerCase().indexOf("doctype") == -1)
				return false;
			else
				return true;
		}
		
		// WHEN NEW EXAMINATION REQUESTED
		
		private void newExamination() {
			MedRecords.newDocument(null, null);
		}
		
		private void openExamination() {
			MedRecords.openDocument();
		}
		
		public void clearAllInputs() {
			valueTabbedPane.clearAllInputs(); // delegates to the value tabbed pane
		}
		
		private void selectFirstInput() {
			try // view the first tab
			{
				TabPanel firstTab = valueTabbedPane.getFirstTab(); // First
				
				valueTabbedPane.gotoTab(firstTab);
				
				firstTab.setSelectedInputComponent(firstTab.getFirstInputComponent());
			} catch (IndexOutOfBoundsException ioobe) {
				Ut.error("Error: no tabs available!"); // Not a lot to do if we have no tabs!
			}
		}
		
		// Close action
		private void close() {
			MedRecords.closeDocument(MedRecordsFrame.this);
		}
		
		// CLOSE / EXIT
		
		protected void onClose()  // from main shell
		{
			super.onClose(); // important
			
			detachListeners();
			
			mVDH.removeMedViewPreferenceListener(dataPreferenceListener);
		}
		
		private void tryExit() {
			MedRecords.exitApplication(); // will ask all documents if they can close
		}
		
		private void detachListeners() // memory leak Fredrik 120407
		{
			presetListPanel.detachAsListener(); // memory leak Fredrik 120407
			
			valueTabbedPane.detachAsListener();
		}
		
		public boolean canCloseDocument() {
			int result = saveDialog(); // returns YES, NO, CANCEL
			
			switch (result) {
				case JOptionPane.YES_OPTION:
				{
					return save(); // save() shows dialogs etc. when errors occur
				}
				case JOptionPane.NO_OPTION:
				{
					return true;
				}
				default:
				{
					return false;
				}
			}
		}
		
		private int saveDialog() {
			if (!isValueChanged()) // do not show dialog if value has not changed
			{
				return JOptionPane.NO_OPTION;
			}
			
			int result = JOptionPane.showConfirmDialog(this,
													   
													   mVDH.getLanguageString(LABEL_SHOULD_SAVE_EXAMINATION_LS_PROPERTY),
													   
													   mVDH.getLanguageString(TITLE_CLOSE_LS_PROPERTY),
													   
													   JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
			
			return result;
		}
		
		
		// GRAPH PREVIEW
		
		private void tryGraphPreview(){
			// create tree and ask MedRecords to show a preview
			try {
				if (!mVDH.isUserIDSet()) {
					throw new UserIDNotSetException(mVDH.getLanguageString(
																		   ERROR_USER_ID_NOT_SET)); // -> UserIDNotSetException
				} else if (!mVDH.isUserNameSet()) {
					throw new UserNameNotSetException(mVDH.getLanguageString(
																			 ERROR_USER_NAME_NOT_SET)); // -> UserNameNotSetException
				}
				// Get a Tree
				
				String pid = getPatientIdentifier(); // -> NoPatientIdentifierTermException ( MIGHT BE P-CODE )
				
				String pidTerm = getPatientIdentifierTerm(); // -> NoPatientIdentifierTermException
				
				String pCode = mVDH.obtainPCode(pid, false); // -> InvalidRawPIDException, CouldNotGeneratePCodeException ( IS P-CODE )
				
				Tree examinationTree = compileTreeFromInputs(pidTerm, pCode); // -> InvalidRawPIDException, CouldNotGeneratePCodeException
				
				MedRecords.showGraphWin(examinationTree, this);
				
			} catch (UserIDNotSetException e) {
				
				MedViewDialogs.instance().createAndShowErrorDialog(this, mVDH.getLanguageString(ERROR_USER_ID_NOT_SET));
				MedRecords.showPreferences(this, TAB_MEDRECORDS_EXPERT_LS_PROPERTY);
				
			} catch (UserNameNotSetException e) {         
				
				MedViewDialogs.instance().createAndShowErrorDialog(this, mVDH.getLanguageString(ERROR_USER_NAME_NOT_SET));
				MedRecords.showPreferences(this, TAB_MEDRECORDS_EXPERT_LS_PROPERTY);
				
			} catch (NoPatientIdentifierTermException e) {
				
				JOptionPane.showMessageDialog(this, e.getMessage(), mVDH.getLanguageString(ERROR_WHILE_GRAPHING), JOptionPane.ERROR_MESSAGE);
				
			} catch (InvalidRawPIDException e) {
				
				JOptionPane.showMessageDialog(this, e.getMessage(), mVDH.getLanguageString(ERROR_WHILE_GRAPHING), JOptionPane.ERROR_MESSAGE);
				
			} catch (CouldNotGeneratePCodeException e) {
				
				JOptionPane.showMessageDialog(this, e.getMessage(), mVDH.getLanguageString( ERROR_WHILE_GRAPHING), JOptionPane.ERROR_MESSAGE);
				
			}
		}
	    
		// IDENTIFICATION SETTING (PID / PCODE)
		
		private void setIdentification(PatientIdentifier pid) {
			valueTabbedPane.setIdentification(pid);
		}
		
		
		// CURRENT EXAMINATION DETAILS
		
		public ExaminationModel getCurrentExamination() {
			return valueTabbedPane.getCurrentExamination();
		}
		
		private void setExaminationDate(Date d) {
			mExaminationDate = d;
		}
		
		// IMPORTING PDA DATA
		
		/**
		 * Fetches pda data. Shows dialog with result.
		 */
		
		private void readPDADataFromDB(ExaminationIdentifier exam) {
			String aDirName = PreferencesModel.instance().getPDADatabaseLocation();
			
			aDirName = aDirName + File.separator + "ExamSaveDir";
			
			TreeFileHandler tfh = new TreeFileHandler(aDirName);
			
			String error = null;
			File file = null;
			
			try {
				file = tfh.getTreeFile(exam);
			} catch (Exception e) {
				error = e.getMessage();
			}
			
			try {
				readPDAData(file);
			} catch (FileNotFoundException e) {
				error = "File not found";
			} catch (IOException e) {
				error = e.getMessage();
			} catch (Exception e) {
				error = e.getMessage();
			}
			
			if (error != null) // error has occured
			{
				String failMessage = mVDH.getLanguageString(ERROR_COULD_NOT_READ_PDA_DATA_LS_PROPERTY) + "\n" + error;
				
				String title = mVDH.getLanguageString(TITLE_IMPORT_DATA_LS_PROPERTY);
				
				JOptionPane.showMessageDialog(this, failMessage, title, JOptionPane.ERROR_MESSAGE);
			} else {
				String mess = mVDH.getLanguageString(LABEL_DATA_IMPORTED_OK_PDA_LS_PROPERTY);
				
				String title = mVDH.getLanguageString(TITLE_IMPORT_DATA_LS_PROPERTY);
				
				JOptionPane.showMessageDialog(this, mess, title, JOptionPane.INFORMATION_MESSAGE);
				
				pdaImportExamination = exam;
			}
		}
		
		// MVDHandler does not handle removing images, thus removing images is disabled
		public void loadExamination(File treeFile) {
			String error = null;
			
			if (treeFile != null) {
				try {
					readTreeFile(treeFile);
				} catch (IOException ioe) {
					error = ioe.getMessage();
				}
				
				if (error != null) // error has occured
				{
					String failMessage = mVDH.getLanguageString(ERROR_COULD_NOT_READ_BACKGROUND_DATA_LS_PROPERTY)
					+ "\n" + error;
					
					String title = mVDH.getLanguageString(TITLE_IMPORT_DATA_LS_PROPERTY);
					
					JOptionPane.showMessageDialog(this, failMessage, title, JOptionPane.ERROR_MESSAGE);
				}
				
				else // successful
				{
					try {
						this.setTitle(getPatientIdentifier() + " " + mVDH.getLanguageString(TITLE_MEDRECORDS_WHEN_SAVED_LS_PROPERTY));
						
						setValueChanged(false);
						// MVDHandler does not handle removing images, thus removing is disabled
						getPhotoPanel().setRemoveDisabled(true);
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
					
					// String mess = mVDH.getLanguageString(LABEL_DATA_IMPORTED_OK_LS_PROPERTY);
					
					// String title = mVDH.getLanguageString(TITLE_IMPORT_DATA_LS_PROPERTY);
					
					// JOptionPane.showMessageDialog(this, mess, title, JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}
		
		// IMPORTING BACKGROUND DATA
		
		/**
		 * Fetches touchscreen data. Shows dialog with the result.
		 */
		private void importTouchScreenMenuItemActionPerformed(ActionEvent evt) {
			String idCode = null;
			
			try {
				idCode = getPatientIdentifier();
			} catch (NoPatientIdentifierTermException e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), "Hittade ingen pid",
											  
											  JOptionPane.ERROR_MESSAGE);
				
				return;
			}
			
			if (!mVDH.validates(idCode)) {
				String errMess = mVDH.getLanguageString(ERROR_MISSING_PCODE_LS_PROPERTY);
				
				String title = mVDH.getLanguageString(TITLE_IMPORT_DATA_LS_PROPERTY);
				
				JOptionPane.showMessageDialog(this, errMess, title, JOptionPane.INFORMATION_MESSAGE);
				
				return;
			}
			
			PreferencesModel prefs = PreferencesModel.instance();
			
			String error = null;
			
			try {
				if (prefs.getTouchScreenDataFromMedForm()) {
					
					MedFormConnection mfConn = new MedFormConnection(prefs.getMedFormURL(),
																	 prefs.getMedFormUser(),
																	 prefs.getMedFormMVD());
					String treeString = mfConn.getLatestTree(idCode);
					readTouchScreenData(new StringReader(treeString));
				}
				
				else {
					readTouchScreenDataFromDB(idCode);
				}
			} catch (java.net.MalformedURLException e) {
				error = "Malformed URL: " + e.getMessage();
			} catch (IOException ioe) {
				error = ioe.getMessage();
			}
			
			if (error != null) // error has occured
			{
				String failMessage = mVDH.getLanguageString(ERROR_COULD_NOT_READ_BACKGROUND_DATA_LS_PROPERTY)
				+ "\n" + error;
				
				String title = mVDH.getLanguageString(TITLE_IMPORT_DATA_LS_PROPERTY);
				
				JOptionPane.showMessageDialog(this, failMessage, title, JOptionPane.ERROR_MESSAGE);
			}
			
			else // successful
			{
				String mess = mVDH.getLanguageString(LABEL_DATA_IMPORTED_OK_LS_PROPERTY);
				
				String title = mVDH.getLanguageString(TITLE_IMPORT_DATA_LS_PROPERTY);
				
				JOptionPane.showMessageDialog(this, mess, title, JOptionPane.INFORMATION_MESSAGE);
			}
		}
		
		
		private boolean readTouchScreenDataFromDB(String idCode) throws IOException {
			//String aDirName = PreferencesModel.instance().getLocalDatabaseLocation();
			String aDirName = mVDH.getExaminationDataLocation();
			aDirName = aDirName + File.separator + "BackgroundData";
			
			File aDir = new File(aDirName);
			
			if (!aDir.isDirectory()) {
				throw new IOException(aDirName + " is not a directory ");
			}
			
			String[] extensions = {"tree", "Tree", "TREE"};
			
			ExtensionFileFilter treeFilter = new ExtensionFileFilter(extensions, true);
			
			File[] files = aDir.listFiles(treeFilter);
			
			ArrayList idFiles = new ArrayList();
			
			for (int i = 0; i < files.length; i++) {
				File aFile = files[i];
				
				String fN = aFile.getName();
				
				if (fN.startsWith(idCode)) // identifies the file (* PNR *)
				{
					idFiles.add(aFile);
				}
			}
			
			if (idFiles.size() < 1) {
				
				String errMess = mVDH.getLanguageString(LABEL_IMPORT_NO_DATA_EXISTS_LS_PROPERTY) + " " + idCode;
				/*
				 String title = mVDH.getLanguageString(TITLE_IMPORT_DATA_LS_PROPERTY);
				 
				 JOptionPane.showMessageDialog(this, errMess, title, JOptionPane.INFORMATION_MESSAGE);
				 
				 return false;
				 */
				throw new IOException(errMess); // Will be shown in dialog
			} else {
				
				File peckFile = getLatestFile(idFiles);
				
				if (peckFile == null) {
					throw new IOException(" Unknown error in MedRecordFrame importTouchScreenMenuItemActionPerformed ");
					
				}
				
				return this.readTouchScreenFile(peckFile);
			}
		}
		
		private File getLatestFile(ArrayList idFiles) {
			if (idFiles == null || idFiles.size() == 0) {
				return null;
			}
			
			long prvTime = 0;
			
			File theFile = null;
			
			for (int i = 0; i < idFiles.size(); i++) {
				File aFile = (File)idFiles.get(i);
				
				long modiTime = aFile.lastModified();
				
				if (modiTime > prvTime) {
					theFile = aFile;
					
					prvTime = modiTime;
				}
			}
			return theFile;
		}
		
		/**
		 *  Read a treefile and update the current form with the contents.
		 */
		private boolean readTreeFile(File tFile) throws FileNotFoundException, IOException {
			boolean ok = OpenTreeHack.instance().readTreeFile(tFile, valueTabbedPane);
			if (ok) {
				setExaminationDate(OpenTreeHack.instance().getDate());
			}
			return ok;
		}
		
		
		/**
		 *  Read a touch screen file and update the current form with the contents.
		 */
		private boolean readTouchScreenFile(File pFile) throws FileNotFoundException, IOException {
			return readTouchScreenData(new FileReader(pFile));
		}
		
		private boolean readTouchScreenData(Reader reader) throws IOException {
			return TouchScreenHack.instance().readTouchScreen(reader, valueTabbedPane);
		}
		
		private boolean readPDAData(File pFile) throws FileNotFoundException, IOException, Exception {
			boolean r = false;
			r = PDAHack.instance().readPDA(new FileReader(pFile), valueTabbedPane);
			return true;
		}
		
		// FIELD FOCUSING WHEN MADE VISIBLE
		
		public void setVisible(boolean visible) // Overrides setVisible() (field focus when visible)
		{
			super.setVisible(visible);
			
			valueTabbedPane.focusSelectedInput();
		}
		
		
		// PHOTO PANEL METHODS
		
		public PhotoPanel getPhotoPanel() {
			return photoPanel;
		}
		
		/**
		 * Override this if you are interested in receiving
		 * image choices. MedRecordsFrame wants to, for example.
		 */
		public void imageChosen(ImageChoiceEvent ice) {
			//System.out.println("MRFrame imageChosen: " + ice.getImagePath());
			getPhotoPanel().addImage(ice.getImagePath());
		}
		
		public void showImageDialog(ImageButton button) {
			int maxWidth = PreferencesModel.instance().getImageWindowWidth();
			
			// Creates the image so that it can be checked for size and scaled if needed
			ImageIcon scaleImageIcon = new ImageIcon(button.getFullImage());
			
			Image image = scaleImageIcon.getImage();
			
			if (image.getWidth(null) > maxWidth) {
				image = image.getScaledInstance(maxWidth, -1, Image.SCALE_DEFAULT);
			}
			
			String title = new File(button.getImagePath()).getName();
			
			MedViewDialogs.instance().createAndShowImageDialog(this, title, image);
		}
		
		// TRANSLATOR SHOWN WHEN NEW VALUE ENTERED
		
		/**
		 * Shows a translation editor (dialog) for a term
		 */
		public void showTranslationEditor(PresetModel presetModel, String newValue) {
			if (translatorHandler == null) {
				translatorHandler = TranslatorHandler.instance();
			}
			
			// add the value to the presets
			
			String termName = presetModel.getTermName();
			
			DataHandlingExtensions.instance().writeValue(termName, newValue);
			
			presetModel.addPreset(newValue);
			
			presetModel.fireStateChanged();
			
			if (PreferencesModel.instance().getShowTranslatorAtNewValue()) {
				try {
					translatorHandler.showTranslator(this, termName, newValue);
				} catch (CouldNotObtainTranslatorException exc) {
					JOptionPane.showMessageDialog(this, exc.getMessage(), mVDH.getLanguageString(
																								 
																								 ERROR_WHILE_TRANSLATING), JOptionPane.ERROR_MESSAGE);
				} catch (TermNotInTranslatorException exc) {
					JOptionPane.showMessageDialog(this, exc.getMessage(), mVDH.getLanguageString(
																								 
																								 ERROR_WHILE_TRANSLATING), JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		
		
		// SAVING EXAMINATIONS
		
		private boolean save() // will display dialogs indicating errors etc, returns if successful
		{
			ExaminationModel exam = valueTabbedPane.getCurrentExamination();
			CategoryModel cat = exam.getCategory(CariesDataHandler.KARIES);
			
			//if cat != null it means there is a caries category and we should collect the caries data
			//from an external database and populate the form with it
			if(cat!=null)
			{
				try
				{
					String pid = getPatientIdentifier(); // -> NoPatientIdentifierTermException ( MIGHT BE P-CODE )
					//strip the '-' from the pid
					String personId = pid.substring(0,8) + pid.substring(9);
					CariesDataHandler.getInstance().getCariesData(personId, valueTabbedPane);
				}
				catch (SQLException e)
				{
					String message = mVDH.getLanguageString(LABEL_COULDNT_GET_CARIES_DATA);
					
					String title = mVDH.getLanguageString(TITLE_ERROR_LS_PROPERTY);
					
					JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
				}
				catch (FileNotFoundException e)
				{
					String message = mVDH.getLanguageString(LABEL_CARIES_FILE_MISSING);
					
					String title = mVDH.getLanguageString(TITLE_ERROR_LS_PROPERTY);
					
					JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
				}
				
				catch (NoPatientIdentifierTermException e)
				{
					//do nothing, this gets handled further down
				}
				
				//caries file is not a valid caries database file
				catch (Exception e)
				{
					String message = mVDH.getLanguageString(LABEL_CARIES_FILE_INVALID);
					
					String title = mVDH.getLanguageString(TITLE_ERROR_LS_PROPERTY);
					
					JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
				}
			}
			
			
			try {
				// try to save the examination
				saveExamination(); // -> all exceptions below
				
				// if we get here, the examination could be saved OK
				
				// Removed 10 06 04 - Don't report normalcy OT
				
				// String message = mVDH.getLanguageString(LABEL_EXAMINATION_SAVED_OK_LS_PROPERTY);
				
				// String title = mVDH.getLanguageString(TITLE_SAVE_LS_PROPERTY);
				
				// JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
				
				// Ask to delete imported file
				
				if (pdaImportExamination!=null) {
					pdaDialog.deleteExam(pdaImportExamination);
				}
				
				// return that the save was successful
				
				return true;
			} catch (UserIDNotSetException e) {
				String m = mVDH.getLanguageString(ERROR_USER_ID_NOT_SET);
				
				MedViewDialogs.instance().createAndShowErrorDialog(this, m);
				
				MedRecords.showPreferences(this, TAB_MEDRECORDS_EXPERT_LS_PROPERTY);
				
				return false;
			} catch (UserNameNotSetException e) {
				String m = mVDH.getLanguageString(ERROR_USER_NAME_NOT_SET);
				
				MedViewDialogs.instance().createAndShowErrorDialog(this, m);
				
				MedRecords.showPreferences(this, TAB_MEDRECORDS_EXPERT_LS_PROPERTY);
				
				return false;
			} catch (PCodeNRGeneratorLocationNotSetException e) {
				String m = mVDH.getLanguageString(ERROR_NR_GENERATOR_LOCATION_NOT_SET);
				
				MedViewDialogs.instance().createAndShowErrorDialog(this, m);
				
				MedRecords.showPreferences(this, TAB_MEDRECORDS_EXPERT_LS_PROPERTY);
				
				return false;
			} catch (DataLocationNotSetException e) {
				String m = mVDH.getLanguageString(ERROR_EXAMINATION_DATA_LOCATION_NOT_SET);
				
				MedViewDialogs.instance().createAndShowErrorDialog(this, m);
				
				MedRecords.showPreferences(this, TAB_MEDRECORDS_INPUT_LS_PROPERTY);
				
				return false;
			} catch (TermDefinitionLocationNotSetException e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), mVDH.getLanguageString(
																						   
																						   ERROR_WHILE_SAVING), JOptionPane.ERROR_MESSAGE);
				
				MedRecords.showPreferences(this, TAB_DATA_LOCATIONS_LS_PROPERTY);
				
				return false;
			} catch (TermValueLocationNotSetException e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), mVDH.getLanguageString(
																						   
																						   ERROR_WHILE_SAVING), JOptionPane.ERROR_MESSAGE);
				
				MedRecords.showPreferences(this, TAB_DATA_LOCATIONS_LS_PROPERTY);
				
				return false;
			} catch (ValueInputException e) {
				// try to scroll and focus erroneous value
				
				TabPanel aTab = e.getTabPanel();
				
				if (aTab != null) {
					ValueInputComponent aComp = e.getInputField();
					
					valueTabbedPane.setSelectedComponent(aTab);
					
					aTab.setSelectedInputComponent(aComp);
					
					aComp.requestFocus();
					aTab.scrollToPanel((JPanel)aComp.getParent());
				}
				
				JOptionPane.showMessageDialog(this, e.getMessage(), mVDH.getLanguageString(
																						   
																						   ERROR_WHILE_SAVING), JOptionPane.ERROR_MESSAGE);
				
				return false;
			} catch (NoPatientIdentifierTermException e) {
				// a stack trace has already been printed at lower levels, will not do it again here
				
				JOptionPane.showMessageDialog(this, e.getMessage(), mVDH.getLanguageString(
																						   
																						   ERROR_WHILE_SAVING), JOptionPane.ERROR_MESSAGE);
				
				return false;
			} catch (InvalidRawPIDException e) {
				// a stack trace has already been printed at lower levels, will not do it again here
				
				JOptionPane.showMessageDialog(this, e.getMessage(), mVDH.getLanguageString(
																						   
																						   ERROR_WHILE_SAVING), JOptionPane.ERROR_MESSAGE);
				
				return false;
			} catch (CouldNotGeneratePCodeException e) {
				// a stack trace has already been printed at lower levels, will not do it again here
				
				JOptionPane.showMessageDialog(this, e.getMessage(), mVDH.getLanguageString(
																						   
																						   ERROR_WHILE_SAVING), JOptionPane.ERROR_MESSAGE);
				
				return false;
			} catch (CouldNotParseDateException e) {
				// a stack trace has already been printed at lower levels, will not do it again here
				
				JOptionPane.showMessageDialog(this, e.getMessage(), mVDH.getLanguageString(
																						   
																						   ERROR_WHILE_SAVING), JOptionPane.ERROR_MESSAGE);
				
				return false;
			} catch (CouldNotConstructIdentifierException e) {
				// a stack trace has already been printed at lower levels, will not do it again here
				
				JOptionPane.showMessageDialog(this, e.getMessage(), mVDH.getLanguageString(
																						   
																						   ERROR_WHILE_SAVING), JOptionPane.ERROR_MESSAGE);
				
				return false;
			} catch (IOException e) {
				// a stack trace has already been printed at lower levels, will not do it again here
				
				JOptionPane.showMessageDialog(this, e.getMessage(), mVDH.getLanguageString(
																						   
																						   ERROR_WHILE_SAVING), JOptionPane.ERROR_MESSAGE);
				
				try {
					performLocalSave(); // -> All kinds of exceptions
					
					JOptionPane.showMessageDialog(this, mVDH.getLanguageString(
																			   
																			   ERROR_COULD_NOT_SAVE_NORMALLY_BUT_SAVED_LOCALLY), mVDH.getLanguageString(
																																						
																																						ERROR_WHILE_SAVING), JOptionPane.ERROR_MESSAGE);
					
					// return that the save was 'successful' (i.e. it was stored locally, so it is saved at least)
					
					return true;
				} catch (Exception exc) {
					exc.printStackTrace();
					
					JOptionPane.showMessageDialog(this, mVDH.getLanguageString(
																			   
																			   ERROR_COULD_NOT_SAVE_NORMALLY_AND_LOCALLY), mVDH.getLanguageString(
																																				  
																																				  ERROR_WHILE_SAVING), JOptionPane.ERROR_MESSAGE);
					
					// return that the save was not successful (it is not saved anywhere!)
					
					return false;
				}
			}
		}
		
		/**
		 * Saves the current examination to the location
		 * currently set in the data layer.
		 */
		private boolean saveExamination() throws ValueInputException, NoPatientIdentifierTermException,
		InvalidRawPIDException, CouldNotGeneratePCodeException, CouldNotParseDateException,
		CouldNotConstructIdentifierException, IOException, UserIDNotSetException,
		UserNameNotSetException, PCodeNRGeneratorLocationNotSetException, DataLocationNotSetException,
		TermDefinitionLocationNotSetException, TermValueLocationNotSetException {
			return this.saveExamination(null);
		}
		
		/**
		 * Saves the current examination to the specified
		 * location.
		 */
		private boolean saveExamination(String location) throws ValueInputException, NoPatientIdentifierTermException,
		InvalidRawPIDException, CouldNotGeneratePCodeException, CouldNotParseDateException,
		CouldNotConstructIdentifierException, IOException, UserIDNotSetException,
		UserNameNotSetException, PCodeNRGeneratorLocationNotSetException, DataLocationNotSetException,
		TermDefinitionLocationNotSetException, TermValueLocationNotSetException {
			if (!mVDH.isUserIDSet()) {
				throw new UserIDNotSetException(mVDH.getLanguageString(
																	   
																	   ERROR_USER_ID_NOT_SET)); // -> UserIDNotSetException
			} else if (!mVDH.isUserNameSet()) {
				throw new UserNameNotSetException(mVDH.getLanguageString(
																		 
																		 ERROR_USER_NAME_NOT_SET)); // -> UserNameNotSetException
			} else if (!mVDH.isPCodeNRGeneratorLocationSet()) {
				throw new PCodeNRGeneratorLocationNotSetException(mVDH.getLanguageString(
																						 
																						 ERROR_NR_GENERATOR_LOCATION_NOT_SET)); // -> PCodeNRGeneratorLocationNotSetException
			} else if ((location == null) && !mVDH.isExaminationDataLocationSet()) {
				throw new DataLocationNotSetException(mVDH.getLanguageString(
																			 
																			 ERROR_EXAMINATION_DATA_LOCATION_NOT_SET)); // -> DataLocationNotSetException
			} else if (!mVDH.isTermDefinitionLocationSet()) {
				throw new TermDefinitionLocationNotSetException(mVDH.getLanguageString(
																					   
																					   ERROR_TERM_LOCATIONS_NOT_SET));
			} else if (!mVDH.isTermValueLocationSet()) {
				throw new TermValueLocationNotSetException(mVDH.getLanguageString(
																				  
																				  ERROR_TERM_LOCATIONS_NOT_SET));
			}
			
			valueTabbedPane.checkValues(); // -> ValueInputException
			
			String pid = getPatientIdentifier(); // -> NoPatientIdentifierTermException ( MIGHT BE P-CODE )
			
			String pidTerm = getPatientIdentifierTerm(); // -> NoPatientIdentifierTermException
			
			String pCode = mVDH.obtainPCode(pid, true); // -> InvalidRawPIDException, CouldNotGeneratePCodeException ( IS P-CODE )
			
			Tree treeToSave = compileTreeFromInputs(pidTerm, pCode); // -> CouldNotGeneratePCodeException, InvalidRawPIDException
			
			// nya namn för bilderna finns i trädet
			
			String[] newImagePaths = treeToSave.getValuesOfNodesNamed(PreferencesModel.instance().getImageTermName());
			
			// faktiska sökvägar till bilderna finns i fotopanelen (fulhack, typ)
			
			byte[][] imageByteArrays = photoPanel.getImageByteArrays(); // cached, so doesnt matter if 'memory stick' or source removed
			
			System.out.println("imageByteArrays.length: " + imageByteArrays.length + " " + newImagePaths.length);
			
			DataHandlingExtensions dHE = DataHandlingExtensions.instance();
			
			ExaminationIdentifier examinationIdentifier = dHE.getExaminationIdentifier(treeToSave); // -> CouldNotParseDateException, CouldNotConstructIdentifierException
			
			ExaminationImage[] imageArray = new ExaminationImage[imageByteArrays.length];
			
			for (int i = 0; i < imageByteArrays.length; i++) {
				imageArray[i] = new ByteArrayExaminationImage(imageByteArrays[i], examinationIdentifier);
				
				imageArray[i].setName(newImagePaths[i]);
			}
			
			System.out.println("imageByteArray.length: " + imageArray.length);
			
			// Perform any extra processing
			postProcessTree(treeToSave);
			
			// spara trädet
			
			if (location == null) {
				mVDH.saveExamination(treeToSave, imageArray); // -> IOException
			} else {
				mVDH.saveExamination(treeToSave, imageArray, location); // -> IOException
			}
			
			this.setTitle(getPatientIdentifier() + " " + mVDH.getLanguageString(TITLE_MEDRECORDS_WHEN_SAVED_LS_PROPERTY));
			
			setValueChanged(false);
			
			return true;
		}
		
		private void performLocalSave() throws ValueInputException, NoPatientIdentifierTermException,
		InvalidRawPIDException, CouldNotGeneratePCodeException, CouldNotParseDateException,
		CouldNotConstructIdentifierException, IOException, UserIDNotSetException,
		UserNameNotSetException, PCodeNRGeneratorLocationNotSetException,
		TermDefinitionLocationNotSetException, TermValueLocationNotSetException {
			File tempMVDDir = new File(System.getProperty("java.io.tmpdir"), "tempMVD");
			
			try {
				saveExamination(tempMVDDir.getPath());
			} catch (DataLocationNotSetException exc) {
				exc.printStackTrace();
				
				// this will not happen here, since we specify the location ourselves
			}
		}
		
		// Save and close the window
		
		private void saveAndClose() {
			if (save()) {
				close();
			}
		}
		
		// PREVIEW AND SAVE
		
		private void tryShowPreview() {
			try {
				showPreview();
			} catch (UserIDNotSetException e) {
				String m = mVDH.getLanguageString(ERROR_USER_ID_NOT_SET);
				
				MedViewDialogs.instance().createAndShowErrorDialog(this, m);
				
				MedRecords.showPreferences(this, TAB_MEDRECORDS_EXPERT_LS_PROPERTY);
			} catch (UserNameNotSetException e) {
				String m = mVDH.getLanguageString(ERROR_USER_NAME_NOT_SET);
				
				MedViewDialogs.instance().createAndShowErrorDialog(this, m);
				
				MedRecords.showPreferences(this, TAB_MEDRECORDS_EXPERT_LS_PROPERTY);
			} catch (PCodeNRGeneratorLocationNotSetException e) {
				String m = mVDH.getLanguageString(ERROR_NR_GENERATOR_LOCATION_NOT_SET);
				
				MedViewDialogs.instance().createAndShowErrorDialog(this, m);
				
				MedRecords.showPreferences(this, TAB_MEDRECORDS_EXPERT_LS_PROPERTY);
			} catch (TermDefinitionLocationNotSetException e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), mVDH.getLanguageString(
																						   
																						   ERROR_WHILE_PREVIEWING), JOptionPane.ERROR_MESSAGE);
				
				MedRecords.showPreferences(this, TAB_DATA_LOCATIONS_LS_PROPERTY);
			} catch (TermValueLocationNotSetException e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), mVDH.getLanguageString(
																						   
																						   ERROR_WHILE_PREVIEWING), JOptionPane.ERROR_MESSAGE);
				
				MedRecords.showPreferences(this, TAB_DATA_LOCATIONS_LS_PROPERTY);
			} catch (NoPatientIdentifierTermException e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), mVDH.getLanguageString(
																						   
																						   ERROR_WHILE_PREVIEWING), JOptionPane.ERROR_MESSAGE);
			} catch (InvalidRawPIDException e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), mVDH.getLanguageString(
																						   
																						   ERROR_WHILE_PREVIEWING), JOptionPane.ERROR_MESSAGE);
			} catch (CouldNotGeneratePCodeException e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), mVDH.getLanguageString(
																						   
																						   ERROR_WHILE_PREVIEWING), JOptionPane.ERROR_MESSAGE);
			} catch (CouldNotGeneratePreviewException e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), mVDH.getLanguageString(
																						   
																						   ERROR_WHILE_PREVIEWING), JOptionPane.ERROR_MESSAGE);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), mVDH.getLanguageString(
																						   
																						   ERROR_WHILE_PREVIEWING), JOptionPane.ERROR_MESSAGE);
			}
		}
		
		private void showPreview() throws UserIDNotSetException, UserNameNotSetException,
		PCodeNRGeneratorLocationNotSetException, NoPatientIdentifierTermException,
		InvalidRawPIDException, CouldNotGeneratePCodeException,	IOException,
		CouldNotGeneratePreviewException, TermDefinitionLocationNotSetException,
		TermValueLocationNotSetException {
			if (!mVDH.isUserIDSet()) {
				throw new UserIDNotSetException(mVDH.getLanguageString(
																	   
																	   ERROR_USER_ID_NOT_SET)); // -> UserIDNotSetException
			} else if (!mVDH.isUserNameSet()) {
				throw new UserNameNotSetException(mVDH.getLanguageString(
																		 
																		 ERROR_USER_NAME_NOT_SET)); // -> UserNameNotSetException
			} else if (!mVDH.isPCodeNRGeneratorLocationSet()) {
				throw new PCodeNRGeneratorLocationNotSetException(mVDH.getLanguageString(
																						 
																						 ERROR_NR_GENERATOR_LOCATION_NOT_SET)); // -> PCodeNRGeneratorLocationNotSetException
			} else if (!mVDH.isTermDefinitionLocationSet()) {
				throw new TermDefinitionLocationNotSetException(mVDH.getLanguageString(
																					   
																					   ERROR_TERM_LOCATIONS_NOT_SET));
			} else if (!mVDH.isTermValueLocationSet()) {
				throw new TermValueLocationNotSetException(mVDH.getLanguageString(
																				  
																				  ERROR_TERM_LOCATIONS_NOT_SET));
			}
			
			// we do not check the input values when previewing
			
			String pid = getPatientIdentifier(); // -> NoPatientIdentifierTermException ( MIGHT BE P-CODE )
			
			String pidTerm = getPatientIdentifierTerm(); // -> NoPatientIdentifierTermException
			
			String pCode = mVDH.obtainPCode(pid, false); // -> InvalidRawPIDException, CouldNotGeneratePCodeException ( IS P-CODE )
			
			Tree examinationTree = compileTreeFromInputs(pidTerm, pCode); // -> InvalidRawPIDException, CouldNotGeneratePCodeException
			
			ExaminationValueContainer container = new ExaminationValueTable(examinationTree); // -> IOException
			
			translatorHandler.showPreview(MedRecordsFrame.this, pCode,
										  
										  MedViewGeneratorUtilities.wrapExaminationValueContainer(container)); // -> CouldNotGeneratePreviewException
		}
		
		
		// TREE CREATION BASED ON INPUTS
		
		private Tree compileTreeFromInputs(String idTerm, String pCode) throws
		InvalidRawPIDException, CouldNotGeneratePCodeException, NoPatientIdentifierTermException {
			DataHandlingExtensions dHE = DataHandlingExtensions.instance();
			
			Tree rootTree = dHE.createExaminationTree(idTerm, mExaminationDate, pCode); // -> InvalidRawPIDException, CouldNotGeneratePCodeException
			
			return completeTreeFromInputs(rootTree, pCode); // -> NoPatientIdentifierTermException
		}
		
		private Tree completeTreeFromInputs(Tree t, String pCode) throws NoPatientIdentifierTermException {
			String idCode = getPatientIdentifier(); // -> NoPatientIdentifierTermException
			
			// get tree structures from all the tabs and add them to our root tree
			
			Tree[] inputTrees = valueTabbedPane.getTreeRepresentations(idCode, mExaminationDate, pCode);
			
			for (int i = 0; i < inputTrees.length; i++) {
				t.addChild(inputTrees[i]);
			}
			
			Tree plaqueTree = valueTabbedPane.getPlaqueTree();
			
			if (plaqueTree != null) {
				t.addChild(plaqueTree);
			}
			
			Tree mucosTree = valueTabbedPane.getMucosTree();
			
			if (mucosTree != null) {
				t.addChild(mucosTree);
			}
			
			return t;
		}
		
		private void postProcessTree(Tree t) {
			// Add special term to store GFSFOB
			addGfsFob(t);
		}
		
		// Hack to be able to add a new term not easily handled by mForm.
		// It counts the number of terms having the value 6 or 7.
		private void addGfsFob(Tree t) {
			final String[] terms = {"GFS1","GFS2","GFS3","GFS4","GFS5","GFS6","GFS7","GFS8","GFS9","GFS10","GFS11","GFS12",
				"GFS13","GFS14","GFS15","GFS16","GFS17","GFS18","GFS19","GFS20","GFS21","GFS22","GFS23","GFS24","GFS25","GFS26",
			"GFS27","GFS28","GFS29","GFS30","GFS31","GFS32"};
			boolean gfsTermFound = false;
			String gfsTerm = null;
			int gfsFobCount = 0;
			
			for (int i=0; i<terms.length; i++) {
				String[] values = t.getValuesOfNodesNamed(terms[i]);
				
				// Should have one or zero values
				if (values.length == 1) {
					gfsTermFound = true;	// some term was found
					gfsTerm = terms[i];
					
					try {
						int value = Integer.parseInt(values[0]);
						
						if (value > 5) { // means phobia
							gfsFobCount++;
						}
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				}
			}
			if (gfsTermFound) {
				Tree gfsFobTree;
				Tree gfsTree = t.getNode(gfsTerm);
				gfsTree = gfsTree.getParent();
				
				gfsFobTree = t.getNode("GFSFOB");
				if (gfsFobTree == null) {
					gfsFobTree = new TreeBranch("GFSFOB");
				}
				gfsFobTree.addChild(new TreeLeaf((new Integer(gfsFobCount)).toString()));
				
				gfsTree.addChild(gfsFobTree);
			}
		}
		
		// PATIENT IDENTIFIER
		
		/**
		 * Find the patient identifier among the fields.
		 * Returns the first value of the first field that is
		 * a patient identifier, or null if none such was found
		 */
		public String getPatientIdentifier() throws NoPatientIdentifierTermException {
			TabPanel[] tabs = valueTabbedPane.getTabs();
			
			for (int tabCount = 0; tabCount < tabs.length; tabCount++) {
				ValueInputComponent[] inputs = tabs[tabCount].getInputComponents();
				
				for (int inputCount = 0; inputCount < inputs.length; inputCount++) {
					ValueInputComponent thisInput = inputs[inputCount];
					
					if (thisInput.isPatientIdentifierType()) {
						String[] values = thisInput.getValues();
						
						if (values.length > 0) {
							return values[0];
						}
					}
				}
			}
			
			// if we get here, there was no patient identifier term found
			
			String message = mVDH.getLanguageString(ERROR_NO_PATIENT_IDENTIFIER_TERM_FOUND);
			
			throw new NoPatientIdentifierTermException(message);
		}
		
		public String getPatientIdentifierTerm() throws NoPatientIdentifierTermException {
			TabPanel[] tabs = valueTabbedPane.getTabs();
			
			for (int tabCount = 0; tabCount < tabs.length; tabCount++) {
				ValueInputComponent[] inputs = tabs[tabCount].getInputComponents();
				
				for (int inputCount = 0; inputCount < inputs.length; inputCount++) {
					ValueInputComponent thisInput = inputs[inputCount];
					
					if (thisInput.isPatientIdentifierType()) {
						return thisInput.getName();
					}
				}
			}
			
			// if we get here, there was no patient identifier term found
			
			String message = mVDH.getLanguageString(ERROR_NO_PATIENT_IDENTIFIER_TERM_FOUND);
			
			throw new NoPatientIdentifierTermException(message);
		}
		
		
		// UTILITY METHODS
		
		private KeyStroke getAcceleratorKeyStroke(int keyCode) {
			return KeyStroke.getKeyStroke(keyCode, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
		}
		
		private String getNewDocumentTitle() {
			String title = TITLE_PREFIX;
			
			if (docNum > 1) {
				title = title + " " + docNum;
			}
			
			return title + " - MedRecords";
		}
		
		private boolean setCurrentPackage(DataComponentPackage pack) // setting to null clears it, returns if successful set
		{
			if (pack == null) {
				// the package we are setting is null
				
				currentPackage = null;
				
				mVDH.clearUserPreference(PreferencesModel.LAST_DATA_COMPONENT_PACKAGE_PROPERTY,
										 
										 PreferencesModel.class);
				
				try {
					setExaminationModel(MedRecords.createEmptyForm()); // setCurrentPackage(null) was called
				} catch (InvalidExaminationModelException exc) {
					exc.printStackTrace(); // should not happen
				}
				
				translatorHandler.clearTemplateLocation();
				
				translatorHandler.clearTranslatorLocation();
				
				PreferencesModel.instance().setGraphTemplateLocation("");
				
				return true; // we have cleared the package - returns successful
			} else {
				// the package we are setting is not null
				
				if ((currentPackage != null) && currentPackage.getPackageName().equals(
																					   
																					   pack.getPackageName()) && currentPackage.getExaminationLocation().equalsIgnoreCase(
																																										  
																																										  pack.getExaminationLocation())) // i.e. we have edited the current package
				{
					// previous package exists, package to set is non-null, they have the same name
					
					this.currentPackage = pack;
					
					translatorHandler.setTemplateLocation(currentPackage.getTemplateLocation()); // template model for previewing
					
					translatorHandler.setTranslatorLocation(currentPackage.getTranslatorLocation()); // translator model for previewing
					
					if(currentPackage.usesGraph())
					{
						PreferencesModel.instance().setGraphTemplateLocation(currentPackage.getGraphLocation());
					}
					else
					{
						PreferencesModel.instance().setGraphTemplateLocation("");
					}
					
					return true; // we have updated the template and translator locations - returns successful
				} else {
					// we dont care if previous package exists or not, package to set is non-null
					
					//if these locations is null or empty the settings has been modified manually
					//this should never happen
					if (pack.getExaminationLocation() == null || pack.getExaminationLocation().length()==0 ||
						pack.getDatabaseLocation() == null || pack.getDatabaseLocation().length()==0 ||
						pack.getTermDefinitionsLocation() == null || pack.getTermDefinitionsLocation().length()==0 ||
						pack.getTermValuesLocation() == null || pack.getTermValuesLocation().length()==0
						)
					{
						mVD.createAndShowErrorDialog(MedRecordsFrame.this, mVDH.getLanguageString(
																								  
																								  ERROR_PACKAGE_DOES_NOT_CONTAIN_EXAMINATION));
						
						return false; // package has no form included which it must have here - returns unsuccessful
					} else {
						this.currentPackage = pack;
						
						try {
							
							mVDH.setTermDefinitionLocation(currentPackage.getTermDefinitionsLocation());
							mVDH.setTermValueLocation(currentPackage.getTermValuesLocation());
							mVDH.setExaminationDataLocation(currentPackage.getDatabaseLocation());
							
							// examination model
							setExaminationModel(new File(currentPackage.getExaminationLocation()));
							
							
							// initial patientidentifier (if set)
							
							if (initialPID != null) {
								setIdentification(initialPID);
							}
							
							mVDH.setUserStringPreference(PreferencesModel.LAST_DATA_COMPONENT_PACKAGE_PROPERTY,
														 
														 currentPackage.getPackageName(), PreferencesModel.class);
							
							// template model for previewing
							
							translatorHandler.setTemplateLocation(currentPackage.getTemplateLocation());
							
							// translator model for previewing
							
							translatorHandler.setTranslatorLocation(currentPackage.getTranslatorLocation());
							
							if(currentPackage.usesGraph())
							{
								PreferencesModel.instance().setGraphTemplateLocation(currentPackage.getGraphLocation());
							}
							else
							{
								PreferencesModel.instance().setGraphTemplateLocation("");
							}
							
							return true; // returns successful
						} catch (InvalidExaminationModelException exc) {
							mVD.createAndShowErrorDialog(MedRecordsFrame.this, exc.getMessage());
							
							return false; // returns unsuccessful
						}
					}
				}
			}
		}
		
		private DataComponentPackage getCurrentPackage() {
			return currentPackage;
		}
		
		private void synchronizePackageDataWithPrefs() {
			allIncludedPackages = DataComponentPackageUtilities.obtainIncludedPackages(PreferencesModel.class);
			
			if (allIncludedPackages.length != 0) {
				boolean haveSetPreviousPackage = false;
				
				if (mVDH.isUserPreferenceSet(PreferencesModel.LAST_DATA_COMPONENT_PACKAGE_PROPERTY, PreferencesModel.class)) {
					String currentPackageName = mVDH.getUserStringPreference(
																			 
																			 PreferencesModel.LAST_DATA_COMPONENT_PACKAGE_PROPERTY, null, PreferencesModel.class);
					
					if (currentPackageName != null) {
						for (int ctr = 0; ctr < allIncludedPackages.length; ctr++) {
							if (allIncludedPackages[ctr].getPackageName().equals(currentPackageName)) {
								setCurrentPackage(allIncludedPackages[ctr]);
								
								haveSetPreviousPackage = true;
								
								break;
							}
						}
					}
				}
				
				if (!haveSetPreviousPackage) // i.e. if the last package set could not be found
				{
					setCurrentPackage(allIncludedPackages[0]);
				}
			} else {
				setCurrentPackage(null); // clears it
			}
		}
		
		private void synchronizePackageComboWithPackageData() {
			updatingPackageCombo = true;
			
			// re-populate combo box list
			
			packageCombo.removeAllItems();
			
			for (int ctr = 0; ctr < allIncludedPackages.length; ctr++) {
				packageCombo.addItem(allIncludedPackages[ctr]);
			}
			
			// try to select the current package in the combo box (only if it is not null)
			
			if (getCurrentPackage() != null) {
				packageCombo.setSelectedItem(currentPackage);
			}
			
			updatingPackageCombo = false;
		}
		
		
		// PACKAGE COMBOBOX LISTENER
		
		private class PackageListener implements ItemListener {
			private DataComponentPackage lastDeselectedPackage = null;
			
			/* NOTE: the only time this method is called and not
			 blocked by the flag, is when the user actually
			 chooses something else in the package combo box. */
			
			private void setLastSelectedPackage() {
				updatingPackageCombo = true;
				
				packageCombo.setSelectedItem(lastDeselectedPackage);
				
				updatingPackageCombo = false;
			}
			
			public void itemStateChanged(ItemEvent e) {
				if (!updatingPackageCombo) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						if (isValueChanged()) {
							int i = saveDialog();
							
							if (i == JOptionPane.YES_OPTION) {
								if (!save()) // save() returns false if unsuccessful
								{
									setLastSelectedPackage();
									
									return; // BREAKS EXECUTION
								}
							} else if (i == JOptionPane.CANCEL_OPTION) {
								setLastSelectedPackage();
								
								return; // BREAKS EXECUTION
							}
						}
						
						boolean setSuccessful = setCurrentPackage(
																  
																  (DataComponentPackage)packageCombo.getSelectedItem());
						
						if (!setSuccessful) {
							setLastSelectedPackage();
						}
					} else if (e.getStateChange() == ItemEvent.DESELECTED) {
						lastDeselectedPackage = (DataComponentPackage)e.getItem();
					}
				}
			}
		}
		
		
		/**
		 * Listens to the datahandling layer for changes that could
		 * affect various resources used in the application.
		 */
		private class PreferenceListener implements MedViewPreferenceListener {
			public void userPreferenceChanged(MedViewPreferenceEvent e) {
				if (e.getPreferenceName().equals(DataComponentPackageConstants.INCLUDED_PACKAGE_SYNC_FLAG)) {
					synchronizePackageDataWithPrefs();
					
					synchronizePackageComboWithPackageData();
					
					openFASSAction.setURL(PreferencesModel.instance().getFASSURL());
				}
			}
			
			public void systemPreferenceChanged(MedViewPreferenceEvent e) {
			}
		}
		
		
		/**
		 * Class used for caching previously set examination models
		 * so that they are not read again.
		 */
		private static class Mapping {
			private String location;
			
			private ExaminationModel model;
			
			public Mapping(ExaminationModel model, String location) {
				this.model = model;
				
				this.location = location;
			}
			
			public String getLocation() {
				return location;
			}
			
			public ExaminationModel getModel() {
				return model;
			}
		}
	}
