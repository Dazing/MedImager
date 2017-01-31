/**
 * @(#) NotifyingRunnable.java
 */

package misc.foundation;

/**
 * A notifying runnable is a Runnable implementation
 * that notifies some ProgressNotifiable instance of
 * progress. When the implementation classes are to
 * notify of progress, they should notify the kept
 * notifiable instance which is protected and thus
 * accessible to the subclasses. A benefit of using
 * such an approach is that the notifiable is
 * 'pluggable' - you can switch notifiable and then
 * run the runnable again on a new thread and receive
 * a different kind of notification of the progress.
 *
 * @author Fredrik Lindahl
 */
public abstract class NotifyingRunnable implements Runnable
{
	/**
	 * Set the progress notifiable.
	 * @param n ProgressNotifiable
	 */
	public void setNotifiable( ProgressNotifiable n )
	{
		this.notifiable = n;
	}

	/**
	 * Obtain a reference to the progress notifiable.
	 * @return ProgressNotifiable
	 */
	public ProgressNotifiable getNotifiable()
	{
		return notifiable;
	}

	protected ProgressNotifiable notifiable;

}
