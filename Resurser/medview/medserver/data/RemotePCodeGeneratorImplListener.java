package medview.medserver.data;

import java.util.*;

import medview.datahandling.*;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public interface RemotePCodeGeneratorImplListener extends EventListener
{
	public void pCodeRequested(RemotePCodeGeneratorEvent e);
}
