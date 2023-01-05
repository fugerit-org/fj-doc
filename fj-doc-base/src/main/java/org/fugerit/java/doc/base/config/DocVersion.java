package org.fugerit.java.doc.base.config;

import java.io.Serializable;

public final class DocVersion implements Serializable, Comparable<DocVersion> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8717045616792106442L;

	public static final DocVersion VERSION_1_0 = DocVersion.newVersion( "1-0" );
	public static final DocVersion VERSION_1_1 = DocVersion.newVersion( "1-1" );
	public static final DocVersion VERSION_1_2 = DocVersion.newVersion( "1-2" );
	public static final DocVersion VERSION_1_3 = DocVersion.newVersion( "1-3" );
	public static final DocVersion VERSION_1_4 = DocVersion.newVersion( "1-4" );
	public static final DocVersion VERSION_1_5 = DocVersion.newVersion( "1-5" );
	public static final DocVersion VERSION_1_6 = DocVersion.newVersion( "1-6" );
	public static final DocVersion VERSION_1_7 = DocVersion.newVersion( "1-7" );
	public static final DocVersion VERSION_1_8 = DocVersion.newVersion( "1-8" );
	public static final DocVersion VERSION_1_9 = DocVersion.newVersion( "1-9" );
	public static final DocVersion VERSION_1_10 = DocVersion.newVersion( "1-10" );
	public static final DocVersion VERSION_2_0 = DocVersion.newVersion( "2-0" );
	
	public static final DocVersion CURRENT_VERSION = VERSION_2_0;
	
	public static final String VERSION_SEPARATOR ="-";
	
	private int major;
	
	private int minor;
	
	private DocVersion( String version ) {
		String[] split = version.split( VERSION_SEPARATOR );
		this.major = Integer.parseInt( split[0] );
		this.minor = Integer.parseInt( split[1] );
	}

	public int getMajor() {
		return major;
	}

	public int getMinor() {
		return minor;
	}

	public String stringVersion() {
		return this.getMajor()+VERSION_SEPARATOR+this.getMinor();
	}
	
	@Override
	public String toString() {
		return this.stringVersion();
	}
	
	@Override
	public int compareTo(DocVersion o) {
		return compare( this , o );
	}
	
	public static int compare( String v1, String v2 ) {
		return compare( newVersion( v1 ) , newVersion( v2 ) );
	}
	
	public static int compare( DocVersion v1, DocVersion v2 ) {
		int res = v1.getMajor() - v2.getMajor();
		if ( res == 0 ) {
			res = v1.getMinor() - v2.getMinor();
		}
		return res;
	}
	
	public static DocVersion newVersion( String version ) {
		return new DocVersion(version);
	}
	
}
