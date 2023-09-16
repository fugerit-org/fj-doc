package org.fugerit.java.doc.lib.autodoc.parser.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;

import org.xmlet.xsdparser.core.XsdParser;
import org.xmlet.xsdparser.xsdelements.XsdElement;

public class AutodocModel implements Serializable {

	public static final String ATT_NAME = "autodocModel";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5016692762844545990L;

	private LinkedHashMap<String, AutodocElement> elements;

	private transient XsdParser xsdParser;
	
	public AutodocModel( XsdParser xsdParser ) {
		this.elements = new LinkedHashMap<>();
		this.xsdParser = xsdParser;
	}
	
	private String version;
	
	private String title;
	
	private String xsdPrefix;
	
	private String autodocPrefix;
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getXsdPrefix() {
		return xsdPrefix;
	}

	public void setXsdPrefix(String xsdPrefix) {
		this.xsdPrefix = xsdPrefix;
	}

	public String getAutodocPrefix() {
		return autodocPrefix;
	}

	public void setAutodocPrefix(String autodocPrefix) {
		this.autodocPrefix = autodocPrefix;
	}

	public XsdParser getXsdParser() {
		return xsdParser;
	}

	public AutodocElement addElement( XsdElement xsdElement ) {
		AutodocElement element = new AutodocElement( this, xsdElement );
		this.elements.put( element.getKey(), element );
		return element;
	}
	
	public Collection<String> getElementNames() {
		return this.elements.keySet();
	}
	
	public Collection<AutodocElement> getElements() {
		return Collections.unmodifiableCollection( this.elements.values() );
	}
	
	public AutodocElement getElement( String name ) {
		return this.elements.get( name );
	}
	
	public boolean containsName( String name ) {
		return this.elements.containsKey( name );
	}
	
}
