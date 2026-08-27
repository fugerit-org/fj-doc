package test.org.fugerit.java.doc.base.parser;

import org.fugerit.java.doc.base.parser.DocParserUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Properties;

class TestDocParserUtil {

    @Test
    void testDoubleNestedPrimaryKeyPresent() {
        Properties atts = new Properties();
        atts.setProperty( "key1", "value1" );
        atts.setProperty( "key2", "value2" );
        String result = DocParserUtil.doubleNested( atts, "key1", "key2" );
        Assertions.assertEquals( "value1", result );
    }

    @Test
    void testDoubleNestedFallsBackToKey2() {
        Properties atts = new Properties();
        atts.setProperty( "key2", "value2" );
        String result = DocParserUtil.doubleNested( atts, "key1", "key2" );
        Assertions.assertEquals( "value2", result );
    }

    @Test
    void testDoubleNestedBothAbsentReturnsNull() {
        Properties atts = new Properties();
        String result = DocParserUtil.doubleNested( atts, "key1", "key2" );
        Assertions.assertNull( result );
    }

    @Test
    void testDoubleNestedWithDefaultIntPrimaryKeyPresent() {
        Properties atts = new Properties();
        atts.setProperty( "key1", "42" );
        atts.setProperty( "key2", "7" );
        int result = DocParserUtil.doubleNestedWithDefaultInt( atts, "key1", "key2", "0" );
        Assertions.assertEquals( 42, result );
    }

    @Test
    void testDoubleNestedWithDefaultIntFallsBackToKey2() {
        Properties atts = new Properties();
        atts.setProperty( "key2", "7" );
        int result = DocParserUtil.doubleNestedWithDefaultInt( atts, "key1", "key2", "0" );
        Assertions.assertEquals( 7, result );
    }

    @Test
    void testDoubleNestedWithDefaultIntFallsBackToDefault() {
        Properties atts = new Properties();
        int result = DocParserUtil.doubleNestedWithDefaultInt( atts, "key1", "key2", "99" );
        Assertions.assertEquals( 99, result );
    }
}
