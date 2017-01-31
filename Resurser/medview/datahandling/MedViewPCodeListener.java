/*
 * @(#)MedViewPCodeListener.java
 *
 * $Id: MedViewPCodeListener.java,v 1.2 2004/11/04 12:05:00 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.datahandling;

import java.util.*;

public interface MedViewPCodeListener extends EventListener
{
	public abstract void generatedPCodePrefixChanged(MedViewPCodeEvent e);

	public abstract void nrGeneratorLocationChanged(MedViewPCodeEvent e);
	
	public abstract void pCodeGeneratorChanged(MedViewPCodeEvent e);
}
