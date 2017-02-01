/*
 * PhotoPanel.java
 *
 * Created on July 5, 2001, 10:46 PM
 *
 * $Id: PhotoPanel.java,v 1.19 2010/06/28 07:12:39 oloft Exp $
 *
 * $Log: PhotoPanel.java,v $
 * Revision 1.19  2010/06/28 07:12:39  oloft
 * MR 4.5 dev
 *
 * Revision 1.18  2008/07/28 06:56:52  it2aran
 * * Package now includes
 * 	termdefinitions
 * 	termvalues
 * 	database
 * 	template
 * 	translator
 * and can be changed withour restarting (both in MSummary and MRecords
 * * removed more termdefinitions checks (the bug that slowed down MRecords) in MedSummary which should make it load faster
 *
 * Revision 1.17  2008/06/12 09:21:21  it2aran
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
 * Revision 1.16  2007/10/17 15:17:03  it2aran
 * Medrecords 4.3 Beta 1
 *
 * Revision 1.15  2007/01/04 14:31:23  oloft
 * Added scaling of full size images
 *
 * Revision 1.14  2006/09/13 22:00:05  oloft
 * Added Open functionality
 *
 * Revision 1.13  2004/12/08 14:42:51  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.12  2003/12/21 21:54:12  oloft
 * Changed settings, removed DataHandlingHandler
 *
 * Revision 1.11  2003/11/11 13:46:29  oloft
 * Switching mainbranch
 *
 * Revision 1.10.2.7  2003/10/26 21:25:40  oloft
 * Debug edits only
 *
 * Revision 1.10.2.6  2003/10/24 19:58:08  oloft
 * Clean up
 *
 * Revision 1.10.2.5  2003/10/24 19:57:05  oloft
 * Removing image fires an event. Removed PictureChooserModel code
 *
 * Revision 1.10.2.4  2003/10/21 06:25:51  oloft
 * *** empty log message ***
 *
 * Revision 1.10.2.3  2003/09/09 17:13:53  erichson
 * Added PREFERRED_HEIGHT field
 *
 * Revision 1.10.2.2  2003/08/16 14:55:09  erichson
 * Added CVS log field
 *
 *
 */

package medview.medrecords.components;

import java.awt.*;
import java.awt.event.*;
import java.awt.dnd.*;
import java.awt.List;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

import java.util.*;
import java.io.IOException;
import java.io.File;

import javax.swing.*;
import javax.swing.event.*;

import medview.datahandling.*;

import medview.medrecords.data.*;
import medview.medrecords.events.*;

/**
 * @author  Nils, modified by Fredrik
 * @version 1.0
 */
public class PhotoPanel extends JPanel implements ActionListener, ChangeListener, DropTargetListener
{
	public static final int PREFERRED_HEIGHT = 140;

	private JScrollPane scrollPane;

	private JPanel buttonPanel;

	private FlowLayout buttonPanelLayout;

	private Vector imageButtons = new Vector();

	private Set imageRemovedListeners = new HashSet();

	private ImageButtonMouseAdapter thisImageButtonMouseAdapter = new ImageButtonMouseAdapter();

	private ImageButton popupMenuSource;

	private int buttonWidth = ThumbnailCache.BUTTON_WIDTH;

	private int buttonHeight = ThumbnailCache.BUTTON_HEIGHT;

	private ApplicationFrame applicationFrame = null;

	private MedViewDataHandler mVDH = MedViewDataHandler.instance();

    private List dropList = new List();

    
    private DropTarget dropTarget = new DropTarget(this, DnDConstants.ACTION_COPY, this);

    
    // Images cannot be deleted from opened examinations
	private boolean removeDisabled;

	public PhotoPanel()
	{
		super();

		removeDisabled = false;
		
		// create the button panel

		buttonPanel = new JPanel();

		buttonPanelLayout = new FlowLayout(FlowLayout.LEFT, 3, 3); // alignment, hgap, vgap

		buttonPanel.setLayout(buttonPanelLayout);

		// create the scroll pane

		scrollPane = new JScrollPane(buttonPanel);

		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		// take all image buttons and place them in the button panel

		rebuild();

		// add the wrapping scrollpane containing the button panel to this panel

		this.setLayout(new BorderLayout());

		this.add(scrollPane, BorderLayout.CENTER);

		setPreferredSize(new Dimension(PREFERRED_HEIGHT, PREFERRED_HEIGHT)); // x-axis should not matter (stretched)

		setMinimumSize(new Dimension(PREFERRED_HEIGHT, PREFERRED_HEIGHT)); // x-axis should not matter (stretched)



    }

