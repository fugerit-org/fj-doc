package org.fugerit.java.doc.freemarker.process;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.fugerit.java.core.cfg.xml.IdConfigType;
import org.fugerit.java.core.util.collection.KeyString;

import lombok.Getter;
import lombok.Setter;

public class DocChainModel implements IdConfigType, KeyString, Serializable {

	public static final String MAP_ATTS_ALL = "all";
	
	public static final String MAP_ATTS_ENUM = "enum";
	
	public static final String MAP_ATTS_DEFAULT = MAP_ATTS_ALL;
	
	public static final String DEFAULT_TEMPLATE_PATH = "${chainId}.ftl";
	
	private static final long serialVersionUID = 9076457107043072322L;

	@Getter @Setter private String id;
	
	@Getter @Setter private String templatePath = DEFAULT_TEMPLATE_PATH;
	
	@Getter @Setter private String mapAtts = MAP_ATTS_DEFAULT;
	
	@Getter @Setter private String parent;

	@Getter @Setter private String sourceType;

	@Getter @Setter private Properties mapAttsEnum;

	@Getter  private List<ChainStepModel> chainStepList = new ArrayList<>();
	
	@Override
	public String getKey() {
		return this.getId();
	} 
	
}
