package org.fugerit.java.doc.val.core.basic;

import java.io.InputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.fugerit.java.core.lang.helpers.JavaVersionHelper;
import org.fugerit.java.doc.val.core.DocTypeValidationResult;
import org.fugerit.java.doc.val.core.DocTypeValidator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ImageValidator extends AbstractDocTypeValidator {
	
	public static boolean javaVersionSupportHelper( int javaMajorVersionFound, int javaMajorVersionRequired ) {
		boolean notSupported = ( javaMajorVersionFound < javaMajorVersionRequired );
		if ( notSupported ) {
			log.warn( "java major version found : '{}' lower than required : '{}'", javaMajorVersionFound, javaMajorVersionRequired );
		}
		return !notSupported;
	}
	
	public static final String FORMAT_JPG = "JPG";
	public static final String MIME_JPG = "image/jpeg";
	public static final Set<String> EXT_JPG = Collections.unmodifiableSet( createSet( FORMAT_JPG, "JPEG" ) );
	
	public static final String FORMAT_PNG = "PNG";
	public static final String MIME_PNG = "image/png";
	public static final Set<String> EXT_PNG = Collections.unmodifiableSet( createSet( FORMAT_PNG ) );
			
	public static final String FORMAT_TIFF = "TIFF";
	public static final String MIME_TIFF = "image/tiff";
	public static final Set<String> EXT_TIFF = Collections.unmodifiableSet( createSet( FORMAT_TIFF, "TIF" ) );
	
	public static final DocTypeValidator JPG_VALIDATOR = new ImageValidator( MIME_JPG, EXT_JPG, FORMAT_JPG );
	
	public static final DocTypeValidator PNG_VALIDATOR = new ImageValidator( MIME_PNG, EXT_PNG, FORMAT_PNG );
	
	/**
	 * Tiff validator, only supported from java 9+
	 */
	public static final DocTypeValidator TIFF_VALIDATOR = new ImageValidator( MIME_TIFF, EXT_TIFF, FORMAT_TIFF, JavaVersionHelper.MAJOR_VERSION_JAVA_9 );
	
	private String format;

	private int javaMajorVersionRequired;
	
	@Override
	public DocTypeValidationResult validate(InputStream is) {
		try ( ImageInputStream iis = ImageIO.createImageInputStream( is ) ) {
			Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName( this.format );
			if (readers.hasNext()) {
				ImageReader reader = readers.next();
				reader.setInput(iis);
				reader.read(0);
				return DocTypeValidationResult.newOk();
			}
		}  catch (Exception exp) {
			log.debug( "checkImage (v2) {}", exp.getMessage() );
		}
		return DocTypeValidationResult.newFail();
	}

	protected ImageValidator(String mimeType, Set<String> supportedExtensions, String format, int javaMajorVersionRequired) {
		super(mimeType, supportedExtensions);
		this.format = format;
		this.javaMajorVersionRequired = javaMajorVersionRequired;
	}
	
	protected ImageValidator(String mimeType, Set<String> supportedExtensions, String format) {
		this(mimeType, supportedExtensions, format, JavaVersionHelper.MAJOR_VERSION_JAVA_8);
	}

	@Override
	public boolean checkCompatibility() {
		return javaVersionSupportHelper(JavaVersionHelper.parseUniversalJavaMajorVersion(), this.javaMajorVersionRequired);
	}

}