    public void dragDropEnd(DragSourceDropEvent e) {}

    public void dragEnter(DragSourceDragEvent e) {}

    public void dragExit(DragSourceEvent e) {}

    public void dragOver(DragSourceDragEvent e) {}

    public void dropActionChanged(DragSourceDragEvent e) {}

    public void dragEnter(DropTargetDragEvent e) {}

    public void dragExit(DropTargetEvent e) {}

    public void dragOver(DropTargetDragEvent e)
    {
        //Due to various design issues the drag'n'drop is disabled for now
        //and therefore we just reject the drag event
        e.rejectDrag();
        
        
        /*
        //show dragndrop icon only if it's an image
        if(e.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
        {
            try
            {
                Transferable tr = e.getTransferable();
                //e.acceptDrop (DnDConstants.ACTION_COPY);
                java.util.List fileList = (java.util.List)tr.getTransferData(DataFlavor.javaFileListFlavor);
                Iterator iterator = fileList.iterator();
                boolean hasImage = false;
                do
                {
                    File file = (File)iterator.next();
                    //check if there's an image among the ones dragged
                    if(isValidImage(file.getCanonicalPath()))
                    {
                        hasImage=true;
                    }
                }
                while (iterator.hasNext() && !hasImage);
                //if there is no image in all the drag files we reject it
                if(!hasImage)
                {
                    e.rejectDrag();
                }
            }
            catch (IOException io)
            {
                io.printStackTrace();
                e.rejectDrag();
            }
            catch (UnsupportedFlavorException ufe)
            {
                ufe.printStackTrace();
                e.rejectDrag();
            }
        }
        else
        {
            e.rejectDrag();
        }
        */
        
    }
    
    public void dropActionChanged(DropTargetDragEvent e) {}

