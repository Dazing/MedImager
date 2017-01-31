/**
 * @(#) BrowseTreePanel.java
 */

package medview.medimager.view;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.*;
import java.awt.image.*;

import java.io.*;

import java.util.*;

import java.text.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.tree.*;

import medview.common.dialogs.*;

import medview.datahandling.*;

import medview.medimager.foundation.*;
import medview.medimager.model.*;

import misc.foundation.io.*;

import misc.gui.actions.*;
import misc.gui.components.*;
import misc.gui.constants.*;
import misc.gui.utilities.*;

public class BrowseTreePanel extends JPanel implements GUIConstants,
	MedImagerActionConstants, MedViewMediaConstants, MedImagerConstants, ClipboardOwner
{
	public BrowseTreePanel()
	{
		try
		{
			jbInit();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/*
	 * NOTE: since the browse tree panel simply is a panel wrapping
	 * a tree, the concept of 'leaf' components used in other parts
	 * of the application does not apply here. Instead, the browse
	 * tree panel has a dynamic behavior dependant upon the current
	 * usability layer, such as what options to display on a popup
	 * menu etc.
	 */

	// various other members

	private JTree tree;

	private NodeModel[] roots;

	private MedImagerModel model;

	private MedImagerFrame frame;

	private DefaultTreeModel treeModel;

	private JScrollPane treeScrollPane;

	private UsabilityModel usabilityModel;

	private DefaultMutableTreeNode swingRoot;

	private ShellState state = new ShellState();

	private Vector internalFocusableComponentVector = new Vector(); // internal components that may gain focus (tree + eventual popup menus)

	protected EventListenerList listenerList = new EventListenerList();


	// CONSTRUCTOR(S) AND RELATED METHODS

	public BrowseTreePanel(NodeModel[] roots, MedImagerModel model, MedImagerFrame frame)
	{
		this.roots = roots;

		this.model = model;

		this.frame = frame;

		// set direct usability model reference

		this.usabilityModel = model.getUsabilityModel();

		// create and set shell plugged-in actions

		state.setAction(NEW_FOLDER_ACTION, new NewFolderAction());

		state.setAction(REMOVE_ACTION, new RemoveAction());

		state.setAction(CUT_ACTION, new CutAction());

		state.setAction(COPY_ACTION, new CopyAction());

		state.setAction(PASTE_ACTION, new PasteAction());

		state.setAction(JOURNAL_ACTION, new JournalAction());

		state.setAction(ENLARGE_IMAGE_ACTION, new EnlargeImageAction());

		state.setAction(INFORMATION_ACTION, new InformationAction());

		state.setAction(SHARE_LOCAL_ACTION, new ShareLocalAction());

		state.setAction(SHARE_GLOBAL_ACTION, new ShareGlobalAction());

		// set up swing tree component

		createTreeComponent();

		// attach the roots to the tree component

		setModelRoots(roots);

		// layout the panel (basically set the tree as center component)

		layoutPanel();

		// setup drag and drop

		new DropTarget(tree, constructDropTargetListener());

		DragSource.getDefaultDragSource().createDefaultDragGestureRecognizer(tree,

			DnDConstants.ACTION_COPY_OR_MOVE, constructDragGestureListener());

		/*
		 * NOTE: the above action specifier specifies what source
		 * operations are allowed. I.e. if 'copy or move' is set,
		 * then (depending on whether the user holds down the COPY
		 * button modifier while dragging) the drag source listener
		 * can receive drag source events where the getAction() can
		 * return either COPY or MOVE.
		 */

		// attach focus handling to the tree component

		tree.addFocusListener(new BrowseTreePanelFocusListener());

		// attach focus handling to the panel itself

		this.addFocusListener(new BrowseTreePanelFocusListener());
	}

	private void createTreeComponent()
	{
		swingRoot = new DefaultMutableTreeNode("Swing Root");

		treeModel = new DefaultTreeModel(swingRoot);

		treeModel.setAsksAllowsChildren(true);

		tree = new JTree(treeModel);

		tree.setRootVisible(false);

		tree.setShowsRootHandles(true);

		tree.setCellRenderer(new MyTreeCellRenderer());

		tree.setRowHeight(-1); // forces tree cell renderer to be queried for row height, instead of being fixed

		internalFocusableComponentVector.add(tree);

		// add listeners to tree component

		tree.addMouseListener(new MyTreeMouseListener());

		tree.addTreeSelectionListener(new MyTreeSelectionListener()); // updates actions
	}

	private void layoutPanel()
	{
		setLayout(new BorderLayout());

		treeScrollPane = new JScrollPane(tree);

		add(treeScrollPane, BorderLayout.CENTER);

		setPreferredSize(new Dimension(320,0));

		setMinimumSize(new Dimension(320,0));
	}


	// TREE AND ROOTS

	public void setModelRoots(NodeModel[] roots)
	{
		// remove the previous roots (if any)

		Enumeration enm = swingRoot.children();

		while (enm.hasMoreElements())
		{
			((MutableTreeNode)enm.nextElement()).removeFromParent();
		}

		// attach the new nodes as roots

		if (roots != null)
		{
			for (int ctr=0; ctr<roots.length; ctr++)
			{
				swingRoot.add(createNode(roots[ctr]));
			}
		}

		// store the root array as a member

		this.roots = roots;

		// signal that the structure has changed

		treeModel.nodeStructureChanged(swingRoot);
	}

	protected DefaultMutableTreeNode createNode(NodeModel nodeModel) // creates node structure
	{
		// create node structure

		final DefaultMutableTreeNode node = new DefaultMutableTreeNode(nodeModel); // the returned node

		node.setAllowsChildren(nodeModel.isBranch());

		if (nodeModel.isBranch())
		{
			NodeModel[] children = nodeModel.getChildren();

			for (int ctr=0; ctr<children.length; ctr++)
			{
				DefaultMutableTreeNode child = createNode(children[ctr]);

				node.insert(child, children[ctr].getNodeIndex());
			}
		}

		// add node model listener

		nodeModel.addNodeModelListener(new NodeModelListener()
		{
			public void nodeAdded(NodeModelEvent e)
			{
				// add node to structure

				DefaultMutableTreeNode newNode = createNode(e.getNode());

				int indexOfNewNode = e.getNode().getNodeIndex();

				node.insert(newNode, indexOfNewNode);

				// notify tree model of added node

				treeModel.nodesWereInserted(node, new int[] { indexOfNewNode });

				// if added node was branch -> notify model of subnodes recursively

				if (e.getNode().isBranch())
				{
					signalNodeInserts(newNode);
				}
			}

			public void nodeRemoved(NodeModelEvent e)
			{
				NodeModel removedNodeModel = e.getNode();

				Enumeration enm = node.children();

				while (enm.hasMoreElements())
				{
					DefaultMutableTreeNode currNode = (DefaultMutableTreeNode) enm.nextElement();

					if (removedNodeModel == currNode.getUserObject())
					{
						int index = node.getIndex(currNode);

						node.remove(currNode);

						treeModel.nodesWereRemoved(node, new int[] {index}, new Object[] {currNode});
					}
				}
			}

			public void nodeUpdated(NodeModelEvent e)
			{
				if (tree.isVisible())
				{
					tree.repaint();
				}
			}
		});

		// return node

		return node;
	}

	/**
	 * Recursively descends into the tree structure initiated by
	 * node, and signals the model that all found nodes have been
	 * inserted (do this after you have added the node structure
	 * but still haven't notified the model of structure change).
	 */
	protected void signalNodeInserts(TreeNode node)
	{
		for (int ctr=0; ctr<node.getChildCount(); ctr++)
		{
			TreeNode curr = node.getChildAt(ctr);

			treeModel.nodesWereInserted(node, new int[] {ctr});

			signalNodeInserts(curr);
		}
	}

	/**
	 * If the panel is in the empty state, i.e. if it is currently
	 * not displaying any roots, this method will return an empty
	 * array. Otherwise, the root array is returned.
	 * @return NodeModel[]
	 */
	public NodeModel[] getRoots( )
	{
		return roots;
	}

	/**
	 * Whether or not the specified node model is one of the
	 * root node models being displayed at the moment by this
	 * browse tree panel.
	 * @param nodeModel NodeModel
	 * @return boolean
	 */
	public boolean isRoot(NodeModel nodeModel)
	{
		NodeModel[] roots = getRoots();

		for (int ctr=0; ctr<roots.length; ctr++)
		{
			if (roots[ctr] == nodeModel)
			{
				return true;
			}
		}

		return false;
	}

	public JTree getTree( )
	{
		return tree;
	}

	public DefaultTreeModel getTreeModel( )
	{
		return treeModel;
	}


	// EVENT NOTIFICATION

	public void addBrowseTreeListener( BrowseTreeListener l )
	{
		listenerList.add(BrowseTreeListener.class, l);
	}

	public void removeBrowseTreeListener( BrowseTreeListener l )
	{
		listenerList.remove(BrowseTreeListener.class, l);
	}

	protected void fireNodeSelectionChanged()
	{
		Object[] listeners = listenerList.getListenerList();

		NodeModel lead = getLeadSelectedNodeModel();

		NodeModel[] all = getSelectedNodeModels();

		BrowseTreeEvent e = new BrowseTreeEvent(this, lead, all);

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == BrowseTreeListener.class)
			{
				((BrowseTreeListener)listeners[i+1]).nodeSelectionChanged(e);
			}
		}
	}


	// CLIPBOARD OWNER INTERFACE METHODS

	public void lostOwnership(Clipboard clipboard, Transferable contents)
	{
		// deliberately a no-op implementation
	}


	// DRAG-AND-DROP

	protected DragGestureListener constructDragGestureListener()
	{
		return new DragGestureListener()
		{
			public void dragGestureRecognized(DragGestureEvent dge)
			{
				if (dge.getDragAction() == DnDConstants.ACTION_MOVE)
				{
					Transferable t = constructSystemAndLocalTreeTransferable(DnDConstants.ACTION_MOVE);

					dge.startDrag(null, t, constructDragSourceListener());
				}
				else if (dge.getDragAction() == DnDConstants.ACTION_COPY)
				{
					Transferable t = constructSystemAndLocalTreeTransferable(DnDConstants.ACTION_COPY);

					dge.startDrag(null, t, constructDragSourceListener());
				}
			}
		};
	}

	protected DragSourceListener constructDragSourceListener()
	{
		/*
		 * NOTE: these methods are called continously during the drag
		 * operation. They are in respect to whatever component using
		 * drag-and-drop the cursor passes by. For instance, if I drag
		 * a node out of the tree, dragExit() is called. If I continue
		 * the drag into the explorer window in Windows, dragEnter() is
		 * called. If I drop the node into the explorer window in Windows,
		 * dragDropEnd() is called.
		 */

		return new DragSourceListener()
		{
			public void dragDropEnd(DragSourceDropEvent dsde)
			{
			}

			public void dragEnter(DragSourceDragEvent dsde)
			{
			}

			public void dragExit(DragSourceEvent dse)
			{
			}

			public void dragOver(DragSourceDragEvent dsde)
			{
			}

			public void dropActionChanged(DragSourceDragEvent dsde)
			{
			}
		};
	}

	protected DropTargetListener constructDropTargetListener()
	{
		return new DropTargetListener()
		{
			public void dragEnter(DropTargetDragEvent dtde)
			{
			}

			public void dragExit(DropTargetEvent dte)
			{
			}

			public void dragOver(DropTargetDragEvent dtde)
			{
				TreePath path = tree.getPathForLocation(dtde.getLocation().x, dtde.getLocation().y);

				if (path != null)
				{
					NodeModel nodeModel = (NodeModel) ((DefaultMutableTreeNode)

						path.getLastPathComponent()).getUserObject();

					if (nodeModel != model.getLibraryRoot())
					{
						if (containsSupportedDropFlavor(dtde.getCurrentDataFlavors()))
						{
							if ((dtde.getDropAction() == DnDConstants.ACTION_COPY) ||

								(dtde.getDropAction() == DnDConstants.ACTION_MOVE))
							{
								dtde.acceptDrag(dtde.getDropAction());
							}

							/*
							 * NOTE: this should be interpreted as 'we accept a drop into
							 * this position if the user is either copying or moving data
							 * into it' (which depends on user modifier keys held down during
							 * the drag operation).
							 */
						}
						else
						{
							dtde.rejectDrag();
						}
					}
					else
					{
						dtde.rejectDrag(); // do not accept drags to these nodes
					}
				}
				else
				{
					if (containsSupportedDropFlavor(dtde.getCurrentDataFlavors()))
					{
						if ((dtde.getDropAction() == DnDConstants.ACTION_COPY) ||

							(dtde.getDropAction() == DnDConstants.ACTION_MOVE))
						{
							dtde.acceptDrag(dtde.getDropAction());
						}
					}
					else
					{
						dtde.rejectDrag();
					}
				}
			}

			public void drop(DropTargetDropEvent dtde)
			{
				/*
				 * NOTE: the drop() method is never called if the
				 * dragOver() has rejected the drag. So the insertion
				 * method does not have to check which nodes the
				 * drop is being made to, since this check is made in
				 * the dragOver() method above.
				 */

				if (insertIntoLocation(dtde.getTransferable(), dtde.getLocation())) // returns true if successful
				{
					dtde.acceptDrop(dtde.getDropAction());
				}
				else
				{
					dtde.rejectDrop(); // could not insert transferable into selection
				}
			}

			public void dropActionChanged(DropTargetDragEvent dtde)
			{
			}
		};
	}

	/**
	 * Returns the currently selected lead node model,
	 * or null if no such model exists.
	 * @return NodeModel
	 */
	private NodeModel getLeadSelectedNodeModel()
	{
		TreePath leadPath = tree.getSelectionModel().getLeadSelectionPath();

		if (leadPath == null)
		{
			return null;
		}
		else
		{
			DefaultMutableTreeNode dN = (DefaultMutableTreeNode) leadPath.getLastPathComponent();

			return (NodeModel) dN.getUserObject();
		}
	}

	/**
	 * Returns an array of all currently selected node
	 * models, or an empty array if no node models are
	 * selected.
	 * @return NodeModel[]
	 */
	private NodeModel[] getSelectedNodeModels()
	{
		TreePath[] selPaths = tree.getSelectionModel().getSelectionPaths();

		if (selPaths == null)
		{
			return new NodeModel[0];
		}
		else
		{
			NodeModel[] retArr = new NodeModel[selPaths.length];

			for (int ctr=0; ctr<selPaths.length; ctr++)
			{
				DefaultMutableTreeNode dN = (DefaultMutableTreeNode) selPaths[ctr].getLastPathComponent();

				retArr[ctr] = (NodeModel) dN.getUserObject();
			}

			return retArr;
		}
	}

	public void updateActions()
	{
		NodeModel selModel = null;

		if (tree.getSelectionPath() != null)
		{
			selModel = (NodeModel) ((DefaultMutableTreeNode)tree.getSelectionPath().

				getLastPathComponent()).getUserObject();
		}

		// boolean helpers

		boolean nodeSelected = (selModel != null);

		boolean libraryRootSelected = (selModel == model.getLibraryRoot());

		boolean myImagesRootSelected = (selModel == model.getMyImagesRoot());

		boolean branchSelected = nodeSelected && (selModel.isBranch()); // lazy evaluation

		Clipboard sCB = Toolkit.getDefaultToolkit().getSystemClipboard(); // reference to system clipboard

		Clipboard lCB = MedImagerDataTransfer.instance().getLocalClipboard(); // reference to local clipboard

		Transferable localTransferable = lCB.getContents(null);

		Transferable systemTransferable = sCB.getContents(null);

		boolean localClipBoardContainsValidTransferable = (localTransferable != null) &&

			containsSupportedDropFlavor(localTransferable.getTransferDataFlavors());

		boolean systemClipBoardContainsValidTransferable = (systemTransferable != null) &&

			containsSupportedDropFlavor(systemTransferable.getTransferDataFlavors());

		// check if 'new folder' should be enabled

		state.getAction(NEW_FOLDER_ACTION).setEnabled(!nodeSelected || (branchSelected && !libraryRootSelected));

		// check if 'remove' should be enabled

		state.getAction(REMOVE_ACTION).setEnabled(nodeSelected && !libraryRootSelected && !myImagesRootSelected);

		// check if 'cut' should be enabled

		state.getAction(CUT_ACTION).setEnabled(nodeSelected && !libraryRootSelected && !myImagesRootSelected);

		// check if 'copy' should be enabled

		state.getAction(COPY_ACTION).setEnabled(nodeSelected && !libraryRootSelected && !myImagesRootSelected);

		// check if 'paste' should be enabled

		state.getAction(PASTE_ACTION).setEnabled(branchSelected && !libraryRootSelected &&

			(localClipBoardContainsValidTransferable || systemClipBoardContainsValidTransferable));

		// check if 'journal' should be enabled

		state.getAction(JOURNAL_ACTION).setEnabled(nodeSelected && !branchSelected);

		// check if 'enlarge image' should be enabled

		state.getAction(ENLARGE_IMAGE_ACTION).setEnabled(nodeSelected && !branchSelected);

		// check if 'information' should be enabled

		state.getAction(INFORMATION_ACTION).setEnabled(nodeSelected && !branchSelected);

		// check if 'share locally' should be enabled

		state.getAction(SHARE_LOCAL_ACTION).setEnabled(false); // for now

		// check if 'share globally' should be enabled

		state.getAction(SHARE_GLOBAL_ACTION).setEnabled(false);	// for now
	}



	// FOCUS-RELATED

	public void attachFocusHandling(Component comp)
	{
		comp.addFocusListener(new BrowseTreePanelFocusListener());
	}

	protected void panelGainedFocusFromOutside()
	{
		updateActions();

		if (frame.getCurrentState() != state)
		{
			frame.pluginState(state);
		}
	}

	protected void panelLostFocusToOutside()
	{
	}


	// UTILITY METHODS

	/**
	 * Returns if the specified array contains a flavor that
	 * is acceptable to drpo into this tree panel.
	 * @param flavors DataFlavor[]
	 * @return boolean
	 */
	protected boolean containsSupportedDropFlavor(DataFlavor[] flavors)
	{
		for (int ctr=0; ctr<flavors.length; ctr++)
		{
			if ((flavors[ctr] == MedImagerDataTransfer.dbImageFlavor) ||

				(flavors[ctr] == MedImagerDataTransfer.nodeFlavor) ||

				(flavors[ctr] == DataFlavor.javaFileListFlavor) ||

				(flavors[ctr] == DataFlavor.imageFlavor))
			{
				return true;
			}
		}

		return false; // no supported data flavor found
	}

	/**
	 * Inserts the specified Transferable into the currently
	 * selected node (if one is selected), if the Transferable
	 * supports one of the following flavors: dbImageFlavor,
	 * imageFlavor, javaFileListFlavor, nodeFlavor. Will not do
	 * anything if none of the above flavors is supported.
	 *
	 * Returns whether or not the Transferable could be inserted
	 * into the current selection (i.e. whether the insertion
	 * was successful or not).
	 *
	 * @param t Transferable
	 */
	protected boolean insertIntoCurrentSelection(Transferable t)
	{
		return insertIntoPath(t, tree.getSelectionPath());
	}

	/**
	 * Inserts the specified Transferable into the specified
	 * location, if the Transferable supports one of the following
	 * flavors: dbImageFlavor, imageFlavor, javaFileListFlavor,
	 * nodeFlavor. Will not do anything if none of the above
	 * flavors is supported.
	 *
	 * Returns whether or not the Transferable could be inserted
	 * into the specified location (i.e. whether the insertion
	 * was successful or not).
	 *
	 * @param t Transferable
	 */
	protected boolean insertIntoLocation(Transferable t, Point loc)
	{
		return insertIntoPath(t, tree.getPathForLocation((int)loc.getX(), (int)loc.getY()));
	}

	protected boolean insertIntoPath(Transferable t, TreePath treePath)
	{
		NodeModel parentNodeModel = null;

		if (treePath == null)
		{
			parentNodeModel = model.getMyImagesRoot(); // the default place to place things
		}
		else
		{
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath.getLastPathComponent();

			NodeModel nodeModel = (NodeModel) node.getUserObject();

			if (nodeModel.isBranch())
			{
				parentNodeModel = nodeModel;
			}
			else
			{
				parentNodeModel = nodeModel.getParent();
			}
		}

		return insertIntoNodeModel(t, parentNodeModel);
	}

	/**
	 * Inserts the specified Transferable into the specified
	 * node model, if the Transferable supports one of the
	 * following flavors: dbImageFlavor, imageFlavor,
	 * javaFileListFlavor, nodeFlavor. Will not do
	 * anything if none of the above flavors is supported.
	 *
	 * Returns whether or not the Transferable could be inserted
	 * into the specified node model (i.e. whether the insertion
	 * was successful or not).
	 *
	 * @param t Transferable
	 */
	protected boolean insertIntoNodeModel(Transferable t, NodeModel parentNodeModel)
	{
		if (t != null)
		{
			if (t.isDataFlavorSupported(MedImagerDataTransfer.dbImageFlavor)) // data from result panel
			{
				try
				{
					// obtain the search images from the transferable

					Vector resultVector = (Vector) t.getTransferData(MedImagerDataTransfer.dbImageFlavor);

					DatabaseImageSearchResult[] results = new DatabaseImageSearchResult[resultVector.size()];

					resultVector.toArray(results);

					// add the search images to the parent node model

					model.addToNode(results, parentNodeModel);

					return true; // successful insertion
				}
				catch (Exception exc)
				{
					exc.printStackTrace();

					return false; // unsuccessful insertion
				}
			}
			else if (t.isDataFlavorSupported(MedImagerDataTransfer.nodeFlavor)) // data from within the tree
			{
				try
				{
					// obtain the node model data from the transferable

					Vector resultVector = (Vector) t.getTransferData(MedImagerDataTransfer.nodeFlavor);

					NodeModel[] models = new NodeModel[resultVector.size()];

					resultVector.toArray(models);

					// add the node models to the parent node model

					for (int ctr=0; ctr<models.length; ctr++)
					{
						parentNodeModel.add(models[ctr]);
					}

					return true; // successful insertion
				}
				catch (Exception e)
				{
					e.printStackTrace();

					return false; // unsuccessful insertion
				}
			}
			else if (t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) // data from file system
			{
				try
				{
					// obtain the file data

					java.util.List transferData = (java.util.List) t.getTransferData(DataFlavor.javaFileListFlavor);

					File[] fileArray = new File[transferData.size()];

					transferData.toArray(fileArray);

					// try to add the files to the node (it has to be image files)

					model.addToNode(fileArray, parentNodeModel);

					return true; // successful insertion
				}
				catch (Exception e)
				{
					e.printStackTrace();

					return false; // unsuccessful insertion
				}
			}
			else if (t.isDataFlavorSupported(DataFlavor.imageFlavor)) // data from imaging application
			{
				try
				{
					BufferedImage bufferedImage = (BufferedImage) t.getTransferData(DataFlavor.imageFlavor);

					model.addToNode(new BufferedImage[] { bufferedImage }, parentNodeModel);

					return true; // successful insertion
				}
				catch (Exception e)
				{
					e.printStackTrace();

					return false; // unsuccessful insertion
				}
			}
			else
			{
				return false; // unsuccessful insertion (the dataflavor was not supported)
			}
		}
		else
		{
			return false; // unsuccessful insertion (the transferable was null!)
		}
	}

	/**
	 * Constructs a Transferable object suited to be placed on the
	 * local clipboard, or used in local DnD operations. The flavors
	 * supported is only one - the 'nodeFlavor' defined in the data
	 * transfer constants interface.
	 * @param operation int
	 * @return Transferable
	 */
	protected Transferable constructLocalTreeTransferable(final int action)
	{
		final NodeModel[] selectedNodeModels = getSelectedNodeModels(); // OBS - must be built here or selection might differ at getTransferData()

		return new Transferable()
		{
			public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
			{
				if (flavor == MedImagerDataTransfer.nodeFlavor)
				{
					Vector retVect = new Vector();

					if (action == DnDConstants.ACTION_MOVE) // observe we are in the local domain
					{
						for (int ctr=0; ctr<selectedNodeModels.length; ctr++)
						{
							retVect.add(selectedNodeModels[ctr]); // we are moving the source node models
						}
					}
					else if (action == DnDConstants.ACTION_COPY) // observe we are in the local domain
					{
						for (int ctr=0; ctr<selectedNodeModels.length; ctr++)
						{
							retVect.add(selectedNodeModels[ctr].clone()); // we clone the source node models
						}
					}

					return retVect;
				}
				else
				{
					throw new UnsupportedFlavorException(flavor);
				}
			}

			public DataFlavor[] getTransferDataFlavors()
			{
				return new DataFlavor[] { MedImagerDataTransfer.nodeFlavor };
			}

			public boolean isDataFlavorSupported(DataFlavor flavor)
			{
				return (flavor == MedImagerDataTransfer.nodeFlavor);
			}
		};
	}

	/**
	 * Constructs a Transferable object from the current selection,
	 * suited to be placed on the system clipboard, or used in inter-
	 * application DnD operations. The flavors supported are:
	 * stringFlavor, imageFlavor, and javaFileListFlavor.
	 * @return Transferable
	 */
	protected Transferable constructSystemTreeTransferable(final int action)
	{
		final NodeModel[] models = getSelectedNodeModels(); // OBS - must be built here or selection might differ at getTransferData()

		return new Transferable()
		{
			public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
			{
				if (flavor == DataFlavor.stringFlavor) // we return the node model descriptions
				{
					StringBuffer buffy = new StringBuffer();

					for (int ctr=0; ctr<models.length; ctr++)
					{
						if (models[ctr].isLeaf())
						{
							if (buffy.length() != 0)
							{
								buffy.append(",");
							}

							buffy.append(models[ctr] + "");
						}
					}

					return buffy.toString();
				}
				else if (flavor == DataFlavor.imageFlavor) // we return only the first node model image
				{
					if (isDataFlavorSupported(DataFlavor.imageFlavor)) // only supported if first node model is an image leaf node model
					{
						return ((ImageLeafNodeModel)models[0]).getFullImage(); // safe since check before cast
					}
					else
					{
						throw new UnsupportedFlavorException(flavor);
					}
				}
				else if (flavor == DataFlavor.javaFileListFlavor)
				{
					Vector retVect = new Vector();

					for (int ctr=0; ctr<models.length; ctr++)
					{
						if (models[ctr] instanceof ImageLeafNodeModel)
						{
							String tmpDir = System.getProperty("java.io.tmpdir");

							String originalFileName = ((ImageLeafNodeModel)models[ctr]).getOriginalImageFileName();

							File currFile = new File(tmpDir, originalFileName);

							if (!currFile.exists())
							{
								IOUtilities.copy(new ByteArrayInputStream(

									((ImageLeafNodeModel)models[ctr]).getFullImageByteArray()), currFile);
							}

							retVect.add(currFile);

							currFile.deleteOnExit();
						}
					}

					return retVect;
				}
				else
				{
					throw new UnsupportedFlavorException(flavor);
				}
			}

			public DataFlavor[] getTransferDataFlavors()
			{
				if (models[0] instanceof ImageLeafNodeModel)
				{
					return new DataFlavor[] { DataFlavor.stringFlavor,

						DataFlavor.imageFlavor, DataFlavor.javaFileListFlavor };
				}
				else
				{
					return new DataFlavor[] { DataFlavor.stringFlavor, DataFlavor.javaFileListFlavor };
				}
			}

			public boolean isDataFlavorSupported(DataFlavor flavor)
			{
				if ((flavor == DataFlavor.imageFlavor) && (models[0] instanceof ImageLeafNodeModel))
				{
					return true;
				}
				else
				{
					return ((flavor == DataFlavor.stringFlavor) || (flavor == DataFlavor.javaFileListFlavor));
				}
			}
		};
	}

	/**
	 * Constructs a Transferable, that can be placed on
	 * @param sourceOperation int
	 * @return Transferable
	 */
	public Transferable constructSystemAndLocalTreeTransferable(final int action)
	{
		return new Transferable()
		{
			private Transferable localTransferable = constructLocalTreeTransferable(action);

			private Transferable systemTransferable = constructSystemTreeTransferable(action);

			public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
			{
				if (localTransferable.isDataFlavorSupported(flavor))
				{
					return localTransferable.getTransferData(flavor);
				}
				else if (systemTransferable.isDataFlavorSupported(flavor))
				{
					return systemTransferable.getTransferData(flavor);
				}
				else
				{
					throw new UnsupportedFlavorException(flavor);
				}
			}

			public DataFlavor[] getTransferDataFlavors()
			{
				DataFlavor[] localFlavors = localTransferable.getTransferDataFlavors();

				DataFlavor[] systemFlavors = systemTransferable.getTransferDataFlavors();

				Vector vector = new Vector();

				for (int ctr=0; ctr<localFlavors.length; ctr++)
				{
					vector.add(localFlavors[ctr]);
				}

				for (int ctr=0; ctr<systemFlavors.length; ctr++)
				{
					vector.add(systemFlavors[ctr]);
				}

				DataFlavor[] retArr = new DataFlavor[vector.size()];

				vector.toArray(retArr);

				return retArr;
			}

			public boolean isDataFlavorSupported(DataFlavor flavor)
			{
				return ((localTransferable.isDataFlavorSupported(flavor)) ||

					(systemTransferable.isDataFlavorSupported(flavor)));
			}
		};
	}

	private void jbInit() throws Exception
	{
	}


	// INTERNAL FOCUS LISTENER

	private class BrowseTreePanelFocusListener implements FocusListener
	{
		public void focusGained(FocusEvent e)
		{
			Component oComp = e.getOppositeComponent();

			Enumeration enm = internalFocusableComponentVector.elements();

			while (enm.hasMoreElements())
			{
				if ((Component)enm.nextElement() == oComp)
				{
					return;
				}
			}

			panelGainedFocusFromOutside(); // if we get here, we gained focus from outside component
		}

		public void focusLost(FocusEvent e)
		{
			Component oComp = e.getOppositeComponent();

			if (frame.isOwnerComponent(oComp))		// i.e. a toolbar button or menu bar
			{
				return;
			}

			Enumeration enm = internalFocusableComponentVector.elements();

			while (enm.hasMoreElements())
			{
				if ((Component)enm.nextElement() == oComp)
				{
					return;
				}
			}

			panelLostFocusToOutside();	// if we get here, we lost focus to outside component
		}
	}


	// TREE CELL RENDERER

	private class MyTreeCellRenderer extends DefaultTreeCellRenderer
	{
		private Font treeStyleFont = null;

		private DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);

		public Component getTreeCellRendererComponent(JTree t, Object v, boolean s, boolean e, boolean l, int r, boolean h)
		{
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) v;

			if (node.getUserObject() instanceof NodeModel)
			{
				NodeModel userObject = (NodeModel) node.getUserObject();

				if (userObject instanceof DefaultLeafNodeModel)
				{
					DefaultLeafNodeModel model = (DefaultLeafNodeModel) userObject;

					ImageIcon smallIcon = new ImageIcon(model.getThumbImage().getScaledInstance(

						THUMB_IMAGE_DIMENSION.width, THUMB_IMAGE_DIMENSION.height, Image.SCALE_FAST));

					JLabel imageLabel = new JLabel(smallIcon);

					// if audio data exists - indicate this in the image graphic

					if (model.containsAudioData())
					{
						imageLabel.setLayout(new BorderLayout());

						JLabel audioIndicativeLabel = new JLabel(MedViewDataHandler.instance().getImageIcon(PLAY_IMAGE_ICON_14));

						audioIndicativeLabel.setHorizontalAlignment(SwingConstants.LEFT);

						audioIndicativeLabel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

						imageLabel.add(audioIndicativeLabel, BorderLayout.SOUTH);
					}

					JLabel descLabel = new JLabel();

					JLabel patientLabel = new JLabel();

					JLabel examinationLabel = new JLabel();

					descLabel.setOpaque(false);

					patientLabel.setOpaque(false);

					examinationLabel.setOpaque(false);

					if (treeStyleFont == null)
					{
						Font treeFont = UIManager.getFont("Tree.font");

						String name = treeFont.getName();

						int style = treeFont.getStyle();

						int size = treeFont.getSize();

						treeStyleFont = new Font(name, style, size);
					}

					descLabel.setFont(treeStyleFont);

					patientLabel.setFont(treeStyleFont);

					examinationLabel.setFont(treeStyleFont);

					descLabel.setText(model.getDescription());

					patientLabel.setText(model.getPID().toString());

					examinationLabel.setText(dateFormat.format(model.getEID().getTime()));

					JPanel descPanel = new JPanel(new GridLayout(3,1,0,0));

					descPanel.setOpaque(false);

					descPanel.add(descLabel);

					descPanel.add(patientLabel);

					descPanel.add(examinationLabel);

					JPanel retPanel = new JPanel(new BorderLayout(CCS,CCS));

					retPanel.setOpaque(true);

					retPanel.setPreferredSize(new Dimension(250, 60));

					retPanel.add(imageLabel, BorderLayout.WEST);

					retPanel.add(descPanel, BorderLayout.CENTER);

					if (s)
					{
						retPanel.setBackground(COLOR_LAF_TREE_SELECTION_BACKGROUND);

						descLabel.setForeground(COLOR_LAF_TREE_SELECTION_FOREGROUND);

						patientLabel.setForeground(COLOR_LAF_TREE_SELECTION_FOREGROUND);

						examinationLabel.setForeground(COLOR_LAF_TREE_SELECTION_FOREGROUND);

						Border lineBorder = BorderFactory.createLineBorder(COLOR_LAF_TREE_SELECTION_BORDER, 1);

						Border emptyBorder = BorderFactory.createEmptyBorder(2,2,2,2);

						retPanel.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
					}
					else
					{
						retPanel.setBackground(COLOR_LAF_TREE_TEXT_BACKGROUND);

						descLabel.setForeground(COLOR_LAF_TREE_TEXT_FOREGROUND);

						patientLabel.setForeground(COLOR_LAF_TREE_TEXT_FOREGROUND);

						examinationLabel.setForeground(COLOR_LAF_TREE_TEXT_FOREGROUND);

						retPanel.setBorder(BorderFactory.createEmptyBorder(3,3,3,3));
					}

					return retPanel;
				}
				else
				{
					return super.getTreeCellRendererComponent(t, v, s, e, l, r, h);
				}
			}
			else
			{
				return super.getTreeCellRendererComponent(t, v, s, e, l, r, h);
			}
		}
	}


	// TREE MOUSE LISTENER

	private class MyTreeMouseListener extends MouseAdapter
	{
		public void mouseClicked(MouseEvent e)
		{
			TreePath path = tree.getPathForLocation(e.getX(), e.getY());

			if (path == null)
			{
				tree.clearSelection();
			}
			else if (e.getClickCount() == 2) // (path is not null) double-click -> show image
			{
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();

				NodeModel nodeModel = (NodeModel) node.getUserObject();

				if (nodeModel instanceof ImageLeafNodeModel)
				{
					tree.setSelectionPath(path);

					state.getAction(ENLARGE_IMAGE_ACTION).actionPerformed(null); // show image
				}
			}
		}

		public void mouseReleased(MouseEvent e)
		{
			if (e.isPopupTrigger())
			{
				//  POPUP MENU BUILD

				TreePath path = tree.getPathForLocation(e.getX(), e.getY());

				NodeModel nodeModel = null;

				if (path == null)
				{
					tree.clearSelection();
				}
				else
				{
					tree.setSelectionPath(path);

					nodeModel = (NodeModel) ((DefaultMutableTreeNode)path.

						getLastPathComponent()).getUserObject();
				}

				JPopupMenu popupMenu = new JPopupMenu();

				internalFocusableComponentVector.add(popupMenu);

				// decide if 'add folder' / 'add album' option is shown

				if (usabilityModel.isFunctionalLayerActive(UsabilityModel.LAYER_ORGANIZE))
				{
					if (path == null)
					{
						popupMenu.add(new SmallIconJMenuItem(GlobalActionMediator.instance().getAction(

							GlobalActionMediator.NEW_ALBUM_ACTION)));
					}
					else
					{
						if (nodeModel.isBranch())
						{
							if (nodeModel != model.getLibraryRoot())
							{
								popupMenu.add(new SmallIconJMenuItem(frame.getShellForID(NEW_FOLDER_ACTION)));
							}
							else
							{
								popupMenu.add(new SmallIconJMenuItem(GlobalActionMediator.instance().getAction(

									GlobalActionMediator.NEW_ALBUM_ACTION)));
							}
						}
					}
				}

				// decide if 'remove subfolder' option is shown

				if ((nodeModel != null) && (nodeModel != model.getLibraryRoot()) && (nodeModel != model.getMyImagesRoot()))
				{
					popupMenu.add(new SmallIconJMenuItem(frame.getShellForID(REMOVE_ACTION)));
				}

				// decide if 'copy' option is shown

				if ((nodeModel != null) && (nodeModel != model.getLibraryRoot()) && (nodeModel != model.getMyImagesRoot()))
				{
					popupMenu.add(new SmallIconJMenuItem(frame.getShellForID(COPY_ACTION)));
				}

				// decide if 'paste' option is shown

				if ((nodeModel != null) && (nodeModel != model.getLibraryRoot()))
				{
					if (!isRoot(nodeModel) && nodeModel.isBranch()) // you can paste into branches except root
					{
						popupMenu.add(new SmallIconJMenuItem(frame.getShellForID(PASTE_ACTION)));
					}
				}

				// show the menu (if it contains anything)

				if (popupMenu.getComponentCount() != 0)
				{
					popupMenu.show(tree, e.getX(), e.getY());
				}
			}
		}
	}


	// TREE SELECTION LISTENER

	private class MyTreeSelectionListener implements TreeSelectionListener
	{
		public void valueChanged(TreeSelectionEvent e)
		{
			updateActions();

			fireNodeSelectionChanged();
		}
	}


	// NEW FOLDER ACTION

	private class NewFolderAction extends AbstractAction // 'new folder' - not 'new album'
	{
		public void actionPerformed(ActionEvent e)
		{
			NodeModel parentNodeModel = getLeadSelectedNodeModel();

			if (parentNodeModel == null)
			{
				parentNodeModel = model.getMyImagesRoot();
			}
			else
			{
				if (!parentNodeModel.isBranch())
				{
					parentNodeModel = parentNodeModel.getParent();
				}
			}

			String folderName = JOptionPane.showInputDialog(SwingUtilities.getRoot(BrowseTreePanel.this), "Nytt katalognamn:");

			if (folderName != null)
			{
				parentNodeModel.add(new DefaultFolderNodeModel(folderName)); // will trigger event from model layer
			}
		}

		public NewFolderAction()
		{
			super("Skapa underkatalog...");
		}
	}


	// REMOVE ACTION

	private class RemoveAction extends AbstractAction
	{
		public void actionPerformed(ActionEvent e)
		{
			NodeModel[] nodeModels = getSelectedNodeModels();

			for (int ctr=0; ctr<nodeModels.length; ctr++)
			{
				if ((model.getLibraryRoot() != nodeModels[ctr]) &&

					(model.getMyImagesRoot() != nodeModels[ctr]))
				{
					nodeModels[ctr].getParent().remove(nodeModels[ctr]);
				}
			}
		}

		public RemoveAction()
		{
			super("Ta bort");
		}
	}


	// CUT ACTION

	private class CutAction extends AbstractAction
	{
		public void actionPerformed(ActionEvent e)
		{
		}

		public CutAction()
		{
			super("Klipp ut");
		}
	}


	// COPY ACTION

	private class CopyAction extends AbstractAction
	{
		public void actionPerformed(ActionEvent e)
		{
			// obtain references to clipboards

			Clipboard sCB = Toolkit.getDefaultToolkit().getSystemClipboard(); // reference to system clipboard

			Clipboard lCB = MedImagerDataTransfer.instance().getLocalClipboard(); // reference to local clipboard

			// set the contents of the clipboards

			sCB.setContents(constructSystemTreeTransferable(DnDConstants.ACTION_COPY), BrowseTreePanel.this);

			lCB.setContents(constructLocalTreeTransferable(DnDConstants.ACTION_COPY), BrowseTreePanel.this);
		}

		public CopyAction()
		{
			super("Kopiera");
		}
	}


	// PASTE ACTION

	private class PasteAction extends AbstractAction
	{
		public void actionPerformed(ActionEvent evt)
		{
			// obtain references to clipboards

			Clipboard sCB = Toolkit.getDefaultToolkit().getSystemClipboard(); // reference to system clipboard

			Clipboard lCB = MedImagerDataTransfer.instance().getLocalClipboard(); // reference to local clipboard

			// obtain the transferables from each respective clipboard

			Transferable localTransferable = lCB.getContents(null);

			Transferable systemTransferable = sCB.getContents(null);

			// insert the transferables into the current selection

			if (localTransferable != null)
			{
				insertIntoCurrentSelection(localTransferable);
			}
			else if (systemTransferable != null)
			{
				insertIntoCurrentSelection(systemTransferable);
			}
		}

		public PasteAction()
		{
			super("Klistra in");
		}
	}


	// JOURNAL ACTION

	private class JournalAction extends AbstractAction
	{
		public void actionPerformed(ActionEvent e)
		{
			TreePath[] paths = tree.getSelectionPaths();

			Vector pidVector = new Vector(paths.length);

			for (int ctr=0; ctr<paths.length; ctr++)
			{
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) paths[ctr].getLastPathComponent();

				NodeModel userObject = (NodeModel) node.getUserObject();

				if (userObject instanceof ExaminationLeafNodeModel)
				{
					pidVector.add(((ExaminationLeafNodeModel)userObject).getPID());
				}
			}

			PatientIdentifier[] pids = new PatientIdentifier[pidVector.size()];

			pidVector.toArray(pids);

			frame.showJournals(pids);
		}

		public JournalAction()
		{
			super("Journal...");
		}
	}


	// ENLARGE IMAGE ACTION

	private class EnlargeImageAction extends AbstractAction
	{
		public void actionPerformed(ActionEvent e)
		{
			TreePath[] paths = tree.getSelectionPaths();

			Vector imageVector = new Vector();

			Vector titleVector = new Vector();

			for (int ctr=0; ctr<paths.length; ctr++)
			{
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) paths[ctr].getLastPathComponent();

				NodeModel userObject = (NodeModel) node.getUserObject();

				if (userObject instanceof DefaultLeafNodeModel)
				{
					DefaultLeafNodeModel defaultNodeModel = (DefaultLeafNodeModel) userObject;

					imageVector.add(defaultNodeModel.getFullImage());

					String pidS = defaultNodeModel.getPID() + "";

					String eidS = defaultNodeModel.getEID() + "";

					titleVector.add(pidS + " - " + eidS);
				}
			}

			Image[] images = new Image[imageVector.size()];

			String[] titles = new String[titleVector.size()];

			imageVector.toArray(images); titleVector.toArray(titles);

			Frame owner = null;

			Window windowAncestor = SwingUtilities.getWindowAncestor(BrowseTreePanel.this);

			if ((windowAncestor != null) && (windowAncestor instanceof Frame))
			{
				owner = (Frame) windowAncestor;
			}

			MedViewDialogs.instance().createAndShowImageDialogs(owner, titles, images);
		}

		public EnlargeImageAction()
		{
			super("Fullstorlek...");
		}
	}


	// INFORMATION ACTION

	private class InformationAction extends AbstractAction
	{
		public void actionPerformed(ActionEvent e)
		{
		}

		public InformationAction()
		{
			super("Ytterligare information...");
		}
	}


	// SHARE LOCAL ACTION

	private class ShareLocalAction extends AbstractAction
	{
		public void actionPerformed(ActionEvent e)
		{
		}

		public ShareLocalAction()
		{
			super("Dela lokalt");
		}
	}


	// SHARE GLOBAL ACTION

	private class ShareGlobalAction extends AbstractAction
	{
		public void actionPerformed(ActionEvent e)
		{
		}

		public ShareGlobalAction()
		{
			super("Dela globalt");
		}
	}
}
