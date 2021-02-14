package org.fugerit.java.doc.mod.fop.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URI;

import org.apache.xmlgraphics.io.Resource;
import org.apache.xmlgraphics.io.ResourceResolver;
import org.fugerit.java.core.lang.helpers.ClassHelper;

public class ClassLoaderResourceResolver implements ResourceResolver, Serializable {

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
		return Thread.currentThread().getContextClassLoader().getResource(uri.toString()).openConnection().getOutputStream();
	}

	@Override
	public Resource getResource(URI uri) throws IOException {
		try {
			String path = "font/"+uri.getPath().substring( uri.getPath().lastIndexOf( "/")+1 );
			InputStream inputStream = ClassHelper.loadFromDefaultClassLoader( path );
			return new Resource(inputStream);
		} catch (Exception e) {
			throw new IOException( e );
		}
		
	}
	
}
