package org.fugerit.java.doc.base.facade;

import java.util.HashMap;
import java.util.Map;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;

public class DocHandlerFacade {

	private Map<String, DocTypeHandler> mapHandlers;
	
	public DocHandlerFacade() {
		this.mapHandlers = new HashMap<>();
	}
	
	public void registerHandler( DocTypeHandler handler ) throws Exception {
		this.mapHandlers.put( handler.getKey(), handler );
	}
	
	public void handle( DocInput docInput, DocOutput docOutput ) throws Exception {
		String type = docInput.getType();
		DocTypeHandler handler = this.mapHandlers.get( type );
		if ( handler != null ) {
			handler.handle( docInput, docOutput );
		} else {
			throw new ConfigException( "DocHandlerFacade - No handler defined for type : "+type );
		}
	}
	
}
