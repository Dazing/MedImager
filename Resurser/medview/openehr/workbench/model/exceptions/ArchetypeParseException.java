//
//  ArchetypeParseException.java
//  WorkBench-GUI
//
//  Created by Olof Torgersson on 2008-12-21.
//
//  $Id: ArchetypeParseException.java,v 1.1 2008/12/25 12:59:13 oloft Exp $
//

package medview.openehr.workbench.model.exceptions;

public class ArchetypeParseException extends Exception {

	public ArchetypeParseException()
	{
		super();
	}

	public ArchetypeParseException(String message)
	{
		super(message);
	}

	public ArchetypeParseException(Throwable cause)
	{
		super(cause);
	}

	public ArchetypeParseException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
