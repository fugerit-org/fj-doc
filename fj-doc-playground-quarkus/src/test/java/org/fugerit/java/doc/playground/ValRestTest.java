package org.fugerit.java.doc.playground;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.fugerit.java.TestConsts;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.playground.val.ValInput;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class ValRestTest {

	private byte[] getInput( String path ) {
		return SafeFunction.get( () ->  {
			String json = StreamIO.readString( ClassHelper.loadFromDefaultClassLoader( "request/payload/"+path ) );
			return json.getBytes();	
		} );
	}
	
    @Test
    void testSupportedExcentions() {
        given()
          .when().get( TestConsts.BASE_API_PATH+"/val/supported_extensions" )
          .then()
             .statusCode(200)
             .body(is("[\"JPG\",\"TIF\",\"DOCX\",\"XLSX\",\"TIFF\",\"PDF\",\"XML\",\"P7M\",\"PNG\",\"DOC\",\"JPEG\",\"XLS\"]"));
        Assertions.assertTrue( Boolean.TRUE );  // the condition is actually checked by rest assured
    }
    
    @Test
    void testValidation200() {
        given()
          .when()
          .multiPart( "file", "pdf_as_pdf.pdf", getInput( "val/pdf_as_pdf.pdf" ) )
          .post( TestConsts.BASE_API_PATH+"/val/check" )
          .then()
             .statusCode(200);
        Assertions.assertTrue( Boolean.TRUE );  // the condition is actually checked by rest assured
    }
    
    @Test
    void testValidation400() {
        given()
          .when()
          .multiPart( "file", "png_as_pdf.pdf", getInput( "val/png_as_pdf.pdf" ) )
          .post( TestConsts.BASE_API_PATH+"/val/check" )
          .then()
             .statusCode(400);
        Assertions.assertTrue( Boolean.TRUE );  // the condition is actually checked by rest assured
    }
    
    @Test
    void testInput() {
    	ValInput input = new ValInput();
    	input.setFile( null );
    	Assertions.assertNull( input.getFile() );
    }
    
}