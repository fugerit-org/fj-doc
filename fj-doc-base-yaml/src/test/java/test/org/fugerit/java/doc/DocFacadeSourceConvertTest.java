package test.org.fugerit.java.doc;

import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.doc.base.facade.DocFacadeSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
class DocFacadeSourceConvertTest {

    private static final String TEST_XML_PATH = "src/test/resources/convert/doc_test_01.xml";
    private static final String TEST_JSON_PATH = "src/test/resources/convert/doc_test_01.json";
    private static final String TEST_YAML_PATH = "src/test/resources/convert/doc_test_01.yaml";

    private static final Map<Integer, String> MAP_SAMPLES = new HashMap<>();
    static {
        MAP_SAMPLES.put( DocFacadeSource.SOURCE_TYPE_XML, TEST_XML_PATH );
        MAP_SAMPLES.put( DocFacadeSource.SOURCE_TYPE_JSON, TEST_JSON_PATH );
        MAP_SAMPLES.put( DocFacadeSource.SOURCE_TYPE_YAML, TEST_YAML_PATH );
    }

    @Test
    void testConvertFormats() {
        List<Integer> sourceTypes = Arrays.asList( DocFacadeSource.SOURCE_TYPE_XML, DocFacadeSource.SOURCE_TYPE_JSON, DocFacadeSource.SOURCE_TYPE_YAML );
        int count = 0;
        for ( Integer fromSourceType : sourceTypes ) {
            for ( Integer toSourceType : sourceTypes ) {
                if ( !fromSourceType.equals( toSourceType )  ) {
                    String testPath = MAP_SAMPLES.get( fromSourceType );
                    SafeFunction.apply( () -> {
                        try (Reader from = new FileReader( new File( testPath ) );
                             Writer to = new StringWriter() ) {
                            log.info( "Convert from {} to {}", fromSourceType, toSourceType );
                            DocFacadeSource.getInstance().convert( from, fromSourceType, to, toSourceType );
                            log.info( "Conversion result : \n{}", to );
                            if ( toSourceType == DocFacadeSource.SOURCE_TYPE_XML ) {
                                Assertions.assertTrue( to.toString().startsWith( "<" ) );
                            } else if ( toSourceType == DocFacadeSource.SOURCE_TYPE_JSON ) {
                                Assertions.assertTrue( to.toString().startsWith( "{" ) );
                            } else if ( toSourceType == DocFacadeSource.SOURCE_TYPE_YAML ) {
                                Assertions.assertTrue( to.toString().startsWith( "---" ) );
                            }
                        }
                    } );
                    count++;
                }
            }
            Assertions.assertTrue( count > 0 );
        }
    }

}
