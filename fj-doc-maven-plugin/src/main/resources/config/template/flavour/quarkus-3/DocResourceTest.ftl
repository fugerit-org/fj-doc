<#import '../flavour-macro.ftl' as fhm>
package test.<@fhm.toProjectPackage context=context/>;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

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

    <#if context.modules?seq_contains("fj-doc-mod-fop")>
    @Test
    void testPdf() {
        given().when().get("/doc/example.pdf").then().statusCode(200);
    }
    </#if>
    <#if context.modules?seq_contains("fj-doc-mod-poi")>
    @Test
    void testXlsx() {
        given().when().get("/doc/example.xlsx").then().statusCode(200);
    }
    </#if>
    <#if context.modules?seq_contains("fj-doc-mod-opencsv")>
    @Test
    void testCsv() {
        given().when().get("/doc/example.csv").then().statusCode(200);
    }
    </#if>

}