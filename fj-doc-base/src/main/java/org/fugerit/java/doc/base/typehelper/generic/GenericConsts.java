package org.fugerit.java.doc.base.typehelper.generic;

import org.fugerit.java.doc.base.model.DocInfo;

/**
 * <p>Constants catalog for properties shared by most DocTypeHandler.</p>
 * 
 * <p>Basic a listing of "keys" in : &lt;info name="${key}"&gt;${value}&lt;/info&gt;</p>
 * 
 * <p>See {@link DocInfo} for document model object.</p>
 */
public class GenericConsts {

	// Generic properties for all (or most) documents - START
	
	/**
	 * <a href="https://venusdocs.fugerit.org/docs/html/doc_meta_info.html#doc-title">See 'doc-title' documentation</a>
	 */
	public static final String INFO_KEY_DOC_TITLE = "doc-title";
	
	/**
	 * <a href="https://venusdocs.fugerit.org/docs/html/doc_meta_info.html#doc-author">See 'doc-author' documentation</a>
	 */
	public static final String INFO_KEY_DOC_AUTHOR = "doc-author";
	
	/**
	 * <a href="https://venusdocs.fugerit.org/docs/html/doc_meta_info.html#doc-subject">See 'doc-subject' documentation</a>
	 */
	public static final String INFO_KEY_DOC_SUBJECT = "doc-subject";
	
	/**
	 * <a href="https://venusdocs.fugerit.org/docs/html/doc_meta_info.html#doc-version">See 'doc-version' documentation</a>
	 */
	public static final String INFO_KEY_DOC_VERSION = "doc-version";
	
	/**
	 * <a href="https://venusdocs.fugerit.org/docs/html/doc_meta_info.html#doc-creator">See 'doc-creator' documentation</a>
	 */
	public static final String INFO_KEY_DOC_CREATOR = "doc-creator";
	
	/**
	 * <a href="https://venusdocs.fugerit.org/docs/html/doc_meta_info.html#doc-language">See 'doc-language' documentation</a>
	 */
	public static final String INFO_KEY_DOC_LANGUAGE = "doc-language";

	// Generic properties for all (or most) documents - END
	
	
	// Generic properties for fixed size documents (like PDF) - START
	
	/**
	 * <a href="https://venusdocs.fugerit.org/docs/html/doc_meta_info.html#margins">See 'margins' documentation</a>
	 */
	public static final String INFO_KEY_MARGINS = "margins";
	
	public static final int POSITION_MARGIN_LEFT = 0;
	public static final int POSITION_MARGIN_RIGHT = 1;
	public static final int POSITION_MARGIN_TOP = 2;
	public static final int POSITION_MARGIN_BOTTOM = 3;
	
	// Generic properties for fixed size documents (like PDF) - END
	
	
	public static final String INFO_DEFAULT_TABLE_PADDING = "default-table-padding";
	public static final String INFO_DEFAULT_TABLE_PADDING_DEF = "0";
	public static final String INFO_DEFAULT_TABLE_SPACING = "default-table-spacing";
	public static final String INFO_DEFAULT_TABLE_SPACING_DEF = "0";
	
}
