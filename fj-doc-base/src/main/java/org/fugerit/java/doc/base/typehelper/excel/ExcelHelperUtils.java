package org.fugerit.java.doc.base.typehelper.excel;

import java.io.InputStream;
import java.util.StringTokenizer;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.io.helper.StreamHelper;
import org.fugerit.java.doc.base.model.DocBase;

public class ExcelHelperUtils {

	public static InputStream resoveTemplateStream( DocBase docBase ) throws Exception {
		String excelTemplate = docBase.getInfo().getProperty( ExcelHelperConsts.PROP_XLS_TEMPLATE );
		InputStream is = null;
		if ( excelTemplate != null ) {
			is = StreamHelper.resolveStream( excelTemplate );
			if ( is == null ) {
				throw new ConfigException( "Cannot find template at path : "+excelTemplate );
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
