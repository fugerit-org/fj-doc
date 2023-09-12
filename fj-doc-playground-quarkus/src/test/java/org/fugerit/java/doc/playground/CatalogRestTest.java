package org.fugerit.java.doc.playground;

import static io.restassured.RestAssured.given;

import org.fugerit.java.TestConsts;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class CatalogRestTest {
	
    @Test
    void testList() {   	
        given()
        .when()
        .get( TestConsts.BASE_API_PATH+"/catalog/list" )
        .then()
           .statusCode(200);
    }
    
    @Test
    void testEntry() {   	
        given()
        .when()
        .get( TestConsts.BASE_API_PATH+"/catalog/entry/default" )
        .then()
           .statusCode(200);
    }

}