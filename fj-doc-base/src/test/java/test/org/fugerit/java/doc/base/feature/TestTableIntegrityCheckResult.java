package test.org.fugerit.java.doc.base.feature;

import org.fugerit.java.core.util.result.Result;
import org.fugerit.java.doc.base.feature.tableintegritycheck.TableIntegrityCheckResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestTableIntegrityCheckResult {

    @Test
    void testResultCodeOk() {
        TableIntegrityCheckResult result = new TableIntegrityCheckResult( Result.RESULT_CODE_OK );
        Assertions.assertEquals( Result.RESULT_CODE_OK, result.getResultCode() );
    }

    @Test
    void testResultCodeKo() {
        TableIntegrityCheckResult result = new TableIntegrityCheckResult( Result.RESULT_CODE_KO );
        Assertions.assertEquals( Result.RESULT_CODE_KO, result.getResultCode() );
    }

    @Test
    void testMessagesEmptyByDefault() {
        TableIntegrityCheckResult result = new TableIntegrityCheckResult( Result.RESULT_CODE_OK );
        Assertions.assertNotNull( result.getMessages() );
        Assertions.assertTrue( result.getMessages().isEmpty() );
    }

    @Test
    void testAddMessages() {
        TableIntegrityCheckResult result = new TableIntegrityCheckResult( Result.RESULT_CODE_KO );
        result.getMessages().add( "Column count mismatch in row 2" );
        result.getMessages().add( "Row span overflow" );
        Assertions.assertEquals( 2, result.getMessages().size() );
        Assertions.assertEquals( "Column count mismatch in row 2", result.getMessages().get( 0 ) );
    }

    @Test
    void testSetMessages() {
        TableIntegrityCheckResult result = new TableIntegrityCheckResult( Result.RESULT_CODE_OK );
        java.util.List<String> msgs = new java.util.ArrayList<>();
        msgs.add( "warning" );
        result.setMessages( msgs );
        Assertions.assertSame( msgs, result.getMessages() );
    }
}
