/*
 * @(#)MedViewMediaHandler.java
 *
 * $Id: MedViewMediaHandler.java,v 1.28 2010/06/28 07:12:39 oloft Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.datahandling;

import java.awt.image.*;

import java.net.*;

import java.util.*;

import javax.imageio.*;

import javax.swing.*;

/**
 * Handles everything related to media resources, such as images
 * and sound. The user specifies one of the constants found in the
 * MedViewMediaConstants interface to the methods (the user will
 * access the MedViewDataHandler facade's exact method replicas, and
 * the MVDH will delegate the fulfillment to this class) and will
 * get a media resource returned.<br>
 * <br>
 * The resources are located by means of calling the system
 * class loader and asking it to locate the resource based on a
 * path string hard-wired in this class. Thus, the resource needs to
 * be found (for instance, by using the -cp option when running the
 * application and supplying a jar-file containing the resources) if
 * the returned resource should be non-null.<br>
 * <br>
 * @author Fredrik Lindahl
 * @version 1.0
 */
public class MedViewMediaHandler implements MedViewMediaConstants
{
	// IMAGE ICON / IMAGE OBTAINAL

	/**
	 * Obtains the ImageIcon object for the specified
	 * media constant id.
	 * @param id String
	 * @return ImageIcon
	 */
	public ImageIcon getImageIcon( String id )
	{
		return new ImageIcon((URL)mediaLoc.get(id));
	}

	/**
	 * Obtains the Image object for the specified media
	 * constant id.
	 * @param id String
	 * @return Image
	 */
	public BufferedImage getImage(String id)
	{
		try
		{
			return ImageIO.read((URL)mediaLoc.get(id));
		}
		catch (Exception exc)
		{
			exc.printStackTrace();

			System.exit(1); // fatal error

			return null;
		}
	}


	// CONSTRUCTOR AND RELATED METHODS

