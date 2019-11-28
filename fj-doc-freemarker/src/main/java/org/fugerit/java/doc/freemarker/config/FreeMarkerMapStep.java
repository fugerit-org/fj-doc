package org.fugerit.java.doc.freemarker.config;

import java.util.Map;

import org.fugerit.java.doc.base.process.DocProcessContext;
import org.fugerit.java.doc.base.process.DocProcessData;
import org.fugerit.java.doc.base.process.DocProcessorBasic;

public class FreeMarkerMapStep extends DocProcessorBasic {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2313658506839366841L;

	@Override
	public int process(DocProcessContext context, DocProcessData data) throws Exception {
		int res = super.process(context, data);
		Map<String, Object> map = FreeMarkerConstants.getFreeMarkerMap( context );
		for ( Object key : this.getCustomConfig().keySet() ) {
			String keyFrom = key.toString();
			String keyTo = this.getCustomConfig().getProperty( keyFrom );
			Object value = context.getAttribute( keyFrom );
			map.put( keyTo , value );
		}
		return res;
	}



}
