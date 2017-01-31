/**
 * $Id: LocalSwedishPCodeGenerator.java,v 1.9 2008/08/25 09:23:17 it2aran Exp $
 *
 * $Log: LocalSwedishPCodeGenerator.java,v $
 * Revision 1.9  2008/08/25 09:23:17  it2aran
 * T4 Server updates so it loads the mvdlocation from the package
 * Visualizer: Chosen terms doesn't have to be sorted alpabetically
 * Visualizer: Can load and save chosen terms
 * Updated the release notes
 *
 * Revision 1.8  2005/06/03 15:46:23  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.7  2005/03/16 13:52:41  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.6  2005/02/17 10:23:09  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.5  2004/12/08 14:47:20  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.4  2004/11/11 22:36:49  lindahlf
 * MedServer Test Pack 2
 *
 * Revision 1.3  2004/11/10 13:04:56  erichson
 * added get/setPCodeNumberGenerator, and a constructor with prefix and numbergenerator as args
 *
 */

package medview.datahandling;

import java.io.*;

import javax.swing.event.*;

import medview.datahandling.examination.*;

import misc.foundation.*;

public class LocalSwedishPCodeGenerator implements PCodeGenerator
{
	// EVENTS

	public void addPCodeGeneratorListener(PCodeGeneratorListener l)
	{
		this.listenerList.add(PCodeGeneratorListener.class, l);
	}

	public void removePCodeGeneratorListener(PCodeGeneratorListener l)
	{
		this.listenerList.remove(PCodeGeneratorListener.class, l);
	}

