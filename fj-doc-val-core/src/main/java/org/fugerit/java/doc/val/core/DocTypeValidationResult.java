package org.fugerit.java.doc.val.core;
import org.fugerit.java.core.util.ObjectUtils;
import org.fugerit.java.core.util.result.BasicResult;

import java.util.ArrayList;
import java.util.List;

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

	public static DocTypeValidationResult newFail( Exception e ) {
		return newFail().withMainException( e );
	}

	private static final String ATT_VALIDATION_MESSAGE = "validation-message";

	private static final String ATT_VALIDATION_EXECPTIONS = "validation-exceptions";


	public DocTypeValidationResult withValidationMessage(String validationMessage) {
		this.getInfoMap().put(ATT_VALIDATION_MESSAGE, validationMessage);
		return this;
	}

	public String getValidationMessage() {
		return (String)this.getInfoMap().get(ATT_VALIDATION_MESSAGE);
	}

	public List<Exception> getValidationExceptions() {
		return  ObjectUtils.objectWithDefault((List<Exception>)this.getInfoMap().get(ATT_VALIDATION_EXECPTIONS), new ArrayList<>());
	}

	public DocTypeValidationResult withMainException( Exception e ) {
		this.withValidationMessage( e.getMessage() );
		this.getValidationExceptions().add( e );
		return this;
	}

}
