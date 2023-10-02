package org.fugerit.java.doc.base.config;

import java.io.OutputStream;

import lombok.Getter;

public class DocOutput {

	@Getter private OutputStream os;

	@Getter private DocResult result;
	
	public DocOutput(OutputStream os) {
		super();
		this.os = os;
		this.result = new DocResult();
	}
	
	public static DocOutput newOutput( OutputStream os ) {
		return new DocOutput( os );
	}

}
