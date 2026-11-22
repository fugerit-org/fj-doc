package test.org.fugerit.java.doc.base.enums;

import org.fugerit.java.doc.base.enums.EnumDocAlignH;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestEnumDocAlignH {

    @Test
    void testFromValueKnown() {
        Assertions.assertEquals( EnumDocAlignH.ALIGN_LEFT, EnumDocAlignH.fromValue( "left" ) );
        Assertions.assertEquals( EnumDocAlignH.ALIGN_CENTER, EnumDocAlignH.fromValue( "center" ) );
        Assertions.assertEquals( EnumDocAlignH.ALIGN_RIGHT, EnumDocAlignH.fromValue( "right" ) );
        Assertions.assertEquals( EnumDocAlignH.ALIGN_JUSTIFY, EnumDocAlignH.fromValue( "justify" ) );
        Assertions.assertEquals( EnumDocAlignH.ALIGN_JUSTIFY_ALL, EnumDocAlignH.fromValue( "justifyall" ) );
        Assertions.assertEquals( EnumDocAlignH.ALIGN_UNSET, EnumDocAlignH.fromValue( "unset" ) );
    }

    @Test
    void testFromValueUnknownReturnsNull() {
        Assertions.assertNull( EnumDocAlignH.fromValue( "unknown-value" ) );
    }

    @Test
    void testIdFromValueWithDefaultKnown() {
        Assertions.assertEquals( EnumDocAlignH.ALIGN_LEFT.getId(),
                EnumDocAlignH.idFromValueWithDefault( "left", -99 ) );
    }

    @Test
    void testIdFromValueWithDefaultFallback() {
        int defaultId = EnumDocAlignH.ALIGN_UNSET.getId();
        Assertions.assertEquals( defaultId,
                EnumDocAlignH.idFromValueWithDefault( "not-a-value", defaultId ) );
    }

    @Test
    void testGetId() {
        Assertions.assertEquals( 1, EnumDocAlignH.ALIGN_LEFT.getId() );
        Assertions.assertEquals( 2, EnumDocAlignH.ALIGN_CENTER.getId() );
        Assertions.assertEquals( 3, EnumDocAlignH.ALIGN_RIGHT.getId() );
        Assertions.assertEquals( 9, EnumDocAlignH.ALIGN_JUSTIFY.getId() );
        Assertions.assertEquals( 8, EnumDocAlignH.ALIGN_JUSTIFY_ALL.getId() );
        Assertions.assertEquals( -1, EnumDocAlignH.ALIGN_UNSET.getId() );
    }

    @Test
    void testGetValue() {
        Assertions.assertEquals( "left", EnumDocAlignH.ALIGN_LEFT.getValue() );
        Assertions.assertEquals( "unset", EnumDocAlignH.ALIGN_UNSET.getValue() );
    }
}
