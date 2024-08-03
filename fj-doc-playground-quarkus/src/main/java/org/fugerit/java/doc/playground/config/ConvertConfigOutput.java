package org.fugerit.java.doc.playground.config;

import lombok.Getter;
import lombok.Setter;
import org.fugerit.java.doc.playground.facade.BasicOutput;

public class ConvertConfigOutput extends BasicOutput {

	@Setter @Getter private String generationTime;
	
	@Setter @Getter private String docOutputBase64;
	
}
