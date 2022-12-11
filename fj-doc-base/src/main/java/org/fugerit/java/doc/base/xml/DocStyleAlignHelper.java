package org.fugerit.java.doc.base.xml;

import org.fugerit.java.doc.base.config.DocConstants;
import org.fugerit.java.doc.base.model.DocPara;

public class DocStyleAlignHelper {

	public static int parseStyle( String style, int defaultStype ) {
		int result = defaultStype;
		if ( DocConstants.STYLE_BOLD.equalsIgnoreCase( style ) ) {
			result = DocPara.STYLE_BOLD;
		} else if ( DocConstants.STYLE_UNDERLINE.equalsIgnoreCase( style ) ) {
			result = DocPara.STYLE_UNDERLINE;
		} else if ( DocConstants.STYLE_ITALIC.equalsIgnoreCase( style ) ) {
			result = DocPara.STYLE_ITALIC;
		} else if ( DocConstants.STYLE_BOLDITALIC.equalsIgnoreCase( style ) ) {
			result = DocPara.STYLE_BOLDITALIC;
		} else if ( DocConstants.STYLE_NORMAL.equalsIgnoreCase( style ) ) {
			result = DocPara.STYLE_NORMAL;
		}
		return result;
	}
	
	public static int getAlign( String align ) {
		int result = DocPara.ALIGN_UNSET;
		if ( DocConstants.ALIGN_CENTER.equalsIgnoreCase( align ) ) {
			result = DocPara.ALIGN_CENTER;
		} else if ( DocConstants.ALIGN_RIGHT.equalsIgnoreCase( align ) ) {
			result = DocPara.ALIGN_RIGHT;
		} else if ( DocConstants.ALIGN_LEFT.equalsIgnoreCase( align ) ) {
			result = DocPara.ALIGN_LEFT;
		} else if ( DocConstants.ALIGN_JUSTIFY.equalsIgnoreCase( align ) ) {
			result = DocPara.ALIGN_JUSTIFY;
		} else if ( DocConstants.ALIGN_JUSTIFYALL.equalsIgnoreCase( align ) ) {
			result = DocPara.ALIGN_JUSTIFY_ALL;
		}
		return result;
	}	
	
	public static int getValign( String align ) {
		int result = DocPara.ALIGN_UNSET;
		if ( DocConstants.VALIGN_MIDDLE.equalsIgnoreCase( align ) ) {
			result = DocPara.ALIGN_MIDDLE;
		} else if ( DocConstants.VALIGN_TOP.equalsIgnoreCase( align ) ) {
			result = DocPara.ALIGN_TOP;
		} else if ( DocConstants.VALIGN_BOTTOM.equalsIgnoreCase( align ) ) {
			result = DocPara.ALIGN_BOTTOM;
		} 
		return result;
	}
	
}
