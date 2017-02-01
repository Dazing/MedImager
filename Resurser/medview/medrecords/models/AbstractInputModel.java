/*
 * $Id: AbstractInputModel.java,v 1.6 2007/10/17 15:17:05 it2aran Exp $
 *
 * Created on August 1, 2001, 12:58 PM
 *
 * $Log: AbstractInputModel.java,v $
 * Revision 1.6  2007/10/17 15:17:05  it2aran
 * Medrecords 4.3 Beta 1
 *
 * Revision 1.5  2004/12/08 14:42:48  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.4  2004/03/18 16:37:14  lindahlf
 * Ordnade till PID-format bugg
 *
 * Revision 1.3  2004/02/19 18:21:28  lindahlf
 * Major update patch 1
 *
 * Revision 1.2  2003/11/11 14:44:48  oloft
 * Switching main-branch
 *
 * Revision 1.1.2.2  2003/10/17 10:54:09  oloft
 * added remove methods
 *
 * Revision 1.1.2.1  2003/09/03 22:19:15  erichson
 * First check-in.
 *
 * Revision 1.12  2003/07/23 00:26:22  erichson
 * Added extra putValue() and clear() methods that allowed for no event firing
 *
 * Revision 1.11  2003/07/22 16:47:32  erichson
 * Just javadoc
 *
 */

package medview.medrecords.models;


import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;

import medview.datahandling.*;

import medview.medrecords.data.*;

/**
 * The basic class for InputComponent models.
 *
 * @author Nils Erichson
 * @version
 */
public abstract class AbstractInputModel implements InputModel
{

	public void addChangeListener(ChangeListener cl)
	{
		if (!changeListeners.contains(cl))
		{
			changeListeners.add(cl);
		}
	}

	public void clear()
	{
		clear(true);
	}

	public void clear(boolean fireEvent)
	{
		currentValues.clear();

		if (fireEvent)
		{
			fireStateChanged();
		}
	}


	public void fireStateChanged()
	{
		for (Iterator it = changeListeners.iterator(); it.hasNext(); )
		{
			ChangeListener c = (ChangeListener)it.next();

			c.stateChanged(new ChangeEvent(this));
		}
	}

	public String getComment()
	{
		return new String(comment);
	}
	public String getDescription()
	{
		return new String(description);
	}

	public String getName()
	{
		return new String(name);
	}

	public PresetModel getPresetModel()
	{
		return presetModel;
	}

	public int getType()
	{
		return type;
	}

	public int getValueCount()
	{
		return currentValues.size();
	}

	public String[] getValues()
	{
		String[] values = new String[currentValues.size()];

		values = (String[])currentValues.toArray(values);

		return values;
	}

	public String getValueText()
	{
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < currentValues.size(); i++)
		{
			sb.append((String)currentValues.get(i));

			if (i < (currentValues.size() - 1)) // don't append enter to the last, because that will create a new line!
			{
				sb.append("\n");
			}
		}

		String result = sb.toString();

		return result;
	}

	public void removeBlankValues()
	{
		for (Iterator it = currentValues.iterator(); it.hasNext(); )
		{
			Object o = it.next();

			if (o instanceof String)
			{
				String s = (String)o;

				if (s.trim().equals("")) // if value can be reduced to empty
				{
					if (MRConst.debug)
					{
						System.out.println("stripBlankValues: Removed [" + s + "]");
					}

					it.remove();
				}
			}
		}
	}

	public boolean isEditable()
	{
		return editable;
	}

	public void removeChangeListener(ChangeListener cl)
	{
		changeListeners.remove(cl);
	}

	public void setEditable(boolean isEditable)
	{
		editable = isEditable;
	}

	public void setName(String new_name)
	{
		name = new_name;

		fireStateChanged();
	}

	public void setComment(String in_comment)
	{
		comment = new String(in_comment);

		fireStateChanged();
	}
	public void setDescription(String in_description)
	{
		description = new String(in_description);

		fireStateChanged();
	}

	public void setPresetModel(PresetModel in_presetmodel)
	{
		presetModel = in_presetmodel;
	}


	public void putValue(String in_value)
	{
		putValue(in_value, true);
	}

	public void putValue(String in_value, boolean fireEvent)
	{
		if (!currentValues.contains(in_value))
		{
			currentValues.add(in_value);

			if (fireEvent)
			{
				fireStateChanged();
			}
		}
	}


	public boolean removeValue(String v)
	{
		return removeValue(v, true);
	}

	public boolean removeValue(String v, boolean fireEvent)
	{
		boolean didChange = currentValues.remove(v);

		if (didChange && fireEvent)
		{
			fireStateChanged();
		}

		return didChange;
	}

	public boolean isSorted()
	{
		return sorted;
	}
	public void setSorted(boolean flag)
	{
		sorted = flag;
	}
    public boolean isTranslateAble()
	{
		return translateAble;
	}

	public void setTranslateAble(boolean flag)
	{
		translateAble = flag;
	}


	public boolean isRequired()
	{
		return required;
	}

	public void setRequired(boolean flag)
	{
		required = flag;
	}


	public boolean isIdentification()
	{
		return identificationTerm;
	}

	public void setIdentification(boolean flag)
	{
		identificationTerm = flag;
	}


	public String toString()
	{
		return (getName() + "(" + currentValues.size() + "values)");
	}


	public AbstractInputModel(String initialName, String initialDescription, String initialComment, PresetModel in_presetModel)
	{
		changeListeners = new Vector();

		currentValues = new Vector();

		setName(initialName);

		setDescription(initialDescription);

        setComment(initialComment);

        if (in_presetModel == null)
		{
			setPresetModel(new PresetModel(initialName));
		}
		else
		{
			setPresetModel(in_presetModel);
		}
	}

	protected int type;

	private String name;

	private String description;

    private String comment;

    private boolean editable = true;

	private PresetModel presetModel;

	protected Vector currentValues;

	protected Vector changeListeners;

	private boolean required = false;

	private boolean translateAble = true;

    private boolean sorted = false;

    private boolean identificationTerm = false;

}
