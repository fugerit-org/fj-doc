package org.fugerit.java.doc.lib.simpletable;

import java.io.OutputStream;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.util.filterchain.MiniFilterChain;
import org.fugerit.java.core.util.filterchain.MiniFilterMap;
import org.fugerit.java.doc.base.config.DocException;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.process.DocProcessContext;
import org.fugerit.java.doc.base.process.DocProcessData;
import org.fugerit.java.doc.freemarker.config.FreeMarkerConfigStep;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfig;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfigFacade;
import org.fugerit.java.doc.lib.simpletable.model.SimpleTable;

public class SimpleTableDocConfig {

	private static final String CONFIG_ID = "FJ_DOC_LIB_SIMPLETABLE";
	
	private FreemarkerDocProcessConfig config;
	
	private SimpleTableDocConfig( FreemarkerDocProcessConfig config ) {
		this.config = config;
	}
	
	public static SimpleTableDocConfig newConfigLatest() throws ConfigException {
		return newConfig( null );
	}
	
	public static SimpleTableDocConfig newConfig() throws ConfigException {
		return newConfig( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_VERSION_2_3_29 );
	}

	public static SimpleTableDocConfig newConfig( String freemarkerVersion ) throws ConfigException {
		FreemarkerDocProcessConfig config = FreemarkerDocProcessConfigFacade.newSimpleConfig( CONFIG_ID, "/fj_doc_lib_simpletable/template/", freemarkerVersion );
		return new SimpleTableDocConfig(config);
	}
	
	public static final String CHAIN_ID_SIMPLE_TABLE = "simple_table";
	
	public MiniFilterMap getConfig() {
		return this.config;
	}
	
	private void process( String chainId, DocProcessContext context, DocProcessData data ) throws Exception {
		MiniFilterChain chain = this.config.getChainCache( chainId );
		chain.apply( context , data );
	}
		
	public void processSimpleTable(  SimpleTable simpleTableModel, DocTypeHandler handler, OutputStream os ) throws DocException {
		try {
			DocProcessData data = new DocProcessData();
			DocProcessContext context = DocProcessContext.newContext( SimpleTable.ATT_NAME, simpleTableModel );
			process( CHAIN_ID_SIMPLE_TABLE , context, data );
			DocInput docInput = DocInput.newInput( handler.getType() , data.getCurrentXmlReader() );
			DocOutput docOutput = DocOutput.newOutput( os );
			handler.handle( docInput , docOutput );
		} catch (Exception e) {
			throw new DocException( "Simple table generation error : "+e, e );
		}
	}

}
