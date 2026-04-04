package test.org.fugerit.java.doc.json.parse;

import org.fugerit.java.doc.json.parse.DocObjectMapperConstants;
import org.fugerit.java.doc.json.parse.JsonConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestJsonConstants {

    @Test
    void testJsonConstantsGetDefaultMapper() {
        Assertions.assertNotNull( JsonConstants.getDefaultMapper() );
    }

    @Test
    void testDocObjectMapperConstantsXsdVersionKey() {
        Assertions.assertEquals( "xsd-version", DocObjectMapperConstants.PROPERTY_XSD_VERSION );
    }
}
