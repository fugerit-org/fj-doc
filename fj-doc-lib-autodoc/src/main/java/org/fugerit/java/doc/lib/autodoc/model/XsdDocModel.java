package org.fugerit.java.doc.lib.autodoc.model;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;

import org.w3c.dom.Element;

public class XsdDocModel {

	private String typePrefix;
	
	public String getTypePrefix() {
		return typePrefix;
	}

	public void setTypePrefix(String typePrefix) {
		this.typePrefix = typePrefix;
	}

	private LinkedHashMap<String, XsdTagModel> elements;
	
	private LinkedHashMap<String, XsdTagModel> simpleTypes;
	
	private LinkedHashMap<String, XsdTagModel> compleTypes;
	
	public XsdDocModel() {
		super();
		this.elements = new LinkedHashMap<>();
		this.simpleTypes = new LinkedHashMap<>();
		this.compleTypes = new LinkedHashMap<>();
	}

	private XsdTagModel finderHelper( LinkedHashMap<String, XsdTagModel> map, String key ) {
		XsdTagModel xsdTagModel = map.get( key );
		if ( xsdTagModel == null ) {
			key = key.replace( this.getTypePrefix() , "" );
			xsdTagModel = map.get( key );
		}
		return xsdTagModel;
	}
	
	public XsdTagModel addElement( Element tag ) {
		XsdTagModel xsdTagModel = new XsdTagModel( this, tag );
		this.elements.put( xsdTagModel.getKey() , xsdTagModel );
		return xsdTagModel;
	}
	
	public Collection<XsdTagModel> getElements() {
		return Collections.unmodifiableCollection( this.elements.values() );
	}
	
	public Collection<String> getElementNames() {
		return this.elements.keySet();
	}

	public XsdTagModel getElement( String name ) {
		return this.finderHelper( this.elements, name );
	}
	
	public XsdTagModel addSimpleType( Element tag ) {
		XsdTagModel xsdTagModel = new XsdTagModel( this, tag );
		this.simpleTypes.put( xsdTagModel.getKey() , xsdTagModel );
		return xsdTagModel;
	}
	
	public Collection<XsdTagModel> getSimpleTypes() {
		return Collections.unmodifiableCollection( this.simpleTypes.values() );
	}
	
	public Collection<String> getSimpleTypeNames() {
		return this.simpleTypes.keySet();
	}
	
	public XsdTagModel getSimpleType( String name ) {
		return this.finderHelper( this.simpleTypes, name );
	}
	
	public XsdTagModel addCompleType( Element tag ) {
		XsdTagModel xsdTagModel = new XsdTagModel( this, tag );
		this.compleTypes.put( xsdTagModel.getKey() , xsdTagModel );
		return xsdTagModel;
	}
	
	public Collection<XsdTagModel> getCompleTypes() {
		return Collections.unmodifiableCollection( this.compleTypes.values() );
	}
	
	public Collection<String> getCompleTypeNames() {
		return this.compleTypes.keySet();
	}
	
	public XsdTagModel getComplexType( String name ) {
		return this.finderHelper( this.compleTypes, name );
	}
	
}
