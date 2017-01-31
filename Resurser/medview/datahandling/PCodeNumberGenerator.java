package medview.datahandling;

public interface PCodeNumberGenerator
{
	/**
	 * Generates the next counter number to
	 * use for the next generated pcode.
	 */
	public int getNextNumber() throws CouldNotObtainNumberException;

	/**
	 * Generates the next counter number to
	 * use for the next generated pcode. This
	 * version of the method allows you to
	 * specify whether or not a number will be
	 * consumed by the call. If not, the next
	 * call will return the same number.
	 */
	public int getNextNumber(boolean consumeNr) throws CouldNotObtainNumberException;
	
	/**
	 * Sets the location from which the number
	 * generator obtains the numbers.
	 * @param loc String the number generator
	 * location.
	 */
	public void setNumberGeneratorLocation(String loc);
	
	/**
	 * Obtains the currently set number generator
	 * location, or null if no such location has
	 * been set.
	 * @return String the currently set number generator
	 * location, or null if no such location has been
	 * set.
	 */
	public String getNumberGeneratorLocation();
	
	/**
	 * Returns whether or not the number generator
	 * location has been set.
	 * @return boolean if the number generator location
	 * has been set.
	 */
	public boolean isNumberGeneratorLocationSet();
}
