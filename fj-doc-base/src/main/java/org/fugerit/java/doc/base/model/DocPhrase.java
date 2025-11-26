package org.fugerit.java.doc.base.model;

import lombok.Getter;
import lombok.Setter;

public class DocPhrase extends DocElement implements DocStyle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -880687263688988196L;

	public static final String TAG_NAME = "phrase";
	
	public DocPhrase() {
		this.text = "";
	}
	
	@Getter @Setter private int style;		// style with default value
	
	@Getter @Setter private int size;
	
	@Getter @Setter private String text;
	
	@Getter @Setter private String foreColor;
	
	@Getter @Setter private String backColor;	
		
	@Getter @Setter private String fontName;
	
	@Getter @Setter private Float leading;
	
	@Getter @Setter private String link;
	
	@Getter @Setter private String anchor;
	
	@Getter @Setter private String whiteSpaceCollapse;

	@Getter @Setter private String className;
	
	@Getter @Setter private int originalStyle;		// style with unset value

	@Override
	public String toString() {
		return super.toString()+"[text:"+this.getText()+"]";
	}
	
	public String getInternalLink() {
		return this.getLink().substring( 1 );
	}
	
	public boolean isInternal() {
		return this.getLink() != null && this.getLink().startsWith( "#" );
	}
	
}
