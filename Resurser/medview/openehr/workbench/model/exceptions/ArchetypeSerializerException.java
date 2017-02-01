//
//  ArchetypeSerializerException.java
//  WorkBench-GUI
//
//  Created by Olof Torgersson on 2008-12-26.
//
//  $Id: ArchetypeSerializerException.java,v 1.1 2008/12/27 23:38:34 oloft Exp $
//

package medview.openehr.workbench.model.exceptions;

public class ArchetypeSerializerException  extends Exception {

	public ArchetypeSerializerException()
	{
		super();
	}

	public ArchetypeSerializerException(String message)
	{
		super(message);
	}

	public ArchetypeSerializerException(Throwable cause)
	{
		super(cause);
	}

	public ArchetypeSerializerException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
