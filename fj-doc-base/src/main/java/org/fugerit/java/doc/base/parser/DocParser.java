package org.fugerit.java.doc.base.parser;

import java.io.Reader;

import org.fugerit.java.doc.base.config.DocException;
import org.fugerit.java.doc.base.model.DocBase;

public interface DocParser {
	
	int getSourceType();
	
	DocBase parse( Reader reader ) throws DocException;
	
	DocValidationResult validateResult( Reader reader ) throws DocException;
	
	int validate( Reader reader ) throws DocException;
	
}
