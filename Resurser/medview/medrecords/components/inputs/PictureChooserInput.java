/*
 * $Id: PictureChooserInput.java,v 1.19 2007/10/17 15:17:04 it2aran Exp $
 *
 * Created on July 4, 2001, 5:17 PM
 *
 */

package medview.medrecords.components.inputs;

import java.io.*;
import java.util.*;

import java.awt.*;
import javax.swing.*;

import java.awt.event.*;
import javax.swing.event.*;

import misc.gui.ExtensionFileFilter;

import medview.medrecords.components.*;
import medview.medrecords.components.inputs.*;
import medview.medrecords.data.*;
import medview.medrecords.events.*;
import medview.medrecords.models.*;
import medview.medrecords.tools.*;

import medview.datahandling.*;
import medview.datahandling.examination.*;
import medview.datahandling.examination.tree.*;
import medview.common.dialogs.MedViewDialogs;
import medview.common.filefilter.DialogTreeFileFilter;

/**
 * A panel for viewing possible images to be added to an examination.
 * @author  d97nix
 * @version
 */
public class PictureChooserInput extends ValueInputComponent implements
	ChangeListener, ActionListener, ImageRemovedListener, MedViewDataConstants
{


    private Set imageChoiceListeners;

	private Vector buttonVector = new Vector();

	private PictureChooserModel mModel;

	private ImageSelectorModel imageSelectorModel;

	private JPanel chooserPanel;

	private JLabel pathLabel; // A Label which shows the current path

	private JButton nextSetButton;

	private JButton prevSetButton;

    private JButton browseButton;

    private FlowLayout chooserPanelLayout;

	private int buttonWidth = ThumbnailCache.BUTTON_WIDTH;

	private int buttonHeight = ThumbnailCache.BUTTON_HEIGHT;

	private int displaySetIndex; // which group of images to show

	private int displaySetSize; // how many should be shown

	private File[] imageFiles; // all available files sorted in time order

	private MedViewDataHandler mVDH = MedViewDataHandler.instance();
	
	private ImageButtonMouseAdapter thisImageButtonMouseAdapter = new ImageButtonMouseAdapter();

	private ImageButton popupMenuSource;

	public PictureChooserInput(PictureChooserModel in_model)
	{
		super(in_model);

		// setup display sets

		displaySetIndex = 0;

		displaySetSize = PreferencesModel.instance().getImageSelectorCount();
		
		// create label containing image path
		
		pathLabel = new JLabel();

		// create the panel containing the pictures

		chooserPanel = new JPanel();
		
		chooserPanelLayout = new FlowLayout(FlowLayout.LEFT, 3, 3); // alignment, hgap, vgap
		
		chooserPanel.setLayout(chooserPanelLayout);

		// layout this panel

		this.setLayout(new BorderLayout());
		
		this.add(chooserPanel, BorderLayout.CENTER);
		
		this.add(pathLabel, BorderLayout.NORTH);

		// 'show previous image set' button

		prevSetButton = new JButton(mVDH.getLanguageString(MedViewLanguageConstants.BUTTON_PREVIOUS_IMAGE_SET));
		
		prevSetButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				showPreviousImageSet(evt);
			}
		});

		// 'show next image set' button

		nextSetButton = new JButton(mVDH.getLanguageString(MedViewLanguageConstants.BUTTON_NEXT_IMAGE_SET));

		nextSetButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				showNextImageSet(evt);
			}
		});

		// show browse folder button

		browseButton = new JButton(mVDH.getLanguageString(MedViewLanguageConstants.BUTTON_BROWSE));

		browseButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				openFolder(evt);
			}
		});

        // create and add button panel

		JPanel buttonPanel = new JPanel();
		
		buttonPanel.add(prevSetButton);
		
		buttonPanel.add(nextSetButton);

        buttonPanel.add(browseButton);

        this.add(buttonPanel, BorderLayout.SOUTH); // Temporary button to test more

		// set the model

		setModel(in_model);
		
		// create listeners
		
		imageChoiceListeners = new HashSet();
		
		imageSelectorModel = new ImageSelectorModel();
	}

	/**
	 * Overridden to enable registering for ImageRemovedEvents.
	 * The idea is that the tab can get the PhotoPanel from its 
	 * ApplicationFrame.
	 */
	public void setParentTab(TabPanel parentTab)
	{
		super.setParentTab(parentTab);
		
		PhotoPanel p = parentTab.getParentPane().getApplicationFrame().getPhotoPanel();
		
		p.registerListeners(this);
	}

	public void addImageChoiceListener(ImageChoiceListener icl)
	{
		imageChoiceListeners.add(icl); // Set can only contain one instance of each, so no need to check
	}

	/**
	 * Fire an image choice event, which means that there has 
	 * been an input choice
	 */
	public void fireImageChoiceEvent(ImageChoiceEvent ice)
	{
		for (Iterator it = imageChoiceListeners.iterator(); it.hasNext(); )
		{
			ImageChoiceListener icl = (ImageChoiceListener)it.next();
			
			icl.imageChosen(ice);
		}
	}

	public void putValue(String value)
	{
		// Does nothing. This should not be possible.
	}

	public void putCustomPreset(String key, String value)
	{
		// empty implementation
	}
    // Should not do anything, but is used for inserting images loaded from file
	public void putPreset(String value)
	{
		// Does nothing - picture choosers don't have presets
		//System.out.println("picChooser putPreset: " + value);
		
		PhotoPanel p = parentTab.getParentPane().getApplicationFrame().getPhotoPanel();

		p.addImage(value);
		
		imageSelectorModel.add(value); 
	}

	/**
	 * Clear out all the pictures
	 */
	public void clearContents()
	{
		mModel.clear(false); // Do not fire event
		
		imageSelectorModel.clear();
		
		viewPicturePath(PreferencesModel.instance().getImageInputLocation());
	}

	public void imageRemoved(ImageRemovedEvent ire)
	{
		System.out.println("imageRemoved");
		
		imageSelectorModel.remove(ire.getImagePath());
		
		fireInputValueChanged();
	}

	/**
	 * setEditable does nothing for this type of component.
	 */
	public void setEditable(boolean e)
	{
	}

	public void setModel(PictureChooserModel in_model)
	{
		detachAsListener();

		mModel = in_model;

		mModel.addChangeListener(this);
	}
	
	public void detachAsListener()
	{		
		if (mModel != null)
		{
			mModel.removeChangeListener(this);
		}
	}

	/**
	 * Reload images from specified path, then rebuild panel
	 */
	public void viewPicturePath(String pathToLoad)
	{
		// clear display sets
		
		resetDisplaySets();

		// clear the image file vector and rebuild it


		File imageSource = new File(pathToLoad);

		if (imageSource.exists())
		{			
			imageFiles = getImageFiles(imageSource);
			
			Arrays.sort(imageFiles, new FileDateComparator());

			if (imageFiles.length == 0)
			{
				pathLabel.setText(imageSource.toString() + " " +
				
					mVDH.getLanguageString(MedViewLanguageConstants.LABEL_IMAGE_DIR_EMPTY_LS_PROPERTY));
			}
			else
			{
				pathLabel.setText("");
			}
		}
		else
		{			
			imageFiles = new File[0];
			
			pathLabel.setText(imageSource.toString() + " " +
					
				mVDH.getLanguageString(MedViewLanguageConstants.LABEL_IMAGE_DIR_NOT_FOUND_LS_PROPERTY));
		}
		
		// rebuild the GUI
		
		rebuild();
	}

	private File[] getImageFiles(File imageSource)
	{
		String[] extensions = {"jpg", "jpeg", "gif", "png"};
		
		ExtensionFileFilter imageFileFilter = new ExtensionFileFilter(extensions, "GIF, PNG and JPEG files", false);

		File[] iFiles = imageSource.listFiles(imageFileFilter);

		if (iFiles == null)
		{
			iFiles = new File[0];
		}
		
		return iFiles;
	}

	public void rebuild()
	{
		rebuildDisplayButtons();

		chooserPanel.removeAll();
		
		int count = buttonVector.size();
		
		for (int i = 0; i < count; i++)
		{
			ImageButton picButton = (ImageButton)buttonVector.get(i);

			chooserPanel.add(picButton);
			
			picButton.addMouseListener(thisImageButtonMouseAdapter); // View full size
			
			picButton.addActionListener(this); // Listen for clicks on this image
		}
		
		updateNavigationButtonStates();
		
		adjustPanelSize();
		
		repaint();
	}

	public PresetModel getPresetModel()
	{
		return new PresetModel(this.getName());
	}

	public String[] getValues()
	{
		return imageSelectorModel.getValues();
	}


	/*
	 * Triggered when the PictureChooserModel has changed
	 * The only metod of the interface ChangeListener
	 */
	public void stateChanged(javax.swing.event.ChangeEvent p1)
	{		
		viewPicturePath(PreferencesModel.instance().getImageInputLocation());
		
		fireInputValueChanged(); // Notify parent that the examination has changed
	}

	public void actionPerformed(ActionEvent e)
	{
		Object source = e.getSource();

		if (source instanceof ImageButton)
		{
			ImageButton imgButton = (ImageButton)source;
			
			String buttonImagePath = imgButton.getImagePath();

			imageSelectorModel.add(buttonImagePath); // keeps selected values for tree-building time

			fireImageChoiceEvent(new ImageChoiceEvent(this, imgButton.getImagePath()));
			
			fireInputValueChanged();
		}
	}

	public void focusInput()
	{
		chooserPanel.requestFocus();
	}

	public void verifyInput()
	{
	}
	
	private void updateNavigationButtonStates()
	{
		prevSetButton.setEnabled(displaySetIndex > 0);

		nextSetButton.setEnabled(imageFiles.length >= ((displaySetIndex + 1) * displaySetSize));
	}

	private void rebuildDisplayButtons()
	{
		int firstDisplayIndex = displaySetIndex * displaySetSize;

		int limit = firstDisplayIndex + displaySetSize;

		int lastFileIndex = imageFiles.length;

		buttonVector.clear();

		if (firstDisplayIndex >= 0 && firstDisplayIndex < lastFileIndex) // this test should not be required
		{
			limit = (limit < lastFileIndex ? limit : lastFileIndex);

			for (int i = firstDisplayIndex; i < limit; i++)
			{
				ImageButton imgButton = new ImageButton(imageFiles[i].getPath());

				buttonVector.add(imgButton);
			}
		}
	}

	private int getButtonWidth()
	{
		return buttonWidth + chooserPanelLayout.getHgap();
	}

	private int getButtonHeight()
	{
		return buttonHeight + chooserPanelLayout.getVgap();
	}

	private void adjustPanelSize()
	{
		int panelWidth = this.getWidth();

		if (panelWidth > 0)
		{
			int buttonPerRow = panelWidth / getButtonWidth();

			int numberOfRows = (buttonVector.size() / buttonPerRow) + 1;

			int newHeight = getButtonHeight() * numberOfRows;

			int newWidth = panelWidth;

			chooserPanel.setPreferredSize(new Dimension(newWidth, newHeight));

		}
		else
		{
			Ut.prt("PhotoPanel.adjustPanelSize(): Bad - scrollpane width zero");

			chooserPanel.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
		}

		chooserPanel.validate();

		this.validate();
	}

	public void popupMenu(int x, int y)
	{
		JPopupMenu popupMenu = new JPopupMenu();

		JMenuItem enlargeItem = new JMenuItem(mVDH.getLanguageString(

			MedViewLanguageConstants.CONTEXT_MENU_VIEW_FULL_SIZE_LS_PROPERTY));

		JMenuItem removeItem = new JMenuItem(mVDH.getLanguageString(

			MedViewLanguageConstants.CONTEXT_MENU_REMOVE_IMAGE_LS_PROPERTY));

		popupMenu.add(enlargeItem);

		enlargeItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				showImageFrame(popupMenuSource);
			}
		});

		removeItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
			}
		});

		popupMenu.show(popupMenuSource, x, y); // Show the popup menu at the location of the mouse pointer

	}

	private void showImageFrame(ImageButton source)
	{
		// new ImageFrame(source).setVisible(true);
		((MedRecordsFrame)parentTab.getParentPane().getApplicationFrame()).showImageDialog(source);
	}

	public Tree getTreeRepresentation(Date date, String pCode)
	{
		return compileSingleTermTreeFromValues(date, pCode);	
	}

	protected Tree compileSingleTermTreeFromValues(Date date, String pCode)
	{
		// obtain the original file names of the images

		String[] values = getValues();

		Map valueTags = imageSelectorModel.getValueTags();

		// create the photo node

		String imageTermName = PreferencesModel.instance().getImageTermName();

		Tree tree = new TreeBranch(imageTermName); // the photo node

		GregorianCalendar cal = new GregorianCalendar();

		cal.setTime(date);

		// add the photos to the photo node

		PatientIdentifier patientIdentifier = new PatientIdentifier(pCode);

		ExaminationIdentifier eID = new MedViewExaminationIdentifier(patientIdentifier, date);

		for (int i = 0; i < values.length; i++)
		{
			String relName = MVD_PICTURES_SUBDIRECTORY + File.separator +

				MVD_PICTURES_SUBDIRECTORY + File.separator + eID +

				"_" + valueTags.get(values[i]) + MVD_IMAGE_FORMAT_FILE_ENDER;

			tree.addChild(new TreeLeaf(relName));
		}

		return tree;
	}

	public void registerListeners(ApplicationFrame frame)
	{
		this.addImageChoiceListener(frame);
	}

	private void resetDisplaySets()
	{
		displaySetIndex = 0;

		displaySetSize = PreferencesModel.instance().getImageSelectorCount();
	}

	private void showNextImageSet(ActionEvent e)
	{
		displaySetIndex++;
		
		rebuild();
	}

	private void showPreviousImageSet(ActionEvent e)
	{
		if (displaySetIndex > 0)
		{
			displaySetIndex--;
		}
		
		rebuild();
	}

	private void openFolder(ActionEvent e)
	{
		//open the file browser
			MedViewDialogs mVD = MedViewDialogs.instance();

            File dir = mVD.createAndShowChooseDirectoryDialog(null);

            if (dir != null)
            {
                try
                {
                    viewPicturePath(dir.getCanonicalPath());
                }
                catch(java.io.IOException ex)
                {
                   System.out.println("IO Exception");
                }

            }
        rebuild();
	}

    private class ImageSelectorModel
	{
		private int num;

		private Map nameMap;

		private java.util.List imagePaths;

		public ImageSelectorModel()
		{
			num = 0;
			
			nameMap = new HashMap();
			
			imagePaths = new ArrayList();
		}

		public void clear()
		{
			num = 0;
			
			nameMap.clear();
			
			imagePaths.clear();
		}

		public void add(String imagePath)
		{
			//System.out.println("PCI add: " + imagePath + "num: " + num);
			if (!nameMap.containsKey(imagePath))
			{
				nameMap.put(imagePath, new Integer(num));
				
				imagePaths.add(imagePath);
				
				num++;
			}
		}

		public void remove(String imagePath)
		{
			nameMap.remove(imagePath);
			
			imagePaths.remove(imagePath);
		}

		public String[] getValues()
		{
			String[] values = new String[imagePaths.size()];

			values = (String[])imagePaths.toArray(values);
			
			return values;
		}

		public Map getValueTags()
		{
			return nameMap;
		}

	}

	private class FileDateComparator implements Comparator
	{
		public int compare(Object o1, Object o2)
		{
			long d1 = ((File)o1).lastModified();
			
			long d2 = ((File)o2).lastModified();

			if (d1 > d2)
			{
				return -1;
			}
			else if (d1 < d2)
			{
				return 1;
			}
			
			return 0;
		}
	}

	private class ImageButtonMouseAdapter extends MouseAdapter
	{
		public void mousePressed(MouseEvent e)
		{
			maybeShowPopup(e);
		}

		public void mouseReleased(MouseEvent e)
		{
			maybeShowPopup(e);
		}

		private void maybeShowPopup(MouseEvent e)
		{
			Object source = e.getSource();

			if (e.isPopupTrigger())
			{
				popupMenuSource = (ImageButton)source;
				
				popupMenu(e.getX(), e.getY());
			}
		}
	}
}
