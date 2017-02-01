/**
 * $Id: DefaultProgressNotifiable.java,v 1.1 2006/05/29 18:33:02 limpan Exp $
 *
 * $Log: DefaultProgressNotifiable.java,v $
 * Revision 1.1  2006/05/29 18:33:02  limpan
 * Added minimed code
 * ----------------------------------------------------------------------
 *
 * Revision 1.2  2004/10/21 12:13:22  erichson
 * Updated with (set/get)Indeterminate since ProgressNotifiable interface was updated.
 *
 */ 

package misc.foundation;

/**
 * A simple implementation of the ProgressNotifiable interface,
 * that allows subclasses to get default behavior used by almost
 * all notifiables. If a subclass overrides one of the methods,
 * it has to call the superclass method prior to doing anything,
 * if things are to work properly.
 * @author Fredrik Lindahl
 */
public class DefaultProgressNotifiable implements ProgressNotifiable
{
	public void setCurrent(int c)
	{
		this.current = c;
	}

	public void setTotal(int t)
	{
		this.total = t;
	}

	public void setDescription(String d)
	{
		this.description = d;
	}

	public int getCurrent()
	{
		return current;
	}

	public int getTotal()
	{
		return total;
	}

	public String getDescription()
	{
		return description;
	}

        public boolean isIndeterminate()
        {
            return indeterminate;
        }
        
        public void setIndeterminate(boolean indeterminate)
        {
            this.indeterminate = indeterminate;
        }
        
	private int total = -1;

	private int current = -1;

	private String description = "Not set";
        
        private boolean indeterminate = false;
}