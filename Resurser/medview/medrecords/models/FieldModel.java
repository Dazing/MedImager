/*
 * $Id: FieldModel.java,v 1.13 2007/10/17 15:17:05 it2aran Exp $
 *
 * Created on June 14, 2001, 11:13 PM
 *
 * $Log: FieldModel.java,v $
 * Revision 1.13  2007/10/17 15:17:05  it2aran
 * Medrecords 4.3 Beta 1
 *
 * Revision 1.12  2005/08/23 08:47:20  erichson
 * cosmetic fix
 *
 * Revision 1.11  2004/02/19 18:21:28  lindahlf
 * Major update patch 1
 *
 * Revision 1.10  2003/11/11 14:44:48  oloft
 * Switching main-branch
 *
 * Revision 1.9.2.2  2003/08/14 12:12:53  erichson
 * Removed old unnecessary fields, added isIdentification() method to overridee the one in AbstractInputModel
 *
 * Revision 1.9.2.1  2003/08/07 00:15:22  erichson
 * Updated type constants to be more unique.
 *
 * Revision 1.9  2003/07/23 00:25:26  erichson
 * Had to put the non-fireEvent methods back in.
 *
 * Revision 1.8  2003/07/22 16:43:07  erichson
 * Removed methods where event firing was optional, since this is a very bad long-term solution which might cause confusion.
 *
 * Revision 1.7  2003/07/22 14:45:05  erichson
 * Added mutator methods that do not fire events. Ugly, but the alternative would have been a larger rewrite and Mats wanted the new version quickly.
 *
 */

package medview.medrecords.models;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;

import medview.medrecords.data.*;
import medview.medrecords.models.*;
import medview.medrecords.components.*;

/**
 * Model class that contains all information about how input
 * can be made into an input field. Does NOT handle values
 * anymore.
 *
 * @author  nils
 * @version
 */
public class FieldModel extends AbstractInputModel
{
    public int getFieldType()
    {
        return fieldType;
    }

    public int getLength()
    {
        return length;
    }
    public int getType()
    {
        return InputModel.INPUT_TYPE_TEXTAREA;
    }

    public void setLength(int in_length)
    {
        length = in_length;
    }

    public void setFieldType(int in_type)
    {
        if ((in_type >= FIELD_TYPE_FIRST) && (in_type <= FIELD_TYPE_LAST))
        {
            fieldType = in_type;
        }
        else
        {
            System.err.println("Note: FieldModel.setType(invalid fieldtype)");
        }

    }

    public boolean isIdentification()
    {
        return (getFieldType() == FieldModel.FIELD_TYPE_IDENTIFICATION);
    }

    public static boolean isSingleValueType(int type)
    {
        switch(type)
        {
            case FIELD_TYPE_SINGLE:

            case FIELD_TYPE_INTERVAL:

            case FIELD_TYPE_QUESTION:

            case FIELD_TYPE_NOTE:

            case FIELD_TYPE_IDENTIFICATION:
            {
                return true;
            }

            default:
            {
                return false;
            }
        }
    }

    public FieldModel(String initialName, int in_fieldType, PresetModel in_presetModel, String initialDescription, String initialComment, String initialText,int initialLength)
    {
        super(initialName,initialDescription,initialComment,in_presetModel);

        length = initialLength;

        if ((in_fieldType < FIELD_TYPE_FIRST) || (in_fieldType > FIELD_TYPE_LAST))
        {
            System.err.println("FieldModel(): Error: in_fieldType out of bounds (" + in_fieldType +"), setting to single");

            fieldType = FIELD_TYPE_SINGLE;
        }
        else
        {
            fieldType = in_fieldType;
        }

        putValue(initialText);
    }

    public FieldModel(String initialName, int in_fieldType,PresetModel in_presetModel, String initialDescription, String initialComment, String initialText)
    {
        this(initialName, in_fieldType, in_presetModel, initialDescription, initialComment, initialText, DEFAULT_LENGTH);
    }

    protected int length;

    protected int fieldType;

    public static final int FIELD_TYPE_FIRST = 1;

    public static final int FIELD_TYPE_SINGLE = 1;

    public static final int FIELD_TYPE_MULTI = 2;

    public static final int FIELD_TYPE_NOTE = 3;

    public static final int FIELD_TYPE_INTERVAL = 4;

    public static final int FIELD_TYPE_IDENTIFICATION = 5;

    public static final int FIELD_TYPE_QUESTION = 6;

    public static final int FIELD_TYPE_LAST = 6;

    public static final int DEFAULT_LENGTH = 40;

    protected static final boolean debug = false;

}
