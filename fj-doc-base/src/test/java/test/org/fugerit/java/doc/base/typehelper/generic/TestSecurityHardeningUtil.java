package test.org.fugerit.java.doc.base.typehelper.generic;

import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.typehelper.generic.GenericConsts;
import org.fugerit.java.doc.base.typehelper.generic.SecurityHardeningConsts;
import org.fugerit.java.doc.base.typehelper.generic.SecurityHardeningUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestSecurityHardeningUtil {

    @Test
    void testDefault() {
        Assertions.assertEquals( SecurityHardeningConsts.SECURITY_HARDENING_DISABLED, SecurityHardeningUtil.findCurrentValue( (String)null ) );
    }

    @Test
    void testDisabled() {
        Assertions.assertEquals( SecurityHardeningConsts.SECURITY_HARDENING_DISABLED, SecurityHardeningUtil.findCurrentValue(GenericConsts.SECURITY_HARDENING_DISABLED) );
    }

    @Test
    void testSH1() {
        Assertions.assertEquals( SecurityHardeningConsts.SECURITY_HARDENING_1, SecurityHardeningUtil.findCurrentValue(GenericConsts.SECURITY_HARDENING_1) );
    }

    @Test
    void testSHMax() {
        Assertions.assertEquals( SecurityHardeningConsts.SECURITY_HARDENING_1, SecurityHardeningUtil.findCurrentValue(GenericConsts.SECURITY_HARDENING_MAX) );
    }

    @Test
    void testSHNumberFormatExceptionSetToDefault() {
        Assertions.assertEquals( SecurityHardeningConsts.SECURITY_HARDENING_DISABLED, SecurityHardeningUtil.findCurrentValue( "not-a-number" ) );
    }

    @Test
    void testDocBase() {
        DocBase docBase = new DocBase();
        docBase.getStableInfoSafe().setProperty( GenericConsts.SECURITY_HARDENING, GenericConsts.SECURITY_HARDENING_1);
        Assertions.assertEquals( SecurityHardeningConsts.SECURITY_HARDENING_1, SecurityHardeningUtil.findCurrentValue( docBase ) );
    }

}
