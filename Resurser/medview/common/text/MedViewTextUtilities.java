package medview.common.text;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class MedViewTextUtilities
{
	private static final char SECTION_DIVIDER = '$';
	
	public static String removeSectionDividers(String text)
	{
		StringBuffer buffy = new StringBuffer();
		
		for (int offs=0; offs<text.length(); offs++)
		{
			if (text.charAt(offs) != SECTION_DIVIDER)
			{
				buffy.append(text.charAt(offs));
			}
		}
		
		return buffy.toString();
	}
}
