package org.fugerit.java.doc.playground;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.TestConsts;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.playground.init.ProjectRest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static io.restassured.RestAssured.given;

@QuarkusTest
@Slf4j
class InitRestTest {

	private byte[] getInput( String path ) {
		return SafeFunction.get( () ->  {
			String json = StreamIO.readString( ClassHelper.loadFromDefaultClassLoader( path ) );
			log.info( "payload : {}", json );
			return json.getBytes();	
		} );
	}
	
	private void testWorker( String apiPath, String jsonPayloadPath, int code ) {
        given()
        .when()
        .accept( MediaType.APPLICATION_JSON )
        .contentType( MediaType.APPLICATION_JSON )
        .body( getInput( jsonPayloadPath ) )
        .post( TestConsts.BASE_API_PATH+apiPath )
        .then()
           .statusCode(code);
	}
	
    @Test
    void testInit() {
		this.testWorker( "/project/init", "request/payload/init/init_ok_1.json", 200 );
		this.testWorker( "/project/init", "request/payload/init/init_ko_1.json", 200 );
		Assertions.assertTrue( Boolean.TRUE );  // the condition is actually checked by rest assured
    }

	@Test
	void testExtensionList() {
		given()
				.when()
				.accept( MediaType.APPLICATION_JSON )
				.contentType( MediaType.APPLICATION_JSON )
				.get( TestConsts.BASE_API_PATH+"/project/extensions-list" )
				.then()
				.statusCode(200);
		Assertions.assertTrue( Boolean.TRUE );  // the condition is actually checked by rest assured
	}

	@Test
	void checkZipFunction() {
		Assertions.assertTrue( ProjectRest.ensureEndWithSlash( "test1" ).endsWith( "/" ) );
		Assertions.assertTrue( ProjectRest.ensureEndWithSlash( "test2/" ).endsWith( "/" ) );
		Assertions.assertThrows( IOException.class, () -> ProjectRest.checkIfInTempFolder( new File( System.getProperty( "user.dir" ) )) );
		Assertions.assertThrows( IOException.class, () -> ProjectRest.zipFolder( new File( "/"+System.currentTimeMillis()+"/not-exist" ), new ByteArrayOutputStream() ) );
	}
    
}