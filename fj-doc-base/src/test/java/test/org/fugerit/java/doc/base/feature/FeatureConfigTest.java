package test.org.fugerit.java.doc.base.feature;

import org.fugerit.java.doc.base.feature.FeatureConfig;
import org.fugerit.java.doc.base.typehelper.generic.GenericConsts;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FeatureConfigTest {

    @Test
    void isFailWhenElementNotFoundTest() {
        Assertions.assertEquals(GenericConsts.FAIL_WHEN_ELEMENT_NOT_FOUND_DEFAULT, FeatureConfig.DEFAULT.isFailWhenElementNotFound());
    }

}
