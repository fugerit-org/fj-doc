package org.fugerit.java.doc.mod.fop.config;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URI;

import org.apache.fop.apps.io.ResourceResolverFactory;
import org.apache.xmlgraphics.io.Resource;
import org.apache.xmlgraphics.io.ResourceResolver;
import org.fugerit.java.core.io.helper.HelperIOException;
import org.fugerit.java.core.lang.helpers.ClassHelper;

public class ResourceResolverWrapper implements ResourceResolver, Serializable {

	private static final long serialVersionUID = 1188711703327821113L;

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
		this.wrapped = getResolver( this.resolverType );
	}
	
	// code added to setup a basic conditional serialization - END
	
	public static ResourceResolver getResolver( String type ) throws IOException {
		return HelperIOException.get( () -> {
			ResourceResolver defaultResolver = ResourceResolverFactory.createDefaultResourceResolver();
			if ( !defaultResolver.getClass().getName().equals( type ) ) {
				defaultResolver = (ResourceResolver)ClassHelper.newInstance( type );
			}
			return defaultResolver;
		} );
	}
	
	private String resolverType;
	
	public ResourceResolverWrapper(ResourceResolver wrapped) {
		super();
		this.wrap(wrapped);
	}

	public ResourceResolverWrapper() {
		this.wrapped = null;
	}
	
	private ResourceResolver wrapped;
	
	public ResourceResolver unwrap() {
		return wrapped;
	}

	public void wrap(ResourceResolver wrapped) {
		this.wrapped = wrapped;
		this.resolverType = wrapped.getClass().getName();
	}

	@Override
	public Resource getResource(URI uri) throws IOException {
		return this.wrapped.getResource(uri);
	}

	@Override
	public OutputStream getOutputStream(URI uri) throws IOException {
		return this.wrapped.getOutputStream(uri);
	}

}
