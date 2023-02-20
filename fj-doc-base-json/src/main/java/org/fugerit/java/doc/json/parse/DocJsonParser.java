package org.fugerit.java.doc.json.parse;

import java.io.Reader;

import org.fugerit.java.doc.base.facade.DocFacadeSource;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.parser.AbstractDocParser;
import org.fugerit.java.doc.base.parser.DocValidationResult;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DocJsonParser extends AbstractDocParser {
	
	private DocObjectMapperHelper helper;
	
	public DocJsonParser() {
		super( DocFacadeSource.SOURCE_TYPE_JSON );
		this.helper = new DocObjectMapperHelper( new ObjectMapper() );
	}

	private DocObjectMapperHelper getHelper() {
		return this.helper;
	}
	
	@Override
	protected DocValidationResult validateWorker(Reader reader, boolean parseVersion) throws Exception {
		return this.getHelper().validateWorkerResult(reader, parseVersion);
	}

	@Override
	protected DocBase parseWorker(Reader reader) throws Exception {
		return this.getHelper().parse(reader);
	}
	
}
