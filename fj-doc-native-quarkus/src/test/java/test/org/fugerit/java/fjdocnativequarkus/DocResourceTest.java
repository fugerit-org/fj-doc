package test.org.fugerit.java.fjdocnativequarkus;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.WebApplicationException;
import org.fugerit.java.fjdocnativequarkus.DocResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
class DocResourceTest {

    @Test
    void testMarkdown() {
        given().when().get("/doc/example.md").then().statusCode(200);
    }

    @Test
    void testHtml() {
        given().when().get("/doc/example.html").then().statusCode(200);
    }

    @Test
    void testAsciiDoc() {
        given().when().get("/doc/example.adoc").then().statusCode(200);
    }

    @Test
    void testCsv() {
        given().when().get("/doc/example.csv").then().statusCode(200);
    }

    @Test
    void testException() {
        DocResource resoure = new DocResource();
        Assertions.assertThrows(WebApplicationException.class, () -> resoure.processDocument("not-exists"));
    }

}