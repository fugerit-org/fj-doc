package test.org.fugerit.java.doc.core.val;

import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.val.core.DocValidatorTypeCheck;
import org.fugerit.java.doc.val.core.basic.ImageValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.InputStream;

@Slf4j
class TestDocValidatorTypeCheck {

    private static final DocValidatorTypeCheck TYPE_CHECK = DocValidatorTypeCheck.newInstance(
            ImageValidator.JPG_VALIDATOR, ImageValidator.PNG_VALIDATOR );

    private static final String BASE_PATH = "sample";

    private String worker( String fileName ) {
        return SafeFunction.get( () -> {
            String path = BASE_PATH+"/"+fileName;
            log.info( "test path {}", path );
            try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( path ) ) {
                return TYPE_CHECK.checkType( is );
            }
        } );
    }

    @Test
    void testJpg() {
        Assertions.assertEquals(ImageValidator.JPG_VALIDATOR.getMimeType(), this.worker( "jpg_as_jpg.jpg" ) );
    }

    @Test
    void testPng() {
        Assertions.assertEquals(ImageValidator.PNG_VALIDATOR.getMimeType(), this.worker( "png_as_png.png" ) );
    }

    @Test
    void testNull() {
        Assertions.assertNull( this.worker( "pdf_as_jpg.jpg" ) );
    }

}
