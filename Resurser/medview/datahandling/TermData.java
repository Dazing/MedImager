package medview.datahandling;

import java.io.*;

import java.util.*;

/**
 * A simple object containing information about
 * a term kept on the server.
 * @author Fredrik Lindahl
 */
public class TermData implements Serializable
{
	/**
	 * Sets the term type.
	 */
	public void setType(int type)
	{
		this.type = type;
	}

	/**
	 * Obtains the term type.
	 */
	public int getType()
	{
		return type;
	}


	/**
	 * Obtains the values for the term.
	 */
	public String[] getValues()
	{
		String[] retArr = new String[values.size()];

		values.toArray(retArr);

		return retArr;
	}

	/**
	 * Adds a value to the value list for the term.
	 */
	public void addValue(Object value)
	{
		if (!values.contains(value))
		{
			values.add(value + "");
		}
	}

	/**
	 * Removes a value from the value list for the term.
	 */
	public void removeValue(Object value)
	{
		if (values.contains(value))
		{
			values.remove(value + "");
		}
	}

	/**
	 * Removes any currently kept values for the
	 * term and sets them to the specified ones.
	 */
	public void setValues(Object[] vals)
	{
		values.clear();

		for (int ctr=0; ctr<vals.length; ctr++)
		{
			values.add(vals[ctr] + "");
		}
	}


	/**
	 * Constructs a term data object for the
	 * specified term.
	 */
	public TermData(String term)
	{
		this.term = term;
	}

	private int type;

	private String term;

	private Vector values = new Vector();
}
