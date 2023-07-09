package org.fugerit.java.doc.freemarker.process;

import java.io.Serializable;

import org.fugerit.java.core.cfg.xml.IdConfigType;
import org.fugerit.java.core.util.collection.KeyString;

import lombok.Data;

@Data
public class DocChainModel implements IdConfigType, KeyString, Serializable {

	public static final String DEFAULT_TEMPLATE_PATH = "${chainId}.ftl";
	
	private static final long serialVersionUID = 9076457107043072322L;

	private String id;
	
	private String templatePath = DEFAULT_TEMPLATE_PATH;

	@Override
	public String getKey() {
		return this.getId();
	} 
	
}
