package org.fugerit.java.doc.sample.facade;

import org.fugerit.java.core.cfg.xml.BasicIdConfigType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class DocCatalogEntry extends BasicIdConfigType {

	private static final long serialVersionUID = -3140948907871403724L;
	
	@Getter @Setter private String type;
	
	@Getter @Setter private String path;
	
	@Getter @Setter private String jsonDataPath;
	
	@Getter @Setter private String description;

}
