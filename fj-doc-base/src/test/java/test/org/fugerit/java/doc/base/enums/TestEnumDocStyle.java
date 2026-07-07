package test.org.fugerit.java.doc.base.enums;

import org.fugerit.java.doc.base.enums.EnumDocStyle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestEnumDocStyle {

    @Test
    void testFromValueKnown() {
        Assertions.assertEquals( EnumDocStyle.STYLE_NORMAL, EnumDocStyle.fromValue( "normal" ) );
        Assertions.assertEquals( EnumDocStyle.STYLE_BOLD, EnumDocStyle.fromValue( "bold" ) );
        Assertions.assertEquals( EnumDocStyle.STYLE_UNDERLINE, EnumDocStyle.fromValue( "underline" ) );
        Assertions.assertEquals( EnumDocStyle.STYLE_ITALIC, EnumDocStyle.fromValue( "italic" ) );
        Assertions.assertEquals( EnumDocStyle.STYLE_BOLDITALIC, EnumDocStyle.fromValue( "bolditalic" ) );
        Assertions.assertEquals( EnumDocStyle.STYLE_UNSET, EnumDocStyle.fromValue( "unset" ) );
    }

    @Test
    void testFromValueUnknownReturnsNull() {
        Assertions.assertNull( EnumDocStyle.fromValue( "no-such-style" ) );
    }

    @Test
    void testIdFromValueWithDefaultKnown() {
        Assertions.assertEquals( EnumDocStyle.STYLE_BOLD.getId(),
                EnumDocStyle.idFromValueWithDefault( "bold", -99 ) );
    }

    @Test
    void testIdFromValueWithDefaultFallback() {
        int defaultId = EnumDocStyle.STYLE_UNSET.getId();
        Assertions.assertEquals( defaultId,
                EnumDocStyle.idFromValueWithDefault( "not-a-style", defaultId ) );
    }

    @Test
    void testGetId() {
        Assertions.assertEquals( 1, EnumDocStyle.STYLE_NORMAL.getId() );
        Assertions.assertEquals( 2, EnumDocStyle.STYLE_BOLD.getId() );
        Assertions.assertEquals( 3, EnumDocStyle.STYLE_UNDERLINE.getId() );
        Assertions.assertEquals( 4, EnumDocStyle.STYLE_ITALIC.getId() );
        Assertions.assertEquals( 5, EnumDocStyle.STYLE_BOLDITALIC.getId() );
        Assertions.assertEquals( -1, EnumDocStyle.STYLE_UNSET.getId() );
    }

    @Test
    void testGetValue() {
        Assertions.assertEquals( "bold", EnumDocStyle.STYLE_BOLD.getValue() );
        Assertions.assertEquals( "unset", EnumDocStyle.STYLE_UNSET.getValue() );
    }
}
