package test.org.fugerit.java.doc.base.xml;

import org.fugerit.java.doc.base.enums.EnumDocAlignH;
import org.fugerit.java.doc.base.enums.EnumDocAlignV;
import org.fugerit.java.doc.base.enums.EnumDocStyle;
import org.fugerit.java.doc.base.xml.DocStyleAlignHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestDocStyleAlignHelper {

    @Test
    void testParseStyleKnown() {
        int result = DocStyleAlignHelper.parseStyle( "bold", EnumDocStyle.STYLE_NORMAL.getId() );
        Assertions.assertEquals( EnumDocStyle.STYLE_BOLD.getId(), result );
    }

    @Test
    void testParseStyleUnknownUsesDefault() {
        int defaultStyle = EnumDocStyle.STYLE_NORMAL.getId();
        int result = DocStyleAlignHelper.parseStyle( "no-such-style", defaultStyle );
        Assertions.assertEquals( defaultStyle, result );
    }

    @Test
    void testGetAlignKnown() {
        int result = DocStyleAlignHelper.getAlign( "left" );
        Assertions.assertEquals( EnumDocAlignH.ALIGN_LEFT.getId(), result );
    }

    @Test
    void testGetAlignUnknownReturnsUnset() {
        int result = DocStyleAlignHelper.getAlign( "not-an-align" );
        Assertions.assertEquals( EnumDocAlignH.ALIGN_UNSET.getId(), result );
    }

    @Test
    void testGetValignKnown() {
        int result = DocStyleAlignHelper.getValign( "top" );
        Assertions.assertEquals( EnumDocAlignV.ALIGN_TOP.getId(), result );
    }

    @Test
    void testGetValignUnknownReturnsUnset() {
        int result = DocStyleAlignHelper.getValign( "not-a-valign" );
        Assertions.assertEquals( EnumDocAlignV.ALIGN_UNSET.getId(), result );
    }

    @Test
    void testAttributeNameConstants() {
        Assertions.assertEquals( "align", DocStyleAlignHelper.ATTRIBUTE_NAME_ALIGN );
        Assertions.assertEquals( "back-color", DocStyleAlignHelper.ATTRIBUTE_NAME_BACK_COLOR );
        Assertions.assertEquals( "fore-color", DocStyleAlignHelper.ATTRIBUTE_NAME_FORE_COLOR );
    }
}
