package org.fugerit.java.doc.freemarker.process;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.fugerit.java.core.cfg.xml.IdConfigType;
import org.fugerit.java.core.util.collection.KeyString;

import lombok.Data;

@Data
public class DocChainModel implements IdConfigType, KeyString, Serializable {

	public static final String MAP_ATTS_ALL = "all";
	
	public static final String MAP_ATTS_ENUM = "enum";
	
	public static final String MAP_ATTS_DEFAULT = MAP_ATTS_ALL;
	
	public static final String DEFAULT_TEMPLATE_PATH = "${chainId}.ftl";
	
	private static final long serialVersionUID = 9076457107043072322L;

	private String id;
	
	private String templatePath = DEFAULT_TEMPLATE_PATH;
	
	private String mapAtts = MAP_ATTS_DEFAULT;
	
	private String parent;
	
	private Properties mapAttsEnum;

	private List<ChainStepModel> chainStepList = new ArrayList<>();
	
	@Override
	public String getKey() {
		return this.getId();
	} 
	
}
