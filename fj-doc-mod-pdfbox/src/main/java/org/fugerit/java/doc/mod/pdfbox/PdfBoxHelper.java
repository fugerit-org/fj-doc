package org.fugerit.java.doc.mod.pdfbox;

import java.util.Properties;

import org.apache.pdfbox.pdmodel.PDDocument;

public class PdfBoxHelper {

	public PdfBoxHelper() {
		this.params = new Properties();
	}
	
	private String defFontName;
	
	private String defFontSize;
	
	private String defFontStyle;
		
	private Properties params;

	public Properties getParams() {
		return params;
	}

	public void setParams(Properties params) {
		this.params = params;
	}

	public String getDefFontName() {
		return defFontName;
	}

	public void setDefFontName(String defFontName) {
		this.defFontName = defFontName;
	}

	public String getDefFontSize() {
		return defFontSize;
	}

	public void setDefFontSize(String defFontSize) {
		this.defFontSize = defFontSize;
	}

	public String getDefFontStyle() {
		return defFontStyle;
	}

	public void setDefFontStyle(String defFontStyle) {
		this.defFontStyle = defFontStyle;
	}
	
	private PDDocument pdfWriter;

	public PDDocument getPdfWriter() {
		return pdfWriter;
	}

	public void setPdfWriter(PDDocument pdfWriter) {
		this.pdfWriter = pdfWriter;
	}
	
}
