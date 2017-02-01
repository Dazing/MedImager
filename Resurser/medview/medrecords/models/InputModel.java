/*
 * $Id: InputModel.java,v 1.15 2007/10/17 15:17:05 it2aran Exp $
 *
 * Created on August 1, 2001, 12:58 PM
 *
 * $Log: InputModel.java,v $
 * Revision 1.15  2007/10/17 15:17:05  it2aran
 * Medrecords 4.3 Beta 1
 *
 * Revision 1.14  2004/02/19 18:21:28  lindahlf
 * Major update patch 1
 *
 * Revision 1.13  2003/11/11 14:44:48  oloft
 * Switching main-branch
 *
 * Revision 1.12.2.1  2003/08/07 00:18:19  erichson
 * Total overhaul: Made abstract class into interface. Moved implemented methods to AbstractInputModel.
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

import medview.medrecords.data.*;

/**
 * The basic class for InputComponent models.
 *
 * @author Nils Erichson, Nader Nazari
 * @version
 */
public interface InputModel
{

	public static final int INPUT_TYPE_LAST = 3;

	public static final int INPUT_TYPE_FIRST = 1;

	public static final int INPUT_TYPE_PLAQUE = 3;

    public static final int INPUT_TYPE_TRAUMA = 4;

    public static final int INPUT_TYPE_EROSION = 7;

    public static final int INPUT_TYPE_MINERALIZATION = 5;

    public static final int INPUT_TYPE_TEXTAREA = 1;

    public static final int INPUT_TYPE_PICTURECHOOSER = 2;

     public static final int INPUT_TYPE_INFO = 6;


    public void addChangeListener(ChangeListener cl);

    public void clear();

    public void clear(boolean fireEvent);

    public String getDescription();

    public String getComment();

    public String getName();

    public PresetModel getPresetModel();

    public int getType();

    public int getValueCount();

    public String[] getValues();

    public String getValueText();

    public boolean isEditable();

    public void removeChangeListener(ChangeListener cl);

    public void setEditable(boolean isEditable);

    public void setName(String new_name);

    public void setDescription(String in_description);

    public void setPresetModel(PresetModel in_presetmodel);

    public boolean isSorted();

    public void setSorted(boolean flag);

    public boolean isTranslateAble();

    public void setTranslateAble(boolean flag);

    public boolean isRequired();

    public void setRequired(boolean flag);

    public boolean isIdentification();

    public void setIdentification(boolean flag);

    public String toString();

}
