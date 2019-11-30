package org.fugerit.java.doc.base.facade;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.cfg.xml.FactoryCatalog;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DocHandlerFacade
 * 
 * Starting from versione 2.2.3.1 registration of type handlers changes : 
 * Now is possible to register a type handler both for type (ex. pdf) or key (ex. pdf-fop, pdf-itext, pdf-box)
 * 
 * @author fugerit
 *
 */
public class DocHandlerFacade implements Serializable {

	private static final Logger logger = LoggerFactory.getLogger( DocHandlerFacade.class );
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8625371479549479952L;
	
	public static final boolean DEFAULT_REGISTER_FOR_TYPE = true;
	
	public static final boolean DEFAULT_ERROR_ON_DUPLICATE = false;
	
	private Map<String, DocTypeHandler> mapHandlers;
	
	public DocHandlerFacade() {
		this.mapHandlers = new HashMap<>();
	}

	private void doRegister( DocTypeHandler handler, String id ) {
		logger.info( "Registering handler with id {} : {}", id, handler.getClass().getName() );
		this.mapHandlers.put( id, handler );	
	}
	
	public void registerHandler( DocTypeHandler handler, boolean registerForType, boolean errorOnDuplicate ) throws Exception {
		doRegister( handler, handler.getKey() );
		if ( registerForType ) {
			String type = handler.getType();
			DocTypeHandler previous = this.mapHandlers.get( type );
			if ( previous != null ) {
				if ( errorOnDuplicate ) {
					throw new ConfigException( "Duplicate handler for type : "+type );
				} else {
					logger.warn( "Warning duplicate handler for type, {} will replace {}", type, handler.getKey(), previous.getKey() );
				}
			}
			doRegister(handler, type);
		}
	}
	
	public void registerHandler( DocTypeHandler handler ) throws Exception {
		this.registerHandler( handler, DEFAULT_REGISTER_FOR_TYPE, DEFAULT_ERROR_ON_DUPLICATE );
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
	
	public DocTypeHandler findHandler( String id ) {
		return this.mapHandlers.get( id );
	}
	
	public void register( String factoryCatalogPath ) {
		
	}
	
	public void register( FactoryCatalog catalog ) {
		
	}
	
}
