package test.org.fugerit.java.doc.json.typehandler;

import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.json.typehandler.DocTypeHandlerCoreJSONUTF8;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;

class TestJsonHandlers {

    private static final String INPUT_DOC = "<doc/>";

    private static final String OUTPUT_DOC = "{\n" +
            "  \"_t\" : \"doc\"\n" +
            "}";

    @Test
    void testHandler() throws Exception {
        DocTypeHandler handler = DocTypeHandlerCoreJSONUTF8.HANDLER;
        try (StringReader from = new StringReader(INPUT_DOC);
             ByteArrayOutputStream os = new ByteArrayOutputStream();
        ) {
            handler.handle(DocInput.newInput( handler.getType(), from ), DocOutput.newOutput(os));
            Assertions.assertEquals( OUTPUT_DOC, os.toString() );
        }
    }

}