	private void initIconLocations( )
	{
		ClassLoader cL = ClassLoader.getSystemClassLoader();

		String preFix = MedViewDataSettingsHandler.instance().getResourcePrefix();

		// size-abstract image icons

		mediaLoc.put(ABOUT_MEDSERVER_IMAGE_ICON, cL.getResource(preFix + "images/275x200aboutMedServer.png"));

		mediaLoc.put(ABOUT_MEDSUMMARY_IMAGE_ICON, cL.getResource(preFix + "images/275x200aboutMedSummary.png"));

		mediaLoc.put(ABOUT_MEDVIEW_IMAGE_ICON, cL.getResource(preFix + "images/390x133aboutMedView.png"));

		mediaLoc.put(ABOUT_SUMMARYCREATOR_IMAGE_ICON, cL.getResource(preFix + "images/275x200aboutSummaryCreator.png"));

		mediaLoc.put(FOLKTANDVARDEN_SIGNATURE_IMAGE_ICON, cL.getResource(preFix + "images/1507x356folktandvarden.jpg"));

		mediaLoc.put(FRAME_IMAGE_ICON, cL.getResource(preFix + "images/16x16frame.png"));

		mediaLoc.put(LEFT_ARROW_IMAGE_ICON,  cL.getResource(preFix + "images/4x7leftArrow.gif"));

		mediaLoc.put(NO_THUMBNAIL_IMAGE_ICON,  cL.getResource(preFix + "images/120x90noThumbnailImage.jpg")); // no 'thumb' image

		mediaLoc.put(ODONTOLOGY_SIGNATURE_IMAGE_ICON,  cL.getResource(preFix + "images/140x139odontologySignature.gif"));

		mediaLoc.put(RIGHT_ARROW_IMAGE_ICON,  cL.getResource(preFix + "images/4x7rightArrow.gif"));

		mediaLoc.put(SPLASH_MEDIMAGER_IMAGE_ICON, cL.getResource(preFix + "images/412x218splashMedImager.jpg"));

		mediaLoc.put(SPLASH_MEDRECORDS_IMAGE_ICON, cL.getResource(preFix + "images/412x218splashMedRecords.jpg"));

		mediaLoc.put(SPLASH_MEDSERVER_IMAGE_ICON, cL.getResource(preFix + "images/412x218splashMedServer.jpg"));

		mediaLoc.put(SPLASH_MEDSUMMARY_IMAGE_ICON,  cL.getResource(preFix + "images/412x218splashMedSummary.jpg"));

		mediaLoc.put(SPLASH_SUMMARYCREATOR_IMAGE_ICON,  cL.getResource(preFix + "images/412x218splashSummaryCreator.jpg"));

		mediaLoc.put(TREES_SMALL_ICON, cL.getResource(preFix + "images/25x16treesSmall.png"));

		mediaLoc.put(TREES_LARGE_ICON, cL.getResource(preFix + "images/25x16treesLarge.png"));

		mediaLoc.put(TWO_RIGHT_ARROWS_IMAGE_ICON,  cL.getResource(preFix + "images/9x7twoRightArrows.gif"));

		// 14x14 image icons

		mediaLoc.put(PLAY_IMAGE_ICON_14, cL.getResource(preFix + "images/14x14play.gif"));

		mediaLoc.put(RECORD_IMAGE_ICON_14, cL.getResource(preFix + "images/14x14record.gif"));

		mediaLoc.put(STOP_IMAGE_ICON_14, cL.getResource(preFix + "images/14x14stop.gif"));

		// 16x16 image icons (small toolbar / menu)

		mediaLoc.put(ADD_PACKAGE_IMAGE_ICON_16, cL.getResource(preFix + "images/16x16addPackage.gif"));

		mediaLoc.put(CONNECT_IMAGE_ICON_16, cL.getResource(preFix + "images/16x16connect.png"));

		mediaLoc.put(COPY_IMAGE_ICON_16, cL.getResource(preFix + "images/16x16copy.gif"));

		mediaLoc.put(CUT_IMAGE_ICON_16, cL.getResource(preFix + "images/16x16cut.gif"));

		mediaLoc.put(DISCONNECT_IMAGE_ICON_16, cL.getResource(preFix + "images/16x16disconnect.png"));

		mediaLoc.put(EDIT_PACKAGE_IMAGE_ICON_16, cL.getResource(preFix + "images/16x16editPackage.gif"));

		mediaLoc.put(EMPTY_LIGHT_IMAGE_ICON_16, cL.getResource(preFix + "images/16x16emptyLight.png"));

		mediaLoc.put(ENLARGE_IMAGE_ICON_16, cL.getResource(preFix + "images/16x16enlargeImage.png"));

		mediaLoc.put(GREEN_LIGHT_IMAGE_ICON_16, cL.getResource(preFix + "images/16x16greenLight.png"));

		mediaLoc.put(INFORMATION_IMAGE_ICON_16, cL.getResource(preFix + "images/16x16information.gif"));

		mediaLoc.put(JOURNAL_IMAGE_ICON_16, cL.getResource(preFix + "images/16x16journal.gif"));

		mediaLoc.put(NEW_FOLDER_IMAGE_ICON_16, cL.getResource(preFix + "images/16x16newFolder.gif"));

		mediaLoc.put(NEW_IMAGE_ICON_16, cL.getResource(preFix + "images/16x16new.gif"));

		mediaLoc.put(OPEN_IMAGE_ICON_16, cL.getResource(preFix + "images/16x16open.gif"));

		mediaLoc.put(PASTE_IMAGE_ICON_16, cL.getResource(preFix + "images/16x16paste.gif"));

		mediaLoc.put(PREFERENCES_IMAGE_ICON_16, cL.getResource(preFix + "images/16x16preferences.png"));

		mediaLoc.put(PREVIEW_IMAGE_ICON_16, cL.getResource(preFix + "images/16x16preview.gif"));

		mediaLoc.put(PRINT_IMAGE_ICON_16, cL.getResource(preFix + "images/16x16print.gif"));

		mediaLoc.put(RED_LIGHT_IMAGE_ICON_16, cL.getResource(preFix + "images/16x16redLight.png"));

		mediaLoc.put(REFRESH_IMAGE_ICON_16, cL.getResource(preFix + "images/16x16refresh.gif"));

		mediaLoc.put(REMOVE_IMAGE_ICON_16, cL.getResource(preFix + "images/16x16remove.gif"));

		mediaLoc.put(REMOVE_PACKAGE_IMAGE_ICON_16, cL.getResource(preFix + "images/16x16removePackage.gif"));

		mediaLoc.put(SAVE_IMAGE_ICON_16, cL.getResource(preFix + "images/16x16save.gif"));

		mediaLoc.put(SHARE_FOLDER_GLOBALLY_IMAGE_ICON_16, cL.getResource(preFix + "images/16x16shareFolderGlobally.png"));

		mediaLoc.put(SHARE_FOLDER_IMAGE_ICON_16, cL.getResource(preFix + "images/16x16shareFolder.png"));

		// 18x18 image icons

		mediaLoc.put(SEARCH_IMAGE_ICON_18, cL.getResource(preFix + "images/18x18search.gif"));

		// 24x24 image icons (large toolbar / menu)

		mediaLoc.put(BOLD_IMAGE_ICON_24,  cL.getResource(preFix + "images/24x24bold.gif"));

		mediaLoc.put(CHOOSE_COLOR_IMAGE_ICON_24, cL.getResource(preFix + "images/24x24changeColor.gif"));

		mediaLoc.put(COPY_IMAGE_ICON_24, cL.getResource(preFix + "images/24x24copy.gif"));

		mediaLoc.put(CUT_IMAGE_ICON_24, cL.getResource(preFix + "images/24x24cut.gif"));

		mediaLoc.put(ENLARGE_IMAGE_ICON_24, cL.getResource(preFix + "images/24x24enlargeImage.png"));
		
		mediaLoc.put(EXPORT_IMAGE_ICON_24, cL.getResource(preFix + "images/24x24export.gif"));
		
		mediaLoc.put(IMPORT_IMAGE_ICON_24, cL.getResource(preFix + "images/24x24import.gif"));

		mediaLoc.put(INFORMATION_IMAGE_ICON_24, cL.getResource(preFix + "images/24x24information.gif"));

		mediaLoc.put(INVOKE_FASS_IMAGE_ICON_24, cL.getResource(preFix + "images/24x24invokeFASS.gif"));

		mediaLoc.put(ITALIC_IMAGE_ICON_24,  cL.getResource(preFix + "images/24x24italic.gif"));

		mediaLoc.put(JOURNAL_IMAGE_ICON_24, cL.getResource(preFix + "images/24x24journal.png"));

		mediaLoc.put(JUSTIFY_CENTER_IMAGE_ICON_24,  cL.getResource(preFix + "images/24x24justifyCenter.gif"));

		mediaLoc.put(JUSTIFY_LEFT_IMAGE_ICON_24,  cL.getResource(preFix + "images/24x24justifyLeft.gif"));

		mediaLoc.put(JUSTIFY_RIGHT_IMAGE_ICON_24,  cL.getResource(preFix + "images/24x24justifyRight.gif"));

		mediaLoc.put(LINK_IMAGE_ICON_24,  cL.getResource(preFix + "images/24x24link.gif"));

		mediaLoc.put(NEW_DAYNOTE_IMAGE_ICON_24, cL.getResource(preFix + "images/24x24newDaynote.gif"));

		mediaLoc.put(NEW_IMAGE_ICON_24,  cL.getResource(preFix + "images/24x24new.gif"));

		mediaLoc.put(NEW_FOLDER_IMAGE_ICON_24, cL.getResource(preFix + "images/24x24newFolder.png"));

		mediaLoc.put(NEW_PATIENT_IMAGE_ICON_24, cL.getResource(preFix + "images/24x24newPatient.gif"));

		mediaLoc.put(NEW_SECTION_IMAGE_ICON_24,  cL.getResource(preFix + "images/24x24newSection.gif"));

		mediaLoc.put(OPEN_IMAGE_ICON_24,  cL.getResource(preFix + "images/24x24open.gif"));

		mediaLoc.put(PASTE_IMAGE_ICON_24,  cL.getResource(preFix + "images/24x24paste.gif"));

		mediaLoc.put(PDA_IMAGE_ICON_24,  cL.getResource(preFix + "images/24x24pda.gif"));
		
		mediaLoc.put(PREFERENCES_IMAGE_ICON_24,  cL.getResource(preFix + "images/24x24preferences.gif"));

		mediaLoc.put(PREVIEW_IMAGE_ICON_24,  cL.getResource(preFix + "images/24x24preview.gif"));

		mediaLoc.put(PRINT_IMAGE_ICON_24,  cL.getResource(preFix + "images/24x24print.gif"));

		mediaLoc.put(REFRESH_IMAGE_ICON_24, cL.getResource(preFix + "images/24x24refresh.gif"));

		mediaLoc.put(REMOVE_IMAGE_ICON_24, cL.getResource(preFix + "images/24x24remove.gif"));

		mediaLoc.put(REMOVE_SECTION_IMAGE_ICON_24,  cL.getResource(preFix + "images/24x24removeSection.gif"));

		mediaLoc.put(SAVE_IMAGE_ICON_24,  cL.getResource(preFix + "images/24x24save.gif"));
		
		mediaLoc.put(SAVE_AND_CLOSE_IMAGE_ICON_24,  cL.getResource(preFix + "images/24x24saveAndClose.gif"));
		
		mediaLoc.put(SAVE_AS_IMAGE_ICON_24,  cL.getResource(preFix + "images/24x24saveAs.gif"));

		mediaLoc.put(SHARE_FOLDER_GLOBALLY_IMAGE_ICON_24, cL.getResource(preFix + "images/24x24shareFolderGlobally.png"));
		
		mediaLoc.put(SHOW_GRAPH_IMAGE_ICON_24, cL.getResource(preFix + "images/24x24showGraph.png"));

		mediaLoc.put(SHARE_FOLDER_IMAGE_ICON_24, cL.getResource(preFix + "images/24x24shareFolder.png"));

		mediaLoc.put(STRIKETHROUGH_IMAGE_ICON_24,  cL.getResource(preFix + "images/24x24strikethrough.gif"));

		mediaLoc.put(SUBSCRIPT_IMAGE_ICON_24,  cL.getResource(preFix + "images/24x24subscript.gif"));

		mediaLoc.put(SUPERSCRIPT_IMAGE_ICON_24,  cL.getResource(preFix + "images/24x24superscript.gif"));

		mediaLoc.put(UNDERLINE_IMAGE_ICON_24,  cL.getResource(preFix + "images/24x24underline.gif"));

		// 40x40 IMAGE ICONS

		mediaLoc.put(ERROR_IMAGE_ICON_40, cL.getResource(preFix + "images/40x40error.png"));

		mediaLoc.put(INFORMATION_IMAGE_ICON_40, cL.getResource(preFix + "images/40x40information.png"));

		mediaLoc.put(QUESTION_IMAGE_ICON_40,  cL.getResource(preFix + "images/40x40question.png"));
	}

	public MedViewMediaHandler( )
	{
		initIconLocations();
	}

	private HashMap mediaLoc = new HashMap();

}
