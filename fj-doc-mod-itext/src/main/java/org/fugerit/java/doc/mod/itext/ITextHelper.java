package org.fugerit.java.doc.mod.itext;

import java.util.Properties;

import com.lowagie.text.pdf.PdfWriter;

public class ITextHelper {

	public ITextHelper() {
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
	
	private PdfWriter pdfWriter;

	public PdfWriter getPdfWriter() {
		return pdfWriter;
	}

	public void setPdfWriter(PdfWriter pdfWriter) {
		this.pdfWriter = pdfWriter;
	}
	
}
