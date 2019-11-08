package org.fugerit.java.doc.base.process;

import java.io.Serializable;

import org.fugerit.java.core.util.filterchain.MiniFilter;
import org.fugerit.java.core.util.filterchain.MiniFilterBase;
import org.fugerit.java.core.util.filterchain.MiniFilterContext;
import org.fugerit.java.core.util.filterchain.MiniFilterData;

public class DocProcessorBasic extends MiniFilterBase implements MiniFilter, DocProcessor, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4243893151811077804L;

	@Override
	public int process(DocProcessContext context, DocProcessData data) throws Exception {
		int res = CONTINUE;
		return res;
	}

	@Override
	public int apply(MiniFilterContext context, MiniFilterData data) throws Exception {
		DocProcessContext dContext = (DocProcessContext) context;
		DocProcessData dData = (DocProcessData) data;
		return process( dContext, dData );
	}
	
}
