package org.fugerit.java.doc.base.helper;

import org.apache.commons.codec.binary.Base64;

public class Base64Helper {

	public static String encodeBase64String( byte[] data ) {
		return Base64.encodeBase64String( data );
	}
	
	public static byte[] decodeBase64String( String data ) {
		return Base64.decodeBase64( data );
	}
	
}
