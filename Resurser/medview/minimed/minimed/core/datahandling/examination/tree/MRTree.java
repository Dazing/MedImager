/*
 * MRTree.java
 *
 * Created on den 21 januari 2003, 13:02
 *
 * $Log: MRTree.java,v $
 * Revision 1.1  2006/05/29 18:32:53  limpan
 * Added minimed code
 * ----------------------------------------------------------------------
 *
 * Revision 1.1  2004/12/08 14:47:44  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.5  2004/10/19 21:40:51  lindahlf
 * Lade tillbaka @deprecation eftersom det faktiskt hjälper om man har en bra IDE
 *
 * Revision 1.4  2004/10/01 16:39:50  lindahlf
 * no message
 *
 * Revision 1.3  2003/11/11 14:26:09  oloft
 * Switching main-branch
 *
 * Revision 1.2.2.3  2003/08/16 14:45:14  erichson
 * some comments, removed toString() (now inherited from Tree)
 *
 * Revision 1.2.2.2  2003/08/16 13:50:28  erichson
 * deprecated methods, added toString()
 *
 * Revision 1.2.2.1  2003/08/07 00:11:47  erichson
 * Removed setParent calls
 *
 */

package minimed.core.datahandling.examination.tree;


/**
 * @author  nader
 */
public class MRTree extends Tree
{
	public MRTree(String in_value, int in_type)
	{
		super(in_value, in_type);
	}
	
	public MRTree(String in_value)
	{
		super(in_value, Tree.TYPE_BRANCH);
	}

	/**
	 * @deprecated this is bad coding style since it is confusing,
	 *  other add methods in Java never return values.
	 */
	public MRTree addNode(String pNode)
	{
		MRTree child = new MRTree(pNode, Tree.TYPE_BRANCH);
		
		this.addChild(child);
		
		return child;
	}

	/**
	 * @deprecated this is bad coding style since it is confusing,
	 * other add methods in Java never return values.
	 */
	public MRTree addLeaf(String pLeaf)
	{
		MRTree child = new MRTree(pLeaf, Tree.TYPE_LEAF);

		this.addChild(child);
		
		return child;
	}
}
