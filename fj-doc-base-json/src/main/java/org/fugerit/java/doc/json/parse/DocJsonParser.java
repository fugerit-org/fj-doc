package org.fugerit.java.doc.json.parse;

import java.io.Reader;

import org.fugerit.java.doc.base.facade.DocFacadeSource;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.parser.AbstractDocParser;
import org.fugerit.java.doc.base.parser.DocValidationResult;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DocJsonParser extends AbstractDocParser {
	
	public DocJsonParser() {
		super( DocFacadeSource.SOURCE_TYPE_JSON );
	}

	@Override
	protected DocValidationResult validateWorker(Reader reader) throws Exception {
		return DocValidationResult.newDefaultNotSupportedResult();
	}

	@Override
	protected DocBase parseWorker(Reader reader) throws Exception {
		DocObjectMapperHelper helper = new DocObjectMapperHelper( new ObjectMapper() );
		return helper.parse(reader);
	}
	
}
