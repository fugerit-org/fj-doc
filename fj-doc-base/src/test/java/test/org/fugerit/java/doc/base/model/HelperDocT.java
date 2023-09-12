package test.org.fugerit.java.doc.base.model;

import org.fugerit.java.doc.base.model.DocElement;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HelperDocT {

	public static final String TEST_ID = "1";
	
	protected void baseTest( DocElement element ) {
		log.info( "id     -> {}", element.getId() );
		log.info( "toS    -> {}", element );
	}
	
}
