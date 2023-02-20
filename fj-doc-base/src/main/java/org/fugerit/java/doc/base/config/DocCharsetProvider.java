package org.fugerit.java.doc.base.config;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.fugerit.java.core.util.ObjectUtils;

public abstract class DocCharsetProvider {

	public abstract Charset resolveCharset( Charset charset );
	
	public static final DocCharsetProvider DEFAULT = newProvider( Charset.defaultCharset() );
	
	public static final DocCharsetProvider DEFAULT_TO_UTF8 = newProvider( StandardCharsets.UTF_8 );
	
	private static DocCharsetProvider defaultProvider = DEFAULT;

	public static DocCharsetProvider getDefaultProvider() {
		return defaultProvider;
	}

	public static void setDefaultProvider(DocCharsetProvider defaultProvider) {
		DocCharsetProvider.defaultProvider = defaultProvider;
	}
	
	public static DocCharsetProvider newProvider( Charset useDefault ) {
		return new DocCharsetProvider() {
			@Override
			public Charset resolveCharset(Charset charset) {
				return ObjectUtils.objectWithDefault( charset , useDefault );
			}
		};
	}; 
	
	
}
