package org.fugerit.java.doc.mod.fop.config;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URI;

import org.apache.xmlgraphics.io.Resource;
import org.apache.xmlgraphics.io.ResourceResolver;

public class ResourceResolverWrapper implements ResourceResolver, Serializable {

	private static final long serialVersionUID = 1188711703327821113L;

	public ResourceResolverWrapper(ResourceResolver wrapped) {
		super();
		this.wrapped = wrapped;
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
