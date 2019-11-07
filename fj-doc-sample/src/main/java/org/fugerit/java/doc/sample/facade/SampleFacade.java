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

import java.io.OutputStream;

import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.facade.DocHandlerFacade;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.type.XlsTypeHandler;
import org.fugerit.java.doc.mod.itext.ITextDocHandler;
import org.fugerit.java.doc.mod.pdfbox.PdfBoxDocHandler;

/**
 * 
 *
 * @author mfranci
 *
 */
public class SampleFacade {

	private static DocHandlerFacade FACADE = new DocHandlerFacade();
	static {
		try {
			//FACADE.registerHandler( org.fugerit.java.doc.mod.pdfbox.PdfBoxTypeHandler.HANDLER );
			FACADE.registerHandler( org.fugerit.java.doc.mod.itext.PdfTypeHandler.HANDLER );
			FACADE.registerHandler( org.fugerit.java.doc.mod.itext.RtfTypeHandler.HANDLER );
			FACADE.registerHandler( org.fugerit.java.doc.mod.itext.HtmlTypeHandler.HANDLER );
			FACADE.registerHandler( XlsTypeHandler.HANDLER );
		} catch (Exception e) {
			throw new RuntimeException( e );
		}	
	}
	
	public static void createXLS( DocBase docBase, OutputStream outputStream ) throws Exception {
		FACADE.handle( DocInput.newInput( XlsTypeHandler.DOC_OUTPUT_XLS , docBase) , DocOutput.newOutput( outputStream ) );
	}	
	
	public static void createPDF( DocBase docBase, OutputStream outputStream ) throws Exception {
		FACADE.handle( DocInput.newInput( PdfBoxDocHandler.DOC_OUTPUT_PDF , docBase) , DocOutput.newOutput( outputStream ) );	
	}		
	
	public static void createRTF( DocBase docBase, OutputStream outputStream ) throws Exception {
		FACADE.handle( DocInput.newInput( ITextDocHandler.DOC_OUTPUT_RTF , docBase) , DocOutput.newOutput( outputStream ) );
	}	
	
	public static void createHTML( DocBase docBase, OutputStream outputStream ) throws Exception {
		FACADE.handle( DocInput.newInput( ITextDocHandler.DOC_OUTPUT_HTML , docBase) , DocOutput.newOutput( outputStream ) );		
	}		
	
}
