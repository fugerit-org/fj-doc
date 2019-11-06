package org.fugerit.java.doc.base.config;

import java.io.OutputStream;

public class DocOutput {

	private OutputStream os;

	private DocResult result;
	
	public OutputStream getOs() {
		return os;
	}

	public DocResult getResult() {
		return result;
	}

	public DocOutput(OutputStream os) {
		super();
		this.os = os;
		this.result = new DocResult();
	}
	
	public static DocOutput newOutput( OutputStream os ) {
		return new DocOutput( os );
	}

}
