package org.fugerit.java.doc.playground.val;

public class ValOutput {

	private boolean valid;
	
	private String message;

	public ValOutput(boolean valid, String message) {
		super();
		this.valid = valid;
		this.message = message;
	}

	public boolean isValid() {
		return valid;
	}

	public String getMessage() {
		return message;
	}
	
}
