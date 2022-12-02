package org.fugerit.java.doc.freemarker.helper;

import java.io.Serializable;

import org.fugerit.java.doc.base.process.DocProcessConfig;

public class FreeMarkerDocProcess implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1277168639030295400L;

	public static final String CONFIG_PATH = "cl://fj_doc_freemarker_config/fm-process-config.xml";
	
	private static DocProcessConfig INSTANCE = DocProcessConfig.loadConfigSafe( CONFIG_PATH );

	public static DocProcessConfig getInstance() {
		return INSTANCE;
	}
	
}
