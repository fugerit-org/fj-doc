package org.fugerit.java.doc.val.imageio.tiff;

import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.core.util.result.Result;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

class TestImageIOTiffValidator {

    private ImageIOTiffValidator validator = new ImageIOTiffValidator();

    @Test
    void testValidTiff() throws Exception {
        try (InputStream is = ClassHelper.loadFromDefaultClassLoader( "sample/file_example_TIFF_1MB.tiff" )) {
            Assertions.assertEquals(Result.RESULT_CODE_OK, validator.validate( is ).getResultCode() );
        }
    }

    @Test
    void testInvalidMetadataTiff() throws Exception {
        try (InputStream is = ClassHelper.loadFromDefaultClassLoader( "sample/image-ko.tiff" )) {
            Assertions.assertEquals("Missing tags : [282 (XResolution), 283 (YResolution), 34675 (ICCProfile)]", validator.validate( is ).getValidationMessage() );
        }
    }

    @Test
    void testPngAsTiff() throws Exception {
        try (InputStream is = ClassHelper.loadFromDefaultClassLoader( "sample/png_as_tiff.tiff" )) {
            Assertions.assertEquals("No TIFF metadata available", validator.validate( is ).getValidationMessage() );
        }
    }

    @Test
    void testPdfAsTiff() throws Exception {
        try (InputStream is = ClassHelper.loadFromDefaultClassLoader( "sample/pdf_as_tiff.tiff" )) {
            Assertions.assertEquals("No ImageReader available", validator.validate( is ).getValidationMessage() );
        }
    }

    @Test
    void testNullStream() {
        Assertions.assertEquals("input == null!", validator.validate( null ).getValidationMessage() );
    }

    void testDefaultTags() {
        Assertions.assertNotEquals( 0, ImageIOTiffValidator.DEFAULT_IT_TAG_LIST.size() );
    }

}
