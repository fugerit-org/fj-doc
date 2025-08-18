package test.org.fugerit.java.doc.base.feature;

import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.util.result.Result;
import org.fugerit.java.doc.base.config.DocException;
import org.fugerit.java.doc.base.feature.DocFeatureRuntimeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class DocFeatureRuntimeExceptionTest {

    private static final String TEST_MESSAGE = "test message";

    private static final Exception DOC_FEATURE_RUNTIME_EXCEPTION =  new DocFeatureRuntimeException( TEST_MESSAGE, new IOException( "1" ), Result.RESULT_CODE_KO );

    private static final Exception DOC_EXCEPTION =  new DocException( TEST_MESSAGE );

    private static final Exception CONFIG_RUNTIME_EXCEPTION =  new ConfigRuntimeException( TEST_MESSAGE );

    @Test
    void testConstructor1() {
        Assertions.assertEquals( TEST_MESSAGE, DOC_FEATURE_RUNTIME_EXCEPTION.getMessage() );
    }

    @Test
    void standardExceptionWrapping1Test() {
        Assertions.assertThrows( DocFeatureRuntimeException.class, () -> DocFeatureRuntimeException.standardExceptionWrapping( DOC_FEATURE_RUNTIME_EXCEPTION ) );
    }

    @Test
    void convertToRuntimeEx1Test() {
        Assertions.assertEquals( TEST_MESSAGE, DocFeatureRuntimeException.convertToRuntimeEx( DOC_EXCEPTION ).getCause().getMessage() );
    }

    @Test
    void convertToRuntimeEx2Test() {
        Assertions.assertEquals( TEST_MESSAGE, DocFeatureRuntimeException.convertToRuntimeEx( CONFIG_RUNTIME_EXCEPTION ).getMessage() );
    }

    @Test
    void convertExMethod1Test() {
        Assertions.assertEquals( "Exception during testMethod : org.fugerit.java.core.cfg.ConfigRuntimeException: test message", DocFeatureRuntimeException.convertExMethod( "testMethod", CONFIG_RUNTIME_EXCEPTION ).getMessage() );
    }

    @Test
    void convertEx1Test() {
        Assertions.assertEquals( "Exception cause is : org.fugerit.java.core.cfg.ConfigRuntimeException: test message", DocFeatureRuntimeException.convertEx( CONFIG_RUNTIME_EXCEPTION ).getMessage() );
    }

}
