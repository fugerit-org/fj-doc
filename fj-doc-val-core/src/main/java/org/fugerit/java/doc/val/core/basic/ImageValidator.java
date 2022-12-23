package org.fugerit.java.doc.val.core.basic;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.fugerit.java.core.lang.helpers.JavaVersionHelper;
import org.fugerit.java.doc.val.core.DocTypeValidationResult;
import org.fugerit.java.doc.val.core.DocTypeValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageValidator extends AbstractDocTypeValidator {
	
	public static final String FORMAT_JPG = "JPG";
	public static final String MIME_JPG = "image/jpeg";
	public static final Set<String> EXT_JPG = createSet( FORMAT_JPG, "JPEG" );
	
	public static final String FORMAT_PNG = "PNG";
	public static final String MIME_PNG = "image/png";
	public static final Set<String> EXT_PNG = createSet( FORMAT_PNG );
			
	public static final String FORMAT_TIFF = "TIFF";
	public static final String MIME_TIFF = "image/tiff";
	public static final Set<String> EXT_TIFF = createSet( FORMAT_TIFF, "TIF" );
	
	public static final DocTypeValidator JPG_VALIDATOR = new ImageValidator( MIME_JPG, EXT_JPG, FORMAT_JPG );
	
	public static final DocTypeValidator PNG_VALIDATOR = new ImageValidator( MIME_PNG, EXT_PNG, FORMAT_PNG );
	
	/**
	 * Tiff validator, only supported from java 9+
	 */
	public static final DocTypeValidator TIFF_VALIDATOR = new ImageValidator( MIME_TIFF, EXT_TIFF, FORMAT_TIFF, JavaVersionHelper.MAJOR_VERSION_JAVA_9 );
	
	private static final Logger logger = LoggerFactory.getLogger( ImageValidator.class );
	
	private String format;

	private int javaMajorVersionRequired;
	
	@Override
	public DocTypeValidationResult validate(InputStream is) throws IOException {
		DocTypeValidationResult result = DocTypeValidationResult.newFail();
		try ( ImageInputStream iis = ImageIO.createImageInputStream( is ) ) {
			Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName( this.format );
			while (readers.hasNext()) {
			    try {        
			        ImageReader reader = readers.next();
			        reader.setInput(iis);
			        reader.read(0);
			        result.setResultCode( DocTypeValidationResult.RESULT_CODE_OK );
			        break;
			    } catch (IOException exp) {
			    	logger.debug( "checkImage {}", exp.getMessage() );
			    }
			}			
		}
		return result;
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
		boolean ok = super.checkCompatibility();
		int javaMajorVersionFound =  JavaVersionHelper.parseUniversalJavaMajorVersion() ;
		if ( javaMajorVersionFound < this.javaMajorVersionRequired ) {
			ok = false;
			logger.warn( "java major version found : '{}' lower than required : '{}'", javaMajorVersionFound, this.javaMajorVersionRequired );
		}
		return ok;
	}

}
