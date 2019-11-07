package org.fugerit.java.doc.base.facade;

import java.io.InputStream;
import java.util.Collection;

import org.fugerit.java.core.cfg.xml.FactoryCatalog;
import org.fugerit.java.core.cfg.xml.FactoryType;
import org.fugerit.java.core.io.helper.StreamHelper;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.base.config.DocTypeHandler;

public class DocHandlerFactory {
	
	public static final String USE_CATALOG_PROP = "user-catalog";
	
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
				DocTypeHandler handler = (DocTypeHandler) ClassHelper.newInstance( ft.getType() );
				facade.registerHandler( handler );
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
	
}
