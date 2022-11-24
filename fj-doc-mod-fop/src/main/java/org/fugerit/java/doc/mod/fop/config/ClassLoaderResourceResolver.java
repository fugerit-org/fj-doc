package org.fugerit.java.doc.mod.fop.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URI;

import org.apache.xmlgraphics.io.Resource;
import org.apache.xmlgraphics.io.ResourceResolver;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This ResourceResolver try to load resources from class loader.
 * 
 * Starting with version 0.5.2 it is deprecated. [0.5.2](https://github.com/fugerit-org/fj-doc/issues/7)
 * 
 * @deprecated use {@link ClassLoaderResourceResolverWrapper} instead.
 * 
 * @author fugerit
 *
 */
@Deprecated
public class ClassLoaderResourceResolver implements ResourceResolver, Serializable {

	private static final Logger logger = LoggerFactory.getLogger( ClassLoaderResourceResolver.class );
	
	private String defaultFontPath;
	
	public String getDefaultFontPath() {
		return defaultFontPath;
	}

	public void setDefaultFontPath(String defaultFontPath) {
		this.defaultFontPath = defaultFontPath;
	}

	public ClassLoaderResourceResolver(String defaultFontPath) {
		super();
		this.defaultFontPath = defaultFontPath;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 4159394346585482675L;

	@Override
	public OutputStream getOutputStream(URI uri) throws IOException {
		logger.warn( "{} is deprecated it is suggested to switch to fop-config-mode='classloader', see: {}", this.getClass().getName(), FopConfigClassLoader.MIN_VERSION_NEW_CLASSLOADER_MODE );
		return Thread.currentThread().getContextClassLoader().getResource(uri.toString()).openConnection().getOutputStream();
	}

	@Override
	public Resource getResource(URI uri) throws IOException {
		try {
			logger.warn( "{} is deprecated it is suggested to switch to fop-config-mode='classloader', see: {}", this.getClass().getName(), FopConfigClassLoader.MIN_VERSION_NEW_CLASSLOADER_MODE );
			String path = this.defaultFontPath+uri.getPath().substring( uri.getPath().lastIndexOf( "/")+1 );
			InputStream inputStream = ClassHelper.loadFromDefaultClassLoader( path );
			return new Resource(inputStream);
		} catch (Exception e) {
			throw new IOException( e );
		}
		
	}
	
}
