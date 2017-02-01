/*
 * @(#)MedSummaryImagePanel.java
 *
 * $Id: MedSummaryImagePanel.java,v 1.20 2008/07/29 09:31:59 it2aran Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.medsummary.view;

import medview.common.dialogs.*;
import medview.datahandling.*;
import medview.medsummary.model.*;
import medview.medsummary.model.exceptions.CouldNotRetrieveImageException;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.*;

import java.io.*;

import java.util.*;

import javax.imageio.*;

import javax.swing.*;
import javax.swing.border.*;

public class MedSummaryImagePanel extends JPanel implements MedSummaryActions, MedViewLanguageConstants, MedViewMediaConstants
{

	// MEMBERS

	private MedViewDataHandler mVDH;

	private JPanel imagePanel;

	private MedSummaryFrame mediator;

	private JScrollPane jSP;

	private int imageHeight;

	private int imageWidth;

	// CONSTRUCTOR(S) AND RELATED METHODS

	public MedSummaryImagePanel(MedSummaryFrame mediator)
	{
		this.mVDH = MedViewDataHandler.instance();

		this.mediator = mediator;

		// dimensions

		JLabel dummyLabel = new JLabel();

		Border outer = BorderFactory.createEmptyBorder(5,5,5,5);

		Border inner = BorderFactory.createEtchedBorder();

		dummyLabel.setBorder(BorderFactory.createCompoundBorder(outer, inner));

		Border dummyBorder = dummyLabel.getBorder();

		Insets bInsets = dummyBorder.getBorderInsets(dummyLabel);

		int borderHeight = bInsets.top + bInsets.bottom;

		int borderWidth = bInsets.left + bInsets.right;

		int iconHeight = ExaminationImageModel.ICON_HEIGHT;

		int iconWidth = ExaminationImageModel.ICON_WIDTH;

		this.imageHeight = iconHeight + borderHeight;

		this.imageWidth = iconWidth + borderWidth;

		// panel

		setLayout(new BorderLayout());

		setMinimumSize(new Dimension(imageWidth + 6,imageHeight + 6));

		imagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));

		imagePanel.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));

		imagePanel.setOpaque(false);

		jSP = new JScrollPane(imagePanel);

		jSP.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));

		jSP.setOpaque(false);

		add(jSP, BorderLayout.CENTER);
	}


	// REMOVE ALL IMAGES FROM PANEL

	public void clearImages()
	{
		imagePanel.removeAll();

		imagePanel.setPreferredSize(calcPrefSize(0));

		imagePanel.revalidate();

		imagePanel.repaint();
	}


	// DISPLAY IMAGES

	public void displayImages(ExaminationModel[] models)
	{
		Vector iModels = new Vector();

		for (int ctr1=0; ctr1<models.length; ctr1++)
		{
			ExaminationImageModel[] currIModels = models[ctr1].getImageModels();

			for (int ctr2=0; ctr2<currIModels.length; ctr2++)
			{
				iModels.add(currIModels[ctr2]);
			}
		}

		imagePanel.removeAll();

		imagePanel.setPreferredSize(calcPrefSize(iModels.size()));

		Enumeration enm = iModels.elements();

		while(enm.hasMoreElements())
		{
			ExaminationImageModel currImageModel = (ExaminationImageModel) enm.nextElement();

			imagePanel.add(new ImageContainer(currImageModel));
		}

		imagePanel.revalidate();

		imagePanel.repaint();

		/* NOTE: * Revalidation only recalculates the needed preferred sizes for the components to accomodate the
		 * eventual new contents, but if the new size needed is the same, or smaller, the revalidate method will
		 * not cause a resize() of the component and will thus the component will not repaint.
		 */
	}

	private Dimension calcPrefSize(int nrOfImages)
	{
		/* NOTE: The scrollbar's width can vary if the user has chosen
		 * another look and feel, thus it is instantiated here to find
		 * out a scrollbars width.
		 */

		JScrollBar dummySB = new JScrollBar(JScrollBar.VERTICAL);

		Dimension prefDim = dummySB.getPreferredSize();

		int sBWidth = prefDim.width;

		int availableWidth = jSP.getWidth();

		int imPerRow = availableWidth / imageWidth;

		int rowsReq = -1;

		if (nrOfImages == 0)
		{
			rowsReq = 1;
		}
		else
		{
			rowsReq = (nrOfImages / (imPerRow + 1)) + 1;
		}

		int prefWidth = -1;

		if (rowsReq == 1)
		{
			prefWidth = jSP.getWidth();
		}
		else
		{
			prefWidth = jSP.getWidth() - sBWidth;
		}

		int prefHeight = rowsReq * imageHeight;

		return new Dimension(prefWidth, prefHeight);
	}


	// IMAGE CONTAINER LABEL

	private class ImageContainer extends JLabel implements ClipboardOwner
	{
		// IMAGE CONTAINER LABEL - MEMBERS

		private String patientCode;

		private String patientID;

		private String examinationDate;

		private PatientModel pModel;

		private ExaminationModel eModel;

		private ExaminationImageModel model;

		private Action viewDetailedAction = new ViewDetailedAction();

		private Action copyDetailedAction = new CopyAction();

		private final Color infoPanelColor = new Color(0,0,0,50);

		private int imageStatus = IMAGE_NOT_LOADED_YET;

		private static final int IMAGE_NOT_LOADED_YET = 0;

		private static final int IMAGE_LOADED_CORRECTLY = 1;

		private static final int IMAGE_DID_NOT_LOAD = 2;

		// IMAGE CONTAINER LABEL - CONSTRUCTOR(S) AND RELATED METHODS

		public ImageContainer(final ExaminationImageModel model)
		{
			this.model = model;

			// obtain references from model

			eModel = model.getExaminationModel();

			pModel = eModel.getPatientModel();

			patientCode = pModel.getPID().getPCode();

			patientID = pModel.getPID().toString();

			examinationDate = eModel.getDateString();

			// layout image container

			setLayout(new BorderLayout());

			Border outer = BorderFactory.createEmptyBorder(5,5,5,5);

			Border inner = BorderFactory.createEtchedBorder();

			setBorder(BorderFactory.createCompoundBorder(outer, inner));

			JPanel infoPanel = new JPanel(new GridLayout(2,1,1,2));

			infoPanel.setBackground(infoPanelColor);

			infoPanel.setOpaque(true);

			JLabel pidLabel = new JLabel(patientID);

			pidLabel.setOpaque(false);

			pidLabel.setForeground(Color.white);

			pidLabel.setHorizontalAlignment(SwingConstants.CENTER);

			infoPanel.add(pidLabel);

			JLabel dateLabel = new JLabel(examinationDate);

			dateLabel.setOpaque(false);

			dateLabel.setForeground(Color.white);

			dateLabel.setHorizontalAlignment(SwingConstants.CENTER);

			infoPanel.add(dateLabel);

			add(Box.createGlue(), BorderLayout.CENTER);

			add(infoPanel, BorderLayout.SOUTH);

			setIcon(model.getImageIcon());

			// add popup listener if valid image

			if (containsValidImage())
			{
				this.addMouseListener(new MouseAdapter()
				{
					public void mouseClicked(MouseEvent e)
					{
						if (e.getClickCount() == 2)
						{
							viewDetailedAction.actionPerformed(null);
						}
					}

					public void mouseReleased(MouseEvent e)
					{
						if (e.isPopupTrigger())
						{
							JPopupMenu popupMenu = new JPopupMenu();

							popupMenu.add(viewDetailedAction);

							popupMenu.addSeparator();

							popupMenu.add(copyDetailedAction);

							popupMenu.show(ImageContainer.this, e.getX(), e.getY());
						}
					}
				});
			}

			// add DnD support if valid image

			if (containsValidImage())
			{
				DragSource.getDefaultDragSource().createDefaultDragGestureRecognizer(ImageContainer.this,

					DnDConstants.ACTION_COPY, new DragGestureListener()
					{
						public void dragGestureRecognized(DragGestureEvent dge)
						{
							dge.startDrag(null, constructSystemTransferable());
						}
					});
			}
		}

		// IMAGE CONTAINER LABEL - OBTAINING THE IMAGE

		public Image getImage()
		{
			if (imageStatus == IMAGE_DID_NOT_LOAD)
			{
				return null;
			}
			else
			{
				try
				{
					Image image = model.getFullImage(); // -> CouldNotRetrieveImageException

					imageStatus = IMAGE_LOADED_CORRECTLY;

					return image;
				}
				catch (CouldNotRetrieveImageException exc)
				{
					imageStatus = IMAGE_DID_NOT_LOAD;

					return null;
				}
			}
		}

		// IMAGE CONTAINER LABEL - WHETHER OR NOT A VALID IMAGE IS DISPLAYED

		public boolean containsValidImage()
		{
			switch (imageStatus)
			{
				case IMAGE_NOT_LOADED_YET:
				{
					return (getImage() != null);
				}
				case IMAGE_LOADED_CORRECTLY:
				{
					return true;
				}
				case IMAGE_DID_NOT_LOAD:
				{
					return false;
				}
			}

			return false; // will never get here
		}

		// IMAGE CONTAINER LABEL - CLIPBOARDOWNER INTERFACE

		public void lostOwnership(Clipboard clipboard, Transferable contents)
		{
			// deliberately a no-op method
		}

		// IMAGE CONTAINER LABEL - MISC METHODS

		private Transferable constructSystemTransferable()
		{
			return new Transferable()
			{
				private File tempFile = null; // for performance reasons

				public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
				{
					if (flavor.equals(DataFlavor.imageFlavor)) // an image transfer
					{
						try
						{
							return model.getFullImage(); // -> CouldNotRetrieveImageException
						}
						catch (CouldNotRetrieveImageException exc)
						{
							throw new IOException(exc.getMessage());
						}
					}
					else if (flavor.equals(DataFlavor.javaFileListFlavor)) // a file transfer
					{
						try
						{
							Vector retVect = new Vector();

							if (tempFile == null)
							{
								tempFile = File.createTempFile(

									model.getExaminationImage().getExaminationIdentifier().toString(), ".jpg");

								ImageIO.write(model.getFullImage(), "jpg", tempFile); // -> CouldNotRetrieveImageException
							}

							retVect.add(tempFile);

							return retVect;
						}
						catch (CouldNotRetrieveImageException exc)
						{
							throw new IOException(exc.getMessage());
						}
					}
					else
					{
						throw new UnsupportedFlavorException(flavor);
					}
				}

				public DataFlavor[] getTransferDataFlavors()
				{
					return new DataFlavor[] { DataFlavor.imageFlavor, DataFlavor.javaFileListFlavor };
				}

				public boolean isDataFlavorSupported(DataFlavor flavor)
				{
					return (flavor.equals(DataFlavor.imageFlavor) || flavor.equals(DataFlavor.javaFileListFlavor));
				}
			};
		}

		// IMAGE CONTAINER LABEL - VIEW DETAILED IMAGE ACTION

		private class ViewDetailedAction extends AbstractAction
		{
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					Image fullImage = model.getFullImage(); // This is a buffered Image
										
					if (fullImage.getWidth(null) > MedSummaryConstants.DEFAULT_FULL_IMAGE_MAX_SIZE) {
							fullImage = fullImage.getScaledInstance(MedSummaryConstants.DEFAULT_FULL_IMAGE_MAX_SIZE, -1, Image.SCALE_DEFAULT);
					}

					MedViewDialogs.instance().createAndShowImageDialog(mediator.getParentFrame(),

						patientID + " (" + patientCode + ") " + "- " + examinationDate, fullImage); // -> CouldNotRetrieveImageException
				}
				catch (CouldNotRetrieveImageException exc)
				{
					MedViewDialogs.instance().createAndShowErrorDialog(mediator.getParentFrame(), exc.getMessage());
				}
			}

			public ViewDetailedAction()
			{
				putValue(Action.NAME, mVDH.getLanguageString(ACTION_NAME_PREFIX_LS_PROPERTY +

					ACTION_VIEW_EXAMINATION_IMAGE_LS_PROPERTY));

				putValue(Action.SHORT_DESCRIPTION, mVDH.getLanguageString(ACTION_SHORT_DESCRIPTION_PREFIX_LS_PROPERTY +

					ACTION_VIEW_EXAMINATION_IMAGE_LS_PROPERTY));
			}
		}

		// IMAGE CONTAINER LABEL - COPY IMAGE ACTION

		private class CopyAction extends AbstractAction
		{
			public void actionPerformed(ActionEvent e)
			{
				Toolkit.getDefaultToolkit().getSystemClipboard().setContents(

					constructSystemTransferable(), ImageContainer.this);
			}

			public CopyAction()
			{
				putValue(Action.NAME, mVDH.getLanguageString(ACTION_NAME_PREFIX_LS_PROPERTY +

					ACTION_COPY_EXAMINATION_IMAGE_LS_PROPERTY));

				putValue(Action.SHORT_DESCRIPTION, mVDH.getLanguageString(ACTION_SHORT_DESCRIPTION_PREFIX_LS_PROPERTY +

					ACTION_COPY_EXAMINATION_IMAGE_LS_PROPERTY));
			}
		}
	}
}
