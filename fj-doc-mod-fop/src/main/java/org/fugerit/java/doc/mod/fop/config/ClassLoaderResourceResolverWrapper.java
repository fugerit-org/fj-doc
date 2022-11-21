package org.fugerit.java.doc.mod.fop.config;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import org.apache.fop.apps.io.ResourceResolverFactory;
import org.apache.xmlgraphics.io.Resource;
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
		return Thread.currentThread().getContextClassLoader().getResource(uri.toString()).openConnection().getOutputStream();
	}

	public boolean canHandle( String uri ) {
		return uri.startsWith( CLASSPATH_SCHEMA );
	}
	
	@Override
	public Resource getResource(URI uri) throws IOException {
		try {
			String path = uri.toString();
			boolean canHandle = this.canHandle( path );
			logger.debug( "getResource() canHandle?:{}, uri:{}", canHandle, uri );
			if ( canHandle ) {
				path = path.substring( CLASSPATH_SCHEMA.length() ); ;
				return new Resource( ClassHelper.loadFromDefaultClassLoader( path ) );
			} else {
				return this.unwrap().getResource(uri);
			}
		} catch (Exception e) {
			throw new IOException( e );
		}
	}

}
