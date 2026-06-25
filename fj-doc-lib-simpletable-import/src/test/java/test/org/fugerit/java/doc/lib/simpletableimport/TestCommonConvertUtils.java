package test.org.fugerit.java.doc.lib.simpletableimport;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.doc.lib.simpletableimport.CommonConvertUtils;
import org.fugerit.java.doc.lib.simpletableimport.ConvertCsvToSimpleTableFacade;
import org.fugerit.java.doc.lib.simpletableimport.ConvertXlsxToSimpleTableFacade;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class TestCommonConvertUtils {

    @Test
    void testCsvConvertRethrowsExceptionByDefault() {
        // empty stream is an invalid CSV input that causes IOException/CsvValidationException
        // with rethrow-exception=true (default), ConfigException should be thrown
        byte[] invalidBytes = "".getBytes( StandardCharsets.UTF_8 );
        // We need to provide a stream that will fail mid-read: simulate by closing stream before use
        ConvertCsvToSimpleTableFacade facade = new ConvertCsvToSimpleTableFacade();
        Properties params = new Properties();
        params.setProperty( CommonConvertUtils.PARAM_KEY_RETHROW_EXCEPTION, "1" );
        // A broken InputStream that throws IOException on read
        InputStream brokenStream = new InputStream() {
            @Override
            public int read() throws java.io.IOException {
                throw new java.io.IOException( "Simulated IO error" );
            }
        };
        Assertions.assertThrows( ConfigException.class,
                () -> facade.convertCsv( brokenStream, params ) );
    }

    @Test
    void testCsvConvertReturnsNullWhenRethrowDisabled() throws Exception {
        ConvertCsvToSimpleTableFacade facade = new ConvertCsvToSimpleTableFacade();
        Properties params = new Properties();
        params.setProperty( CommonConvertUtils.PARAM_KEY_RETHROW_EXCEPTION, "0" );
        InputStream brokenStream = new InputStream() {
            @Override
            public int read() throws java.io.IOException {
                throw new java.io.IOException( "Simulated IO error" );
            }
        };
        // Should return null instead of throwing
        org.fugerit.java.doc.lib.simpletable.model.SimpleTable result =
                facade.convertCsv( brokenStream, params );
        Assertions.assertNull( result );
    }

    @Test
    void testXlsxConvertRethrowsExceptionByDefault() {
        ConvertXlsxToSimpleTableFacade facade = new ConvertXlsxToSimpleTableFacade();
        Properties params = new Properties();
        params.setProperty( CommonConvertUtils.PARAM_KEY_RETHROW_EXCEPTION, "1" );
        // Not a valid xlsx file content
        byte[] invalidBytes = "not-an-xlsx".getBytes( StandardCharsets.UTF_8 );
        InputStream badStream = new ByteArrayInputStream( invalidBytes );
        Assertions.assertThrows( ConfigException.class,
                () -> facade.convertXlsx( badStream, params ) );
    }

    @Test
    void testXlsxConvertReturnsNullWhenRethrowDisabled() throws Exception {
        ConvertXlsxToSimpleTableFacade facade = new ConvertXlsxToSimpleTableFacade();
        Properties params = new Properties();
        params.setProperty( CommonConvertUtils.PARAM_KEY_RETHROW_EXCEPTION, "0" );
        byte[] invalidBytes = "not-an-xlsx".getBytes( StandardCharsets.UTF_8 );
        InputStream badStream = new ByteArrayInputStream( invalidBytes );
        org.fugerit.java.doc.lib.simpletable.model.SimpleTable result =
                facade.convertXlsx( badStream, params );
        Assertions.assertNull( result );
    }

    @Test
    void testParamKeyConstant() {
        Assertions.assertEquals( "rethrow-exception", CommonConvertUtils.PARAM_KEY_RETHROW_EXCEPTION );
    }

    @Test
    void testParamValueDefaultConstant() {
        Assertions.assertEquals( "1", CommonConvertUtils.PARAM_VALUE_RETHROW_EXCEPTION_DEFAULT );
    }
}
