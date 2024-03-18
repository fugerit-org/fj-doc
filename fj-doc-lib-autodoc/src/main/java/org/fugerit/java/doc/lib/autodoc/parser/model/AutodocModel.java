package org.fugerit.java.doc.lib.autodoc.parser.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;

import lombok.Getter;
import lombok.Setter;
import org.xmlet.xsdparser.core.XsdParser;
import org.xmlet.xsdparser.xsdelements.XsdElement;

public class AutodocModel implements Serializable {

	public static final String ATT_NAME = "autodocModel";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5016692762844545990L;

	private final LinkedHashMap<String, AutodocElement> elements;

	@Getter
	private transient final XsdParser xsdParser;

	@Getter @Setter
	private String version;

	@Getter @Setter
	private String title;

	@Getter @Setter
	private String xsdPrefix;

	@Getter @Setter
	private String autodocPrefix;
	
	public AutodocModel( XsdParser xsdParser ) {
		this.elements = new LinkedHashMap<>();
		this.xsdParser = xsdParser;
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
