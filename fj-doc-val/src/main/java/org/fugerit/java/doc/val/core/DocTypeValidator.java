package org.fugerit.java.doc.val.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

public interface DocTypeValidator {

	Set<String> getSupportedExtensions();
	
	String getMimeType();
	
	boolean isExtensionSupported( String ext );
	
	boolean isMimeTypeSupported( String mime );
	
	boolean check( InputStream is ) throws IOException;
	
	boolean checkCompatibility();
	
	DocTypeValidationResult validate( InputStream is ) throws IOException;
	
}
