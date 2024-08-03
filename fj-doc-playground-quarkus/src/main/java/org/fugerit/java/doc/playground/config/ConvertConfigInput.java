package org.fugerit.java.doc.playground.config;

import lombok.Getter;
import lombok.Setter;
import org.fugerit.java.doc.playground.facade.BasicInput;

public class ConvertConfigInput extends BasicInput {
    
	@Setter @Getter private String freemarkerJsonData;
	
}