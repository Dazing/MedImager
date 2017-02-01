package medview.datahandling;

public interface RawPIDValidator
{
	/**
	 * Validates the specified pid string, i.e. returns
	 * whether or not the pid string is recognized as
	 * being in a valid pid format.
	 */
	public abstract boolean validates(String pid);

	/**
	 * Converts the specified pid to a 'normalized' format.
	 */
	public abstract String normalizePID(String pid) throws
		InvalidRawPIDException;
}