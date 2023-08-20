package org.fugerit.java.doc.playground;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.fugerit.java.TestConsts;

@QuarkusTest
class ValRestTest {

    @Test
    void testHelloEndpoint() {
        given()
          .when().get( TestConsts.BASE_API_PATH+"/val/supported_extensions" )
          .then()
             .statusCode(200)
             .body(is("[\"JPG\",\"TIF\",\"DOCX\",\"XLSX\",\"TIFF\",\"PDF\",\"PNG\",\"DOC\",\"JPEG\",\"XLS\"]"));
    }

}