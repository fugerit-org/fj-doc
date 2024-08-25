package org.fugerit.java.doc.playground;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.TestConsts;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
@Slf4j
class ConfigConvertRestTest {

	private byte[] getInput( String path ) {
		return SafeFunction.get( () ->  {
			String json = StreamIO.readString( ClassHelper.loadFromDefaultClassLoader( "request/payload/"+path ) );
			log.info( "payload : {}", json );
			return json.getBytes();	
		} );
	}
	
	private void testWorker( String apiPath, String jsonPayloadPath, int expectedStatus ) {
        given()
        .when()
        .accept( MediaType.APPLICATION_JSON )
        .contentType( MediaType.APPLICATION_JSON )
        .body( getInput( jsonPayloadPath ) )
        .post( TestConsts.BASE_API_PATH+apiPath )
        .then()
           .statusCode(expectedStatus);
	}
	
    @Test
    void testConvertConfig() {
		this.testWorker( "/config/convert", "convert_config/test_convert_config_1.json", 200 );
		Assertions.assertTrue( Boolean.TRUE );  // the condition is actually checked by rest assured
    }

	@Test
	void testConvertError() {
		this.testWorker( "/config/convert", "convert_config/test_convert_config_error.json", 200 );
		this.testWorker( "/config/convert", "convert_config/test_convert_config_ni1.json", 200 );
		this.testWorker( "/config/convert", "convert_config/test_convert_config_ni2.json", 200 );
		Assertions.assertTrue( Boolean.TRUE );  // the condition is actually checked by rest assured
	}


}