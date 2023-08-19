package org.fugerit.java.doc.base.helper;

import java.io.InputStream;
import java.util.Properties;

import org.fugerit.java.core.lang.helpers.ClassHelper;

public class DefaultMimeHelper {

	private DefaultMimeHelper() {} // java:S1118
	
	private static Properties props = new Properties();
	static {
		try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( "config/default_mime.xml" ) ) {
			props.loadFromXML( is );
		} catch (Exception e) {
			throw new RuntimeException( e );
		}
	}
	
	public static String getDefaultMime(String type) {
		return props.getProperty(type);
	}
	
	
	
}
