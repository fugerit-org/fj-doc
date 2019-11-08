package org.fugerit.java.doc.base.process;

public interface DocProcessor {

	public int process( DocProcessContext context, DocProcessData data  ) throws Exception;
	
}
