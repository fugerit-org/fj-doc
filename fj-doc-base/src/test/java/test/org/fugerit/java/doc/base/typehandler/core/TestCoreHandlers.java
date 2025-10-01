package test.org.fugerit.java.doc.base.typehandler.core;

import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.typehandler.core.DocTypeHandlerCoreSource;
import org.fugerit.java.doc.base.typehandler.core.DocTypeHandlerCoreXMLUTF8;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;

class TestCoreHandlers {

    private static final String INPUT_DOC = "<doc/>";

    private String testWorker( DocTypeHandler handler ) throws Exception {
        try (StringReader from = new StringReader(INPUT_DOC);
             ByteArrayOutputStream os = new ByteArrayOutputStream();
        ) {
            handler.handle(DocInput.newInput( handler.getType(), from ), DocOutput.newOutput(os));
            return os.toString();
        }
    }

    @Test
    void testCoreXml()  throws Exception {
        Assertions.assertEquals( INPUT_DOC, this.testWorker( DocTypeHandlerCoreXMLUTF8.HANDLER ) );
    }

    @Test
    void testCoreSource()  throws Exception {
        Assertions.assertEquals( INPUT_DOC, this.testWorker(  DocTypeHandlerCoreSource.HANDLER ) );
    }

}
