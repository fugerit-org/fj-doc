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
    void testListFilterFltx() {   	
        given()
        .when()
        .get( TestConsts.BASE_API_PATH+"/catalog/list/type/XML" )
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

    @Test
    void testRes200() {
        given()
                .when()
                .get( TestConsts.BASE_API_PATH+"/catalog/res/convert-config-stub.properties" )
                .then()
                .statusCode(200);
    }

    @Test
    void testRes404() {
        given()
                .when()
                .get( TestConsts.BASE_API_PATH+"/catalog/res/404b.properties" )
                .then()
                .statusCode(404);
    }

}