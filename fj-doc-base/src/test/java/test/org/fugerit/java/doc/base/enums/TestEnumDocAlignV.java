package test.org.fugerit.java.doc.base.enums;

import org.fugerit.java.doc.base.enums.EnumDocAlignV;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestEnumDocAlignV {

    @Test
    void testFromValueKnown() {
        Assertions.assertEquals( EnumDocAlignV.ALIGN_TOP, EnumDocAlignV.fromValue( "top" ) );
        Assertions.assertEquals( EnumDocAlignV.ALIGN_MIDDLE, EnumDocAlignV.fromValue( "middle" ) );
        Assertions.assertEquals( EnumDocAlignV.ALIGN_BOTTOM, EnumDocAlignV.fromValue( "bottom" ) );
        Assertions.assertEquals( EnumDocAlignV.ALIGN_UNSET, EnumDocAlignV.fromValue( "unset" ) );
    }

    @Test
    void testFromValueUnknownReturnsNull() {
        Assertions.assertNull( EnumDocAlignV.fromValue( "no-such-align" ) );
    }

    @Test
    void testIdFromValueWithDefaultKnown() {
        Assertions.assertEquals( EnumDocAlignV.ALIGN_TOP.getId(),
                EnumDocAlignV.idFromValueWithDefault( "top", -99 ) );
    }

    @Test
    void testIdFromValueWithDefaultFallback() {
        int defaultId = EnumDocAlignV.ALIGN_UNSET.getId();
        Assertions.assertEquals( defaultId,
                EnumDocAlignV.idFromValueWithDefault( "not-a-value", defaultId ) );
    }

    @Test
    void testGetId() {
        Assertions.assertEquals( 4, EnumDocAlignV.ALIGN_TOP.getId() );
        Assertions.assertEquals( 5, EnumDocAlignV.ALIGN_MIDDLE.getId() );
        Assertions.assertEquals( 6, EnumDocAlignV.ALIGN_BOTTOM.getId() );
        Assertions.assertEquals( -1, EnumDocAlignV.ALIGN_UNSET.getId() );
    }

    @Test
    void testGetValue() {
        Assertions.assertEquals( "middle", EnumDocAlignV.ALIGN_MIDDLE.getValue() );
        Assertions.assertEquals( "unset", EnumDocAlignV.ALIGN_UNSET.getValue() );
    }
}
