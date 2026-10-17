package test.org.fugerit.java.doc.mod.opencsv.coverage;

import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;

import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.mod.opencsv.OpenCSVTypeHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * Tests for the non-UTF8 {@link OpenCSVTypeHandler} to complement the existing
 * UTF-8 handler coverage in {@link TestOpencsvCoverage}.
 */
@Slf4j
class TestOpenCSVTypeHandler {

    private byte[] handle( String resourcePath ) throws Exception {
        try ( InputStreamReader reader = new InputStreamReader(
                ClassHelper.loadFromDefaultClassLoader( resourcePath ) );
              ByteArrayOutputStream buffer = new ByteArrayOutputStream() ) {
            OpenCSVTypeHandler handler = new OpenCSVTypeHandler();
            handler.handle(
                    DocInput.newInput( OpenCSVTypeHandler.TYPE_CSV, reader ),
                    DocOutput.newOutput( buffer ) );
            return buffer.toByteArray();
        }
    }

    @Test
    void testHandlerWithCustomSeparatorAndLineEnd() throws Exception {
        // default_doc.xml uses csv-separator=; and csv-line-end=\r\n
        byte[] result = this.handle( "coverage/xml/default_doc.xml" );
        Assertions.assertTrue( result.length > 0, "Output should not be empty" );
        String csv = new String( result );
        log.info( "CSV output length: {}", result.length );
        // The table has 3 columns separated by semicolon
        Assertions.assertTrue( csv.contains( ";" ), "CSV should use semicolon separator" );
    }

    @Test
    void testHandlerWithNoSeparatorOverride() throws Exception {
        // default_doc_alt.xml has no csv-separator configured, uses default comma
        byte[] result = this.handle( "coverage/xml/default_doc_alt.xml" );
        Assertions.assertTrue( result.length > 0, "Output should not be empty" );
        log.info( "CSV output length: {}", result.length );
    }

    @Test
    void testHandlerWithMissingTableIdProducesNoOutput() throws Exception {
        // default_doc_fail1.xml has no csv-table-id or a non-matching one
        byte[] result = this.handle( "coverage/xml/default_doc_fail1.xml" );
        Assertions.assertEquals( 0, result.length, "Output should be empty when table id is missing" );
    }

    @Test
    void testStaticHandlerConstant() {
        Assertions.assertNotNull( OpenCSVTypeHandler.HANDLER );
        Assertions.assertEquals( OpenCSVTypeHandler.TYPE_CSV, OpenCSVTypeHandler.HANDLER.getType() );
    }

    @Test
    void testHandlerUtf8StaticConstant() {
        Assertions.assertNotNull( OpenCSVTypeHandler.HANDLER_UTF8 );
        Assertions.assertEquals( OpenCSVTypeHandler.TYPE_CSV, OpenCSVTypeHandler.HANDLER_UTF8.getType() );
    }

    @Test
    void testMimeAndModuleConstants() {
        Assertions.assertEquals( "text/csv", OpenCSVTypeHandler.MIME );
        Assertions.assertEquals( "csv", OpenCSVTypeHandler.MODULE );
        Assertions.assertEquals( "csv", OpenCSVTypeHandler.TYPE_CSV );
    }
}
