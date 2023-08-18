package org.fugerit.java.doc.base.helper;

import java.io.Serializable;

import org.fugerit.java.core.lang.helpers.AttributeHolderDefault;
import org.fugerit.java.doc.base.model.DocBase;

public class DocTypeFacadeHelper extends AttributeHolderDefault {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6181869636857694927L;

	private Serializable docBase;

	public final static int ROOT_DEPTH = 0;
	
	public DocBase getDocBase() {
		return (DocBase)docBase;
	}

	public DocTypeFacadeHelper(DocBase docBase) {
		super();
		this.docBase = docBase;
		this.depth = ROOT_DEPTH-1;
	}
	
	private int depth;

	public int getDepth() {
		return depth;
	}
	
	public void depthPlusOne() {
		this.depth++;
	}
	
	public void depthMinusOne() {
		this.depth--;
	}
	
}
