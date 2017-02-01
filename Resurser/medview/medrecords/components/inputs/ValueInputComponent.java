/*
 * ValueInputComponent.java
 *
 * Created on den 4 augusti 2003, 23:30
 *
 * $Id: ValueInputComponent.java,v 1.6 2008/06/12 09:21:22 it2aran Exp $
 *
 * $Log: ValueInputComponent.java,v $
 * Revision 1.6  2008/06/12 09:21:22  it2aran
 * Fixed bug:
 * -------------------------------
 * 413: Scrollar till felaktigt textf�lt om man sparar med felaktigt infyllt textf�lt.
 * 164: Tabbning mellan inputs scrollar hela formul�ret s� att den aktuella inputen alltid �r synlig
 * �vrigt
 * ------
 * Parametrar -Xms128m -Xmx256m skickas till JVM f�r att tilldela mer minne s� att st�rre bilder kan hanteras
 * Mucositkomponenten helt omgjord. Utseendet passar b�ttre in samt att inga nollor sparas om inget �r ifyllt.
 * Drag'n'drop f�r bilder fungerade ej och �r borttaget tills vidare
 * Text i felmeddelandet vid inmatat v�rde utan att trycka p� enter �ndrat
 *
 * Revision 1.5  2005/02/17 10:25:38  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.4  2004/12/08 14:42:56  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.3  2004/06/01 10:36:12  lindahlf
 * no message
 *
 * Revision 1.2  2003/11/11 13:52:33  oloft
 * Switching mainbranch
 *
 * Revision 1.1.2.4  2003/10/18 14:50:46  oloft
 * Builds tree file with new file names
 *
 * Revision 1.1.2.3  2003/10/14 11:55:13  oloft
 * Enabled Shift-Tab
 *
 * Revision 1.1.2.2  2003/10/10 14:48:55  oloft
 * Commented debug println
 *
 * Revision 1.1.2.1  2003/09/08 13:15:06  erichson
 * First check-in
 *
 */

package medview.medrecords.components.inputs;

import java.util.*;

import medview.datahandling.examination.tree.*; // Tree
import medview.medrecords.models.*;

import javax.swing.*;

/**
 * Abstract class which priovides Component-specific functionality to ValueInput
 *
 * @author  nix
 */
public abstract class ValueInputComponent extends AbstractInputComponent
{
	public ValueInputComponent(InputModel inputModel)
	{
		super(inputModel);
	}

	protected void gotoPreviousInput()
	{
		ValueInputComponent preComponent = parentTab.getInputBefore(this);

		if (preComponent != null)
		{
			preComponent.focusInput();
            parentTab.scrollToPanel((JPanel)preComponent.getParent());
        }
	}

	protected void gotoNextInput()
	{
		ValueInputComponent nextComponent = parentTab.getInputAfter(this);

		if (nextComponent != null)
		{
			nextComponent.focusInput();
            parentTab.scrollToPanel((JPanel)nextComponent.getParent());
        }
	}

	/**
	 * Compiles a simple Tree for input types that represent
	 * a single term. Calls getValues() and adds these as
	 * children under a root node whose name is the name of
	 * this input.
	 */
	protected Tree compileSingleTermTreeFromValues()
	{
		String[] values = getValues();

		Tree tree = new TreeBranch(this.getName());

		Tree child;

		for (int i = 0; i < values.length; i++)
		{
			child = new TreeLeaf(values[i]);

			tree.addChild(child);
		}

		return tree;
	}
}
