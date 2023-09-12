package org.fugerit.java;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class GreetingResourceTest {

    @Test
    void testHelloEndpoint() {
        given()
          .when().get( TestConsts.BASE_API_PATH+"/hello" )
          .then()
             .statusCode(200)
             .body(is("Hello from RESTEasy Reactive"));
    }

}