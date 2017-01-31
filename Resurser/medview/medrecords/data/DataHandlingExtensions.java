/*
 * $Id: DataHandlingExtensions.java,v 1.14 2005/02/17 10:24:34 lindahlf Exp $
 *
 * Created on den 2 oktober 2002, 09:58
 *
 * $Log: DataHandlingExtensions.java,v $
 * Revision 1.14  2005/02/17 10:24:34  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.13  2004/12/08 14:42:54  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.12  2004/12/06 13:20:03  erichson
 * Removed reference to medview.common.utilities.*;
 *
 * Revision 1.11  2004/11/24 15:18:30  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.10  2004/11/11 22:37:14  lindahlf
 * MedServer Test Pack 2
 *
 * Revision 1.9  2004/11/09 21:14:41  lindahlf
 * Datalayer upgrade in connection with server development
 *
 * Revision 1.8  2004/11/04 12:05:07  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.7  2004/05/18 18:21:05  lindahlf
 * Åtgärdade fel med bild-filnamn skapade utan datum
 *
 * Revision 1.6  2004/03/18 16:37:14  lindahlf
 * Ordnade till PID-format bugg
 *
 * Revision 1.5  2004/03/10 17:04:02  lindahlf
 * Tweak av PID-map
 *
 * Revision 1.4  2004/02/24 18:41:36  lindahlf
 * Same pcode generated for existant pid
 *
 * Revision 1.3  2004/02/19 18:21:28  lindahlf
 * Major update patch 1
 *
 * Revision 1.2  2004/01/27 15:43:28  oloft
 * PID introduction
 *
 * Revision 1.1  2003/12/21 22:02:06  oloft
 * Imported
 *
 * Revision 1.16  2003/11/11 14:22:38  oloft
 * Switching main-branch
 *
 * Revision 1.15.2.8  2003/10/18 14:50:46  oloft
 * Builds tree file with new file names
 *
 * Revision 1.15.2.7  2003/10/06 09:16:25  oloft
 * interim
 *
 * Revision 1.15.2.6  2003/10/03 15:44:30  oloft
 * Added getAvailableLanguages method
 *
 * Revision 1.15.2.5  2003/10/03 09:14:41  oloft
 * Added changeLanguage method
 *
 * Revision 1.15.2.4  2003/09/09 17:18:02  erichson
 * saveTree -> saveExamination
 *
 * Revision 1.15.2.3  2003/09/08 13:21:45  erichson
 * changed getTermProperty to get/setTermDefinitionLocation and get/setTermValueLocation
 *
 * Revision 1.15.2.2  2003/08/16 14:46:03  erichson
 * Added setExaminationDataLocation(), some cleanup
 *
 * Revision 1.15.2.1  2003/08/14 10:53:52  erichson
 * Updated field types, added saveTree method
 *
 */

package medview.medrecords.data;

import java.io.*;

import java.util.*;

import medview.common.data.*;

import medview.datahandling.*;
import medview.datahandling.examination.*;
import medview.datahandling.examination.tree.*;

/**
 * The DataHandlingExtensions provides a few extensions 
 * to medview.datahandling.MedViewDataHandler.
 */
public class DataHandlingExtensions
{
	// MEMBERS

	private MedViewDataHandler mVDH;

	private static DataHandlingExtensions instance;

	// CONSTRUCTORS

	private DataHandlingExtensions()
	{
		mVDH = MedViewDataHandler.instance();
	}


	// SINGLETON

	public static DataHandlingExtensions instance()
	{
		if (instance == null)
		{
			instance = new DataHandlingExtensions();
		}

		return instance;
	}


	// TERM HANDLING

	public String[] getPresets(String termName) throws IOException, NoSuchTermException
	{
		return mVDH.getValues(termName);
	}

	public void writeValue(String pTerm, String pValue)
	{
		try
		{
			mVDH.addValue(pTerm, pValue);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	// EXAMINATION IDENTIFIER

	/**
	 * Constructs an examination identifier from the information
	 * contained in the specified tree.
	 */
	public ExaminationIdentifier getExaminationIdentifier(Tree t) throws 
		CouldNotParseDateException, CouldNotConstructIdentifierException
	{
		return MedViewUtilities.constructExaminationIdentifier(t);
	}

	// EXAMINATION TREE

	/**
	 * The pid term is the 'personal identifier' term used in the
	 * form, it can have as value either a pcode or some other way
	 * of identifying the patient. Thus, its value is not always a
	 * pcode.
	 */
	public Tree createExaminationTree(String pidTerm, Date d, String pCode) throws
		InvalidRawPIDException, CouldNotGeneratePCodeException
	{
		if (!pidTerm.equals(MedViewDataConstants.PCODE_TERM_NAME)) // if 'pid' is not the p-code...
		{	
			return ExaminationTreeFactory.createExaminationTree(pCode, d, true); // true -> add p-code node to tree
		}
		else
		{
			return ExaminationTreeFactory.createExaminationTree(pCode, d, false); // false -> do not add p-code node
		}
	}
}
