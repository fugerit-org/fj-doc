package org.fugerit.java.doc.mod.fop;

import java.io.Serializable;

import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfig;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfigFacade;

public class FreeMarkerFopProcess implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1277168639030295400L;

	public static final String CONFIG_PATH = "cl://fj_doc_mod_fop_config/fm-fop-process-config.xml";
	
	private static FreemarkerDocProcessConfig INSTANCE = FreemarkerDocProcessConfigFacade.loadConfigSafe( CONFIG_PATH );

	public static FreemarkerDocProcessConfig getInstance() {
		return INSTANCE;
	}
	
}
