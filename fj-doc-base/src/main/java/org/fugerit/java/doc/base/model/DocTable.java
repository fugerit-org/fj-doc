package org.fugerit.java.doc.base.model;

import org.fugerit.java.doc.base.model.util.DocTableUtil;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 *
 * @author mfranci
 *
 */
public class DocTable extends DocContainer implements DocStyle {

	public static final String RENDER_MODE_NORMAL = "normal";
	public static final String RENDER_MODE_INLINE = "inline";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4708466907819886346L;

	public DocTableUtil getUtil() {
		return new DocTableUtil( this );
	}
	
	public static final String TAG_NAME = "table";
	
	public static final String ATTRIBUTE_NAME_ALT = "alt";
	
	@Setter @Getter int padding;
	
	@Setter @Getter private int spacing;
	
	@Setter @Getter private String renderMode;
	
	@Setter @Getter private Float spaceBefore;
	
	@Setter @Getter private Float spaceAfter;
	
	@Setter @Getter private int[] colWithds;
	
	@Setter @Getter private int width;
	
	@Setter @Getter private int columns;
	
	@Setter @Getter private String foreColor;
	
	@Setter @Getter private String backColor;	
	
	@Setter @Getter private String alt;
	
}
