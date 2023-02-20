package org.fugerit.java.doc.base.config;

public class DocTypeHandlerXMLUTF8 extends DocTypeHandlerDecorator {

	private static final long serialVersionUID = -8512962187951518109L;

	public static final DocTypeHandler HANDLER = new DocTypeHandlerXMLUTF8();
	
	public DocTypeHandlerXMLUTF8() {
		super( DocTypeHandlerXML.HANDLER_UTF8 );
	}
	
}
