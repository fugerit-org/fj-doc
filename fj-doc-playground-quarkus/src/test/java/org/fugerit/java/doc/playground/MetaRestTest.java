package org.fugerit.java.doc.playground;

import static io.restassured.RestAssured.given;

import org.fugerit.java.TestConsts;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class MetaRestTest {

    @Test
    void testMetaOk() {
        given()
                .when()
                .get(TestConsts.BASE_API_PATH + "/meta/version")
                .then()
                .statusCode(200);
        Assertions.assertTrue(Boolean.TRUE); // the condition is actually checked by rest assured
    }

    @Test
    void testMetaInfoOk() {
        given()
                .when()
                .get(TestConsts.BASE_API_PATH + "/meta/info")
                .then()
                .statusCode(200);
        Assertions.assertTrue(Boolean.TRUE); // the condition is actually checked by rest assured
    }

}