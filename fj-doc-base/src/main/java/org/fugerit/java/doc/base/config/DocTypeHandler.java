package org.fugerit.java.doc.base.config;

import java.nio.charset.Charset;

import org.fugerit.java.core.util.collection.KeyString;

public interface DocTypeHandler extends KeyString {

	String getType();
	
	String getModule();
	
	String getMime();
	
	Charset getCharset();
	
	void handle( DocInput docInput, DocOutput docOutput ) throws Exception;
	
}
