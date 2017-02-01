package misc.foundation;

/**
 * A simply extension of the DefaultProgressNotifiable
 * class, that prints progress to the System.out stream.
 * This can be very useful when debugging.
 * @author Fredrik Lindahl
 */
public class SystemOutputProgressNotifiable extends DefaultProgressNotifiable
{
	public void setCurrent(int c)
	{
		super.setCurrent(c);

		printStuff();
	}

	public void setTotal(int t)
	{
		super.setTotal(t);

		printStuff();
	}

	public void setDescription(String d)
	{
		super.setDescription(d);

		printStuff();
	}

	private void printStuff()
	{
		String a = "[" + getDescription() + "] ";

		String b = "(" + getCurrent() + "/";

		String c = getTotal() + ")";

		System.out.println(a + b + c);
	}
}