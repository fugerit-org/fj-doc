package org.fugerit.java.doc.freemarker.config;

import java.util.HashMap;
import java.util.Map;

import org.fugerit.java.doc.base.process.DocProcessContext;

public class FreeMarkerConstants {

	private FreeMarkerConstants() {}
	
	public static final String ATT_FREEMARKER_CONFIG = "FreeMarkerConfig";
	
	public static final String ATT_FREEMARKER_MAP = "FreeMarkerMap";
	
	public static Map<String, Object> getFreeMarkerMap( DocProcessContext context ) {
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>)context.getAttribute( FreeMarkerConstants.ATT_FREEMARKER_MAP );
		if ( map == null ) {
			Map<String, Object> newMap = new HashMap<>();
			context.setAttribute( FreeMarkerConstants.ATT_FREEMARKER_MAP , newMap );
			return newMap;
		} else {
			return map;
		}
	}
	
}
