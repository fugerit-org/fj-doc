package org.fugerit.java.doc.base.parser;

import java.util.ArrayList;
import java.util.List;

import org.fugerit.java.core.util.result.BasicResult;
import org.fugerit.java.core.util.result.Result;

public class DocValidationResult extends BasicResult {

	public static final int VALIDATION_OK = Result.RESULT_CODE_OK;
	
	public static final int VALIDATION_KO = Result.RESULT_CODE_KO;
	
	public static final int VALIDATION_NOT_SUPPORTED = VALIDATION_KO-11;
	
	public static final int VALIDATION_NOT_DEFINIED = VALIDATION_KO-12;
	
	public DocValidationResult( int result ) {
		super( result );
		this.infoList = new ArrayList<>();
		this.errorList = new ArrayList<>();
	}
	
	private List<String> infoList;
	
	private List<String> errorList;

	public List<String> getInfoList() {
		return infoList;
	}

	public List<String> getErrorList() {
		return errorList;
	}
	
	public static DocValidationResult newDefaultOKResult() {
		return new DocValidationResult( VALIDATION_OK );
	}
	
	public static DocValidationResult newDefaultKOResult() {
		return new DocValidationResult( VALIDATION_KO );
	}
	
	public static DocValidationResult newDefaultNotSupportedResult() {
		return new DocValidationResult( VALIDATION_NOT_SUPPORTED );
	}
	
	public static DocValidationResult newDefaultNotDefiniedResult() {
		return new DocValidationResult( VALIDATION_NOT_DEFINIED );
	}
	
	public int evaluateResult() {
		if ( this.getResultCode() != VALIDATION_NOT_SUPPORTED ) {
			if ( this.getErrorList().isEmpty() ) {
				this.setResultCode( VALIDATION_OK );
			} else {
				this.setResultCode( VALIDATION_KO );
			}	
		}
		return this.getResultCode();
	}
	
}
