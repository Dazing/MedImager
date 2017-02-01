package minimed.core.datahandling;

/**
 * Defines constants used in the datahandling layer.
 * @author Fredrik Lindahl
 */
public interface MedViewDataConstants
{
	// HINTS

	/**
	 * Constant indicating (for those methods that use it) that
	 * optimization should be done toward efficiency.
	 */
	public static final int OPTIMIZE_FOR_EFFICIENCY = 1;

	/**
	 * Constant indicating (for those methods that use it) that
	 * optimization should be doen for memory-efficiency.
	 */
	public static final int OPTIMIZE_FOR_MEMORY = 2;


	// CLASS CONSTANTS

	/**
	 * The name of the class to use as the default term datahandler
	 * if a datahandler is not explicitly set.
	 */
	public static final String DEFAULT_TERM_DATA_HANDLER_CLASS = "medview.datahandling.ParsedTermDataHandler";

	/**
	 * The name of the class to use as the default examination datahandler
	 * if a datahandler is not explicitly set.
	 */
	public static final String DEFAULT_EXAMINATION_DATA_HANDLER_CLASS = "medview.datahandling.examination.MVDHandler";

	/**
	 * The name of the class to use as the default template and
	 * translator datahandler if a datahandler is not explicitly set.
	 */
	public static final String DEFAULT_TEMPLATE_AND_TRANSLATOR_DATA_HANDLER_CLASS = "se.chalmers.cs.medview.docgen.BasicXMLTemplateAndTranslatorDataHandler";

	/**
	 * The name of the class to use as the default pcode generator
	 * if one is not explicitly set.
	 */
	public static final String DEFAULT_PCODE_GENERATOR_CLASS = "medview.datahandling.LocalSwedishPCodeGenerator";

	/**
	 * The name of the class to use as the example pid generator,
	 * which generates example patient identifier objects.
	 */
	public static final String EXAMPLE_PID_GENERATOR_CLASS = "medview.datahandling.SwedishExamplePIDGenerator";


	// TERM NAME CONSTANTS

	/**
	 * A constant for one of the reserved term names.
	 */
	public static final String PCODE_TERM_NAME = "P-code";

	/**
	 * A constant for one of the reserved term names.
	 */
	public static final String PHOTO_TERM_NAME = "Photo";

	/**
	 * A constant for one of the reserved term names.
	 */
	public static final String USER_ID_TERM_NAME = "User-ID";

	/**
	 * A constant for one of the reserved term names.
	 */
	public static final String USER_NAME_TERM_NAME = "User-name";

	/**
	 * A constant for one of the reserved term names.
	 */
	public static final String CONCRETE_ID_TERM_NAME = "Konkret_identifikation";

	/**
	 * A constant for one of the reserved term names.
	 */
	public static final String DATE_TERM_NAME = "Datum";

	/**
	 * A constant for one of the reserved term names.
	 */
	public static final String PID_TERM_NAME = "PID";


	// MVD STRUCTURE

	/**
	 * A constant for the subdirectory of the forest in an MVD.
	 * NOTE: does not end with a file separator.
	 */
	public static final String MVD_FOREST_SUBDIRECTORY = "Forest.forest";

	/**
	 * A constant for the subdirectory of the pictures in an MVD.
	 * NOTE: does not end with a file separator.
	 */
	public static final String MVD_PICTURES_SUBDIRECTORY = "Pictures";

	/**
	 * A constant for one of the reserved term names.
	 */
	public static final String MVD_TREE_FILE_ENDER = ".tree";

	/**
	 * A constant for an interior design consideration in a tree file.
	 */
	public static final String MVD_NODE_ENDER = "#";

	/**
	 * Constant defining the image file format's file ender
	 * used in an MVD.
	 */
	public static final String MVD_IMAGE_FORMAT_FILE_ENDER = ".jpg";

	/**
	 * Constants defining the prefix placed before nodes in
	 * the tree file format.
	 */
	public static final String MVD_NODE_PREFIX = "N";

	/**
	 * Constant defining the prefix placed before leaves in
	 * the tree file format.
	 */
	public static final String MVD_LEAF_PREFIX = "L";

	/**
	 * Constants defining the suffix of an mvd.
	 */
	public static final String MVD_DIRECTORY_SUFFIX = ".mvd";


	// OTHER

	/**
	 * The default prefix prepended to generated pcodes.
	 */
	public static final String GENERATED_PCODE_PREFIX_DEFAULT = "GPC";

	/**
	 * The name of the mvd cache file.
	 */
	public static final String MVD_CACHE_FILE_NAME = "mvdCache.cch";

}
