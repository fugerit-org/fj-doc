package org.fugerit.java.doc.json.ng;

import org.fugerit.java.doc.base.facade.DocFacadeSource;
import org.fugerit.java.doc.json.parse.DocJsonParser;
import org.fugerit.java.xml2json.XmlToJsonHandler;

public class DocJsonParserNG extends DocJsonParser {

	public DocJsonParserNG() {
		super(DocFacadeSource.SOURCE_TYPE_JSON_NG, new XmlToJsonHandler( new XmlToJsonConverterNG() ) );
	}

}
