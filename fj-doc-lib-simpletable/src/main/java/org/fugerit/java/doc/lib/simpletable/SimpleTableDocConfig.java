package org.fugerit.java.doc.lib.simpletable;

import java.io.InputStream;
import java.io.OutputStream;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.core.util.filterchain.MiniFilterChain;
import org.fugerit.java.doc.base.config.DocException;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.facade.DocFacade;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.process.DocProcessConfig;
import org.fugerit.java.doc.base.process.DocProcessContext;
import org.fugerit.java.doc.base.process.DocProcessData;
import org.fugerit.java.doc.lib.simpletable.model.SimpleTable;

public class SimpleTableDocConfig {

	private final static String CONFIG_PATH = "fj_doc_lib_simpletable/doc-process-config.xml";
	
	private DocProcessConfig config;
	
	private SimpleTableDocConfig( DocProcessConfig config ) {
		this.config = config;
	}
	
	public static SimpleTableDocConfig newConfig() throws ConfigException {
		SimpleTableDocConfig simpleTableDocConfig = null;
		try ( InputStream is = ClassHelper.loadFromDefaultClassLoader(CONFIG_PATH) )  {
			DocProcessConfig config = DocProcessConfig.loadConfig( is );
			simpleTableDocConfig = new SimpleTableDocConfig( config );
		} catch (Exception e) {
			throw new ConfigException( "Error creating configuration : "+e, e );
		}
		return simpleTableDocConfig;
	}

	public static final String CHAIN_ID_SIMPLE_TABLE = "simple_table";
	
	public DocProcessConfig getConfig() {
		return config;
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
			DocBase docBase = DocFacade.parse( data.getCurrentXmlReader() );
			DocInput docInput = DocInput.newInput( handler.getType() , docBase );
			DocOutput docOutput = DocOutput.newOutput( os );
			handler.handle( docInput , docOutput );
		} catch (Exception e) {
			throw new DocException( "Simple table generation error : "+e, e );
		}
	}
	
}
