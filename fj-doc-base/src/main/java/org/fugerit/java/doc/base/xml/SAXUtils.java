package org.fugerit.java.doc.base.xml;

import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParserFactory;

import org.fugerit.java.core.cfg.ConfigRuntimeException;

public class SAXUtils {

	private SAXUtils() {}
	
	public static SAXParserFactory newFactory( Map<String, Boolean> features ) {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		for ( String key : features.keySet() ) {
			boolean value = features.get( key );
			try {
				factory.setFeature( key, value );
			} catch (Exception e) {
				throw ConfigRuntimeException.convertExMethod("newFactory", e);
			}
		}
		return factory;
	}
	
	public static SAXParserFactory newSafeFactory() {
		Map<String, Boolean> features = new HashMap<>();
		features.put( "http://xml.org/sax/features/external-general-entities" , false );
		features.put( "http://xml.org/sax/features/external-parameter-entities" , false );
		return newFactory( features );
	}
	
}
