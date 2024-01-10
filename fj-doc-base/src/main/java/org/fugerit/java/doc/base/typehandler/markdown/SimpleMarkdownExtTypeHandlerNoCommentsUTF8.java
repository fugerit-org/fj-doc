package org.fugerit.java.doc.base.typehandler.markdown;

import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.config.DocTypeHandlerDecorator;

public class SimpleMarkdownExtTypeHandlerNoCommentsUTF8 extends DocTypeHandlerDecorator {

	private static final long serialVersionUID = 2821034278291920037L;

	public static final DocTypeHandler HANDLER = new SimpleMarkdownExtTypeHandlerNoCommentsUTF8();
	
	public SimpleMarkdownExtTypeHandlerNoCommentsUTF8() {
		super( SimpleMarkdownExtTypeHandler.HANDLER_NOCOMMENTS_UTF8 );
	}
	
}
