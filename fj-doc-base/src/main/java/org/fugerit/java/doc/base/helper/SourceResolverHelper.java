package org.fugerit.java.doc.base.helper;

import java.net.URL;

import org.apache.commons.codec.binary.Base64;
import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.core.io.helper.StreamHelper;

public class SourceResolverHelper {

	public static final String MODE_CLASSLOADER = StreamHelper.PATH_CLASSLOADER;
	
	public static final String MODE_BASE64 = "base64://";
	
	public static byte[] resolveByte( String path ) throws Exception {
		byte[] data = null;
		if ( path != null ) {
			if ( path.startsWith( StreamHelper.PATH_CLASSLOADER ) ) {
				data = StreamIO.readBytes( StreamHelper.resolveStream( path ) );
			} else if ( path.startsWith( MODE_BASE64 ) ) {
				String base64 = path.substring( MODE_BASE64.length() );
				data = Base64.decodeBase64( base64 );
			} else {
				URL url = new URL( path );
				data = StreamIO.readBytes( url.openConnection().getInputStream() );
			}	
		} else {
			throw new Exception( "Null path provided!" );
		}
		return data;
	}
	
}
