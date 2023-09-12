package org.fugerit.java.doc.freemarker.process;

import java.io.Serializable;
import java.util.Properties;

import lombok.Getter;
import lombok.Setter;

public class ChainStepModel implements Serializable {

	private static final long serialVersionUID = 622077549080786391L;
	
	@Getter @Setter private String stepType;
	
	@Getter @Setter private Properties attributes;
	
}
