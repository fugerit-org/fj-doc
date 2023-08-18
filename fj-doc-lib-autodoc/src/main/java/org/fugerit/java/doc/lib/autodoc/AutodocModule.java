package org.fugerit.java.doc.lib.autodoc;

import lombok.Getter;

public class AutodocModule {

	public static final String CURRENT_VERSION = "1.0.0";
	
	@Getter private String version;
	
	public AutodocModule() {		
		this.version = CURRENT_VERSION;
	}
	
	public static final String DEFAULT_HTML_CSS_LINK = "https://venusguides.fugerit.org/src/css/default_venus_docs_style.css";
	
}
