package org.fugerit.java.doc.freemarker.helper;

import java.io.Serializable;

import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfig;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfigFacade;

public class FreeMarkerDocProcess implements Serializable {

	private FreeMarkerDocProcess() {}
	
	/**
	 * Default configuration path
	 */
	public static final String CONFIG_PATH = "cl://fj_doc_freemarker_config/fm-freemarker-doc-process-config.xml";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1277168639030295400L;

	/*
	 * Default implemnentation
	 */
	private static final FreemarkerDocProcessConfig INSTANCE = newInstance( CONFIG_PATH );

	public static FreemarkerDocProcessConfig newInstance( String configPath ) {
		return FreemarkerDocProcessConfigFacade.loadConfigSafe( configPath );
	}
	
	public static FreemarkerDocProcessConfig getInstance() {
		return INSTANCE;
	}
	
}
