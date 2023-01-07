package org.fugerit.java.doc.lib.autodoc.parser.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmlet.xsdparser.xsdelements.XsdChoice;
import org.xmlet.xsdparser.xsdelements.XsdElement;
import org.xmlet.xsdparser.xsdelements.XsdSequence;

public abstract class AutodocMulti implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5741209190264158477L;

	private final static Logger logger = LoggerFactory.getLogger( AutodocMulti.class );
	
	protected abstract Stream<XsdElement> getElementsStream();
	
	protected abstract Stream<XsdChoice> getChoiceStream();
	
	protected abstract Stream<XsdSequence> getSequencesStream();
	
	public Collection<XsdElement> getXsdElements() {
		List<XsdElement> result = null;
		try {
			result = this.getElementsStream().collect( Collectors.toList() );
		} catch (NullPointerException npe) {
			logger.warn( "Null pointer exception getting elements "+npe );
		}
		return result;
	}
	
	public Collection<AutodocChoice> getAutodocChoices() {
		List<AutodocChoice> result = null;
		try {
			result = this.getChoiceStream().map( current -> { return new AutodocChoice( current ); } ).collect( Collectors.toList() );
		} catch (NullPointerException npe) {
			logger.warn( "Null pointer exception getting choices "+npe );
		}
		return result;
	}
	
	public Collection<AutodocSequence> getAutodocSequence() {
		List<AutodocSequence> result = null;
		try {
			result = this.getSequencesStream().map( current -> { return new AutodocSequence( current ); } ).collect( Collectors.toList() );
		} catch (NullPointerException npe) {
			logger.warn( "Null pointer exception getting sequences "+npe );
		}
		return result;
	}

}