	protected void firePrefixChanged()
	{
		final Object[] listeners = this.listenerList.getListenerList();

		final PCodeGeneratorEvent event = new PCodeGeneratorEvent(this);

		event.setPrefix(this.prefix);

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == PCodeGeneratorListener.class)
			{
				((PCodeGeneratorListener)listeners[i+1]).prefixChanged(event);
			}
		}
	}

	protected void fireLocationChanged()
	{
		final Object[] listeners = this.listenerList.getListenerList();

		final PCodeGeneratorEvent event = new PCodeGeneratorEvent(this);

		event.setLocation(this.currentMVDLocation);

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == PCodeGeneratorListener.class)
			{
				((PCodeGeneratorListener)listeners[i+1]).locationChanged(event);
			}
		}
	}


	/**
	 * Allows the datahandler to deal with the system
	 * shutting down. For instance, if the datahandler is
	 * a client to a server, lets it tell the server that it
	 * is shutting down so the server can remove it from its
	 * notification list.
	 */
	public void shuttingDown()
	{
	}


	// PID RECOGNITION

	public boolean recognizes(String pid)
	{
		if (this.swedishPNRPIDValidator.validates(pid))
		{
			return true;
		}
		else if (this.oldPCodeFormatRawPIDValidator.validates(pid))
		{
			return true;
		}
		else if (this.newPCodeFormatRawPIDValidator.validates(pid))
		{
			return true;
		}
		else
		{
			return false;
		}
	}


	// GENERATED PCODE PREFIX

	public void setGeneratedPCodePrefix(String p)
	{
		if ((this.prefix == null) || !this.prefix.equals(p))
		{
			this.prefix = p;

			firePrefixChanged();
		}
	}

	public String getGeneratedPCodePrefix()
	{
		return this.prefix;
	}

	public boolean isGeneratedPCodePrefixSet()
	{
		return (this.prefix != null);
	}


	// EXAMINATION DATA LOCATION

	public void setExaminationDataLocation(String loc)
	{
		this.currentMVDLocation = loc;

		fireLocationChanged();
	}

	public String getExaminationDataLocation()
	{
		return this.currentMVDLocation;
	}

	public boolean isExaminationDataLocationSet()
	{
		return (this.currentMVDLocation != null);
	}


	// NUMBER GENERATOR

	public void setNumberGeneratorLocation(String loc)
	{
		this.numberGenerator.setNumberGeneratorLocation(loc); // TODO: add listener to PCNRGEN to pass forward this event
	}

	public String getNumberGeneratorLocation()
	{
		return this.numberGenerator.getNumberGeneratorLocation();
	}

	public boolean isNumberGeneratorLocationSet()
	{
		return this.numberGenerator.isNumberGeneratorLocationSet();
	}

        /**
         * Sets the PCodeNumberGenerator to use
         */
        public void setPCodeNumberGenerator(PCodeNumberGenerator numberGenerator)
        {
            this.numberGenerator = numberGenerator;
        }

        /**
         * Gets the current PCodeNumberGenerator
         */
        public PCodeNumberGenerator getPCodeNumberGenerator()
        {
            return this.numberGenerator;
        }


	// PCODE GENERATION

	public String obtainPCode(String pid, ProgressNotifiable not) throws
		InvalidRawPIDException, CouldNotGeneratePCodeException
	{
		return this.obtainPCode(pid, true, not);
	}

	public String obtainPCode(String pid) throws
		InvalidRawPIDException, CouldNotGeneratePCodeException
	{
		return this.obtainPCode(pid, true, null);
	}

	public String obtainPCode(String pid, boolean consumeNr) throws
		InvalidRawPIDException, CouldNotGeneratePCodeException
	{
		return this.obtainPCode(pid, consumeNr, null);
	}

	public String obtainPCode(String pid, boolean consumeNr, ProgressNotifiable not) throws
		InvalidRawPIDException, CouldNotGeneratePCodeException
	{
		// check that the pid is not already in pcode format

		if (newPCodeFormatRawPIDValidator.validates(pid))
		{
			return pid; // pid is in new pcode format already
		}
		else if (oldPCodeFormatRawPIDValidator.validates(pid))
		{
			return pid; // pid is in old pcode format already
		}

		// check that prefix and number generator location are set and valid

		if (!numberGenerator.isNumberGeneratorLocationSet())
		{
			throw new CouldNotGeneratePCodeException("Number Generator location not set!");
		}
		else if (!isGeneratedPCodePrefixSet())
		{
			throw new CouldNotGeneratePCodeException("Prefix in generated pcodes not set!");
		}

		// start processing (but only if the pid is in swedish pid form)

		if (swedishPNRPIDValidator.validates(pid))
		{
			// normalize the pid (ex 197704032222 -> 19770403-2222)

			pid = swedishPNRPIDValidator.normalizePID(pid);

			// obtain and return from cache if the pid has a mapping there already

			try
			{
				String pCode = MVDCacheHandler.instance().getCorrespondingPCode(pid, new File(currentMVDLocation), null); // -> IOException

				if (pCode != null)
				{
					return pCode; // the pid corresponds to this pcode from earlier data
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();

				throw new CouldNotGeneratePCodeException(e.getMessage());
			}

			// do the actual generation of a new pcode based on the pid

			String generatedPCode = prefix; // append prefix

			int number = -1;

			try
			{
				number = numberGenerator.getNextNumber(consumeNr); // get running number (löpnummer) from gen
			}
			catch (CouldNotObtainNumberException exc)
			{
				exc.printStackTrace();

				throw new CouldNotGeneratePCodeException("Could not generate running number!");
			}

			String nrS = number + "";

			while (nrS.length() != 6)
			{
				nrS = "0" + nrS; // prepend 0's
			}

			generatedPCode = generatedPCode + nrS;

			if (pid.substring(0,2).equalsIgnoreCase("19")) // convert to pcode format year
			{
				generatedPCode = generatedPCode + "9";
			}
			else
			{
				generatedPCode = generatedPCode + "0";
			}

			generatedPCode = generatedPCode + pid.substring(2,4); // append year to generated pcode

			String lIBNS = pid.substring(11,12); // start processing of gender of pnr

			String[] even = new String[] {"0", "2", "4", "6", "8"};

			int gender = 1; // 1 = male, 0 = female

			for (int ctr=0; ctr<even.length; ctr++)
			{
				if (lIBNS.equalsIgnoreCase(even[ctr])) // check if control number is even (f)
				{
					gender = 0;

					break;
				}
			}

			generatedPCode = generatedPCode + gender; // add gender number to end

			return generatedPCode; // return the generated pcode
		}
		else
		{
			throw new InvalidRawPIDException("Could not recognize pid " + pid);
		}
	}

        /** CONSTRUCTORS */

	public LocalSwedishPCodeGenerator()
	{
		listenerList = new EventListenerList();

		numberGenerator = new LockFilePCodeNumberGenerator();

		oldPCodeFormatRawPIDValidator = new OldPCodeFormatRawPIDValidator();

		newPCodeFormatRawPIDValidator = new NewPCodeFormatRawPIDValidator();

		swedishPNRPIDValidator = new SwedishPNRRawPIDValidator();
	}

        /**
         * Constructs a LocalSwedishPCodeGenerator with a certain PCode prefix and PCodeNumberGenerator
         */
        public LocalSwedishPCodeGenerator(String pcodePrefix, PCodeNumberGenerator numberGenerator)
        {
            this();

            setGeneratedPCodePrefix(pcodePrefix);

            setPCodeNumberGenerator(numberGenerator);
        }

	private String prefix = null;

	private String currentMVDLocation = null;

	protected EventListenerList listenerList;

	protected PCodeNumberGenerator numberGenerator;

	protected RawPIDValidator oldPCodeFormatRawPIDValidator;

	protected RawPIDValidator newPCodeFormatRawPIDValidator;

	protected RawPIDValidator swedishPNRPIDValidator;

	protected static final String DEBUG_PREFIX = "LocalSwedishPCodeGenerator> ";


	// UNIT TEST

	public static void main(String[] args)
	{
	}

}
