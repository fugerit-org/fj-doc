package org.fugerit.java.doc.ent.servlet;

import java.io.OutputStream;
import java.io.StringReader;
import java.text.NumberFormat;

import javax.servlet.http.HttpServletRequest;

import org.fugerit.java.core.log.BasicLogObject;
import org.fugerit.java.doc.base.facade.DocFacade;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.ent.servlet.facade.DocRequestConfig;


public class DocContext extends BasicLogObject {

	private DocRequestConfig docRequestConfig;
	
	public DocContext(DocRequestConfig docRequestConfig) {
		super();
		this.docRequestConfig = docRequestConfig;
	}

	private String fileName;
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	private String name;
	
	private String contentType;
	
	private String encoding;
	
	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String type;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getXmlData() {
		return xmlData;
	}

	public void setXmlData(String xmlData) {
		this.xmlData = xmlData;
	}
	
	private String xmlData;
	
	private OutputStream bufferStream;

	public OutputStream getBufferStream() {
		return bufferStream;
	}

	public void setBufferStream(OutputStream bufferStream) {
		this.bufferStream = bufferStream;
	}

	private static String formatMemory( long mem ) {
		return NumberFormat.getInstance().format( mem );
	}
	
	private static String formatRuntime() {
		return formatMemory( Runtime.getRuntime().freeMemory() ) + " / "+formatMemory( Runtime.getRuntime().totalMemory() );
	} 
	
	public DocBase getDocBase( HttpServletRequest request ) throws Exception {
		DocBase docBase = null;
		if ( this.getXmlData() != null ) {
			docBase = DocFacade.parse( new StringReader( this.getXmlData()  ), this.docRequestConfig.getDocHelper() );
		} else {
			docBase = (DocBase) request.getAttribute( DocHandler.ATT_NAME_DOC );
		}
		return docBase;
	}
	
	public String checkRuntime() {
		return formatRuntime();
	}
	
	
}
