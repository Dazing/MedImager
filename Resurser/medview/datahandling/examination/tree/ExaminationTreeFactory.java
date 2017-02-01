/*
 * ExaminationTreeFactory.java
 *
 * Created on den 14 augusti 2003, 14:21
 *
 * $Id: ExaminationTreeFactory.java,v 1.5 2005/02/17 10:06:24 lindahlf Exp $
 *
 * $Log: ExaminationTreeFactory.java,v $
 * Revision 1.5  2005/02/17 10:06:24  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.4  2004/11/09 21:14:27  lindahlf
 * Datalayer upgrade in connection with server development
 *
 * Revision 1.3  2004/02/19 18:21:27  lindahlf
 * Major update patch 1
 *
 * Revision 1.2  2004/01/27 15:48:47  oloft
 * PID introduction
 *
 * Revision 1.1  2003/08/18 21:16:44  erichson
 * First check-in.
 *
 */

package medview.datahandling.examination.tree;

import java.util.*;

import medview.datahandling.*;

/**
 * Factory class to generate a basic Tree for examinations.
 * @author Compiled by Nils Erichson from code by Nader Nazari
 */
public class ExaminationTreeFactory implements MedViewDataConstants
{
	/**
	 * Creates a root node for an examination Tree. Adds
	 * konkret-identification and date term leaves. The
	 * term names used are taken from the TreeFileHandler
	 * class.
	 * @param date the date of the examination
	 * @param idCode the patient identification (aka p-code)
	 * @return a Tree object containing the above information.
	 */
	public static Tree createExaminationTree(String pCode, Date date, boolean addPCodeNode)
	{
		// tree file name and date

		String shortDate = TreeFileHandler.TREEFILENAME_DATE_FORMAT.format(date);

		String longDate = TreeFileHandler.TREEFILE_DATUM_FIELD_DATE_FORMAT.format(date);

		String treeFileName = pCode + "_" + shortDate + MVD_TREE_FILE_ENDER;

		Tree rootNode = new TreeBranch(shortDate);

		// concrete id

		TreeBranch konkretIdentifikationBranch = new TreeBranch(CONCRETE_ID_TERM_NAME);

		konkretIdentifikationBranch.addChild(new TreeLeaf(treeFileName));

		// date

		TreeBranch datumBranch = new TreeBranch(TreeFileHandler.DATE_TERM_NAME);

		datumBranch.addChild(new TreeLeaf(longDate));

		rootNode.addChild(konkretIdentifikationBranch);

		rootNode.addChild(datumBranch);

		// p-code

		if (addPCodeNode) // only add if addPCodeNode is true
		{
			TreeBranch pCodeBranch = new TreeBranch(PCODE_TERM_NAME);

			pCodeBranch.addChild(new TreeLeaf(pCode));

			rootNode.addChild(pCodeBranch);
		}

		// user-id

		if (mVDH.isUserIDSet())
		{
			TreeBranch userIDBranch = new TreeBranch(USER_ID_TERM_NAME);

			userIDBranch.addChild(new TreeLeaf(mVDH.getUserID()));

			rootNode.addChild(userIDBranch);
		}
		else
		{
			String m1 = "ExaminationTreeFactory: WARNING: ";

			String m2 = "creating an examination tree without ";

			String m3 = "the user-ID set! Will result in a tree ";

			String m4 = "without the User-ID node";

			System.err.println(m1 + m2 + m3 + m4);
		}

		// user-name

		if (mVDH.isUserNameSet())
		{
			TreeBranch userNameBranch = new TreeBranch(USER_NAME_TERM_NAME);

			userNameBranch.addChild(new TreeLeaf(mVDH.getUserName()));

			rootNode.addChild(userNameBranch);
		}
		else
		{
			String m1 = "ExaminationTreeFactory: WARNING: ";

			String m2 = "creating an examination tree without ";

			String m3 = "the user name set! Will result in a tree ";

			String m4 = "without the User-name node";

			System.err.println(m1 + m2 + m3 + m4);
		}

		return rootNode;
	}

	private static final MedViewDataHandler mVDH = MedViewDataHandler.instance();

}
