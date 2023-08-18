package org.fugerit.java.doc.base.config;

import java.io.Serializable;

import org.fugerit.java.core.cfg.ConfigException;
import org.w3c.dom.Element;

public class DocTypeHandlerDecorator extends DocTypeHandlerDefault {

	private static final long serialVersionUID = 5531355008187717238L;
	
	private Serializable handler;
	
	public DocTypeHandlerDecorator( DocTypeHandler handler ) {
		super( handler.getType(), handler.getModule(), handler.getMime(), handler.getCharset() );
		this.handler = handler;
	}

	public DocTypeHandler unwrap() {
		return (DocTypeHandler)handler;
	}

	@Override
	public void handle(DocInput docInput, DocOutput docOutput) throws Exception {
		((DocTypeHandler)this.handler).handle(docInput, docOutput);
	}

	@Override
	public void configure(Element tag) throws ConfigException {
		if ( this.handler instanceof DocTypeHandlerDefault ) {
			((DocTypeHandlerDefault)this.handler).configure(tag);
		}
	}

}
