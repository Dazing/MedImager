/*
 * @(#)JournalPageCreator.java
 *
 * $Id: JournalPageCreator.java,v 1.1 2003/04/10 01:49:16 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.print;

import java.util.*;

import misc.gui.print.*;

public interface JournalPageCreator extends PageCreator
{
	public void setPCode(String pCode);

	public void setDate(Date date);
}