package test.org.fugerit.java.doc.mod.fop;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URI;

import org.apache.xmlgraphics.io.Resource;
import org.apache.xmlgraphics.io.ResourceResolver;

public class DoNothingResourceResolver implements ResourceResolver, Serializable {
	
	private static final long serialVersionUID = -679614064826282439L;
	@Override
	public Resource getResource(URI uri) throws IOException {
		return null;
	}
	@Override
	public OutputStream getOutputStream(URI uri) throws IOException {
		return null;
	}
	
}
