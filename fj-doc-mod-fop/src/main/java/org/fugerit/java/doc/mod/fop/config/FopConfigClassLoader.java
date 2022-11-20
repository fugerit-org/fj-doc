package org.fugerit.java.doc.mod.fop.config;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;

import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.FopFactoryBuilder;
import org.apache.fop.apps.io.InternalResourceResolver;
import org.apache.fop.apps.io.ResourceResolverFactory;
import org.apache.fop.configuration.DefaultConfigurationBuilder;
import org.apache.xmlgraphics.io.ResourceResolver;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.mod.fop.FopConfig;

public class FopConfigClassLoader implements FopConfig, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 188843074194800812L;
	
	private String fopConfigPath;
	
	public String getFopConfigPath() {
		return fopConfigPath;
	}

	private String defaultFontPath;
	
	public String getDefaultFontPath() {
		return defaultFontPath;
	}

	public FopConfigClassLoader(String fopConfigPath, String defaultFontPath) {
		super();
		this.fopConfigPath = fopConfigPath;
		this.defaultFontPath = defaultFontPath;
	}

	@Override
	public FopFactory newFactory() throws Exception {
		InputStream fopConfigStream = ClassHelper.loadFromDefaultClassLoader( this.getFopConfigPath() );
		ResourceResolver customResourceResolver = new ClassLoaderResourceResolver( this.getDefaultFontPath() );
	    FopFactoryBuilder builder = new FopFactoryBuilder(new File(".").toURI(), customResourceResolver);
	    FopFactory factory = builder.setConfiguration(new DefaultConfigurationBuilder().build(fopConfigStream)).build();
	    // fix for bug https://github.com/fugerit-org/fj-doc/issues/6 - start #6
	    InternalResourceResolver irr = factory.getFontManager().getResourceResolver();
	    factory.getFontManager().setResourceResolver( ResourceResolverFactory.createInternalResourceResolver( irr.getBaseURI(), customResourceResolver ) );
	    // fix for bug https://github.com/fugerit-org/fj-doc/issues/6 - start #6
	    fopConfigStream.close();
	    return factory;
	}

}
