package org.fugerit.java.doc.lib.autodoc.parser.model;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.xmlet.xsdparser.xsdelements.XsdAnnotation;
import org.xmlet.xsdparser.xsdelements.XsdSimpleType;

import java.io.Serializable;

@Slf4j
public class AutodocSimpleType implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1549582953481172034L;

	@Getter
	private transient XsdSimpleType xsdSimpleType;

	private transient AutodocModel autodocModel;

	public AutodocSimpleType(AutodocModel autodocModel,XsdSimpleType xsdSimpleType) {
		super();
		this.autodocModel = autodocModel;
		this.xsdSimpleType = xsdSimpleType;
	}

	public String getKey() {
		return AutodocUtils.toKey( this.xsdSimpleType );
	}

	public XsdAnnotation getXsdAnnotationDeep() {
		return this.getXsdSimpleType().getAnnotation();
	}

	public String getNote() {
		return AutodocUtils.toNote( this.getXsdSimpleType().getName(), this.autodocModel, this.getXsdSimpleType().getAllRestrictions() );
	}

	public String getNoteNoBase() {
		return AutodocUtils.toNoteNoBase( this.autodocModel, this.getXsdSimpleType().getAllRestrictions() );
	}

	public String getBaseName() {
		return AutodocUtils.getBaseName( this.getXsdSimpleType().getAllRestrictions(), this.autodocModel );
	}

	public boolean isBaseXsd() {
		String baseType = this.getBaseName();
		return "string".equalsIgnoreCase( baseType );
	}

}
