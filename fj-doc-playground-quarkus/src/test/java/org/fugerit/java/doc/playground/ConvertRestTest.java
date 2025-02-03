package org.fugerit.java.doc.playground;

import static io.restassured.RestAssured.given;

import org.fugerit.java.TestConsts;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;

@QuarkusTest
@Slf4j
class ConvertRestTest {

    private byte[] getInput(String path) {
        return SafeFunction.get(() -> {
            String json = StreamIO.readString(ClassHelper.loadFromDefaultClassLoader("request/payload/" + path));
            log.info("payload : {}", json);
            return json.getBytes();
        });
    }

    private void testWorker(String apiPath, String jsonPayloadPath, int code) {
        given()
                .when()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(getInput(jsonPayloadPath))
                .post(TestConsts.BASE_API_PATH + apiPath)
                .then()
                .statusCode(code);
    }

    @Test
    void testConvertDocument200() {
        int[] testId = { 1, 2, 3, 6, 7, 8 };
        for (int k = 0; k < testId.length; k++) {
            this.testWorker("/convert/doc", "convert/test_convert_input_0" + testId[k] + ".json", 200);
        }
        Assertions.assertTrue(Boolean.TRUE); // the condition is actually checked by rest assured
    }

    @Test
    void testConvertDocument400() {
        int[] testId = { 4, 5 };
        for (int k = 0; k < testId.length; k++) {
            this.testWorker("/convert/doc", "convert/test_convert_input_0" + testId[k] + ".json", 400);
        }
        Assertions.assertTrue(Boolean.TRUE); // the condition is actually checked by rest assured
    }

}