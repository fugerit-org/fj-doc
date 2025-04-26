package org.fugerit.java.doc.base.xml;

import org.fugerit.java.doc.base.enums.EnumDocAlignH;
import org.fugerit.java.doc.base.enums.EnumDocAlignV;
import org.fugerit.java.doc.base.enums.EnumDocStyle;

public class DocStyleAlignHelper {
 
	private DocStyleAlignHelper() {} // java:S1118
	
	public static final String ATTRIBUTE_NAME_ALIGN = "align";
	
	public static final String ATTRIBUTE_NAME_BACK_COLOR = "back-color";
	
	public static final String ATTRIBUTE_NAME_FORE_COLOR = "fore-color";

	public static int parseStyle( String style, int defaultStyle ) {
		return EnumDocStyle.idFromValueWithDefault( style, defaultStyle );
	}
	
	public static int getAlign( String align ) {
		return EnumDocAlignH.idFromValueWithDefault( align, EnumDocAlignH.ALIGN_UNSET.getId() );
	}	
	
	public static int getValign( String align ) {
		return EnumDocAlignV.idFromValueWithDefault( align, EnumDocAlignV.ALIGN_UNSET.getId() );
	}
	
}
