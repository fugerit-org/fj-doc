package org.fugerit.java.doc.freemarker.tool.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ChainModel implements Serializable {
	
	private static final long serialVersionUID = 3421438389573953861L;

	private List<StepModel> stepList;
	
	private String id;
	
	private String parent;
	
	public ChainModel( String id ) {
		this.setId( id );
		this.stepList = new ArrayList<>();
	}
	
}
