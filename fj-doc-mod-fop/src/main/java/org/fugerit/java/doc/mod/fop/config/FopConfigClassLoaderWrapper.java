package org.fugerit.java.doc.mod.fop.config;

import java.io.InputStream;
import java.io.Serializable;
import java.net.URI;

import org.apache.fop.apps.EnvironmentalProfileFactory;
import org.apache.fop.apps.FopConfParser;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.FopFactoryBuilder;
import org.apache.xmlgraphics.io.ResourceResolver;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.mod.fop.FopConfig;

public class FopConfigClassLoaderWrapper implements FopConfig, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 188843074194800812L;
	
	private String fopConfigPath;
	
	public String getFopConfigPath() {
		return fopConfigPath;
	}

	private ResourceResolver customResourceResolver;

	public ResourceResolver getCustomResourceResolver() {
		return customResourceResolver;
	}

	public FopConfigClassLoaderWrapper(String fopConfigPath, ResourceResolver customResourceResolver) {
		super();
		this.fopConfigPath = fopConfigPath;
		this.customResourceResolver = customResourceResolver;
	}

	@Override
	public FopFactory newFactory() throws Exception {
		FopFactory fopFactory = null;
		try ( InputStream fopConfigStream = ClassHelper.loadFromDefaultClassLoader( this.getFopConfigPath() ) ) {
			FopConfParser confParser =  new FopConfParser( fopConfigStream, EnvironmentalProfileFactory.createRestrictedIO(new URI("."), this.customResourceResolver) );
		    FopFactoryBuilder confBuilder = confParser.getFopFactoryBuilder();
		    fopFactory = confBuilder.build();	
		}
	    return fopFactory;
	}

}
