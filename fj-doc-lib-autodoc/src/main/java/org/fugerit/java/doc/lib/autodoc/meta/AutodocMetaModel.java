package org.fugerit.java.doc.lib.autodoc.meta;

import java.util.Properties;

import org.fugerit.java.doc.lib.autodoc.meta.model.AutodocMeta;

import lombok.Getter;

public class AutodocMetaModel {

	public static final String ATT_NAME = "autodocMetaModel";
	
	@Getter private AutodocMeta autodocMeta;

	@Getter private Properties admProperties;
	
	public AutodocMetaModel(AutodocMeta autodocMeta) {
		super();
		this.autodocMeta = autodocMeta;
		Properties props = new Properties();
		this.getAutodocMeta().getAdmProperty().stream().forEach( 
				p -> props.setProperty( p.getName() , p.getDescription() ) 
		);
		this.admProperties = props;
		
	}
	
}
