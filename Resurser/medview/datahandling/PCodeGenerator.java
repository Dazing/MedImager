/**
 * @(#) PCodeGenerator.java
 * 
 * $Id: PCodeGenerator.java,v 1.9 2005/02/17 10:23:09 lindahlf Exp $
 * 
 * $Log: PCodeGenerator.java,v $
 * Revision 1.9  2005/02/17 10:23:09  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.8  2004/11/11 22:36:50  lindahlf
 * MedServer Test Pack 2
 *
 * Revision 1.7  2004/11/10 13:03:51  erichson
 * added get/setPCodeNumberGenerator
 *
 */

package medview.datahandling;

import java.io.*;

import misc.foundation.*;

public interface PCodeGenerator
{
	
	// LISTENER ADDITION / REMOVAL
	
	/**
	 * Adds a PCodeGeneratorListener to the generator.
	 * @param l PCodeGeneratorListener the listener to add.
	 */
	public void addPCodeGeneratorListener( PCodeGeneratorListener l );
	
	/**
	 * Removes a PCodeGeneratorListener from the generator.
	 * @param l PCodeGeneratorListener the listener to remove.
	 */
	public void removePCodeGeneratorListener( PCodeGeneratorListener l );
	
	
	// SHUT DOWN NOTIFICATION
	
	/**
	 * Allows the datahandler to deal with the system
	 * shutting down. For instance, if the datahandler is
	 * a client to a server, lets it tell the server that it
	 * is shutting down so the server can remove it from its
	 * notification list.
	 */
	public void shuttingDown();
	
	
	// PID RECOGNITION
	
	/**
	 * Whether or not the specified pid is recognized by this
	 * pcode generator.
	 * @param pid String the pid to check if it is recognized.
	 * @return boolean if the specified pid is recognized.
	 * @throws IOException if, due to IO error, the pid could
	 * not be checked for recognition.
	 */
	public boolean recognizes( String pid ) throws IOException;
	
	
	// PCODE GENERATION
	
	/**
	 * Generates a pcode, you can specify whether or not a number 
	 * from the corresponding number generator location is to be
	 * consumed or remain the same. Notifies a ProgressNotifiable
	 * of the progress. If the pid already has a pcode mapped
	 * to it, no generation takes place but the mapped pcode
	 * will be returned.
	 * @param pid String the pid to obtain a pcode for.
	 * @param consumeNr boolean whether or not to consume a number
	 * from the number generator location.
	 * @param not ProgressNotifiable receives notification of the
	 * progress made.
	 * @return String the obtained pcode, either generated or retrieved
	 * from a previous generation.
	 */
	public String obtainPCode( String pid, boolean consumeNr, ProgressNotifiable not ) throws
		InvalidRawPIDException, CouldNotGeneratePCodeException;
	
	/**
	 * Generates a pcode. Notifies a ProgressNotifiable
	 * of the progress. If the pid already has a pcode mapped
	 * to it, no generation takes place but the mapped pcode
	 * will be returned.
	 * @param pid String the pid to obtain a pcode for.
	 * @param not ProgressNotifiable receives notification of the
	 * progress made.
	 * @return String the obtained pcode, either generated or retrieved
	 * from a previous generation.
	 */
	public String obtainPCode( String pid, ProgressNotifiable not ) throws
		InvalidRawPIDException, CouldNotGeneratePCodeException;
	
	/**
	 * Generates a pcode, you can specify whether or not a number 
	 * from the corresponding number generator location is to be
	 * consumed or remain the same. If the pid already has a pcode mapped
	 * to it, no generation takes place but the mapped pcode
	 * will be returned.
	 * @param pid String the pid to obtain a pcode for.
	 * @param consumeNr boolean whether or not to consume a number
	 * from the number generator location.
	 * @return String the obtained pcode, either generated or retrieved
	 * from a previous generation.
	 */
	public String obtainPCode( String pid, boolean consumeNr ) throws
		InvalidRawPIDException, CouldNotGeneratePCodeException;
	
	/**
	 * Generates a pcode. If the pid already has a pcode mapped
	 * to it, no generation takes place but the mapped pcode
	 * will be returned.
	 * @param pid String the pid to obtain a pcode for.
	 * @return String the obtained pcode, either generated or retrieved
	 * from a previous generation.
	 */
	public String obtainPCode( String pid ) throws
		InvalidRawPIDException, CouldNotGeneratePCodeException;
	
	
	// EXAMINATION DATA LOCATION SETTING
	
	/**
	 * Sets the location from where to fetch information
	 * about examinations (for the PID cache).
	 * @return String the examination data location.
	 */
	public void setExaminationDataLocation( String loc );
	
	/**
	 * Returns the currently set examination data location.
	 * @return String the currently set examination data 
	 * location (for the PID cache).
	 */
	public String getExaminationDataLocation( );
	
	/**
	 * Returns whether or not the examination data location
	 * has been set (for the PID cache).
	 * @return boolean whether or not the examination data location
	 * has been set.
	 */
	public boolean isExaminationDataLocationSet( );
	
	
	// NUMBER GENERATOR
	
	/**
	 * Sets the location from which to obtain 'löp-nummer'.
	 * @param loc String the location from which we obtain
	 * the 'löp-nummer' used to generated unique pcodes.
	 */
	public void setNumberGeneratorLocation(String loc);
	
	/**
	 * Obtains the currently set number generator location.
	 * @return String the currently set number generator
	 * location.
	 */
	public String getNumberGeneratorLocation();
	
	/**
	 * Returns whether or not the number generator location
	 * has been set.
	 * @return boolean if the number generator location has
	 * been set.
	 */
	public boolean isNumberGeneratorLocationSet();
	
	
	// GENERATED PCODE PREFIX
	
	/**
	 * Sets the prefix prepended to generated pcodes.
	 * @param prefix String the prefix prepended to 
	 * generated pcodes.
	 */
	public void setGeneratedPCodePrefix( String prefix );
	
	/**
	 * Obtains the prefix prepended to generated pcodes.
	 * @return String the prefix prepended to generated 
	 * pcodes.
	 */
	public String getGeneratedPCodePrefix( );
	
	/**
	 * Whether or not the prefix prepended to generated
	 * pcodes has been set.
	 * @return boolean if the pcode prefix has been set.
	 */
	public boolean isGeneratedPCodePrefixSet( );
	
        /**
         * Sets the PCodeNumberGenerator to use
         */
        public void setPCodeNumberGenerator(PCodeNumberGenerator numberGenerator) throws
		CouldNotSetPCodeNumberGeneratorException;
        
        /**
         * Gets the current PCodeNumberGenerator 
         */        
        public PCodeNumberGenerator getPCodeNumberGenerator() throws
		CouldNotGetPCodeNumberGeneratorException;
}
