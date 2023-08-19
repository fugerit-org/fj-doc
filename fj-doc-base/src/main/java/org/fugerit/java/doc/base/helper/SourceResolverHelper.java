package org.fugerit.java.doc.base.helper;

import java.io.IOException;
import java.net.URL;

import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.core.io.helper.StreamHelper;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.doc.base.model.DocImage;

public class SourceResolverHelper {

	private SourceResolverHelper() {} // java:S1118
	
	public static final String MODE_CLASSLOADER = StreamHelper.PATH_CLASSLOADER;
	
	public static String resolveImageToBase64( DocImage img ) throws IOException {
		String path = img.getUrl();
		String base64 = img.getBase64();
		if ( StringUtils.isEmpty( base64 ) && path != null ) {
			try {
				byte[] data = null;
				if ( path.startsWith( StreamHelper.PATH_CLASSLOADER ) ) {
					data = StreamIO.readBytes( StreamHelper.resolveStream( path ) );
				} else {
					URL url = new URL( path );
					data = StreamIO.readBytes( url.openConnection().getInputStream() );
				}
				base64 = Base64Helper.encodeBase64String( data );
			} catch (Exception e) {
				throw new IOException( "Errore on resolveImageToBase64 : "+e , e );
			}
		} else {
			throw new IOException( "Null path and base64 provided!" );
		}
		return base64;
	}
	
	public static byte[] resolveImage( DocImage img ) throws IOException {
		byte[] data = null;
		String path = img.getUrl();
		String base64 = img.getBase64();
		if ( StringUtils.isNotEmpty( base64 ) ) {
			data = Base64Helper.decodeBase64String( base64 );
		} else if ( path != null ) {
			try {
				if ( path.startsWith( StreamHelper.PATH_CLASSLOADER ) ) {
					data = StreamIO.readBytes( StreamHelper.resolveStream( path ) );
				} else {
					URL url = new URL( path );
					data = StreamIO.readBytes( url.openConnection().getInputStream() );
				}	
			} catch (Exception e) {
				throw new IOException( "Errore on resolveImage : "+e , e );
			}
		} else {
			throw new IOException( "Null path provided!" );
		}
		return data;
	}
	
}
