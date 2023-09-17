package org.fugerit.java.doc.freemarker.tool.model;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

public class StepModel implements Serializable {

	private static final long serialVersionUID = 8626936119015950596L;

	@Getter @Setter private String type;

	@Getter private LinkedHashMap<String, String> atts;
	
	public StepModel(String type) {
		super();
		this.type = type;
		this.atts = new LinkedHashMap<>();
	}
	
	public Set<String> getAttNames() {
		return this.getAtts().keySet();
	}
	
}
