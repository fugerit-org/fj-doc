package org.fugerit.java.doc.playground;

import static io.restassured.RestAssured.given;

import org.fugerit.java.TestConsts;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.lib.simpletable.SimpleTableFacade;
import org.fugerit.java.doc.lib.simpletable.model.SimpleTable;
import org.fugerit.java.doc.playground.doc.GenerateRest;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;

@QuarkusTest
@Slf4j
class GenerateRestTest {

	@Test
	void testAddRow() {
		SimpleTable simpleTable = SimpleTableFacade.newTable( 100 );
		GenerateRest.addRow( simpleTable, 1, "error", "test" );
		Assert.assertEquals( 1, simpleTable.getRows().size() );
	}

	private byte[] getInput( String path ) {
		return SafeFunction.get( () ->  {
			String json = StreamIO.readString( ClassHelper.loadFromDefaultClassLoader( "request/payload/"+path ) );
			log.info( "payload : {}", json );
			return json.getBytes();	
		} );
	}
	
	private void testWorker( String apiPath, String jsonPayloadPath ) {
        given()
        .when()
        .accept( MediaType.APPLICATION_JSON )
        .contentType( MediaType.APPLICATION_JSON )
        .body( getInput( jsonPayloadPath ) )
        .post( TestConsts.BASE_API_PATH+apiPath )
        .then()
           .statusCode(200);
	}
	
    @Test
    void testGenerateDocument() {   	
    	int[] testId = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };
    	for ( int k=0; k<testId.length; k++ ) {
    		String current = String.valueOf( testId[k]<10 ? "0"+testId[k] : testId[k] ); 
    		this.testWorker( "/generate/document", "generate/test_generate_input_"+current+".json" );
    	}   
    }

    @Test
    void testValidateDocument() {
    	int[] testId = { 1, 5, 6 };
    	for ( int k=0; k<testId.length; k++ ) {
    		this.testWorker( "/generate/validate", "generate/test_generate_input_0"+testId[k]+".json" );
    	}    	
    }
    
}