package org.fugerit.java.doc.mod.poi;

import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.config.DocTypeHandlerDefault;

public class XlsxTypeHandler extends DocTypeHandlerDefault {
	
	public XlsxTypeHandler() {
		super( DocConfig.TYPE_XLSX );
	}

	public static DocTypeHandler HANDLER = new XlsxTypeHandler();

	@Override
	public void handle(DocInput docInput, DocOutput docOutput) throws Exception {
		//TODO: implementation
	}
	
}