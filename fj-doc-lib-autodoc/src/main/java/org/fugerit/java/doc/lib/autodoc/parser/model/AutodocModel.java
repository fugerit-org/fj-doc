package org.fugerit.java.doc.lib.autodoc.parser.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.xmlet.xsdparser.core.XsdParser;
import org.xmlet.xsdparser.xsdelements.XsdComplexType;
import org.xmlet.xsdparser.xsdelements.XsdElement;
import org.xmlet.xsdparser.xsdelements.XsdSimpleType;

@Slf4j
public class AutodocModel implements Serializable {

	public static final String ATT_NAME = "autodocModel";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5016692762844545990L;

	private final LinkedHashMap<String, AutodocElement> elements;

	private final LinkedHashMap<String, AutodocType> types;

	private final LinkedHashMap<String, AutodocSimpleType> simpleTypes;

	@Getter
	private final transient XsdParser xsdParser;

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
		this.types = new LinkedHashMap<>();
		this.simpleTypes = new LinkedHashMap<>();
		this.xsdParser = xsdParser;
	}

	public AutodocElement addElement( XsdElement xsdElement ) {
		AutodocElement element = new AutodocElement( this, xsdElement );
		this.elements.put( element.getKey(), element );
		return element;
	}

	public AutodocType addType( XsdComplexType xsdComplexType ) {
		AutodocType type = new AutodocType(  xsdComplexType );
		String key = AutodocUtils.toKey( xsdComplexType );
		log.info( "xsdComplexType key : {}", key );
		this.types.put( key, type );
		return type;
	}

	public AutodocSimpleType addSimpleType( XsdSimpleType xsdSimpleType ) {
		AutodocSimpleType type = new AutodocSimpleType(  xsdSimpleType );
		String key = AutodocUtils.toKey( xsdSimpleType );
		log.info( "xsdSimpleType key : {}", key );
		this.simpleTypes.put( key, type );
		return type;
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

	public Collection<AutodocType> getTypes() {
		return Collections.unmodifiableCollection( this.types.values() );
	}

	public Collection<AutodocSimpleType> getSimpleTypes() {
		return Collections.unmodifiableCollection( this.simpleTypes.values() );
	}

}
