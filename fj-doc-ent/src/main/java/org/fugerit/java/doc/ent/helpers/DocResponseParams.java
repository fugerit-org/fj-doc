package org.fugerit.java.doc.ent.helpers;

import javax.servlet.http.HttpServletResponse;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

public class DocResponseParams implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3146477035874110342L;

	public static final String DEF_CONTANT_TYPE = "application/octet-stream";
	
	private HttpServletRequest request;
	
	private HttpServletResponse response;
	
	private String jspGeneratorPath;
	
	private String fileName;
	
	private boolean inline;

	private String type;
	
	private String contentType;
	
	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public String getJspGeneratorPath() {
		return jspGeneratorPath;
	}

	public void setJspGeneratorPath(String jspGeneratorPath) {
		this.jspGeneratorPath = jspGeneratorPath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public boolean isInline() {
		return inline;
	}

	public void setInline(boolean inline) {
		this.inline = inline;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public DocResponseParams(HttpServletRequest request, HttpServletResponse response, String jspGeneratorPath, String type, String fileName, boolean inline) {
		super();
		this.request = request;
		this.response = response;
		this.jspGeneratorPath = jspGeneratorPath;
		this.type = type;
		this.fileName = fileName;
		this.inline = inline;
		this.contentType = DEF_CONTANT_TYPE;
	}
	
	public static DocResponseParams newParams( HttpServletRequest request, HttpServletResponse response, String jspGeneratorPath, String type, String fileName, boolean inline ) {
		return new DocResponseParams(request, response, jspGeneratorPath, type, fileName, inline);
	}
	
	public static DocResponseParams newParams( HttpServletRequest request, HttpServletResponse response, String jspGeneratorPath, String type ) {
		int index = jspGeneratorPath.lastIndexOf( "/" );
		String fileName = jspGeneratorPath.substring( 0, jspGeneratorPath.length()-3 );
		if ( index != -1 ) {
			fileName = fileName.substring( index+1 );
		}
		fileName+=type;
		boolean inline = true;
		return newParams(request, response, jspGeneratorPath, type, fileName, inline);
	}
	
}
