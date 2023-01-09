package org.fugerit.java.doc.base.parser;

import java.io.InputStream;
import java.io.Reader;

import org.fugerit.java.doc.base.config.DocException;
import org.fugerit.java.doc.base.facade.DocFacadeSource;
import org.fugerit.java.doc.base.model.DocBase;

/**
 * Class implementing the parsing and validation of doc source
 * 
 * @author fugerit
 *
 */
public interface DocParser {
	
	/**
	 * Returns the source type handled by this parser
	 * 
	 * @return	the <code>int</code> representation  of the source type handled by this parser
	 * {@link DocFacadeSource}
	 */
	int getSourceType();
	
	/**
	 * Parse a source
	 * 
	 * @param is	the source stream
	 * @return		the parsed document
	 * @throws DocException		in case of problems
	 */
	DocBase parse( InputStream is ) throws DocException;
	
	/**
	 * Parse a source
	 * 
	 * @param reader	the source reader
	 * @return		the parsed document
	 * @throws DocException		in case of problems
	 */
	DocBase parse( Reader reader ) throws DocException;
	
	/**
	 * Validate a source
	 * 
	 * @param reader	the source reader
	 * @return		the result of the validation
	 * @throws DocException		in case of problems
	 */
	DocValidationResult validateResult( Reader reader ) throws DocException;
	
	/**
	 * Validate a source
	 * 
	 * @param reader	the source reader
	 * @return		the result of the validation (<code>0</code> in case of success)
	 * @throws DocException		in case of problems
	 */
	int validate( Reader reader ) throws DocException;
	
	/**
	 * Validate a source, it tries to find the xsd version and validate against the specific version
	 * 
	 * @param reader	the source reader
	 * @return		the result of the validation
	 * @throws DocException		in case of problems
	 */
	DocValidationResult validateVersionResult( Reader reader ) throws DocException;
	
	/**
	 * Validate a source, it tries to find the xsd version and validate against the specific version
	 * 
	 * @param reader	the source reader
	 * @return		the result of the validation as an <code>int</code> (<code>0</code> in case of success)
	 * @throws DocException		in case of problems
	 */
	int validateVersion( Reader reader ) throws DocException;
	
}
