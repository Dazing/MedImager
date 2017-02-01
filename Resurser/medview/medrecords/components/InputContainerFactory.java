/*
 * InputContainerFactory.java
 *
 * Created on den 4 augusti 2003, 15:51
 *
 * $Log: InputContainerFactory.java,v $
 * Revision 1.3  2004/12/08 14:42:51  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.2  2003/11/11 13:40:52  oloft
 * new file
 *
 * Revision 1.1.2.1  2003/09/08 13:33:02  erichson
 * First check-in.
 *
 *
 */

package medview.medrecords.components;

import medview.medrecords.components.*;
import medview.medrecords.components.inputs.*;

/**
 * Factory class for InputContainers
 *
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 */
public class InputContainerFactory
{
	public static InputContainerPanel createContainerPanel(ValueInputComponent inputComponent)
	{
		return new InputContainerPanel(inputComponent);
	}
}
