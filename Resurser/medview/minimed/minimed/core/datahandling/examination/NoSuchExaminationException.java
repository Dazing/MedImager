package minimed.core.datahandling.examination;

/** Exception to throw when an Examination cannot be found or doesn't exist.
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 */
public class NoSuchExaminationException extends Exception
{
        /**
        * Creates new <code>NoSuchExaminationException</code> without detail message.
        */
	public NoSuchExaminationException()
	{
		super();
	}

        /** Constructs an <code>NoSuchExaminationException</code> with the specified detail message.
         * @param mess the detail message.
         */
	public NoSuchExaminationException(String mess)
	{
		super(mess);
	}

        public NoSuchExaminationException(ExaminationIdentifier id) {
            super(id.toString());
        }
}