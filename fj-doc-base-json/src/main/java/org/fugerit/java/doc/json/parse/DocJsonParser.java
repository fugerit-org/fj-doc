package org.fugerit.java.doc.json.parse;

import java.io.Reader;

import org.fugerit.java.doc.base.config.DocException;
import org.fugerit.java.doc.base.facade.DocFacadeSource;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.parser.AbstractDocParser;
import org.fugerit.java.doc.base.parser.DocValidationResult;
import org.fugerit.java.xml2json.XmlToJsonHandler;

public class DocJsonParser extends AbstractDocParser {
	
	private DocObjectMapperHelper helper;
	
	public DocJsonParser( XmlToJsonHandler handler ) {
		super( DocFacadeSource.SOURCE_TYPE_JSON );
		this.helper = new DocObjectMapperHelper( handler );
	}

	public DocJsonParser() {
		this( new XmlToJsonHandler() );
	}

	private DocObjectMapperHelper getHelper() {
		return this.helper;
	}
	
	@Override
	protected DocValidationResult validateWorker(Reader reader, boolean parseVersion) throws DocException {
		return this.getHelper().validateWorkerResult(reader, parseVersion);
	}

	@Override
	protected DocBase parseWorker(Reader reader) throws DocException {
		return this.getHelper().parse(reader);
	}
	
}
