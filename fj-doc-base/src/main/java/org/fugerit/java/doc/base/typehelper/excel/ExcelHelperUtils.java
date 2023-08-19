package org.fugerit.java.doc.base.typehelper.excel;

import java.io.IOException;
import java.io.InputStream;
import java.util.StringTokenizer;

import org.fugerit.java.core.io.helper.HelperIOException;
import org.fugerit.java.core.io.helper.StreamHelper;
import org.fugerit.java.doc.base.model.DocBase;

public class ExcelHelperUtils {

	private ExcelHelperUtils() {} // java:S1118
	
	public static InputStream resoveTemplateStream( DocBase docBase ) throws IOException {
		String excelTemplate = docBase.getInfo().getProperty( ExcelHelperConsts.PROP_XLS_TEMPLATE );
		InputStream is = null;
		if ( excelTemplate != null ) {
			try {
				is = StreamHelper.resolveStream( excelTemplate );
				if ( is == null ) {
					throw new IOException( "Cannot find template at path : "+excelTemplate );
				}	
			} catch (Exception e) {
				throw HelperIOException.convertExMethod( "resoveTemplateStream" , e);
			}
		}
		return is;			
	}
	
	public static String convertComma( String s ) {
		int index = s.indexOf( ',' );
		if ( index!=-1 ) {
			s = s.substring( 0, index )+"."+s.substring( index+1 );
		}
		return s; 	
	}	
	
	public static String removeDots( String s ) {
		StringBuffer r = new StringBuffer();
		StringTokenizer st = new StringTokenizer( s, "." );
		while (st.hasMoreTokens()) {
			r.append( st.nextToken() );
		}
		return r.toString(); 	
	}
	
	public static String prepareNumber( String s ) {
		s = removeDots( s );
		s = convertComma( s );
		return s;
	}
	
}
