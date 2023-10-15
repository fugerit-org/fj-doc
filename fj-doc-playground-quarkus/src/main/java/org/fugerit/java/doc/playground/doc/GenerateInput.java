package org.fugerit.java.doc.playground.doc;

import org.fugerit.java.doc.playground.facade.BasicInput;

import lombok.Getter;
import lombok.Setter;

public class GenerateInput extends BasicInput {
    
	@Setter @Getter private String freemarkerJsonData;
	
}