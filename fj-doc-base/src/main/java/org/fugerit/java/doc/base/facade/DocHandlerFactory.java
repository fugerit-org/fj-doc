package org.fugerit.java.doc.base.facade;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;

import org.fugerit.java.core.cfg.xml.FactoryCatalog;
import org.fugerit.java.core.cfg.xml.FactoryType;
import org.fugerit.java.core.cfg.xml.FactoryTypeHelper;
import org.fugerit.java.core.io.helper.StreamHelper;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DocHandlerFactory extends HashMap<String, DocHandlerFacade> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4879476159814004578L;
	
	public static final String USE_CATALOG_PROP = "user-catalog";
	
	private String useCatalog;
	
	private static final Logger logger = LoggerFactory.getLogger( DocHandlerFactory.class );
	
	private static final FactoryTypeHelper<DocTypeHandler> HELPER = FactoryTypeHelper.newInstance( DocTypeHandler.class );
	
	public static DocHandlerFacade register( String factoryCatalogPath ) throws Exception {
		return register( factoryCatalogPath, null );
	}
	
	public static DocHandlerFacade register( String factoryCatalogPath, String useFactory ) throws Exception {
		DocHandlerFacade facade = null;
		try ( InputStream is = StreamHelper.resolveStream( factoryCatalogPath ) ) {
			FactoryCatalog catalog = new FactoryCatalog();
			catalog.configureXML( is );
			facade = register( catalog, useFactory );
		}
		return facade;
	}
	
	
	public static DocHandlerFacade register( Collection<FactoryType> col ) throws Exception {
		DocHandlerFacade facade = null;
		if ( col != null ) {
			facade = new DocHandlerFacade();
			for ( FactoryType ft : col ) {
				DocTypeHandler handler = HELPER.createHelper( ft );
				if ( handler != null ) {
					facade.registerHandler( handler );
				} else {
					logger.info( "skipped null handler for -> {}", ft );
				}
			}
		}
		return facade;
	}
	
	public static DocHandlerFacade register( FactoryCatalog catalog ) throws Exception {
		return register( catalog, null );
	}
		
	public static DocHandlerFacade register( FactoryCatalog catalog, String useFactory  ) throws Exception {
		DocHandlerFacade facade = null;
		String useCatalog = useFactory;
		if ( useCatalog == null ) {
			useCatalog = catalog.getGeneralProps().getProperty( USE_CATALOG_PROP );
		}
		if ( useCatalog != null ) {
			Collection<FactoryType> col = catalog.getDataList( useCatalog );
			facade = register( col );
		}
		return facade;
	}

	
	public static DocHandlerFactory newInstance( InputStream factoryConfig ) throws Exception {
		FactoryCatalog catalog = new FactoryCatalog();
		catalog.configureXML( factoryConfig );
		DocHandlerFactory map = newInstance( catalog );
		return map;
	}
	
	public static DocHandlerFactory newInstance( String factoryCatalogPath ) throws Exception {
		DocHandlerFactory map = null;
		try ( InputStream is = StreamHelper.resolveStream( factoryCatalogPath ) ) {
			map = newInstance( is );
		}
		return map;
	}
	
	public static DocHandlerFactory newInstance( FactoryCatalog catalog ) throws Exception {
		DocHandlerFactory map = new DocHandlerFactory( catalog.getGeneralProps().getProperty( USE_CATALOG_PROP ) );
		for ( String id : catalog.getIdSet() ) {
			Collection<FactoryType> col = catalog.getDataList( id );
			DocHandlerFacade facade = register( col );
			map.put( id , facade );
		}
		return map;
	}

	private DocHandlerFactory(String useCatalog) {
		super();
		this.useCatalog = useCatalog;
	}

	public String getUseCatalog() {
		return useCatalog;
	}

}

