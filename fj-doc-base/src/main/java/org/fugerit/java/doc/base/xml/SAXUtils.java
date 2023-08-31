package org.fugerit.java.doc.base.xml;

import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParserFactory;

import org.fugerit.java.core.cfg.ConfigRuntimeException;

public class SAXUtils {

	private SAXUtils() {}
	
	private static final Map<String, Boolean> NO_FEATURES = new HashMap<>();
	
	public static SAXParserFactory newSafeFactory( Map<String, Boolean> features ) {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			factory.setFeature( "http://xml.org/sax/features/external-general-entities" , false );
			factory.setFeature( "http://xml.org/sax/features/external-parameter-entities" , false );
			// additional features if any
			for ( String key : features.keySet() ) {
				boolean value = features.get( key );
				factory.setFeature( key, value );
			}
		} catch (Exception e) {
			throw ConfigRuntimeException.convertExMethod("newFactory", e);
		}
		
		return factory;
	}
	
	public static SAXParserFactory newSafeFactory() {
		return newSafeFactory(NO_FEATURES);
	}
	
}
