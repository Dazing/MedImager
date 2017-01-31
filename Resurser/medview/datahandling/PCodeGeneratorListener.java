/*
 * @(#)PCodeGeneratorListener.java
 *
 * $Id: PCodeGeneratorListener.java,v 1.1 2004/01/20 19:42:18 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.datahandling;

import java.util.*;

public interface PCodeGeneratorListener extends EventListener
{
	public void locationChanged(PCodeGeneratorEvent e);

	public void prefixChanged(PCodeGeneratorEvent e);
}