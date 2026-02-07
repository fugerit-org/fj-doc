package test.org.fugerit.java.doc.sample.facade;

import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.base.facade.DocFacadeSource;
import org.fugerit.java.doc.base.parser.DocParser;
import org.fugerit.java.doc.base.parser.DocValidationResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.InputStreamReader;
import java.io.Reader;

@Slf4j
class TestValidate {

    private boolean validateWorker( int sourceType, String path ) throws Exception {
        log.info( "java.version : {}, sourceType: {}, path: {}", System.getProperty( "java.version" ), sourceType, path );
        try (Reader reader = new InputStreamReader(ClassHelper.loadFromDefaultClassLoader( path ))) {
            DocFacadeSource facadeSource = new DocFacadeSource();
            DocParser parser = facadeSource.getParserForSource( sourceType );
            DocValidationResult result = parser.validateVersionResult( reader  );
            log.info( "result : {}", result.isResultOk() );
            result.getInfoList().forEach(log::info);
            result.getErrorList().forEach(log::info);
            return result.isResultOk();
        }
    }

    @Test
    void testConversionAndValidateXml() throws Exception {
        Assertions.assertTrue(
                this.validateWorker( DocFacadeSource.SOURCE_TYPE_XML ,
                        "sample_docs/junit_base/intro_01.xml"  ) );
    }

    @Test
    void testConversionAndValidateJson() throws Exception {
        Assertions.assertTrue(
                this.validateWorker( DocFacadeSource.SOURCE_TYPE_JSON ,
                        "sample_docs/junit_base/intro_01.json"  ) );
    }

}
