package org.fugerit.java.doc.lib.autodoc.parser.model;

import java.util.stream.Stream;

import org.xmlet.xsdparser.xsdelements.XsdChoice;
import org.xmlet.xsdparser.xsdelements.XsdElement;
import org.xmlet.xsdparser.xsdelements.XsdSequence;

public class AutodocChoice extends AutodocMulti {


	/**
	 * 
	 */
	private static final long serialVersionUID = 6551113305251367239L;
		
	public XsdChoice getContent() {
		return content;
	}

	private XsdChoice content;

	public AutodocChoice(XsdChoice content) {
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
