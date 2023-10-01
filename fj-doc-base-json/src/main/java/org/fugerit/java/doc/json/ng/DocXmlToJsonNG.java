package org.fugerit.java.doc.json.ng;

import org.fugerit.java.doc.json.parse.DocXmlToJson;
import org.fugerit.java.xml2json.XmlToJsonHandler;

public class DocXmlToJsonNG extends DocXmlToJson {

	public DocXmlToJsonNG() {
		super(new XmlToJsonHandler( new XmlToJsonConverterNG() ) );
	}

	
	
}
