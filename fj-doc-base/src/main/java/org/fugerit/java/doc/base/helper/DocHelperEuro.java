package org.fugerit.java.doc.base.helper;

import org.fugerit.java.doc.base.config.DocConstants;
import org.fugerit.java.doc.base.model.DocHelper;

public class DocHelperEuro extends DocHelper {

	@Override
	public String filterText(String temp) {
		StringBuilder text = new StringBuilder();
		for ( int k=0; k<temp.length(); k++ ) {
			char c = temp.charAt( k );
			int i = (int)c;
			if ( i == 164 ) {
				text.append( DocConstants.getInstance().getEuro() );
			} else {
				text.append( c );
			}
		}
		return text.toString();
	}

	
	
}