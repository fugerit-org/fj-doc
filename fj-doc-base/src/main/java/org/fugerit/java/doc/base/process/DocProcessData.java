package org.fugerit.java.doc.base.process;

import java.io.Reader;
import java.io.StringReader;

import org.fugerit.java.core.util.filterchain.MiniFilterData;

public class DocProcessData implements MiniFilterData {

	private String currentXmlData;

	public String getCurrentXmlData() {
		return currentXmlData;
	}

	public void setCurrentXmlData(String currentXmlData) {
		this.currentXmlData = currentXmlData;
	}

	public Reader getCurrentXmlReader() {
		return new StringReader( this.getCurrentXmlData() );
	}
	
}
