package org.fugerit.java.doc.base.typehelper.generic;

import org.fugerit.java.doc.base.model.DocElement;
import org.fugerit.java.doc.base.model.DocInfo;

/**
 * <p>Constants catalog for properties shared by most DocTypeHandler.</p>
 * 
 * <p>Basic a listing of "keys" in : &lt;info name="${key}"&gt;${value}&lt;/info&gt;</p>
 * 
 * <p>See {@link DocInfo} for document model object.</p>
 */
public class GenericConsts {

	private GenericConsts() {}
	
	// Generic properties for all (or most) documents - START
	
	/**
	 * <a href="https://venusdocs.fugerit.org/docs/html/doc_meta_info.html#doc-title">See 'doc-title' documentation</a>
	 * 
	 * @since 1.0
	 */
	public static final String INFO_KEY_DOC_TITLE = "doc-title";
	
	/**
	 * <a href="https://venusdocs.fugerit.org/docs/html/doc_meta_info.html#doc-author">See 'doc-author' documentation</a>
	 * 
	 * @since 1.0
	 */
	public static final String INFO_KEY_DOC_AUTHOR = "doc-author";
	
	/**
	 * <a href="https://venusdocs.fugerit.org/docs/html/doc_meta_info.html#doc-subject">See 'doc-subject' documentation</a>
	 * 
	 * @since 1.0
	 */
	public static final String INFO_KEY_DOC_SUBJECT = "doc-subject";
	
	/**
	 * <a href="https://venusdocs.fugerit.org/docs/html/doc_meta_info.html#doc-version">See 'doc-version' documentation</a>
	 * 
	 * @since 1.0
	 */
	public static final String INFO_KEY_DOC_VERSION = "doc-version";
	
	/**
	 * <a href="https://venusdocs.fugerit.org/docs/html/doc_meta_info.html#doc-creator">See 'doc-creator' documentation</a>
	 * 
	 * @since 1.0
	 */
	public static final String INFO_KEY_DOC_CREATOR = "doc-creator";
	
	/**
	 * <a href="https://venusdocs.fugerit.org/docs/html/doc_meta_info.html#doc-language">See 'doc-language' documentation</a>
	 * 
	 * @since 1.0
	 */
	public static final String INFO_KEY_DOC_LANGUAGE = "doc-language";

	// Generic properties for all (or most) documents - END
	

	// Generic properties for tables - START

	/**
	 * <a href="https://venusdocs.fugerit.org/docs/html/doc_meta_info.html#default-table-padding">See 'default-table-padding' documentation</a>
	 * 
	 * @since 1.5
	 */
	public static final String INFO_KEY_DEFAULT_TABLE_PADDING = "default-table-padding";
	
	/**
	 * <a href="https://venusdocs.fugerit.org/docs/html/doc_meta_info.html#default-table-padding">See 'default-table-padding' documentation, default value is '0' (no padding)</a>
	 * 
	 * @since 1.5
	 */
	public static final String INFO_VALUE_DEFAULT_TABLE_PADDING = DocElement.STRING_0;
	
	/**
	 * <a href="https://venusdocs.fugerit.org/docs/html/doc_meta_info.html#default-table-padding">See 'default-table-padding' documentation</a>
	 * 
	 * @since 1.5
	 */
	public static final String INFO_KEY_DEFAULT_TABLE_SPACING = "default-table-spacing";
	
	/**
	 * <a href="https://venusdocs.fugerit.org/docs/html/doc_meta_info.html#default-table-spacing">See 'default-table-spacing' documentation, default value is '0' (no padding)</a>
	 * 
	 * @since 1.5
	 */
	public static final String INFO_VALUE_DEFAULT_TABLE_SPACING = DocElement.STRING_0;
	
	/**
	 * <a href="https://venusdocs.fugerit.org/docs/html/doc_meta_info.html#default-table-spacing">See 'default-table-spacing' documentation</a>
	 * 
	 * @since 1.5
	 */
	public static final String INFO_KEY_DEFAULT_CELL_BORDER_WIDTH = "default-cell-border-width";
	
	/**
	 * <a href="https://venusdocs.fugerit.org/docs/html/doc_meta_info.html#default-cell-border-width">See 'default-cell-border-width' documentation, default value is '-1' (unset)</a>
	 * 
	 * @since 1.5
	 */
	public static final String INFO_VALUE_DEFAULT_CELL_BORDER_WIDTH = DocElement.UNSET;
	
	// Generic properties for tables - END
	
	
	// Generic properties for fixed size documents (like PDF) - START
	
	/**
	 * <a href="https://venusdocs.fugerit.org/docs/html/doc_meta_info.html#margins">See 'margins' documentation</a>
	 */
	public static final String INFO_KEY_MARGINS = "margins";
	
	/**
	 * <a href="https://venusdocs.fugerit.org/docs/html/doc_meta_info.html#margins">See 'margins' documentation, this is the index of the left position in margins [left];[right];[top];[bottom]</a>
	 */
	public static final int POSITION_MARGIN_LEFT = 0;
	
	/**
	 * <a href="https://venusdocs.fugerit.org/docs/html/doc_meta_info.html#margins">See 'margins' documentation, this is the index of the right position in margins [left];[right];[top];[bottom]</a>
	 */
	public static final int POSITION_MARGIN_RIGHT = 1;
	

	/**
	 * <a href="https://venusdocs.fugerit.org/docs/html/doc_meta_info.html#margins">See 'margins' documentation, this is the index of the top position in margins [left];[right];[top];[bottom]</a>
	 */
	public static final int POSITION_MARGIN_TOP = 2;
	

	/**
	 * <a href="https://venusdocs.fugerit.org/docs/html/doc_meta_info.html#margins">See 'margins' documentation, this is the index of the bottom position in margins [left];[right];[top];[bottom]</a>
	 */
	public static final int POSITION_MARGIN_BOTTOM = 3;
	
	// Generic properties for fixed size documents (like PDF) - END
	
	
	/**
	 * Same as {@link GenericConsts#INFO_KEY_DEFAULT_TABLE_PADDING} (left for compatibility)
	 */
	public static final String INFO_DEFAULT_TABLE_PADDING = INFO_KEY_DEFAULT_TABLE_PADDING;
	

	/**
	 * Same as {@link GenericConsts#INFO_VALUE_DEFAULT_TABLE_PADDING} (left for compatibility)
	 */
	public static final String INFO_DEFAULT_TABLE_PADDING_DEF = INFO_VALUE_DEFAULT_TABLE_PADDING;
	

	/**
	 * Same as {@link GenericConsts#INFO_KEY_DEFAULT_TABLE_SPACING} (left for compatibility)
	 */
	public static final String INFO_DEFAULT_TABLE_SPACING = INFO_KEY_DEFAULT_TABLE_SPACING;
	

	/**
	 * Same as {@link GenericConsts#INFO_VALUE_DEFAULT_TABLE_PADDING} (left for compatibility)
	 */
	public static final String INFO_DEFAULT_TABLE_SPACING_DEF = INFO_VALUE_DEFAULT_TABLE_PADDING;
	
}
