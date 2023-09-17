package org.fugerit.java.doc.mod.fop.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URI;

import org.apache.fop.apps.EnvironmentalProfileFactory;
import org.apache.fop.apps.FopConfParser;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.FopFactoryBuilder;
import org.apache.xmlgraphics.io.ResourceResolver;
import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.mod.fop.FopConfig;

public class FopConfigClassLoaderWrapper implements FopConfig, Serializable {

	// code added to setup a basic conditional serialization - START
	
	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		// this class is conditionally serializable, depending on contained object
		// special situation can be handleded using this method in future
		out.defaultWriteObject();
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		// this class is conditionally serializable, depending on contained object
		// special situation can be handleded using this method in future
		in.defaultReadObject();
		this.customResourceResolver = ResourceResolverWrapper.getResolver( this.resolverType );
	}
	
	// code added to setup a basic conditional serialization - END
	
	public static final ResourceResolver DEFAULT_RESOURCE_RESOLVER = new ClassLoaderResourceResolverWrapper();
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 188843074194800812L;
	
	private String fopConfigPath;
	
	private String resolverType;
	
	public String getFopConfigPath() {
		return fopConfigPath;
	}

	private transient ResourceResolver customResourceResolver;

	public ResourceResolver getCustomResourceResolver() {
		return customResourceResolver;
	}

	public FopConfigClassLoaderWrapper(String fopConfigPath ) {
		this( fopConfigPath, DEFAULT_RESOURCE_RESOLVER );
	}
	
	public FopConfigClassLoaderWrapper(String fopConfigPath, ResourceResolver customResourceResolver) {
		super();
		this.fopConfigPath = fopConfigPath;
		this.customResourceResolver = customResourceResolver;
		this.resolverType = customResourceResolver.getClass().getName();
	}

	@Override
	public FopFactory newFactory() throws ConfigException {
		return ConfigException.get( () -> {
			FopFactory fopFactory = null;
			try ( InputStream fopConfigStream = ClassHelper.loadFromDefaultClassLoader( this.getFopConfigPath() ) ) {
				FopConfParser confParser =  new FopConfParser( fopConfigStream, EnvironmentalProfileFactory.createRestrictedIO(new URI("."), this.customResourceResolver) );
			    FopFactoryBuilder confBuilder = confParser.getFopFactoryBuilder();
			    fopFactory = confBuilder.build();	
			}
		    return fopFactory;
		} );
	}

}
