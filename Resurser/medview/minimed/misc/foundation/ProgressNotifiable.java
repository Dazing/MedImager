/**
 * @(#) ProgressNotifiable.java
 *
 * $Id: ProgressNotifiable.java,v 1.1 2006/05/29 18:33:03 limpan Exp $
 *
 * $Log: ProgressNotifiable.java,v $
 * Revision 1.1  2006/05/29 18:33:03  limpan
 * Added minimed code
 * ----------------------------------------------------------------------
 *
 * Revision 1.5  2005/06/03 16:05:42  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.2  2005/04/01 11:03:24  lindahlf
 * Added header and class documentation
 *
 * Revision 1.1  2005/04/01 09:55:11  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.4  2004/10/21 12:14:11  erichson
 * Added getIndeterminate and setIndeterminate.
 *
 */

package misc.foundation;

/**
 * A ProgressNotifiable can receive progress updates
 * from some time-consuming process or from some other
 * process where it is informative to know progress.
 * Attached to the current value of progress is a
 * description describing what is currently being done
 * to increase progress. The implementation classes can
 * do with the received values what they wish - for instance,
 * a dialog implementation might display the progress in a
 * progress bar.<br>
 * <br>
 * It is perfectly legal to set/reset the total value
 * and current value several times during one time-consuming
 * operation. For instance, say you are to retrieve five
 * objects from some network connection, and each of these
 * five objects contains a various amount of subobjects which
 * also takes time to retrieve. You could first set the total
 * to the number of subobjects for the first object and increase
 * the current value for each read subobject along with setting
 * a descriptive text indicating that you are currently reading
 * the subobjects for the first object. After all first object
 * subobjects have been read, you set the total to the number
 * of subobjects for the second object, and reset the current value
 * to zero along with setting a description that you are now to
 * read the subobjects for the second object. Then you increase
 * the current value for each second object subobject read. And
 * so on...
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Main Project: The MedView Project @ Chalmers University of Technology</p>
 *
 * <p>Project Web: http://www.cs.chalmers.se/proj/medview/website/medview/</p>
 *
 * @author Fredrik Lindahl
 * @version 1.0
 */
public interface ProgressNotifiable
{

	/**
	 * Sets the current value of progress. This value is
	 * considered in regard to the total value set by the
	 * setTotal() method.
	 * @param c the current value of progress to be set.
	 */
	public void setCurrent(int c);

	/**
	 * Sets the total value of progress. When the current
	 * value reaches this value, the current progress is
	 * finished, as indicated by the description set by the
	 * progress handler.
	 * @param t the total value of progress to be set.
	 */
	public void setTotal(int t);

	/**
	 * Sets the current value of progress. This value is
	 * considered in regard to the total value set by the
	 * setTotal() method.
	 * @param d description of current progress action.
	 */
	public void setDescription(String d);

	/**
	 * Returns the current value of progress. This could be
	 * used by methods updating a progress notifiable and
	 * that only knows that they have increased the current
	 * value by one.
	 */
	public int getCurrent();

	/**
	 * Returns the total value of progress. When the current
	 * value reaches this value, the current progress is
	 * finished, as indicated by the description set by the
	 * progress handler.
	 */
	public int getTotal();

	/**
	 * Returns the description currently set for the current
	 * progress made in the progress notifiable.
	 */
	public String getDescription();

        /**
         * Gets the <code>indeterminate</code> property of the progress bar, which determines whether the progress bar is in determinate or indeterminate mode.
         * @see javax.swing.JProgressBar#setIndeterminate(boolean)
         * @return the value of the <code>indeterminate</code> property
         */
        public boolean isIndeterminate();

        /**
         * Sets the <code>indeterminate</code> property of the progress bar, which determines whether the progress bar is in determinate or indeterminate mode.
         * @see javax.swing.JProgressBar#setIndeterminate(boolean)
         */
        public void setIndeterminate(boolean indeterminate);
}
