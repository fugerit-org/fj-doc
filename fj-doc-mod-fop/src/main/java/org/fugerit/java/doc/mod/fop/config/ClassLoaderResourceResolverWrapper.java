package org.fugerit.java.doc.mod.fop.config;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import org.apache.fop.apps.io.ResourceResolverFactory;
import org.apache.xmlgraphics.io.Resource;
import org.fugerit.java.core.io.helper.HelperIOException;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class loader try to laod from class loader path starting with classpath://
 * 
 * All other uri path are delegated to apache fop default ResourceResolver
 * 
 * @author fugerit
 *
 */
public class ClassLoaderResourceResolverWrapper extends ResourceResolverWrapper {

	private static final long serialVersionUID = -4400353184756089423L;

	private static final Logger logger = LoggerFactory.getLogger( ClassLoaderResourceResolverWrapper.class );
	
	public static final String CLASSPATH_SCHEMA = "classpath://";
	
	public ClassLoaderResourceResolverWrapper() {
		super( ResourceResolverFactory.createDefaultResourceResolver() );
	}

	@Override
	public OutputStream getOutputStream(URI uri) throws IOException {
		logger.debug( "getOutputStream() uri -> {}", uri );
		return this.unwrap().getOutputStream(uri);
	}

	public boolean canHandle( String uri ) {
		return uri.startsWith( CLASSPATH_SCHEMA );
	}
	
	@Override
	public Resource getResource(URI uri) throws IOException {
		return HelperIOException.get( () -> {
			String path = uri.toString();
			boolean canHandle = this.canHandle( path );
			logger.debug( "getResource() canHandle?:{}, uri:{}", canHandle, uri );
			if ( canHandle ) {
				path = path.substring( CLASSPATH_SCHEMA.length() );
				return new Resource( ClassHelper.loadFromDefaultClassLoader( path ) );
			} else {
				return this.unwrap().getResource(uri);
			}
		} );
	}

}
