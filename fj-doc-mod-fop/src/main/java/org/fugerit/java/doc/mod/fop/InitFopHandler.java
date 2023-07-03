package org.fugerit.java.doc.mod.fop;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.doc.base.config.InitHandler;

/**
 * PdfFopTypeHandler Initializer based on a base configuration.
 * If AOT initialization is needed it is possible to call these Facade.
 * 
 */
public class InitFopHandler {

	public static final PdfFopTypeHandler HANDLER = new PdfFopTypeHandler();
	
	public static boolean initDoc() throws ConfigException {
		return InitHandler.initDoc( HANDLER );
	}
	
	public static void initDocAsync() {
		InitHandler.initDocAsync( HANDLER );
	}
	
}
