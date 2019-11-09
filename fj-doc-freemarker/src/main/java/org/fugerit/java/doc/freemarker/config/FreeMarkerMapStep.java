package org.fugerit.java.doc.freemarker.config;

import java.util.HashMap;
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
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>)context.getAttribute( FreeMarkerConstants.ATT_FREEMARKER_MAP );
		if ( map == null ) {
			map = new HashMap<>();
			context.setAttribute( FreeMarkerConstants.ATT_FREEMARKER_MAP , map );
		}
		for ( Object key : this.getCustomConfig().keySet() ) {
			String keyFrom = key.toString();
			String keyTo = this.getCustomConfig().getProperty( keyFrom );
			Object value = context.getAttribute( keyFrom );
			map.put( keyTo , value );
		}
		return res;
	}



}
