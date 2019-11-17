/*****************************************************************
<copyright>
	Fugerit Java Library org.fugerit.java.doc.ent 

	Copyright (c) 2019 Fugerit

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
 * @(#)SampleFacade.java
 *
 * @project    : org.fugerit.java.doc.ent
 * @package    : org.fugerit.java.doc.sample.facade
 * @creation   : 13/set/06
 * @license	   : META-INF/LICENSE.TXT
 */
package org.fugerit.java.doc.sample.facade;

import org.fugerit.java.doc.base.facade.DocHandlerFacade;
import org.fugerit.java.doc.base.facade.DocHandlerFactory;

/**
 * 
 *
 * @author mfranci
 *
 */
public class SampleFacade {

	private static DocHandlerFactory init( String path ) {
		DocHandlerFactory factory = null;
		try {
			factory = DocHandlerFactory.newInstance( path );
		} catch (Exception e) {
			throw new RuntimeException( e );
		}
		return factory;
	}
	
	private static DocHandlerFactory FACTORY = init( "cl://config/doc-handler-sample.xml" );
	
	public static final String MAIN_FACTORY = "default-complete";
	
	public static final String ALT_COMPLETE_FACTORY = "alternate-complete";
	
	public static final String ALT_FOP_FACTORY = "alternate-fop";
	
	public static final String ALT_HTML_FM = "alternate-html-fm";
	
	public static DocHandlerFacade getFacade( String id ) {
		return FACTORY.get( id );
	}

	public static DocHandlerFacade getInstance() {
		return getFacade( MAIN_FACTORY ) ;
	}	
	
}
