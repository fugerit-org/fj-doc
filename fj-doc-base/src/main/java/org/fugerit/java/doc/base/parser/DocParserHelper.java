package org.fugerit.java.doc.base.parser;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.fugerit.java.doc.base.model.DocBackground;
import org.fugerit.java.doc.base.model.DocBookmarkTree;
import org.fugerit.java.doc.base.model.DocCell;
import org.fugerit.java.doc.base.model.DocContainer;
import org.fugerit.java.doc.base.model.DocFooter;
import org.fugerit.java.doc.base.model.DocHeader;
import org.fugerit.java.doc.base.model.DocLi;
import org.fugerit.java.doc.base.model.DocList;
import org.fugerit.java.doc.base.model.DocPara;
import org.fugerit.java.doc.base.model.DocRow;
import org.fugerit.java.doc.base.model.DocTable;

public class DocParserHelper {

	private static final String[] ELEMENT_CONTAINER = { DocTable.TAG_NAME, 
														DocRow.TAG_NAME,
														DocCell.TAG_NAME,
														DocList.TAG_NAME,
														DocLi.TAG_NAME,
														DocContainer.TAG_NAME_PL,
														DocContainer.TAG_NAME_BODY, 
														DocContainer.TAG_NAME_META, 
														DocContainer.TAG_NAME_METADATA, 
														DocHeader.TAG_NAME, 
														DocFooter.TAG_NAME,
														DocPara.TAG_NAME,
														DocPara.TAG_NAME_H,
														DocHeader.TAG_NAME_EXT, 
														DocFooter.TAG_NAME_EXT,
														DocBackground.TAG_NAME,
														DocBookmarkTree.TAG_NAME };
	
	private static final Set<String> CONTAINER_SET = Collections.unmodifiableSet( new HashSet<>( Arrays.asList( ELEMENT_CONTAINER ) ) );
	
	private static final DocParserHelper INSTANCE = new DocParserHelper();
	
	public static DocParserHelper getInstance() {
		return INSTANCE;
	}
	
	public Set<String> getContainerNames() {
		return CONTAINER_SET; 
	}
	
	public boolean isContainerElement( String name ) {
		return this.getContainerNames().contains( name );
	}
	
}
