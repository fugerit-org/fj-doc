package org.fugerit.java.doc.base.typehelper.generic;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.util.format.TimeFormatDefault;

public class FormatTypeConsts {

	private FormatTypeConsts() {}

	public static final String TYPE_STRING = "string";

	public static final String TYPE_NUMBER = "number";

	public static final String TYPE_DATE = "date";
	public static final String FORMAT_DATE_YYYY_MM_DD = TimeFormatDefault.DATE_YYYY_MM_DD;
	public static final String FORMAT_DATE_YYYY_MM_DD_HH_MM_SS = TimeFormatDefault.TIMESTAMP_YYYY_MM_DD_HH_MM_SS;
	public static final String FORMAT_DATE_ISO = TimeFormatDefault.TIMESTAMP_ISO_TZ;
	public static final String FORMAT_DATE_DEFAULT = FORMAT_DATE_YYYY_MM_DD;

	public static Date standardDateParse( String value, String format ) {
		return SafeFunction.get( () -> format == null ? java.sql.Date.valueOf( value ) : new SimpleDateFormat(format).parse( value ) );
	}
	
	public static Number standardNumberParse( String value, String format ) {
		return SafeFunction.get( () -> format == null ? new BigDecimal( value ) : new DecimalFormat( format ).parse( value ) );
	}

}
