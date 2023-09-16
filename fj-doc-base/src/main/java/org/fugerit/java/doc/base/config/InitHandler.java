package org.fugerit.java.doc.base.config;

import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;

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
	
	public static boolean initDoc( DocTypeHandler handler ) throws ConfigException {
		boolean init = true;
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
