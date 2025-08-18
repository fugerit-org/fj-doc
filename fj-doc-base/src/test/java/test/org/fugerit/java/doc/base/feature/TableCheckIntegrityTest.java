package test.org.fugerit.java.doc.base.feature;

import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.db.daogen.BasicDaoResult;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.base.facade.DocFacadeSource;
import org.fugerit.java.doc.base.feature.DocFeatureRuntimeException;
import org.fugerit.java.doc.base.feature.FeatureConfig;
import org.fugerit.java.doc.base.model.DocBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
class TableCheckIntegrityTest {

    private int testWorker( String xmlPath ) {
        String fullCLPath = String.format( "feature-info/table-check-integrity/%s.xml", xmlPath );
        log.info( "xmlPath: {}, fullPath: {}", xmlPath, fullCLPath );
        try (Reader reader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader( fullCLPath ) ) ) {
            DocFacadeSource docFacadeSource = DocFacadeSource.getInstance();
            DocBase docBase = docFacadeSource.parseRE( reader, DocFacadeSource.SOURCE_TYPE_XML, FeatureConfig.DEFAULT );
            log.info( "docBase:{}", docBase );
            return BasicDaoResult.RESULT_CODE_OK;
        } catch (IOException e) {
            throw ConfigRuntimeException.convertEx( e );
        }
    }

    @Test
    void testOk() {
        Assertions.assertEquals( BasicDaoResult.RESULT_CODE_OK, this.testWorker( "colspan-rowspan-sample-ok" ) );
        Assertions.assertTrue( Boolean.TRUE );
    }

    @Test
    void testKoNoException() {
        Arrays.asList( "colspan-rowspan-sample-ko-disabled", "colspan-rowspan-sample-ko-warn" )
                .forEach( k -> Assertions.assertEquals( BasicDaoResult.RESULT_CODE_OK, this.testWorker( k ) ) );
        Assertions.assertTrue( Boolean.TRUE );
    }

    @Test
    void testFailException() {
        Map<String, String> testCases = new LinkedHashMap<>();
        testCases.put( "colspan-rowspan-sample-ko-fail-less-column", "Row 0 has 2 columns instead of 3" );
        testCases.put( "colspan-rowspan-sample-ko-fail-extra-column", "Row 3 has 5 columns instead of 3" );
        testCases.put( "colspan-rowspan-sample-ko-fail", "Row 1 has 4 columns instead of 3" );
        testCases.put( "colspan-rowspan-sample-ko-fail-unfinished", "Unfinished rowspan at column 0" );
        for ( String k : testCases.keySet() ) {
            try {
                this.testWorker( k );
            } catch (DocFeatureRuntimeException e) {
                log.info( "messages: {}", e.getMessages() );
                Assertions.assertTrue( e.getMessages().contains( testCases.get( k ) ) );
            }
        }
        Assertions.assertTrue( Boolean.TRUE );
    }

}
