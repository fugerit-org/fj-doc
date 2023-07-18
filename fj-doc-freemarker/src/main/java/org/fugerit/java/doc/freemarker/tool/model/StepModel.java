package org.fugerit.java.doc.freemarker.tool.model;

import java.util.LinkedHashMap;
import java.util.Set;

import lombok.Data;

@Data
public class StepModel {

	private String type;

	private LinkedHashMap<String, String> atts;
	
	public StepModel(String type) {
		super();
		this.type = type;
		this.atts = new LinkedHashMap<>();
	}
	
	public Set<String> getAttNames() {
		return this.getAtts().keySet();
	}
	
}
