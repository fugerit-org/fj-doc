package org.fugerit.java.doc.playground.doc;

import org.fugerit.java.doc.playground.facade.BasicOutput;

import lombok.Getter;
import lombok.Setter;

public class GenerateOutput extends BasicOutput {

	@Setter
	@Getter
	private String generationTime;
	
	@Setter
	@Getter
	private String docOutputBase64;
	
}
