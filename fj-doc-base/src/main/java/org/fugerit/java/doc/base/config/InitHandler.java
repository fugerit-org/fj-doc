package org.fugerit.java.doc.base.config;

import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.core.util.checkpoint.CheckpointUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * DocTypeHandler Initializer.
 * If AOT initialization is needed it is possible to call these Facade.
 * 
 */
@Slf4j
public class InitHandler {

	private InitHandler() {} 	// java:S1118
	
	public static final String PATH_INIT_DOC = "config/init_doc/doc-init.xml";
	
	/**
	 * <p>Initialize a collection of handlers in different threads.</p>
	 * 
	 * @param handlers	the handlers to initialize
	 */
	public static void initDocAllAsync( Collection<DocTypeHandler> handlers ) {
		for ( DocTypeHandler handler : new HashSet<>( handlers ) ) {
			initDocAsync( handler );	
		}
	}
	
	/**
	 * <p>Initialize a collection of handlers</p>
	 * 
	 * @param handlers	the handlers to initialize
	 * @return			the map of handlers which failed startup with their exception if any
	 */
	public static Map<DocTypeHandler, Exception> initDocAll( Collection<DocTypeHandler> handlers ) {
		Map<DocTypeHandler, Exception> failed = new HashMap<>();
		for ( DocTypeHandler handler : new HashSet<>( handlers ) ) {
			try {
				initDoc( handler );	
			} catch (ConfigException e) {
				failed.put( handler , (Exception)e.getCause() );
			}
		}
		return failed;
	}
	
	/**
	 * <p>Initialize one handler.</p>
	 * 
	 * @param handler 				the handler to initialize
	 * @return						if the handler has been initialized
	 * @throws ConfigException		in case of exceptions
	 */
	public static boolean initDoc( DocTypeHandler handler ) throws ConfigException {
		boolean init = true;
		log.info( "initDoc start : {}", handler );
		long startTime = System.currentTimeMillis();
		try ( InputStreamReader reader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader( PATH_INIT_DOC ) );
				ByteArrayOutputStream baos = new ByteArrayOutputStream() )  {
			handler.handle( DocInput.newInput( handler.getType() , reader ) , DocOutput.newOutput( baos ) );
			log.info( "Init handler time {} -> {}", handler, CheckpointUtils.formatTimeDiffMillis( startTime , System.currentTimeMillis() ) );
		} catch (Exception e) {
			throw new ConfigException( "Init exception : "+e, e );
		}
		return init;
	}
	
	/**
	 * <p>Initialize one handler in a different thread.</p>
	 * 
	 * <p>Exceptions will be just logged.</p>
	 * 
	 * @param handler		the handler to initialize
	 */
	public static void initDocAsync( DocTypeHandler handler ) {
		Thread t = new Thread( () -> {
			log.info( "Init handler start : {}", handler );
			SafeFunction.applySilent( () -> {
				boolean initOk = initDoc(handler);
				log.info( "Init handler end : {} -> {}", handler, initOk );
			});	
		} );
		t.start();
	}
	
}