    public void drop(DropTargetDropEvent e)
    {
        try {
            //if the drop object is a file
            if(e.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
            {
                Transferable tr = e.getTransferable();
                e.acceptDrop (DnDConstants.ACTION_COPY);
                java.util.List fileList = (java.util.List)tr.getTransferData(DataFlavor.javaFileListFlavor);
                Iterator iterator = fileList.iterator();
                while (iterator.hasNext())
                {
                    File file = (File)iterator.next();
                    //only add images
                    if(isValidImage(file.getCanonicalPath()))
                    {
                        addImage(file.getCanonicalPath());
                        
                        //TODO also add image to the internal data
                    }
                }
                e.getDropTargetContext().dropComplete(true);
            }
            else
            {
                e.rejectDrop();
            }
        }
        catch (IOException io) {
            io.printStackTrace();
            e.rejectDrop();
        }
        catch (UnsupportedFlavorException ufe) {
            ufe.printStackTrace();
            e.rejectDrop();
        }
    }


    public boolean isValidImage(String path)
    {
        int dotIdx = path.lastIndexOf(".");
        String extension = path.substring(dotIdx+1,path.length());
        return (
           extension.equalsIgnoreCase("jpg")  ||
           extension.equalsIgnoreCase("jpeg") ||
           extension.equalsIgnoreCase("png")  ||
           extension.equalsIgnoreCase("gif"));
    }


    public void setApplicationFrame(ApplicationFrame af)
	{
		applicationFrame = af;
	}

	public ApplicationFrame getApplicationFrame()
	{
		return applicationFrame;
	}
	public void setRemoveDisabled(boolean b) {
		removeDisabled = b;
	}
	
	private int getButtonWidth()
	{
		return buttonWidth + buttonPanelLayout.getHgap();
	}

	private int getButtonHeight()
	{
		return buttonHeight + buttonPanelLayout.getVgap();
	}

	public void removeImageButton(ImageButton ib)
	{
		imageButtons.remove(ib);

		buttonPanel.remove(ib);

		buttonPanel.revalidate();

		buttonPanel.repaint();

		fireImageRemovedEvent(new ImageRemovedEvent(this, ib.getImagePath()));

		adjustPanelSize();
	}

	public void addImage(String path)
	{		
		ImageButton imgButton = new ImageButton(path); // caches the image data in the button
		
		if (imageButtons.contains(imgButton))
		{
			return; // do nothing - the image is already there
		}
		else
		{
			buttonWidth = (int)imgButton.getPreferredSize().getWidth();
	
			buttonHeight = (int)imgButton.getPreferredSize().getHeight();
	
			imageButtons.add(imgButton);
	
			buttonPanel.add(imgButton);
	
			imgButton.addMouseListener(thisImageButtonMouseAdapter);
	
			imgButton.addActionListener(this);
	
			adjustPanelSize();
	
			buttonPanel.revalidate();			
		}
	}

	public void clear()
	{
		imageButtons.removeAllElements();

		buttonPanel.removeAll();

		buttonPanel.repaint();
	}

	public void rebuild()
	{
		buttonPanel.removeAll();

		for (Iterator it = imageButtons.iterator(); it.hasNext(); )
		{
			ImageButton nextButton = (ImageButton)it.next();

			buttonPanel.add(nextButton);

			buttonWidth = (int)nextButton.getPreferredSize().getWidth();

			buttonHeight = (int)nextButton.getPreferredSize().getHeight();

			nextButton.addActionListener(this);

			nextButton.addMouseListener(thisImageButtonMouseAdapter);
		}

		adjustPanelSize();

		buttonPanel.revalidate();
	}

	public void stateChanged(javax.swing.event.ChangeEvent ev)
	{
		rebuild();
	}

	public void actionPerformed(java.awt.event.ActionEvent e)
	{
		Object actionSource = e.getSource();

		if (actionSource instanceof ImageButton)
		{
			showImageFrame((ImageButton)actionSource);
		}
	}

	public void popupMenu(int x, int y)
	{
		JPopupMenu popupMenu = new JPopupMenu();

		JMenuItem enlargeItem = new JMenuItem(mVDH.getLanguageString(
		
			MedViewLanguageConstants.CONTEXT_MENU_VIEW_FULL_SIZE_LS_PROPERTY));

		JMenuItem removeItem = new JMenuItem(mVDH.getLanguageString(
		
			MedViewLanguageConstants.CONTEXT_MENU_REMOVE_IMAGE_LS_PROPERTY));

		popupMenu.add(enlargeItem);

		// For now images cannot be removed from opened examinations
		if (!removeDisabled) {
		popupMenu.add(removeItem);
		}

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
				Object actionSource = e.getSource();

				removeImageButton(popupMenuSource);
			}
		});

		popupMenu.show(popupMenuSource, x, y); // Show the popup menu at the location of the mouse pointer
	}

	public void adjustPanelSize()
	{
		if (scrollPane == null)
		{
			return;
		}

		int scrollPaneWidth = scrollPane.getWidth();

		if (scrollPaneWidth > 0)
		{
			int buttonPerRow = scrollPaneWidth / getButtonWidth();

			int numberOfRows = (imageButtons.size() / buttonPerRow) + 1;

			int newHeight = getButtonHeight() * numberOfRows;

			int newWidth = scrollPaneWidth;

			buttonPanel.setPreferredSize(new Dimension(newWidth, newHeight));
		}
		else
		{
			buttonPanel.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
		}

		buttonPanel.validate();
	}

	private void showImageFrame(ImageButton source)
	{
		//new ImageFrame(source).setVisible(true);
		((MedRecordsFrame)applicationFrame).showImageDialog(source);
	}
	
	
	// OBTAINING IMAGE BYTE ARRAYS
	
	public byte[][] getImageByteArrays()
	{
		byte[][] retArr = new byte[imageButtons.size()][];
		
		Enumeration enm = imageButtons.elements();
		
		int counter = 0;
		
		while (enm.hasMoreElements())
		{
			retArr[counter++] = ((ImageButton)enm.nextElement()).getImageData();
		}
		
		return retArr;
	}


	// LISTENERS

	public void registerListeners(ImageRemovedListener irl)
	{
		this.addImageRemovedListener(irl);
	}

	public void addImageRemovedListener(ImageRemovedListener irl)
	{
		imageRemovedListeners.add(irl); // set can only contain one instance of each, so no need to check
	}

	public void fireImageRemovedEvent(ImageRemovedEvent ire)
	{
		for (Iterator it = imageRemovedListeners.iterator(); it.hasNext(); )
		{
			ImageRemovedListener irl = (ImageRemovedListener)it.next();

			irl.imageRemoved(ire);
		}
	}

	
	// INTERNAL MOUSE ADAPTER CLASS

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
