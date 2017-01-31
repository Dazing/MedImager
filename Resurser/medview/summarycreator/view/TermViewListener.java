/*
 * @(#)TermViewListener.java
 *
 * $Id: TermViewListener.java,v 1.5 2002/10/12 14:11:07 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.summarycreator.view;

public interface TermViewListener extends java.util.EventListener
{
	public abstract void termChanged(TermViewEvent e);
}