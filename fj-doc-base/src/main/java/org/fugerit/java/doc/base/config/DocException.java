package org.fugerit.java.doc.base.config;

public class DocException extends Exception {

	public static final String DEFAULT_CODE = "DOC001";
	
	private String code = DEFAULT_CODE;
	
	public DocException() {
		super();
	}

	public DocException(String message, Throwable cause) {
		super(message, cause);
	}

	public DocException(String message) {
		super(message);
	}

	public DocException(Throwable cause) {
		super(cause);
	}

	public DocException(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -8872968598674596827L;
	
}
