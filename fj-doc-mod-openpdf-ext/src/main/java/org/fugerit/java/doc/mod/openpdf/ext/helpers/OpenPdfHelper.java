package org.fugerit.java.doc.mod.openpdf.ext.helpers;

import java.io.IOException;
import java.util.Properties;

import com.lowagie.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.lang.helpers.BooleanUtils;
import org.fugerit.java.doc.base.typehelper.generic.GenericConsts;

@Slf4j
public class OpenPdfHelper {

	public OpenPdfHelper() {
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

	public void handelError( String message ) throws IOException {
		if (BooleanUtils.isTrue( this.getParams().getProperty(GenericConsts.DOC_SUPPRESS_WRONG_TYPE_ERROR ) ) ) {
			log.warn( "Suppressed type error : {}", message );
		} else {
			throw new IOException(message);
		}
	}
	
}
