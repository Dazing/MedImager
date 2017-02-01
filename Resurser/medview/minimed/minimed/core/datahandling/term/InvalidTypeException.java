package minimed.core.datahandling.term;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: The MedView Project @ Chalmers University of Technology</p>
 *
 * @author Fredrik Lindahl
 * @version 1.0
 */
public class InvalidTypeException extends Exception {
	public InvalidTypeException() {
		super();
	}

	public InvalidTypeException(String message) {
		super(message);
	}

	public InvalidTypeException(Throwable cause) {
		super(cause);
	}

	public InvalidTypeException(String message, Throwable cause) {
		super(message, cause);
	}
}
