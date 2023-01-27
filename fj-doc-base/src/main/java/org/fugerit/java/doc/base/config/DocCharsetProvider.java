package org.fugerit.java.doc.base.config;

import java.nio.charset.Charset;

import org.fugerit.java.core.util.ObjectUtils;

public abstract class DocCharsetProvider {

	public abstract Charset resolveCharset( Charset charset );
	
	public static final DocCharsetProvider DEFAULT = new DocCharsetProvider() {
		@Override
		public Charset resolveCharset(Charset charset) {
			return ObjectUtils.objectWithDefault( charset , Charset.defaultCharset() );
		}
	}; 
	
	private static DocCharsetProvider defaultProvider = DEFAULT;

	public static DocCharsetProvider getDefaultProvider() {
		return defaultProvider;
	}

	public static void setDefaultProvider(DocCharsetProvider defaultProvider) {
		DocCharsetProvider.defaultProvider = defaultProvider;
	}
	
}
