package org.fugerit.java.doc.freemarker.tool.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class ChainModel implements Serializable {
	
	private static final long serialVersionUID = 3421438389573953861L;

	@Getter private List<StepModel> stepList;
	
	@Getter @Setter private String id;
	
	@Getter @Setter private String parent;
	
	public ChainModel( String id ) {
		this.setId( id );
		this.stepList = new ArrayList<>();
	}
	
}
