package org.fugerit.java.doc.base.typehandler.markdown;

import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.config.DocTypeHandlerDecorator;

public class SimpleMarkdownExtTypeHandlerUTF8 extends DocTypeHandlerDecorator {

	private static final long serialVersionUID = 2821034278291920037L;

	public static final DocTypeHandler HANDLER = new SimpleMarkdownExtTypeHandlerUTF8();
	
	public SimpleMarkdownExtTypeHandlerUTF8() {
		super( SimpleMarkdownExtTypeHandler.HANDLER_UTF8 );
	}
	
}
