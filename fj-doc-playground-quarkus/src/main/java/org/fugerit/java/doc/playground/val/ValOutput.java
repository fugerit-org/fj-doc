package org.fugerit.java.doc.playground.val;

import org.fugerit.java.doc.playground.facade.BasicOutput;

public class ValOutput extends BasicOutput {

	private boolean valid;
	
	public ValOutput(boolean valid, String message) {
		super();
		this.valid = valid;
		this.setMessage(message);
	}

	public boolean isValid() {
		return valid;
	}
	
}
