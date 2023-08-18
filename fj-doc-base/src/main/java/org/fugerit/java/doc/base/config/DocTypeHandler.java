package org.fugerit.java.doc.base.config;

import java.io.Serializable;
import java.nio.charset.Charset;

import org.fugerit.java.core.util.collection.KeyString;

public interface DocTypeHandler extends KeyString, Serializable {

	String getType();
	
	String getModule();
	
	String getMime();
	
	String getFormat();
	
	Charset getCharset();
	
	void handle( DocInput docInput, DocOutput docOutput ) throws Exception;
	
}
