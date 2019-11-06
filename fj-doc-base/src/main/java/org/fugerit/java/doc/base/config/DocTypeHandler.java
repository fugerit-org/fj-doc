package org.fugerit.java.doc.base.config;

import org.fugerit.java.core.util.collection.KeyString;

public interface DocTypeHandler extends KeyString {

	String getType();
	
	String getMime();
	
	void handle( DocInput docInput, DocOutput docOutput ) throws Exception;
	
}
