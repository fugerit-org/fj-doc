package org.fugerit.java.doc.lib.autodoc.facade;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.core.lang.helpers.reflect.MethodHelper;
import org.fugerit.java.doc.lib.autodoc.parser.model.AutodocElement;
import org.fugerit.java.doc.lib.autodoc.parser.model.AutodocModel;
import org.fugerit.java.doc.lib.autodoc.parser.model.AutodocUtils;
import org.fugerit.java.doc.lib.simpletable.SimpleTableFacade;
import org.fugerit.java.doc.lib.simpletable.SimpleTableHelper;
import org.fugerit.java.doc.lib.simpletable.model.SimpleTable;
import org.xmlet.xsdparser.xsdelements.XsdAbstractElement;
import org.xmlet.xsdparser.xsdelements.XsdAttribute;
import org.xmlet.xsdparser.xsdelements.XsdBuiltInDataType;
import org.xmlet.xsdparser.xsdelements.XsdChoice;
import org.xmlet.xsdparser.xsdelements.XsdComplexType;
import org.xmlet.xsdparser.xsdelements.XsdElement;
import org.xmlet.xsdparser.xsdelements.XsdSequence;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AutodocModelToSinpleTableFacade {

	private void handleOccurs( final StringBuilder builder, XsdAbstractElement xsdObject ) {
		try {
			Integer minOccurs = (Integer) MethodHelper.invokeGetter( xsdObject , "minOccurs" );
			String maxOccurs = (String) MethodHelper.invokeGetter( xsdObject , "maxOccurs" );
			if ( minOccurs == null ) {
				minOccurs = 1;
			}
			if ( maxOccurs == null ) {
				maxOccurs = "1";
			}
			builder.append( "[" );
			builder.append( minOccurs );
			builder.append( "-" );
			builder.append( maxOccurs );
			builder.append( "]" );
		} catch (Exception e) {
			throw new ConfigRuntimeException( "Method invocation error "+e, e );
		}
	}
	
	private String handleElement( XsdElement xsdObject ) {
		final StringBuilder builder = new StringBuilder();
		builder.append( xsdObject.getRawName() );
		try {
			Integer minOccurs = (Integer) MethodHelper.invokeGetter( xsdObject , "minOccurs" );
			String maxOccurs = (String) MethodHelper.invokeGetter( xsdObject , "maxOccurs" );
			if ( minOccurs != null || maxOccurs != null ) {
				if ( minOccurs == null ) {
					minOccurs = 1;
				}
				if ( maxOccurs == null ) {
					maxOccurs = "1";
				}
				if ( minOccurs != 1 || !maxOccurs.equals( "1" ) ) {
					builder.append( "[" );
					builder.append( minOccurs );
					builder.append( "-" );
					builder.append( maxOccurs );
					builder.append( "]" );
				}
			}
		} catch (Exception e) {
			throw new ConfigRuntimeException( "Method invocation error "+e, e );
		}
		return builder.toString();
	}
	
	private String handleChoiceStream( Stream<XsdChoice> stream, String separator ) {
		String res = null;
		try {
			List<String> list = stream.map( current -> { 
				StringBuilder temp = new StringBuilder();
				this.handleChoice(temp, current); 
				return temp.toString();
			} ).collect( Collectors.toList() ); 
			res = StringUtils.concat(separator, list );
		} catch (NullPointerException npe) {
			log.warn( "npe "+npe );
		}
		return res;
	}
	
	private String handleSequenceStream( Stream<XsdSequence> stream, String separator ) {
		String res = null;
		try {
			List<String> list = stream.map( current -> { 
				StringBuilder temp = new StringBuilder();
				this.handleSequence(temp, current); 
				return temp.toString();
			} ).collect( Collectors.toList() ); 
			res = StringUtils.concat( separator, list );
		} catch (NullPointerException npe) {
			log.warn( "npe "+npe );
		}
		return res;
	}
	
	private void addIfNotEmpty( String s, List<String> list ) {
		if ( StringUtils.isNotEmpty( s ) ) {
			list.add( s );
		}
	}
	
	private void handleSequence( final StringBuilder builder, XsdSequence xsdObject ) {
		String separator = " , ";
		if ( xsdObject != null ) {
			builder.append( "(" );
			List<String> list = new ArrayList<>();
			this.addIfNotEmpty( StringUtils.concat( separator , xsdObject.getChildrenElements().map( current -> this.handleElement(current) ).collect( Collectors.toList() )  ), list );
			this.addIfNotEmpty( this.handleChoiceStream(xsdObject.getChildrenChoices(), separator) , list );
			this.addIfNotEmpty( this.handleSequenceStream(xsdObject.getChildrenSequences(), separator) , list );
			builder.append( StringUtils.concat( separator , list ) );
			builder.append( ")" );
			this.handleOccurs(builder, xsdObject);
		}
	}
	
	private void handleChoice( final StringBuilder builder, XsdChoice xsdObject ) {
		String separator = " | ";
		if ( xsdObject != null ) {
			builder.append( "(" );
			List<String> list = new ArrayList<>();
			this.addIfNotEmpty( StringUtils.concat( separator , xsdObject.getChildrenElements().map( current -> this.handleElement(current) ).collect( Collectors.toList() )  ), list );
			this.addIfNotEmpty(  this.handleChoiceStream(xsdObject.getChildrenChoices(), separator) , list );
			this.addIfNotEmpty( this.handleSequenceStream(xsdObject.getChildrenSequences(), separator) , list );
			builder.append( StringUtils.concat( separator , list ) );
			builder.append( ")" );
			this.handleOccurs(builder, xsdObject);
		}
	}
	
	private void handleComplexType( final StringBuilder builder, XsdComplexType complexType ) {
		try {
			this.handleSequence(builder, complexType.getChildAsSequence());
		} catch (NullPointerException npe) {
			log.warn( "npe 1 "+npe+" -> "+complexType.getRawName() );
		}
		try {
			this.handleChoice(builder, complexType.getChildAsChoice());
		} catch (NullPointerException npe) {
			log.warn( "npe 2 "+npe+" -> "+complexType.getRawName() );
		}
	}
	
	private String handleAttributeType( XsdAttribute xsdAttribute ) {
		String type = xsdAttribute.getType();
		if ( type.startsWith( "xsd:" ) ) {
			type = type.replace( "xsd:", "" );
		}
		if ( type.startsWith( "doc:" ) ) {
			type = type.replace( "doc:", "" );
		}
		return type;
	}
	
	private void iterateElements( SimpleTableHelper helper, SimpleTable simpleTable, AutodocModel autodocModel ) {
		for ( AutodocElement element : autodocModel.getElements() ) {
			XsdElement xsdElement = element.getXsdElement();
			String name = xsdElement.getRawName();
			String description = AutodocUtils.annotationAsSingleStringHelper( xsdElement.getAnnotation() );
			StringBuilder children = new StringBuilder();
			XsdComplexType complexType = element.getComplexType();
			if ( complexType != null ) {
				this.handleComplexType(children, complexType);			
			}
			XsdBuiltInDataType builtInDataType = xsdElement.getTypeAsBuiltInDataType();
			if ( builtInDataType != null ) {
				children.append( builtInDataType.toString() );
			}
			simpleTable.addRow( helper.newNormalRow( name, description, children.toString() ) );
		}
	}
	
	private void iterateAttributes( SimpleTableHelper helper, SimpleTable simpleTable, AutodocModel autodocModel ) {
		for ( AutodocElement element : autodocModel.getElements() ) {
			XsdElement xsdElement = element.getXsdElement();
			XsdComplexType complexType = element.getComplexType();
			if ( complexType != null ) {
				try {
					List<XsdAttribute> listAtts = complexType.getAllXsdAttributes().collect( Collectors.toList() );
					if ( !listAtts.isEmpty() ) {
						simpleTable.addRow( helper.newHeaderRow( "Attributes for : "+xsdElement.getRawName(), "Description", "Type" ) );
						for ( XsdAttribute xsdAttribute : listAtts ) {
							simpleTable.addRow( helper.newNormalRow( xsdAttribute.getRawName(), AutodocUtils.annotationAsSingleStringHelper( xsdAttribute.getAnnotation() ), this.handleAttributeType(xsdAttribute) ) );
						}
					}
				} catch (NullPointerException npe) {
					log.warn( "Exception on attributes {}", npe.toString() );;
				}
				
			}
		}
	}
	
	public SimpleTable toSimpleTable( AutodocModel autodocModel ) {
		SimpleTableHelper helper = SimpleTableFacade.newHelper().withDefaultBorderWidth( 1 );
		SimpleTable simpleTable = helper.newTable( 20, 40, 40 );
		// elements start
		// header
		simpleTable.addRow( helper.newHeaderRow( "Element", "Description", "Children" ) );
		this.iterateElements(helper, simpleTable, autodocModel);
		// attributes start
		this.iterateAttributes(helper, simpleTable, autodocModel);
		return simpleTable;
	}
	
}
