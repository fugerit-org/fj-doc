package org.fugerit.java.doc.base.typehelper.excel;

import org.fugerit.java.doc.base.model.DocInfo;

/**
 * <p>Constants catalog for properties specific to spreadsheet format (XLS/XLSX).</p>
 * 
 * <p>A listing of "keys" in : &lt;info name="${key}"&gt;${value}&lt;/info&gt;</p>
 * 
 * <p>See {@link DocInfo} for document model object.</p>
 */
public class ExcelHelperConsts {

	private ExcelHelperConsts() {} // java:S1118
	
	/**
	 * <a href="https://venusdocs.fugerit.org/docs/html/doc_meta_info.html#excel-table-id">See 'excel-table-id' documentation</a>
	 * 
	 * @since 1.0
	 */
	public static final String PROP_XLS_TABLE_ID = "excel-table-id";
	
	/**
	 * <a href="https://venusdocs.fugerit.org/docs/html/doc_meta_info.html#excel-try-autoresize">See 'excel-try-autoresize' documentation</a>
	 * 
	 * @since 1.0
	 */
	public static final String PROP_XLS_TRY_AUTORESIZE = "excel-try-autoresize";
	public static final String PROP_XLS_TRY_AUTORESIZE_FALSE = "false";
	public static final String PROP_XLS_TRY_AUTORESIZE_TRUE = "true";
	public static final String PROP_XLS_TRY_AUTORESIZE_DEFAULT = PROP_XLS_TRY_AUTORESIZE_FALSE;
	
	/**
	 * <a href="https://venusdocs.fugerit.org/docs/html/doc_meta_info.html#excel-fail-on-autoresize-error">See 'excel-fail-on-autoresize-error' documentation</a>
	 * 
	 * @since 1.0
	 */
	public static final String PROP_XLS_FAIL_ON_AUTORESIZE_ERROR = "excel-fail-on-autoresize-error";
	public static final String PROP_XLS_FAIL_ON_AUTORESIZE_ERROR_FALSE = "false";
	public static final String PROP_XLS_FAIL_ON_AUTORESIZE_ERROR_TRUE = "true";
	public static final String PROP_XLS_FAIL_ON_AUTORESIZE_ERROR_DEFAULT = PROP_XLS_FAIL_ON_AUTORESIZE_ERROR_TRUE;
	
	/**
	 * <a href="https://venusdocs.fugerit.org/docs/html/doc_meta_info.html#excel-ignore-format">See 'excel-ignore-format' documentation</a>
	 * 
	 * @since 1.0
	 */
	public static final String PROP_XLS_IGNORE_FORMAT = "excel-ignore-format";
	
	/**
	 * <a href="https://venusdocs.fugerit.org/docs/html/doc_meta_info.html#excel-template">See 'excel-template' documentation</a>
	 * 
	 * @since 1.0
	 */
	public static final String PROP_XLS_TEMPLATE = "excel-template";
	
	/**
	 * <a href="https://venusdocs.fugerit.org/docs/html/doc_meta_info.html#excel-width-multiplier">See 'excel-width-multiplier' documentation</a>
	 * 
	 * @since 1.0
	 */
	public static final String PROP_XLS_WIDTH_MULTIPLIER = "excel-width-multiplier";
	public static final String PROP_XLS_WIDTH_MULTIPLIER_DEFAULT = "256";
	
}
