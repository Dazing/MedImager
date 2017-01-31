/*
 * $Id: PictureChooserModel.java,v 1.7 2007/10/17 15:17:05 it2aran Exp $
 *
 * Created on August 6, 2001, 12:36 AM
 *
 * $Log: PictureChooserModel.java,v $
 * Revision 1.7  2007/10/17 15:17:05  it2aran
 * Medrecords 4.3 Beta 1
 *
 * Revision 1.6  2004/12/08 14:42:48  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.5  2004/02/19 18:21:28  lindahlf
 * Major update patch 1
 *
 * Revision 1.4  2003/11/11 14:44:49  oloft
 * Switching main-branch
 *
 * Revision 1.3.2.4  2003/10/22 20:53:24  oloft
 * 4.0b15a
 *
 * Revision 1.3.2.3  2003/10/21 06:25:51  oloft
 * *** empty log message ***
 *
 * Revision 1.3.2.2  2003/10/17 10:55:45  oloft
 * Now actually a meaningful subclass
 *
 * Revision 1.3.2.1  2003/08/14 12:14:01  erichson
 * updated clear methods, extends AbstractInputModel
 *
 */

package medview.medrecords.models;

import java.awt.event.*;

import java.util.*;

import medview.medrecords.models.*;

/**
 *
 * @author  nils
 * @version
 */
public class PictureChooserModel extends AbstractInputModel
{
	public PictureChooserModel()
	{
		super("Main Model", "","", null);
	}

	public void clear(boolean fireEvent)
	{
		num = 0;

		nameMap.clear();

		super.clear(fireEvent);
	}

	public int getDaysAgo()
	{
		return daysAgo;
	}

	public void addChosenImage(String imagePath)
	{
		putValue(imagePath, false);
	}

	public void putValue(String in_value, boolean fireEvent)
	{
		if (!nameMap.containsKey(in_value))
		{
			nameMap.put(in_value, new Integer(num));

			num++;
		}

		super.putValue(in_value, fireEvent);
	}

	public String[] getChosenImagePaths()
	{
		return getValues();
	}

	public Map getValueTags()
	{
		return nameMap;
	}

	public String getImagePath()
	{
		return imagePath;
	}

	public int getImageFormDaysAgo()
	{
		return daysAgo;
	}

	public void removeImage(String pPath)
	{
		removeValue(pPath, false);
	}

	public boolean removeValue(String v, boolean fireEvent)
	{
		nameMap.remove(v);

		return super.removeValue(v, fireEvent);
	}

	public void setImagePath(String in_imagePath)
	{
		imagePath = in_imagePath;

		fireStateChanged();
	}

	public int getType()
	{
		return InputModel.INPUT_TYPE_PICTURECHOOSER;
	}

	public PictureChooserModel(String initialName, String initialDescription, String initialComment, String in_imagePath, int pDaysAgo)
	{
		super(initialName, initialDescription, initialComment, null);

		setImagePath(in_imagePath);

		daysAgo = pDaysAgo;

		num = 0;

		nameMap = new HashMap();
	}

	private int num;

	private Map nameMap;

	private String imagePath;

	private int daysAgo = 0;

}
