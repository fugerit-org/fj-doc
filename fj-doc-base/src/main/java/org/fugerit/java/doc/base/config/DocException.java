package org.fugerit.java.doc.base.config;

import org.fugerit.java.core.lang.ex.ExConverUtils;

public class DocException extends Exception {

	public static final String DEFAULT_CODE = "DOC001";
	
	private final String code;
	
	public DocException() {
		super();
		this.code = DEFAULT_CODE;
	}

	public DocException(String message, Throwable cause) {
		super(message, cause);
		this.code = DEFAULT_CODE;
	}

	public DocException(String message) {
		super(message);
		this.code = DEFAULT_CODE;
	}

	public DocException(Throwable cause) {
		super(cause);
		this.code = DEFAULT_CODE;
	}

	public DocException(final String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public String getCode() {
		return code;
	}
	
	public static DocException convertEx( String baseMessage, Exception e ) {
		DocException res = null;
		if ( e instanceof DocException ) {
			res = (DocException)e;
		} else {
			res = new DocException( ExConverUtils.defaultMessage(baseMessage, e), e );
		}
		return res;
	}
	
	public static DocException convertExMethod( String method, Exception e ) {
		return convertEx( ExConverUtils.defaultMethodMessage(method), e );
	}
	
	public static DocException convertEx( Exception e ) {
		return convertEx( ExConverUtils.DEFAULT_CAUSE_MESSAGE, e );
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8872968598674596827L;
	
}
