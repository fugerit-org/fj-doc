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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.util.Iterator;
import java.util.Properties;

import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.io.helper.StreamHelper;
import org.fugerit.java.core.lang.helpers.BooleanUtils;
import org.fugerit.java.core.log.LogFacade;
import org.fugerit.java.core.util.ObjectUtils;
import org.fugerit.java.doc.base.config.DocException;
import org.fugerit.java.doc.base.config.DocVersion;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.model.DocContainer;
import org.fugerit.java.doc.base.model.DocElement;
import org.fugerit.java.doc.base.model.DocHelper;
import org.fugerit.java.doc.base.parser.DocParser;
import org.fugerit.java.doc.base.parser.DocValidationResult;
import org.fugerit.java.doc.base.xml.DocXmlParser;

import lombok.extern.slf4j.Slf4j;

/**
 * Better to avoid this implementations, which is left only for compatibility.
 * 
 * Should be used instande {@link DocFacadeSource} or a {@link DocParser} instance.
 *
 * @author mfranci
 *
 */
@Slf4j
public class DocFacade {
	
	private DocFacade() {} // java:S1118
	
	public static final String CURRENT_VERSION = DocVersion.CURRENT_VERSION.stringVersion();
	
	/**
	 * 
	 */
	public static final String PARAM_KEY_CLOSE_STREAM = "close-stream";
	
	/**
	 * 
	 */
	public static final String PARAM_VALUE_CLOSE_STREAM_DEFAULT = BooleanUtils.BOOLEAN_1;
	
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
	
	
	public static final String PARAM_DEFINITION_MODE = "definition-mode";
	public static final String PARAM_DEFINITION_MODE_XSD = "xsd";
	public static final String PARAM_DEFINITION_MODE_DTD = "dtd";
	public static final String PARAM_DEFINITION_MODE_DEFAULT = PARAM_DEFINITION_MODE_XSD;
	
	public static final String SYSTEM_ID = "http://javacoredoc.fugerit.org";
	
	public static boolean validate( Reader is) throws DocException {
		return validate( is, DEFAULT_PARAMS );
	}
	
	public static boolean validate( Reader is, Properties params ) throws DocException {
		boolean valRes = false;
		try {
			params = ObjectUtils.objectWithDefault( params , DEFAULT_PARAMS );
			DocXmlParser parser = new DocXmlParser( DocHelper.DEFAULT );
			int result = parser.validate( is );
			valRes = ( result == DocValidationResult.VALIDATION_OK );
		} catch (Exception e) {
			throw DocException.convertExMethod( "validate", e );
		} finally {
			if ( BooleanUtils.isTrue( params.getProperty( PARAM_KEY_CLOSE_STREAM, PARAM_VALUE_CLOSE_STREAM_DEFAULT ) ) ) {
				StreamHelper.closeSafe( is );	
			}
		}
		return valRes;
	}
	
	public static DocBase parse( Reader is, DocHelper docHelper ) throws DocException {
		return parse( is, docHelper, DEFAULT_PARAMS );
	}
	
	public static DocBase parse( Reader is, DocHelper docHelper, Properties params ) throws DocException {
		DocBase docBase = null;
		try {
			LogFacade.getLog().warn( "parse() method with DocHelper parameter should be avoided , as currently supported, param value : {}", docHelper );
			params = ObjectUtils.objectWithDefault( params , DEFAULT_PARAMS );
			DocXmlParser parser = new DocXmlParser( DocHelper.DEFAULT );
			docBase = parser.parse(is);
		} catch (Exception e) {
			throw DocException.convertExMethod( "parse", e );
		} finally {
			if ( BooleanUtils.isTrue( params.getProperty( PARAM_KEY_CLOSE_STREAM, PARAM_VALUE_CLOSE_STREAM_DEFAULT ) ) ) {
				StreamHelper.closeSafe( is );	
			}
		}
		return docBase;
	}	
	
	public static DocBase parse( InputStream is, DocHelper docHelper, Properties params ) throws DocException {
		return parse( new InputStreamReader( is ), docHelper, params );
	}	
	
	public static DocBase parse( Reader is ) throws DocException {
		return parse( is, DocHelper.DEFAULT, DEFAULT_PARAMS );
	}
	
	public static DocBase parseRE( Reader is, int sourceType ) {
		DocBase doc = null;
		try {
			log.debug( "sourceType : {}", sourceType );
			doc = parse( is, DocHelper.DEFAULT, DEFAULT_PARAMS );
		} catch (Exception e) {
			throw new ConfigRuntimeException( "Exception on parseRE : "+e, e );
		}
		return doc;
	}
	
	
	public static DocBase parseRE( Reader is ) {
		DocBase doc = null;
		try {
			doc = parse( is, DocHelper.DEFAULT, DEFAULT_PARAMS );
		} catch (Exception e) {
			throw new ConfigRuntimeException( "Exception on parseRE : "+e, e );
		}
		return doc;
	}
	
	public static DocBase parse( InputStream is ) throws DocException {
		return parse( is, DocHelper.DEFAULT, DEFAULT_PARAMS );
	}
		
}