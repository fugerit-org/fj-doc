package org.fugerit.java.doc.base.facade;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;

import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.cfg.xml.FactoryCatalog;
import org.fugerit.java.core.cfg.xml.FactoryType;
import org.fugerit.java.core.cfg.xml.FactoryTypeHelper;
import org.fugerit.java.core.function.SafeFunction;
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
	
	@Override
	public int hashCode() {
		// super class implementation is ok
		return super.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		// super class implementation is ok
		return super.equals(o);
	}
	
	/**
	 * <p>Creates and configure an instance of DocHandlerFacade with default catalog id.</p>
	 * 
	 * <p>NOTE: starting from version 8.4.X java.lang.Exception removed in favor of org.fugerit.java.core.cfg.ConfigRuntimeException.</p>
	 * 
	 * @see <a href="https://fuzzymemory.fugerit.org/src/docs/sonar_cloud/java-S112.html">Define and throw a dedicated exception instead of using a generic one.</a>
	 * 
	 * @param factoryCatalogPath		the factory catalog xml path
	 * @return							the configured facade
	 */
	public static DocHandlerFacade register( String factoryCatalogPath ) {
		return register( factoryCatalogPath, null );
	}
	
	/**
	 * <p>Creates and configure an instance of DocHandlerFacade</p>
	 * 
	 * <p>NOTE: starting from version 8.4.X java.lang.Exception removed in favor of org.fugerit.java.core.cfg.ConfigRuntimeException.</p>
	 * 
	 * @see <a href="https://fuzzymemory.fugerit.org/src/docs/sonar_cloud/java-S112.html">Define and throw a dedicated exception instead of using a generic one.</a>
	 * 
	 * @param factoryCatalogPath		the factory catalog xml path
	 * @param useCatalog				the catalog id to use
	 * @return							the configured facade
	 */
	public static DocHandlerFacade register( String factoryCatalogPath, String useCatalog ) {
		return SafeFunction.get( () -> {
			DocHandlerFacade facade = null;
			try ( InputStream is = StreamHelper.resolveStream( factoryCatalogPath ) ) {
				FactoryCatalog catalog = new FactoryCatalog();
				catalog.configureXML( is );
				facade = register( catalog, useCatalog );
			}
			return facade;
		} );
	}
	
	/**
	 * <p>Creates and configure an instance of DocHandlerFacade</p>
	 * 
	 * <p>NOTE: starting from version 8.4.X java.lang.Exception removed in favor of org.fugerit.java.core.cfg.ConfigRuntimeException.</p>
	 * 
	 * @see <a href="https://fuzzymemory.fugerit.org/src/docs/sonar_cloud/java-S112.html">Define and throw a dedicated exception instead of using a generic one.</a>
	 * 
	 * @param col	the collection of factory type
	 * @return		the configured facade
	 */
	public static DocHandlerFacade register( Collection<FactoryType> col ) {
		return SafeFunction.get( () -> {
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
		} );

	}
	
	/**
	 * <p>Creates and configure an instance of DocHandlerFacade with default catalog id.</p>
	 * 
	 * <p>NOTE: starting from version 8.4.X java.lang.Exception removed in favor of org.fugerit.java.core.cfg.ConfigRuntimeException.</p>
	 * 
	 * @see <a href="https://fuzzymemory.fugerit.org/src/docs/sonar_cloud/java-S112.html">Define and throw a dedicated exception instead of using a generic one.</a>
	 * 
	 * @param catalog		the factory catalog
	 * @return				the doc handler facade
	 */
	public static DocHandlerFacade register( FactoryCatalog catalog ) {
		return register( catalog, null );
	}
		
	/**
	 * <p>Creates and configure an instance of DocHandlerFacade</p>
	 * 
	 * <p>NOTE: starting from version 8.4.X java.lang.Exception removed in favor of org.fugerit.java.core.cfg.ConfigRuntimeException.</p>
	 * 
	 * @see <a href="https://fuzzymemory.fugerit.org/src/docs/sonar_cloud/java-S112.html">Define and throw a dedicated exception instead of using a generic one.</a>
	 * 
	 * @param catalog		the factory catalog
	 * @param useCatalog	the catalog id to use to create the facade
	 * @return				the doc handler facade
	 */
	public static DocHandlerFacade register( FactoryCatalog catalog, String useCatalog  ) {
		String checkCatalog = useCatalog;
		DocHandlerFacade facade = null;
		if ( checkCatalog == null ) {
			checkCatalog = catalog.getGeneralProps().getProperty( USE_CATALOG_PROP );
		}
		if ( checkCatalog != null ) {
			Collection<FactoryType> col = catalog.getDataList( checkCatalog );
			facade = register( col );
		}
		return facade;
	}

	
	/**
	 * <p>Creates and configure an instance of DocHandlerFactory</p>
	 * 
	 * <p>NOTE: starting from version 8.4.X java.lang.Exception removed in favor of org.fugerit.java.core.cfg.ConfigRuntimeException.</p>
	 * 
	 * @see <a href="https://fuzzymemory.fugerit.org/src/docs/sonar_cloud/java-S112.html">Define and throw a dedicated exception instead of using a generic one.</a>
	 * 
	 * @param factoryConfig		a stream on xml configuration document
	 * @return			the configured DocHandlerFactory
	 * @throws 			ConfigRuntimeException in case of issues during loading
	 */
	public static DocHandlerFactory newInstance( InputStream factoryConfig ) {
		return SafeFunction.get( () -> {
			FactoryCatalog catalog = new FactoryCatalog();
			catalog.configureXML( factoryConfig );
			return newInstance( catalog );
		} );
	}
	
	/**
	 * <p>Creates and configure an instance of DocHandlerFactory</p>
	 * 
	 * <p>NOTE: starting from version 8.4.X java.lang.Exception removed in favor of org.fugerit.java.core.cfg.ConfigRuntimeException.</p>
	 * 
	 * @see <a href="https://fuzzymemory.fugerit.org/src/docs/sonar_cloud/java-S112.html">Define and throw a dedicated exception instead of using a generic one.</a>
	 * 
	 * @param factoryCatalogPath	the path to the factory catalog xml
	 * @return			the configured DocHandlerFactory
	 * @throws 			ConfigRuntimeException in case of issues during loading
	 */
	public static DocHandlerFactory newInstance( String factoryCatalogPath ) {
		return SafeFunction.get( () -> {
			DocHandlerFactory map = null;
			try ( InputStream is = StreamHelper.resolveStream( factoryCatalogPath ) ) {
				map = newInstance( is );
			}
			return map;
		} );
	}
	
	/**
	 * <p>Creates and configure an instance of DocHandlerFactory</p>
	 * 
	 * <p>NOTE: starting from version 8.4.X java.lang.Exception removed in favor of org.fugerit.java.core.cfg.ConfigRuntimeException.</p>
	 * 
	 * @see <a href="https://fuzzymemory.fugerit.org/src/docs/sonar_cloud/java-S112.html">Define and throw a dedicated exception instead of using a generic one.</a>
	 * 
	 * @param catalog		the factory catalog to use
	 * @return				the configured factory
	 */
	public static DocHandlerFactory newInstance( FactoryCatalog catalog ) {
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

