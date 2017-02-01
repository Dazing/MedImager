/*
 * @(#)MedViewTextConstants.java
 *
 * $Id: MedViewTextConstants.java,v 1.4 2003/04/10 01:49:16 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.text;

/**
	Contains static constants for identifying various text actions in use
	in the MedView program system (MedSummary - SummaryCreator - InterfaceCreator -
	MedRecords). The MedViewTextActions class contains implementations of the
	actions identified by the constants in this class.
	@author Fredrik Lindahl
*/
public interface MedViewTextConstants
{
	/** Styled Editor Kit (SEK) action identifier - sets font family to SansSerif. */
	public static final String SEK_FONT_FAMILY_SANSSERIF_ACTION = "font-family-SansSerif";

	/** Styled Editor Kit (SEK) action identifier - sets font family to MonoSpaced. */
	public static final String SEK_FONT_FAMILY_MONOSPACED_ACTION = "font-family-Monospaced";

	/** Styled Editor Kit (SEK) action identifier - sets font family to Serif. */
	public static final String SEK_FONT_FAMILY_SERIF_ACTION = "font-family-Serif";



	/** Styled Editor Kit (SEK) action identifier - sets font size to 8. */
	public static final String SEK_FONT_SIZE_8_ACTION = "font-size-8";

	/** Styled Editor Kit (SEK) action identifier - sets font size to 10. */
	public static final String SEK_FONT_SIZE_10_ACTION = "font-size-10";

	/** Styled Editor Kit (SEK) action identifier - sets font size to 11. */
	public static final String SEK_FONT_SIZE_11_ACTION = "font-size-11";

	/** Styled Editor Kit (SEK) action identifier - sets font size to 12. */
	public static final String SEK_FONT_SIZE_12_ACTION = "font-size-12";

	/** Styled Editor Kit (SEK) action identifier - sets font size to 14. */
	public static final String SEK_FONT_SIZE_14_ACTION = "font-size-14";

	/** Styled Editor Kit (SEK) action identifier - sets font size to 16. */
	public static final String SEK_FONT_SIZE_16_ACTION = "font-size-16";

	/** Styled Editor Kit (SEK) action identifier - sets font size to 18. */
	public static final String SEK_FONT_SIZE_18_ACTION = "font-size-18";

	/** Styled Editor Kit (SEK) action identifier - sets font size to 24. */
	public static final String SEK_FONT_SIZE_24_ACTION = "font-size-24";

	/** Styled Editor Kit (SEK) action identifier - sets font size to 36. */
	public static final String SEK_FONT_SIZE_36_ACTION = "font-size-36";

	/** Styled Editor Kit (SEK) action identifier - sets font size to 48. */
	public static final String SEK_FONT_SIZE_48_ACTION = "font-size-48";



	/** Styled Editor Kit (SEK) action identifier - sets paragraph adjustment to left. */
	public static final String SEK_LEFT_JUSTIFY_ACTION = "left-justify";

	/** Styled Editor Kit (SEK) action identifier - sets paragraph adjustment to center. */
	public static final String SEK_CENTER_JUSTIFY_ACTION = "center-justify";

	/** Styled Editor Kit (SEK) action identifier - sets paragraph adjustment to right. */
	public static final String SEK_RIGHT_JUSTIFY_ACTION = "right-justify";



	/** Styled Editor Kit (SEK) action identifier - sets font to bold. */
	public static final String SEK_BOLD_ACTION = "font-bold";

	/** Styled Editor Kit (SEK) action identifier - sets font to bold. */
	public static final String SEK_ITALIC_ACTION = "font-italic";

	/** Styled Editor Kit (SEK) action identifier - sets font to bold. */
	public static final String SEK_UNDERLINE_ACTION = "font-underline";



	/** Styled Editor Kit (SEK) action identifier - cuts selection to clipboard. */
	public static final String SEK_CUT_ACTION = "cut-to-clipboard";

	/** Styled Editor Kit (SEK) action identifier - copies selection to clipboard. */
	public static final String SEK_COPY_ACTION = "copy-to-clipboard";

	/** Styled Editor Kit (SEK) action identifier - pastes clipboard content. */
	public static final String SEK_PASTE_ACTION = "paste-from-clipboard";



	/** Custom action identifier - sets font to superscript. */
	public static final String TEXT_SUPERSCRIPT_ACTION = "textSuperscript";

	/** Custom action identifier - sets font to subscript. */
	public static final String TEXT_SUBSCRIPT_ACTION = "textSubscript";

	/** Custom action identifier - sets font to strikethrough. */
	public static final String TEXT_STRIKETHROUGH_ACTION = "textStrikethrough";



	/** Custom action identifier - sets font color. */
	public static final String CHOOSE_COLOR_ACTION = "chooseColor";



	/** Java standard String identifier for the SansSerif font. */
	public static final String JAVA_SANSSERIF_STRING = "SansSerif";

	/** Java standard String identifier for the MonoSpaced font. */
	public static final String JAVA_MONOSPACED_STRING = "MonoSpaced";

	/** Java standard String identifier for the Serif font. */
	public static final String JAVA_SERIF_STRING = "Serif";
}