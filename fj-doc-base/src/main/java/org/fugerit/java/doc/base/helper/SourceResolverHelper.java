package org.fugerit.java.doc.base.helper;

import java.io.IOException;
import org.fugerit.java.core.io.helper.StreamHelper;
import org.fugerit.java.doc.base.model.DocImage;

/**
 * Method in this class are not used anymore, but are left for backward compatibility.
 *
 * Implementation has been moved to DocImage to fix SonarCloud issue :
 *
 * Circular dependencies between classes across packages should be resolved, javaarchitecture:S7091
 *
 */
public class SourceResolverHelper {

	private SourceResolverHelper() {} // java:S1118
	
	public static final String MODE_CLASSLOADER = StreamHelper.PATH_CLASSLOADER;

	public static String resolveImageToBase64( DocImage img ) throws IOException {
		return DocImage.resolveImageToBase64( img );
	}
	
	public static byte[] resolveImage( DocImage img ) throws IOException {
		return DocImage.resolveImage( img );
	}
	
}
