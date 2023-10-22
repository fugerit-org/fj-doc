package org.fugerit.java.doc.base.typehelper.csv;

import org.fugerit.java.doc.base.model.DocInfo;

/**
 * <p>Constants catalog for properties specific to CSV format..</p>
 * 
 * <p>A listing of "keys" in : &lt;info name="${key}"&gt;${value}&lt;/info&gt;</p>
 * 
 * <p>See {@link DocInfo} for document model object.</p>
 */
public class CsvHelperConsts {

	private CsvHelperConsts() {} // java:S1118
	 
	/**
	 * <a href="https://venusdocs.fugerit.org/docs/html/doc_meta_info.html#csv-table-id">See 'csv-table-id' documentation</a>
	 * 
	 * @since 1.4
	 */
	public static final String PROP_CSV_TABLE_ID = "csv-table-id";
	
	/**
	 * <a href="https://venusdocs.fugerit.org/docs/html/doc_meta_info.html#csv-separator">See 'csv-separator' documentation</a>
	 * 
	 * @since 3.1
	 */
	public static final String PROP_CSV_SEPARATOR = "csv-separator";
	
	/**
	 * <a href="https://venusdocs.fugerit.org/docs/html/doc_meta_info.html#csv-line-end">See 'csv-line-end' documentation</a>
	 * 
	 * @since 3.1
	 */
	public static final String PROP_CSV_LINE_END= "csv-line-end";
	
}
