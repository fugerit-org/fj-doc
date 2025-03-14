package test.org.fugerit.java.doc.base.config;

import java.io.IOException;
import java.sql.SQLException;

import org.fugerit.java.doc.base.config.DocException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class TestDocException {

	@Test
	void testApplySilent() throws DocException {
		boolean ok = true;
		DocException.applySilent( () -> { throw new IOException( "junit test scenario" ); } );
		Assertions.assertTrue( ok );
	}
	
	@Test
	void testGetSilent() throws DocException {
		Object result = DocException.getSilent( () -> { throw new IOException( "junit test scenario" ); } );
		Assertions.assertNull( result );
	}
	
	@Test
	void testApplyEX() {
		Assertions.assertThrows( DocException.class ,() -> DocException.apply( () -> { throw new IOException( "junit test scenario" ); } ) );
	}

	@Test
	void testGetEX() {
		Assertions.assertThrows( DocException.class ,() -> DocException.get( () -> { throw new IOException( "junit test scenario" ); } ) );
	}
	
	@Test
	void testApplyEXMessage() {
		Assertions.assertThrows( DocException.class ,() -> DocException.applyWithMessage( () -> { throw new IOException( "junit test scenario" ); }, "test message" ) );
	}

	@Test
	void testGetEXMessage() {
		Assertions.assertThrows( DocException.class ,() -> DocException.getWithMessage( () -> { throw new IOException( "junit test scenario" ); }, "test message" ) );
	}
	
	@Test
	void testApplyEXMessageOk() throws DocException {
		boolean ok = true;
		DocException.applyWithMessage( () -> log.info( "test ok" ) , "test message" );
		Assertions.assertTrue( ok );
	}

	@Test
	void testGetEXMessageOk() throws DocException {
		Assertions.assertNotNull( DocException.getWithMessage( () -> "test ok" , "test message" ) );
	}
	
	@Test
	void testApply() throws DocException {
		DocException.apply( () ->  log.info( "apply ok" ) );
		Assertions.assertThrows( DocException.class ,() -> DocException.apply( () -> { throw new SQLException( "junit test scenario" ); } ) );
	}

	@Test
	void testGet() throws DocException {
		Assertions.assertNotNull( DocException.get( () -> "ok" ) );
		Assertions.assertThrows( DocException.class ,() -> DocException.get( () -> { throw new SQLException( "junit test scenario" ); } ) );
	}
	
	@Test
	void testEx1() {
		Assertions.assertNotNull( new DocException() );
	}
	
	@Test
	void testEx2() {
		Assertions.assertNotNull( new DocException( "a" ) );
	}
	
	@Test
	void testEx3() {
		Assertions.assertNotNull( new DocException( new SQLException( "b" ) ) );
	}
	
	@Test
	void testEx4() {
		Assertions.assertNotNull( new DocException( "c", new SQLException( "d" ) ) );
	}
	
	@Test
	void testEx5() {
		Assertions.assertNotNull( DocException.convertEx( "e" , new SQLException( "f" ) ) );
	}
	
	@Test
	void testEx6() {
		Assertions.assertNotNull( DocException.convertEx( "g" , new DocException( "g" ) ) );
	}
	
	@Test
	void testEx7() {
		Assertions.assertNotNull( DocException.convertExMethod( "e" , new SQLException( "f" ) ) );
	}

	@Test
	void testEx8() {
		Assertions.assertNotNull( new DocException(  "code", "e" , new SQLException( "f" ) ).getCode() );
	}
	
}
