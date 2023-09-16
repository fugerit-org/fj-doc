package org.fugerit.java.doc.lib.autodoc.parser.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.xmlet.xsdparser.xsdelements.XsdChoice;
import org.xmlet.xsdparser.xsdelements.XsdElement;
import org.xmlet.xsdparser.xsdelements.XsdSequence;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AutodocMulti implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5741209190264158477L;

	protected abstract Stream<XsdElement> getElementsStream();
	
	protected abstract Stream<XsdChoice> getChoiceStream();
	
	protected abstract Stream<XsdSequence> getSequencesStream();
	
	public Collection<XsdElement> getXsdElements() {
		List<XsdElement> result = null;
		try {
			result = this.getElementsStream().collect( Collectors.toList() );
		} catch (NullPointerException npe) {
			log.warn( "Null pointer exception getting elements {}", npe.toString() );
		}
		return result;
	}
	
	public Collection<AutodocChoice> getAutodocChoices() {
		List<AutodocChoice> result = null;
		try {
			result = this.getChoiceStream().map( AutodocChoice::new ).collect( Collectors.toList() );
		} catch (NullPointerException npe) {
			log.warn( "Null pointer exception getting choices {}", npe.toString() );
		}
		return result;
	}
	
	public Collection<AutodocSequence> getAutodocSequence() {
		List<AutodocSequence> result = null;
		try {
			result = this.getSequencesStream().map( AutodocSequence::new ).collect( Collectors.toList() );
		} catch (NullPointerException npe) {
			log.warn( "Null pointer exception getting sequences {}", npe.toString() );
		}
		return result;
	}

}
