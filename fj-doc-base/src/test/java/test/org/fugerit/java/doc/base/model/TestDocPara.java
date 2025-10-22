package test.org.fugerit.java.doc.base.model;

import java.util.Date;
import java.util.Locale;

import org.fugerit.java.doc.base.facade.DocFacadeSource;
import org.fugerit.java.doc.base.model.DocPara;
import org.fugerit.java.doc.base.typehelper.generic.FormatTypeConsts;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class TestDocPara {

	@Test
	void testNumber() {
		log.info( "java version : {}" , System.getProperty( "java.version" ) );
		Locale.setDefault( Locale.UK );
		DocPara model = new DocPara();
		model.setText( "100000.0" );
		model.setType( FormatTypeConsts.TYPE_NUMBER );
		log.info( "test : {}, type : {}", model.getText(), model.getType() );
		Number n = FormatTypeConsts.standardNumberParse( model.getText() , model.getFormat() );
		log.info( "n : {} -> {}", n.getClass(), n );
		model.setFormat( "#,###" );
		n = FormatTypeConsts.standardNumberParse( model.getText() , model.getFormat() );
		log.info( "n : {} -> {}", n.getClass(), n );
		Assertions.assertEquals( 100000L , n );
	}
	
	@Test
	void testDate() {
		DocPara model = new DocPara();
		model.setText( "2023-10-03" );
		model.setType( FormatTypeConsts.TYPE_DATE );
		log.info( "test : {}, type : {}", model.getText(), model.getType() );
		Date d = FormatTypeConsts.standardDateParse( model.getText() , model.getFormat() );
		log.info( "d : {} -> {}", d.getClass(), d );
		Assertions.assertEquals( java.sql.Date.valueOf( model.getText() ), d );
		model.setFormat( "yyyy-MM-dd" );
		FormatTypeConsts.standardDateParse( model.getText() , model.getFormat() );
		log.info( "d : {} -> {}", d.getClass(), d );
	}
	
}
