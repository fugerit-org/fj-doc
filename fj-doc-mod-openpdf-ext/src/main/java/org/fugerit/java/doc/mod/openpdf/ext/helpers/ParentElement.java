package org.fugerit.java.doc.mod.openpdf.ext.helpers;

import org.fugerit.java.doc.base.config.DocException;

import com.lowagie.text.Element;

public interface ParentElement {
	
	public void add( Element element ) throws DocException;
	
}
