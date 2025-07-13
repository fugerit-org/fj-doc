package test.org.fugerit.java.doc.core.val;

import org.fugerit.java.doc.val.core.DocTypeValidationResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestDocTypeValidationResult {

    @Test
    void getValidationMessage() {
        String testMessage = "test message";
        DocTypeValidationResult result =DocTypeValidationResult.newFail().withValidationMessage( testMessage );
        Assertions.assertEquals( testMessage, result.getValidationMessage() );
    }

}
