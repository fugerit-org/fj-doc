package org.fugerit.java.doc.val.core.basic;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

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
	public static final DocTypeValidator TIFF_VALIDATOR = new ImageValidator( MIME_TIFF, EXT_TIFF, FORMAT_TIFF );
	
	private static final Logger logger = LoggerFactory.getLogger( ImageValidator.class );
	
	private String format;

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

	protected ImageValidator(String mimeType, Set<String> supportedExtensions, String format) {
		super(mimeType, supportedExtensions);
		this.format = format;
	}

	protected ImageValidator(String mimeType, String extension, String format) {
		super(mimeType, extension);
		this.format = format;
	}

}
