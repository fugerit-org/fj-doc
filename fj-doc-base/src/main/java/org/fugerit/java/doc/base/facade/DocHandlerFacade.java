package org.fugerit.java.doc.base.facade;

import java.io.OutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.core.util.ObjectUtils;
import org.fugerit.java.core.util.collection.ListMapStringKey;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * DocHandlerFacade
 * 
 * Starting from version 0.2.X registration of type handlers changes : 
 * Now is possible to register a type handler both for type (ex. pdf) or key (ex. pdf-fop, pdf-itext, pdf-box)
 * 
 * @author fugerit
 *
 */
@Slf4j
public class DocHandlerFacade implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8625371479549479952L;
	
	public static final boolean DEFAULT_REGISTER_FOR_TYPE = true;
	
	public static final boolean DEFAULT_ERROR_ON_DUPLICATE = false;
	
	private Map<String, DocTypeHandler> mapHandlers; // map handlers registered by id
	
	private Map<String, ListMapStringKey<DocTypeHandler>> mapTypeHandlers;	// map handlers registered by type
	
	private Map<String, DocTypeHandler> fullMap;
	
	public DocHandlerFacade() {
		this.mapHandlers = new HashMap<>();
		this.mapTypeHandlers = new HashMap<>();
		this.fullMap = new HashMap<>();
	}

	private void doRegister( DocTypeHandler handler, String id ) {
		log.info( "Registering handler with id {} : {}", id, handler.getClass().getName() );
		this.mapHandlers.put( id, handler );
		ListMapStringKey<DocTypeHandler> list = this.mapTypeHandlers.get( handler.getFormat() );
		if ( list == null ) {
			list = new ListMapStringKey<>();
			this.mapTypeHandlers.put( handler.getFormat() , list );
		}
		list.add( handler );
	}
	
	public void registerHandler( DocTypeHandler handler, boolean registerForType, boolean errorOnDuplicate ) throws Exception {
		doRegister( handler, handler.getKey() );
		if ( registerForType ) {
			String format = handler.getFormat();
			String type = handler.getType();
			DocTypeHandler previous = this.mapHandlers.get( format );
			if ( previous != null ) {
				if ( errorOnDuplicate ) {
					throw new ConfigException( "Duplicate handler for format : "+format+" (type:"+type+")" );
				} else {
					log.warn( "Warning duplicate handler for format, {} will replace {}", format, handler.getKey(), previous.getKey() );
				}
			}
			doRegister(handler, format);
		}
		log.info( "list keys current -> {} : list {}", handler, this.mapHandlers.keySet() );
		log.debug( "test" );
	}
	
	public void registerHandlerAndId( String id, DocTypeHandler handler ) throws Exception {
		this.registerHandlerAndId(id, handler, false);
	}
	
	private void registerOnFullMap( String currentKey, DocTypeHandler handler ) {
		DocTypeHandler checkPrevious = this.fullMap.put( currentKey , handler );
		if ( checkPrevious != null ) {
			log.info( "overwriting currentKey : {}, handler : {}", currentKey, checkPrevious );
		}
	}
	
	public void registerHandlerAndId( String id, DocTypeHandler handler, boolean allowDuplicatedId ) throws Exception {
		if ( this.mapHandlers.containsKey( id ) ) {
			if ( allowDuplicatedId ) {
				log.warn( "duplicated id for : id {}, handler : {}", id, handler );
			} else {
				throw new ConfigRuntimeException( "Duplicate handler id not allowd : "+id );
			}
		}
		this.mapHandlers.put( id , handler);
		// new full map handling start
		String format = handler.getFormat();
		String type = handler.getType();
		this.registerOnFullMap( id, handler );
		if ( !id.equals( format ) ) {
			this.registerOnFullMap( format, handler );
		}
		if ( !type.equals( format ) ) {
			this.registerOnFullMap( type, handler );
		}
		// new full map handling end
		this.registerHandler( handler, DEFAULT_REGISTER_FOR_TYPE, DEFAULT_ERROR_ON_DUPLICATE );
	}
	
	public void registerHandler( DocTypeHandler handler ) throws Exception {
		this.registerHandler( handler, DEFAULT_REGISTER_FOR_TYPE, DEFAULT_ERROR_ON_DUPLICATE );
	}

	/**
	 * Handler lookup by id, throws a ConfigRuntimeException if not found.
	 *
	 * @param handlerId		the id of the handler
	 * @return				the handler
	 * throws ConfigRuntimeException if the handler is not found
	 */
	public DocTypeHandler findHandlerRequired( String handlerId ) {
		DocTypeHandler handler = this.findHandler( handlerId );
		if ( handler == null ) {
			throw new ConfigRuntimeException( String.format( "No handler found for id %s, available handler ids are : %s", handlerId, StringUtils.concat( ", ", this.mapHandlers.keySet() ) ) );
		} else {
			return handler;
		}
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

	public void direct( Reader xml, String type, DocOutput docOutput ) throws Exception {
		DocInput docInput = DocInput.newInput( type , xml );
		this.handle( docInput , docOutput);
	}
	
	public void direct( Reader xml, String type, OutputStream os ) throws Exception {
		DocInput docInput = DocInput.newInput( type , xml );
		DocOutput docOutput = DocOutput.newOutput( os );
		this.handle( docInput , docOutput );
	}

	public DocTypeHandler findHandler( String id ) {
		// fail safe on map handler
		DocTypeHandler handler = this.fullMap.get( id );
		if ( handler != null ) {
			return handler;
		} else {
			return this.mapHandlers.get( id );
		}
	}

	public ListMapStringKey<DocTypeHandler> listHandlersForType( String type ) {
		return this.mapTypeHandlers.get( type );
	}

	public Collection<DocTypeHandler> handlers() {
		return this.mapHandlers.values();
	}

	public void logHandlersInfo() {
		log.info( "mapHandlers ids : '{}'", this.mapHandlers.keySet() );
		this.mapHandlers.entrySet().forEach(
				e -> log.info( "key : {}, value : {}", e.getKey(), e.getValue() )
		);
		log.info( "fullMap ids : '{}'", this.fullMap.keySet() );
		this.fullMap.entrySet().forEach(
				e -> log.info( "key : {}, value : {}", e.getKey(), e.getValue() )
		);
	}
	
}
