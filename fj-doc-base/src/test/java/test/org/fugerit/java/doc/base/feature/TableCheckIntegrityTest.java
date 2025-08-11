package test.org.fugerit.java.doc.base.feature;

import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.db.daogen.BasicDaoResult;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.base.config.DocException;
import org.fugerit.java.doc.base.facade.DocFacadeSource;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.parser.DocParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
class TableCheckIntegrityTest {

    private int testWorker( String xmlPath ) {
        DocParser docParser = DocFacadeSource.getInstance().getParserForSource( DocFacadeSource.SOURCE_TYPE_XML );
        String fullCLPath = String.format( "feature-info/table-check-integrity/%s.xml", xmlPath );
        log.info( "xmlPath: {}, fullPath: {}", xmlPath, fullCLPath );
        try (InputStream is = ClassHelper.loadFromDefaultClassLoader( fullCLPath ) ) {
            DocBase docBase = docParser.parse( is );
            return BasicDaoResult.RESULT_CODE_OK;
        } catch (DocException | IOException e) {
            throw ConfigRuntimeException.convertEx( e );
        }
    }

    @Test
    void testOk1() {
        Assertions.assertEquals( BasicDaoResult.RESULT_CODE_OK, this.testWorker( "colspan-rowspan-sample-ok" ) );
    }

    @Test
    void testKo1() {
        Assertions.assertEquals( BasicDaoResult.RESULT_CODE_OK, this.testWorker( "colspan-rowspan-sample-ko" ) );
    }

}
