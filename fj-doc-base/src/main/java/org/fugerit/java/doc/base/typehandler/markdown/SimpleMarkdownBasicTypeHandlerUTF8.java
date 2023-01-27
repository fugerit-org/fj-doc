package org.fugerit.java.doc.base.typehandler.markdown;

import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.config.DocTypeHandlerDecorator;

public class SimpleMarkdownBasicTypeHandlerUTF8 extends DocTypeHandlerDecorator {

	private static final long serialVersionUID = 2821034278291920037L;

	public static final DocTypeHandler HANDLER = new SimpleMarkdownBasicTypeHandlerUTF8();
	
	public SimpleMarkdownBasicTypeHandlerUTF8() {
		super( SimpleMarkdownBasicTypeHandler.HANDLER_UTF8 );
	}
	
}
