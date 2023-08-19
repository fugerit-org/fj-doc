package org.fugerit.java.doc.base.helper;

import java.util.Base64;

/**
 * <p>Utility class for encoding decoding base64</p>
 * 
 * <p>NOTE: it was created because once the library supported java 1.6 and 1.7 where java.util.Base64 was not present.</p> 
 * 
 */
public class Base64Helper {

	private Base64Helper() {} // java:S1118
	
	public static String encodeBase64String( byte[] data ) {
		return Base64.getEncoder().encodeToString( data );
	}
	
	public static byte[] decodeBase64String( String data ) {
		return Base64.getDecoder().decode( data );
	}
	
}
