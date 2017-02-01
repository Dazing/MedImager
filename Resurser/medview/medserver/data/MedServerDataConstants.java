package medview.medserver.data;

/**
 * An interface defining constants used in
 * the MedServer communication classes.
 * @author Fredrik Lindahl
 */
public interface MedServerDataConstants
{
	/**
	 * The name by which the remote term datahandler is bound
	 * in the server-side rmi registry.
	 */
	public static final String REMOTE_TERM_DATAHANDLER_BOUND_NAME = "remoteTermDataHandler";

	/**
	 * The name by which the remote examination datahandler is
	 * bound in the server-side rmi registry.
	 */
	public static final String REMOTE_EXAMINATION_DATAHANDLER_BOUND_NAME = "remoteExaminationDataHandler";
	
	/**
	 * The name by which the remote pcode generator is bound in
	 * the server-side rmi registry.
	 */
	public static final String REMOTE_PCODE_GENERATOR_BOUND_NAME = "remotePCodeGenerator";
}
