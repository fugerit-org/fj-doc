package org.fugerit.java.doc.sample.facade;

import org.fugerit.java.core.cfg.xml.BasicIdConfigType;

public class DocCatalogEntry extends BasicIdConfigType {

	private static final long serialVersionUID = -3140948907871403724L;
	
	public String path;
	
	public String description;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return super.toString()+"[path=" + path + ", description=" + description + "]";
	}

}
