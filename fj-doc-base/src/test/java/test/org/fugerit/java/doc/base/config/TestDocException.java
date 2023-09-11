package test.org.fugerit.java.doc.base.config;

import java.sql.SQLException;

import org.fugerit.java.doc.base.config.DocException;
import org.junit.Assert;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestDocException {

	@Test
	public void testApply() throws DocException {
		DocException.apply( () ->  log.info( "apply ok" ) );
		Assert.assertThrows( DocException.class ,() -> DocException.apply( () -> { throw new SQLException( "junit test scenario" ); } ) );
	}

	@Test
	public void testGet() throws DocException {
		Assert.assertNotNull( DocException.get( () -> "ok" ) );
		Assert.assertThrows( DocException.class ,() -> DocException.get( () -> { throw new SQLException( "junit test scenario" ); } ) );
	}
	
	@Test
	public void testEx1() {
		Assert.assertNotNull( new DocException() );
	}
	
	@Test
	public void testEx2() {
		Assert.assertNotNull( new DocException( "a" ) );
	}
	
	@Test
	public void testEx3() {
		Assert.assertNotNull( new DocException( new SQLException( "b" ) ) );
	}
	
	@Test
	public void testEx4() {
		Assert.assertNotNull( new DocException( "c", new SQLException( "d" ) ) );
	}
	
	@Test
	public void testEx5() {
		Assert.assertNotNull( DocException.convertEx( "e" , new SQLException( "f" ) ) );
	}
	
	@Test
	public void testEx6() {
		Assert.assertNotNull( DocException.convertEx( "g" , new DocException( "g" ) ) );
	}
	
	@Test
	public void testEx7() {
		Assert.assertNotNull( DocException.convertExMethod( "e" , new SQLException( "f" ) ) );
	}

	@Test
	public void testEx8() {
		Assert.assertNotNull( new DocException(  "code", "e" , new SQLException( "f" ) ).getCode() );
	}
	
}
