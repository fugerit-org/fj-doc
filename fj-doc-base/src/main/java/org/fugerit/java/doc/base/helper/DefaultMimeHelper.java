package org.fugerit.java.doc.base.helper;

import java.util.Properties;

import org.fugerit.java.core.util.PropsIO;

public class DefaultMimeHelper {

	private DefaultMimeHelper() {} // java:S1118
	
	private static Properties props = PropsIO.loadFromClassLoaderSafe( "config/default_mime.xml" );

	public static String getDefaultMime(String type) {
		return props.getProperty(type);
	}
	
	
	
}
