package org.fugerit.java.doc.base.config;

import lombok.Getter;

public class DocConfig {
	
	@Getter private String versionConfig;
	
	public DocConfig() {
		this.versionConfig = VERSION;
	}
	
	public static final String VERSION = " FUGERIT DOC Version 2.1 (2023-08-19) ";
	
	public static final String TYPE_XML = "xml";
	
	public static final String TYPE_PDF = "pdf";
	public static final String FORMAT_PDF_A_1A = "PDF/A-1a";
	public static final String FORMAT_PDF_A_1B = "PDF/A-1b";
	
	public static final String TYPE_RTF = "rtf";
	
	public static final String TYPE_HTML = "html";
	
	public static final String TYPE_HTML_FRAGMENT = "fhtml";
	
	public static final String TYPE_XLS = "xls";
	
	public static final String TYPE_XLSX = "xlsx";
	
	public static final String TYPE_FO = "fo";
	
	public static final String TYPE_MD = "md";
	
	public static final String TYPE_CSV = "csv";
	
	public static final String DOC_VERSION_COMPATIBILITY_2_X = "2-x";
	
	public static final String DOC_VERSION_COMPATIBILITY_1_X = "1-x";
	
	public static final String DOC_VERSION_COMPATIBILITY_DEFAULT = DOC_VERSION_COMPATIBILITY_2_X;
	
}
