package org.fugerit.java.doc.base.helper;

import java.util.Base64;

public class Base64Helper {

	public static String encodeBase64String( byte[] data ) {
		return Base64.getEncoder().encodeToString( data );
	}
	
	public static byte[] decodeBase64String( String data ) {
		return Base64.getDecoder().decode( data );
	}
	
}
