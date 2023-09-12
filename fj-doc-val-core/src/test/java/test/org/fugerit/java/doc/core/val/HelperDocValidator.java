package test.org.fugerit.java.doc.core.val;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import org.fugerit.java.doc.val.core.DocTypeValidationResult;
import org.fugerit.java.doc.val.core.basic.AbstractDocTypeValidator;

public class HelperDocValidator extends AbstractDocTypeValidator {

	public static final String MIME_TEST = "test/test";
	
	public static final String FORMAT_TEST = "test";
	
	public static final Set<String> EXT_TEST = createSet( FORMAT_TEST  );
	
	public HelperDocValidator() {
		this( MIME_TEST, EXT_TEST );
	}
	
	public HelperDocValidator(String mimeType, String extension) {
		super(mimeType, extension);
	}

	protected HelperDocValidator(String mimeType, Set<String> supportedExtensions) {
		super(mimeType, supportedExtensions);
	}

	@Override
	public DocTypeValidationResult validate(InputStream is) throws IOException {
		return DocTypeValidationResult.newOk();
	}

}
