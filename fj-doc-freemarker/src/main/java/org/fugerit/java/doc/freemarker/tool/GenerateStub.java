package org.fugerit.java.doc.freemarker.tool;

import java.io.Writer;
import java.util.Properties;

import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.doc.base.process.DocProcessContext;
import org.fugerit.java.doc.base.process.DocProcessData;
import org.fugerit.java.doc.freemarker.helper.FreeMarkerDocProcess;

public class GenerateStub {

	public static final String CONFIG_STUB_CHAIN_ID = "freemarker-doc-process-config-stub";
	
	public static final String ATT_STUB_PARAMS = "stubParams";

	/**
	 * Generate the docHandlerConfig stub ('1' = enabled, default = '1')
	 */
	public static final String PARAM_STUB_HANDLER = "stub-handler";
	
	/**
	 * Generate the docChain stub ('0' = enabled, default = '1')
	 */
	public static final String PARAM_STUB_CHAIN = "stub-chain";
	
	/**
	 * Enable FOP basic type handler in stub ('1' = enabled, default = '0')
	 */
	public static final String PARAM_ENABLE_FOP_BASE = "enable-fop-base";
	
	/**
	 * Enable FOP basic type handler in stub ('1' = enabled, default = '0')
	 */
	public static final String PARAM_ENABLE_FOP_FULL = "enable-fop-full";
	
	/**
	 * Enable POI type handler in stub ('1' = enabled, default = '0')
	 */
	public static final String PARAM_ENABLE_POI = "enable-poi";
	
	/**
	 * Enable opencsv type handler in stub ('1' = enabled, default = '0')
	 */
	public static final String PARAM_ENABLE_OPENCSV = "enable-opencsv";
	
	/**
	 * Freemarker config id (default = 'FJ_DOC_STUB')
	 */
	public static final String PARAM_CONFIG_ID = "config-id";
	
	/**
	 * Freemarker version (default = '2.3.29')
	 */
	public static final String PARAM_FM_VERSION = "fm-version";
	
	/**
	 * Freemarker template path (default = '/free_marker/')
	 */
	public static final String PARAM_FM_TEMPLATE_PATH = "fm-template-path";
	
	public static void generate( Writer w, Properties params ) throws Exception {
		DocProcessData data = new DocProcessData();
		FreeMarkerDocProcess.getInstance().process( CONFIG_STUB_CHAIN_ID, DocProcessContext.newContext( ATT_STUB_PARAMS, params ), data );
		StreamIO.pipeChar( data.getCurrentXmlReader() , w, StreamIO.MODE_CLOSE_IN_ONLY );
	}
	
}
