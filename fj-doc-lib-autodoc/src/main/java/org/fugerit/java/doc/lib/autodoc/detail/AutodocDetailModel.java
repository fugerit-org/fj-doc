package org.fugerit.java.doc.lib.autodoc.detail;

import java.util.Properties;

import org.fugerit.java.doc.lib.autodoc.detail.model.AutodocDetail;

public class AutodocDetailModel {

	public static final String ATT_NAME = "autodocDetailModel";
	
	private AutodocDetail autodocDetail;

	public AutodocDetail getAutodocDetail() {
		return autodocDetail;
	}

	public AutodocDetailModel(AutodocDetail autodocDetail) {
		super();
		this.autodocDetail = autodocDetail;
	}
	
	public Properties getAdProperties() {
		Properties props = new Properties();
		this.getAutodocDetail().getAdProperty().stream().forEach( 
				p -> props.setProperty( p.getName() , p.getDescription() ) 
		);
		return props;
	}

}
