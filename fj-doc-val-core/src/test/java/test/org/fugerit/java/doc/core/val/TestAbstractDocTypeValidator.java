package test.org.fugerit.java.doc.core.val;

import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.doc.val.core.DocTypeValidationResult;
import org.fugerit.java.doc.val.core.DocTypeValidator;
import org.fugerit.java.doc.val.core.basic.AbstractDocTypeValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

class TestAbstractDocTypeValidator {

    @Test
    void testValidate() throws IOException {
        DocTypeValidator validator = new AbstractDocTypeValidator( "a", "b" ) {
            @Override
            public DocTypeValidationResult validate(InputStream is) {
                this.logFailedCheck( this.getMimeType(), new ConfigRuntimeException( "scenario exception" ) );
                return null;
            }
        };
        Assertions.assertNull( validator.validate( (InputStream) null ) );
    }

}
