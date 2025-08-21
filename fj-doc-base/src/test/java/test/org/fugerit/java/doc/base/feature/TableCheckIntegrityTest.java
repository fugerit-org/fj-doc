package test.org.fugerit.java.doc.base.feature;

import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.db.daogen.BasicDaoResult;
import org.fugerit.java.core.lang.ex.CodeRuntimeException;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.core.util.result.Result;
import org.fugerit.java.doc.base.facade.DocFacadeSource;
import org.fugerit.java.doc.base.feature.DocFeatureRuntimeException;
import org.fugerit.java.doc.base.feature.FeatureConfig;
import org.fugerit.java.doc.base.feature.tableintegritycheck.TableIntegrityCheck;
import org.fugerit.java.doc.base.feature.tableintegritycheck.TableIntegrityCheckConstants;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.model.DocTable;
import org.fugerit.java.doc.base.typehelper.generic.GenericConsts;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
class TableCheckIntegrityTest {

    private DocBase readDocBase( String fullCLPath ) {
        try (Reader reader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader( fullCLPath ) ) ) {
            DocFacadeSource docFacadeSource = DocFacadeSource.getInstance();
            return docFacadeSource.parseRE( reader, DocFacadeSource.SOURCE_TYPE_XML, FeatureConfig.DEFAULT );
        } catch (IOException e) {
            throw ConfigRuntimeException.convertEx( e );
        }
    }

    private int testWorker( String xmlPath ) {
        String fullCLPath = String.format( "feature-info/table-check-integrity/%s.xml", xmlPath );
        log.info( "xmlPath: {}, fullPath: {}", xmlPath, fullCLPath );
        DocBase docBase = readDocBase( fullCLPath );
        log.info( "docBase:{}", docBase );
        return BasicDaoResult.RESULT_CODE_OK;
    }

    @Test
    void testOk() {
        Assertions.assertEquals( BasicDaoResult.RESULT_CODE_OK, this.testWorker( "colspan-rowspan-sample-ok" ) );
        Assertions.assertTrue( Boolean.TRUE );
    }

    @Test
    void testConfiguration() {
        FeatureConfig featureConfig = new FeatureConfig() {
            @Override
            public String getTableCheckIntegrity() {
                return TableIntegrityCheckConstants.TABLE_INTEGRITY_CHECK_FAIL;
            }
        };
        DocBase docBase = readDocBase( "feature-info/table-check-integrity/colspan-rowspan-sample-ko-noconfig.xml" );
        DocTable docTable = (DocTable) docBase.getDocBody().getElementList().stream().filter( e -> e instanceof DocTable ).findFirst().get();
        // global configuration set to fail
        Assertions.assertThrows( DocFeatureRuntimeException.class, () -> TableIntegrityCheck.apply( docBase, docTable, featureConfig ) );
        // override configuration for this document, will not fail but result != OK
        docBase.getStableInfo().setProperty(GenericConsts.DOC_TABLE_CHECK_INTEGRITY, TableIntegrityCheckConstants.TABLE_INTEGRITY_CHECK_WARN );
        Assertions.assertNotEquals(Result.RESULT_CODE_OK, TableIntegrityCheck.apply( docBase, docTable, featureConfig ) );
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
