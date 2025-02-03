package org.fugerit.java;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class GreetingResourceTest {

    @Test
    void testHelloEndpoint() {
        given()
                .when().get(TestConsts.BASE_API_PATH + "/hello")
                .then()
                .body(is("Hello from RESTEasy Reactive"))
                .statusCode(200);
        Assertions.assertTrue(Boolean.TRUE); // the condition is actually checked by rest assured
    }

}