package org.fugerit.java.doc.freemarker.html;

import java.io.Serializable;

import org.fugerit.java.doc.base.process.DocProcessConfig;

public class FreeMarkerHtmlProcess implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1277168639030295400L;

	public static final String CONFIG_PATH = "cl://fm_html/html-process-config.xml";
	
	private static DocProcessConfig INSTANCE = DocProcessConfig.loadConfigSafe( CONFIG_PATH );

	public static DocProcessConfig getInstance() {
		return INSTANCE;
	}
	
}
