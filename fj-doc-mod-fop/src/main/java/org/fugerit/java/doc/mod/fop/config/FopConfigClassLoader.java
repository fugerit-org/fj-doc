package org.fugerit.java.doc.mod.fop.config;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;

import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.FopFactoryBuilder;
import org.apache.fop.configuration.DefaultConfigurationBuilder;
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
	    FopFactoryBuilder builder = new FopFactoryBuilder(new File(".").toURI(), new ClassLoaderResourceResolver( this.getDefaultFontPath() ));
	    FopFactory factory = builder.setConfiguration(new DefaultConfigurationBuilder().build(fopConfigStream)).build();
	    fopConfigStream.close();
	    return factory;
	}

}
