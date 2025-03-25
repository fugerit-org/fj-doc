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

import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;

import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.doc.base.helper.Base64Helper;
import org.fugerit.java.doc.base.helper.SourceResolverHelper;

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
	
	public String getResolvedBase64() {
		return SafeFunction.get( () -> {
			String res = this.getBase64();
			if ( StringUtils.isEmpty( res ) ) {
				res = SourceResolverHelper.resolveImageToBase64( this );
			}
			return res;	
		} );
	}

	public String getResolvedText() {
		return SafeFunction.get( () -> new String( SourceResolverHelper.resolveImage( this ) ) );
	}
	
	public String getResolvedType() {
		return StringUtils.valueWithDefault( this.getType() , this.getUrl() );
	}

	public boolean isSvg() {
		return TYPE_SVG.equals( this.getResolvedType() );
	}

}
