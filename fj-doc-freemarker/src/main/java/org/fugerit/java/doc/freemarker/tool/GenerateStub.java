package org.fugerit.java.doc.freemarker.tool;

import java.io.*;
import java.util.Properties;

import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.doc.base.process.DocProcessContext;
import org.fugerit.java.doc.base.process.DocProcessData;
import org.fugerit.java.doc.freemarker.helper.FreeMarkerDocProcess;
import org.fugerit.java.doc.freemarker.tool.model.ConfigModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GenerateStub {
	
	private GenerateStub() {}
	
	public static final String ATT_CONFIG_MODEL = "configModel";
	
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
	 * Set the attribute registerById for element docHandlerConfig ('true' = enabled, default = 'false')
	 */
	public static final String PARAM_REGISTER_BY_ID = "registerById";

	/**
	 * Set the attribute allowDuplicatedId for element docHandlerConfig ('true' = enabled, default = 'false')
	 */
	public static final String PARAM_ALLOW_DUPLICATED_ID = "allowDuplicatedId";

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
	
	public static final String ARG_INPUT_FILE = "input";
	
	public static void generate( Writer out, Properties params ) throws Exception {
		String input = params.getProperty(ARG_INPUT_FILE);
		if ( StringUtils.isNotEmpty( input ) ) {
			try ( InputStream is = new FileInputStream( new File( input ) ) ) {
				generate(out, params, is);
			}
		} else {
			generate(out, params, null);
		}
	}

	public static void generate( Writer out, Properties params, InputStream is ) throws Exception {
		ConfigModel configModel = null;
		DocProcessData data = new DocProcessData();
		DocProcessContext context = DocProcessContext.newContext( ATT_STUB_PARAMS, params );
		if ( is != null ) {
			log.info( "read legacy config model" );
			configModel = LegacyConfigRead.readConfig(is);
			context = context.withAtt( ATT_CONFIG_MODEL , configModel );
		}
		FreeMarkerDocProcess.getInstance().process( CONFIG_STUB_CHAIN_ID, context, data );
		StreamIO.pipeChar( data.getCurrentXmlReader() , out, StreamIO.MODE_CLOSE_IN_ONLY );
	}
	
}
