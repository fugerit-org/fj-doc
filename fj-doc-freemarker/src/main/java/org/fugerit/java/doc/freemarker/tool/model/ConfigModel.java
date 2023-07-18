package org.fugerit.java.doc.freemarker.tool.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ConfigModel implements Serializable {

	private static final long serialVersionUID = 5198896174327509127L;
	
	private List<ChainModel> chainList;
	
	public ConfigModel() {
		this.chainList = new ArrayList<>();
	}
	
}
