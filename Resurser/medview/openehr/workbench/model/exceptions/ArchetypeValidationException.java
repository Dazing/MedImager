//
//  ArchetypeValidationException.java
//  WorkBench-GUI
//
//  Created by Olof Torgersson on 2008-12-21.
//
//  $Id: ArchetypeValidationException.java,v 1.1 2008/12/25 12:59:13 oloft Exp $
//

package medview.openehr.workbench.model.exceptions;

public class ArchetypeValidationException extends Exception {

	public ArchetypeValidationException()
	{
		super();
	}

	public ArchetypeValidationException(String message)
	{
		super(message);
	}

	public ArchetypeValidationException(Throwable cause)
	{
		super(cause);
	}

	public ArchetypeValidationException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
