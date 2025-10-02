
package org.fugerit.java.doc.base.model;

import org.fugerit.java.doc.base.typehelper.generic.GenericConsts;

import lombok.Getter;
import lombok.Setter;

/**
 * Object representing a <code>info</code> tag in the Venus Doc XML Format.
 * 
 * <p><a href="https://venusdocs.fugerit.org/docs/html/doc_meta_info.html">See html documentation.</a></p>
 * 
 * <p>See {@link GenericConsts} for "info" generic properties name keys.</p>
 *
 * @author Matteo Franci a.k.a. Fugerit
 *
 */
public class DocInfo extends DocElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1258119296671751462L;

	public static final String TAG_NAME = "info";
	
	public static final String ATT_NAME = "name";
	
	public DocInfo() {
		this.content = new StringBuilder();
	}
	
	/*
	 * Properties constants have been moved to specific classes (see above).
	 */
	
	// properties for document meta informations
	
	/**
	 * See {@link GenericConsts#INFO_KEY_DOC_TITLE}.
	 */
	public static final String INFO_DOC_TITLE = GenericConsts.INFO_KEY_DOC_TITLE;

	/**
	 * See {@link GenericConsts#INFO_KEY_DOC_AUTHOR}.
	 */
	public static final String INFO_DOC_AUTHOR = GenericConsts.INFO_KEY_DOC_AUTHOR;
	
	/**
	 * See {@link GenericConsts#INFO_KEY_DOC_SUBJECT}.
	 */
	public static final String INFO_DOC_SUBJECT = GenericConsts.INFO_KEY_DOC_SUBJECT;
	
	/**
	 * See {@link GenericConsts#INFO_KEY_DOC_VERSION}.
	 */
	public static final String INFO_DOC_VERSION = GenericConsts.INFO_KEY_DOC_VERSION;
	
	/**
	 * See {@link GenericConsts#INFO_KEY_DOC_CREATOR}.
	 */
	public static final String INFO_DOC_CREATOR = GenericConsts.INFO_KEY_DOC_CREATOR;

    /**
     * See {@link GenericConsts#INFO_KEY_DOC_PRODUCER}.
     */
    public static final String INFO_DOC_PRODUCER = GenericConsts.INFO_KEY_DOC_PRODUCER;
	
	/**
	 * See {@link GenericConsts#INFO_KEY_DOC_LANGUAGE}.
	 */
	public static final String INFO_DOC_LANGUAGE = GenericConsts.INFO_KEY_DOC_LANGUAGE;
	
	// properties specific for HTML format
	public static final String INFO_NAME_CSS_LINK = "html-css-link";
	public static final String INFO_NAME_CSS_STYLE = "html-css-style";
	public static final String INFO_NAME_HTML_ADD_TO_HEAD = "html-add-to-head";
	
	// properties specific for fixed size formats (like PDF)
	public static final String INFO_NAME_MARGINS = GenericConsts.INFO_KEY_MARGINS;
	public static final String INFO_NAME_PAGE_ORIENT = "page-orient";
	public static final String INFO_VALUE_PAGE_ORIENT_PORTRAIT = "vertical";
	public static final String INFO_VALUE_PAGE_ORIENT_LANDSCAPE = "horizontal";
	public static final String INFO_NAME_PAGE_WIDTH = "page-width";
	public static final String INFO_NAME_PAGE_HEIGHT = "page-height";
	public static final String INFO_NAME_PDF_FORMAT = "pdf-format";

	public static final String INFO_DOC_VERSION_COMPATIBILITY = "doc-version-compatibility";
	
	@Getter @Setter private String name;
	
	@Getter @Setter private StringBuilder content;
	
}
