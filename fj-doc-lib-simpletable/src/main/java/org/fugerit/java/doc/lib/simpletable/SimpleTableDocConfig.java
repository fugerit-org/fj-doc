package org.fugerit.java.doc.lib.simpletable;

import java.io.OutputStream;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.doc.base.config.DocException;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.facade.DocFacade;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.process.DocProcessConfig;
import org.fugerit.java.doc.base.process.DocProcessContext;
import org.fugerit.java.doc.base.process.DocProcessData;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfig;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfigFacade;
import org.fugerit.java.doc.lib.simpletable.model.SimpleTable;

public class SimpleTableDocConfig {

	private static final String CONFIG_ID = "FJ_DOC_LIB_SIMPLETABLE";
	
	private FreemarkerDocProcessConfig config;
	
	private SimpleTableDocConfig( FreemarkerDocProcessConfig config ) {
		this.config = config;
	}
	
	public static SimpleTableDocConfig newConfig() throws ConfigException {
		FreemarkerDocProcessConfig config = FreemarkerDocProcessConfigFacade.newSimpleConfig( CONFIG_ID, "/fj_doc_lib_simpletable/template/" );
		return new SimpleTableDocConfig(config);
	}

	public static final String CHAIN_ID_SIMPLE_TABLE = "simple_table";
	
	public DocProcessConfig getConfig() {
		return this.config;
	}
	
	private void process( String chainId, DocProcessContext context, DocProcessData data ) throws Exception {
		this.config.process(CONFIG_ID, CHAIN_ID_SIMPLE_TABLE, context, data);
	}
		
	public void processSimpleTable(  SimpleTable simpleTableModel, DocTypeHandler handler, OutputStream os ) throws DocException {
		try {
			DocProcessData data = new DocProcessData();
			DocProcessContext context = DocProcessContext.newContext( SimpleTable.ATT_NAME, simpleTableModel );
			process( CHAIN_ID_SIMPLE_TABLE , context, data );
			DocBase docBase = DocFacade.parse( data.getCurrentXmlReader() );
			DocInput docInput = DocInput.newInput( handler.getType() , docBase );
			DocOutput docOutput = DocOutput.newOutput( os );
			handler.handle( docInput , docOutput );
		} catch (Exception e) {
			throw new DocException( "Simple table generation error : "+e, e );
		}
	}

}
