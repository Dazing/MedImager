package medview.medimager.view;

import java.util.*;

import medview.medimager.model.*;

public class BrowseTreeEvent extends EventObject
{
	/**
	 * Returns the lead node selection (i.e. the
	 * last node selected in the current selection).
	 */
	public NodeModel getLeadSelectedNode()
	{
		return lead;
	}

	/**
	 * Returns all currently selected nodes, in
	 * no particular order.
	 */
	public NodeModel[] getAllSelectedNodes()
	{
		return all;
	}

	public BrowseTreeEvent(Object source, NodeModel lead, NodeModel[] all)
	{
		super(source);

		this.lead = lead;

		this.all = all;
	}

	private NodeModel lead;

	private NodeModel[] all;
}
