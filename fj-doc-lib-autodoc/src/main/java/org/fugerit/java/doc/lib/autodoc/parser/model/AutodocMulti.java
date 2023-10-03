package org.fugerit.java.doc.lib.autodoc.parser.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.xmlet.xsdparser.xsdelements.XsdChoice;
import org.xmlet.xsdparser.xsdelements.XsdElement;
import org.xmlet.xsdparser.xsdelements.XsdMultipleElements;
import org.xmlet.xsdparser.xsdelements.XsdSequence;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AutodocMulti<T extends XsdMultipleElements> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5741209190264158477L;

	protected Stream<XsdElement> getElementsStream() {
		return this.content.getChildrenElements();
	}

	protected Stream<XsdChoice> getChoiceStream() {
		return this.content.getXsdElements().filter(XsdChoice.class::isInstance).map(XsdChoice.class::cast);
	}

	protected Stream<XsdSequence> getSequencesStream() {
		return this.content.getXsdElements().filter(XsdSequence.class::isInstance).map(XsdSequence.class::cast);
	}

	protected AutodocMulti(T content) {
		super();
		this.content = content;
	}

	@Getter protected transient T content;
	
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
