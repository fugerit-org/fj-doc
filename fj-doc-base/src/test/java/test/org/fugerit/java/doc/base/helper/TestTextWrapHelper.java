package test.org.fugerit.java.doc.base.helper;

import org.fugerit.java.doc.base.helper.TextWrapHelper;
import org.junit.Assert;
import org.junit.Test;

public class TestTextWrapHelper {

    @Test
    public void testWrap() {
        String input = "123";
        String test = "1"+TextWrapHelper.ZERO_WITH_SPACE+"2"+TextWrapHelper.ZERO_WITH_SPACE+"3"+TextWrapHelper.ZERO_WITH_SPACE;
        String out = TextWrapHelper.padZeroWithSpace( input );
        Assert.assertEquals( test, out );
    }

}
