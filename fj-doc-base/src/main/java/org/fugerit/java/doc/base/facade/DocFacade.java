/*****************************************************************
<copyright>
	Fugerit Java Library org.fugerit.java.doc.base 

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
 * @(#)DocFacade.java
 *
 * @project    : org.fugerit.java.doc.base
 * @package    : org.fugerit.java.doc.base
 * @creation   : 06/set/06
 * @license	   : META-INF/LICENSE.TXT
 */
package org.fugerit.java.doc.base.facade;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.util.Iterator;
import java.util.Properties;

import javax.xml.parsers.SAXParser;

import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.core.lang.helpers.Result;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.core.xml.XMLValidator;
import org.fugerit.java.core.xml.sax.XMLFactorySAX;
import org.fugerit.java.core.xml.sax.XMLValidatorSAX;
import org.fugerit.java.core.xml.sax.dh.DefaultHandlerComp;
import org.fugerit.java.core.xml.sax.er.ByteArrayEntityResolver;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.model.DocContainer;
import org.fugerit.java.doc.base.model.DocElement;
import org.fugerit.java.doc.base.model.DocHelper;
import org.fugerit.java.doc.base.xml.DocContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

/**
 * 
 *
 * @author mfranci
 *
 */
public class DocFacade {

	public static final String CURRENT_VERSION = "1-3";
	
	private static Logger logger = LoggerFactory.getLogger( DocFacade.class );
	
	private static final Properties DEFAULT_PARAMS = new Properties();
	
	private static void print( PrintStream s, DocContainer docContainer, String indent ) {
		Iterator<DocElement> it = docContainer.docElements();
		while ( it.hasNext() ) {
			DocElement docElement = (DocElement) it.next();
			s.println( indent+docElement );
			if ( docElement instanceof DocContainer ) {
				print( s, (DocContainer)docElement, indent+"  " );
			}
		}
	}
	
	private static void print( PrintStream s, DocContainer docContainer ) {
		print( s, docContainer, "" );
	}
	
	public static void print( PrintStream s, DocBase doc ) {
		print( s, doc.getDocBody() );
	}
	
	private static final String DTD = "/config/doc-1-0.dtd";
	private static final String XSD = "/config/doc-"+CURRENT_VERSION+".xsd";
	
	private static byte[] readData( String res, byte[] alreadRead ) {
		byte[] data = alreadRead;
		if ( data != null ) {
			try {
				data = StreamIO.readBytes( DocFacade.class.getResourceAsStream( res ) );	
			} catch (Exception e) {
				logger.warn( "Read error", e );
			}	
		}
		return data;
	}
	
	private static final byte[] XSD_DATA = readData( XSD, null );
	private static final byte[] DTD_DATA = readData( DTD, null );
	
	public static final String PARAM_DEFINITION_MODE = "definition-mode";
	public static final String PARAM_DEFINITION_MODE_XSD = "xsd";
	public static final String PARAM_DEFINITION_MODE_DTD = "dtd";
	public static final String PARAM_DEFINITION_MODE_DEFAULT = PARAM_DEFINITION_MODE_XSD;
	
	public final static String SYSTEM_ID = "http://javacoredoc.fugerit.org";
	
	private static ByteArrayEntityResolver newEntityResolver( Properties params ) {
		byte[] data = null;
		String validatationMode = params.getProperty( PARAM_DEFINITION_MODE, PARAM_DEFINITION_MODE_DEFAULT ) ;
		if ( PARAM_DEFINITION_MODE_DTD.equalsIgnoreCase( validatationMode ) ) {
			data = DTD_DATA;
		} else {
			data = XSD_DATA;
		}
		logger.debug( PARAM_DEFINITION_MODE+" -> "+validatationMode );
		ByteArrayEntityResolver er = new ByteArrayEntityResolver( data, null, SYSTEM_ID );
		return er;
	}
	
	public static boolean validate( Reader is, Properties params ) throws Exception {
		ByteArrayEntityResolver er = newEntityResolver( params );
		XMLValidator validator = XMLValidatorSAX.newInstance( er );
		Result result = validator.validateXML( is );
		return result.isTotalSuccess();
	}
	
	public static DocBase parse( Reader is, DocHelper docHelper ) throws Exception {
		return parse( is, docHelper, DEFAULT_PARAMS );
	}
	
	public static DocBase parse( Reader is, DocHelper docHelper, Properties params ) throws Exception {
		SAXParser parser = XMLFactorySAX.makeSAXParser( false ,  false );
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		StreamIO.pipeStream( DocFacade.class.getResourceAsStream( DTD  ), baos, StreamIO.MODE_CLOSE_BOTH );
		ByteArrayEntityResolver er = newEntityResolver( params );
		DocContentHandler dch =  new DocContentHandler( docHelper );
		DefaultHandlerComp dh = new DefaultHandlerComp( dch );
		dh.setWrappedEntityResolver( er );
		parser.parse( new InputSource(is), dh);
		DocBase docBase = dch.getDocBase();
		is.close();
		String xsdVersion = docBase.getXsdVersion();
		if ( StringUtils.isNotEmpty( xsdVersion ) && xsdVersion.compareTo( CURRENT_VERSION ) > 0 ) {
			logger.warn( "Document version {} is higher than maximum version supported by this release od fj-doc {}, some feature may be not supported.", xsdVersion, CURRENT_VERSION  );
		}
		return docBase;
	}	
	
	public static DocBase parse( InputStream is, DocHelper docHelper, Properties params ) throws Exception {
		return parse( new InputStreamReader( is ), docHelper, params );
	}	
	
	public static DocBase parse( Reader is ) throws Exception {
		return parse( is, DocHelper.DEFAULT, DEFAULT_PARAMS );
	}
	
	public static DocBase parseRE( Reader is ) {
		DocBase doc = null;
		try {
			doc = parse( is, DocHelper.DEFAULT, DEFAULT_PARAMS );
		} catch (Exception e) {
			throw new RuntimeException( e );
		}
		return doc;
	}
	
	public static DocBase parse( InputStream is ) throws Exception {
		return parse( is, DocHelper.DEFAULT, DEFAULT_PARAMS );
	}
		
}