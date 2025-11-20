/*****************************************************************
<copyright>
	Fugerit Java Library 

	Copyright (c) 2007 Fugerit

	All rights reserved. This program and the accompanying materials
	are made available under the terms of the Apache License v2.0
	which accompanies this distribution, and is available at
	http://www.apache.org/licenses/
	(txt version : http://www.apache.org/licenses/LICENSE-2.0.txt
	html version : http://www.apache.org/licenses/LICENSE-2.0.html)

   This product includes software developed at
   The Apache Software Foundation (http://www.apache.org/).
</copyright>
*****************************************************************/
/*
 * @(#)DocImage.java
 *
 * @project     : org.fugerit.java.doc.base
 * @package     : org.fugerit.java.doc.base
 * @creation	: 15/lug/07
 * @release		: xxxx.xx.xx
 */
package org.fugerit.java.doc.base.model;


import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;

import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.core.io.helper.HelperIOException;
import org.fugerit.java.core.io.helper.StreamHelper;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.doc.base.helper.Base64Helper;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 *
 * @author Fugerit
 *
 */
public class DocImage extends DocElement {

	public static final String TYPE_PNG = "png";
	
	public static final String TYPE_JPG = "jpg";
	
	public static final String TYPE_GIF = "gif";

	public static final String TYPE_SVG = "svg";

	public static Collection<String> getAcceptedImageTypes() {
		return Arrays.asList( TYPE_PNG, TYPE_JPG, TYPE_GIF, TYPE_SVG );
	}
	
	public static final String TAG_NAME = "image";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5892416838638462834L;

	@Getter @Setter private Integer scaling;
	
	@Getter @Setter private String url;
	
	@Getter @Setter private String base64;
	
	@Getter @Setter private String type;
	
	@Getter @Setter private String alt;
	
	@Getter @Setter private int align;

	@Getter @Setter private String content = "";

	public String getResolvedBase64() {
		return SafeFunction.get( () -> {
			String res = this.getBase64();
			if ( StringUtils.isEmpty( res ) ) {
				res = resolveImageToBase64( this );
			}
			return res;	
		} );
	}

	public String getResolvedText() {
		return SafeFunction.get( () -> {
			if ( StringUtils.isEmpty( this.content ) ) {
				return new String( resolveImage( this ) );
			} else {
				return this.content;
			}
		});
	}
	
	public String getResolvedType() {
		return StringUtils.valueWithDefault( this.getType() , this.getUrl() );
	}

	public boolean isSvg() {
		return TYPE_SVG.equals( this.getResolvedType() );
	}

	private static byte[] byteResolverHelper( String path ) throws IOException, URISyntaxException {
		try ( InputStream is = path.startsWith( StreamHelper.PATH_CLASSLOADER ) ? StreamHelper.resolveStream( path ) : new URI( path ).toURL().openConnection().getInputStream() ) {
			return StreamIO.readBytes( is );
		}
	}

	public static String resolveImageToBase64( DocImage img ) throws IOException {
		return HelperIOException.get( () -> {
			String path = img.getUrl();
			String base64 = img.getBase64();
			if ( StringUtils.isEmpty( base64 ) && path != null ) {
				byte[] data = byteResolverHelper( path );
				base64 = Base64Helper.encodeBase64String( data );
			} else {
				throw new IOException( "Null path and base64 provided!" );
			}
			return base64;
		} );
	}

	public static byte[] resolveImage( DocImage img ) throws IOException {
		return HelperIOException.get( () -> {
			byte[] data = null;
			String path = img.getUrl();
			String base64 = img.getBase64();
			if ( StringUtils.isNotEmpty( base64 ) ) {
				data = Base64Helper.decodeBase64String( base64 );
			} else if ( path != null ) {
				data = byteResolverHelper( path );
			} else if ( StringUtils.isNotEmpty( img.getContent() ) ) {
				data = img.getContent().getBytes( StandardCharsets.UTF_8 );
			} else {
				throw new IOException( "Null path provided!" );
			}
			return data;
		} );
	}

}
