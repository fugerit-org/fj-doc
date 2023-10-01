package org.fugerit.java.doc.yaml.parse;

import java.io.Reader;

import org.fugerit.java.doc.base.config.DocException;
import org.fugerit.java.doc.base.facade.DocFacadeSource;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.parser.AbstractDocParser;
import org.fugerit.java.doc.base.parser.DocValidationResult;
import org.fugerit.java.doc.json.parse.DocObjectMapperHelper;
import org.fugerit.java.xml2json.XmlToJsonHandler;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

public class DocYamlParser extends AbstractDocParser {
	
	private DocObjectMapperHelper helper;
	
	public DocYamlParser( XmlToJsonHandler handler ) {
		super( DocFacadeSource.SOURCE_TYPE_YAML );
		this.helper = new DocObjectMapperHelper( handler );
	}
	
	public DocYamlParser() {
		this( new XmlToJsonHandler( new YAMLMapper() ) );
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
