package org.fugerit.java.doc.lib.autodoc.parser.model;

import java.util.stream.Stream;

import org.xmlet.xsdparser.xsdelements.XsdChoice;
import org.xmlet.xsdparser.xsdelements.XsdElement;
import org.xmlet.xsdparser.xsdelements.XsdSequence;

public class AutodocSequence extends AutodocMulti {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3570826182659230472L;

	public XsdSequence getContent() {
		return content;
	}

	private transient XsdSequence content;

	public AutodocSequence(XsdSequence content) {
		super();
		this.content = content;
	}
	
	@Override
	protected Stream<XsdElement> getElementsStream() {
		return this.getContent().getChildrenElements();
	}


	@Override
	protected Stream<XsdChoice> getChoiceStream() {
		return this.getContent().getChildrenChoices();
	}

	@Override
	protected Stream<XsdSequence> getSequencesStream() {
		return this.getContent().getChildrenSequences();
	}

}
