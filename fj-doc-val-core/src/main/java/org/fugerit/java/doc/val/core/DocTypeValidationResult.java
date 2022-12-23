package org.fugerit.java.doc.val.core;
import org.fugerit.java.core.util.result.BasicResult;

public class DocTypeValidationResult extends BasicResult {

	public DocTypeValidationResult(int resultCode) {
		super(resultCode);
	}
	
	public static DocTypeValidationResult newOk() {
		return new DocTypeValidationResult( RESULT_CODE_OK );
	}
	
	public static DocTypeValidationResult newFail() {
		return new DocTypeValidationResult( RESULT_CODE_KO );
	}

}
