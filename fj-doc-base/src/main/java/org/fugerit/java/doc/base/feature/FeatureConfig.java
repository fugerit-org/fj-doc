package org.fugerit.java.doc.base.feature;


import org.fugerit.java.doc.base.feature.tableintegritycheck.TableIntegrityCheckConstants;
import org.fugerit.java.doc.base.typehelper.generic.GenericConsts;

public interface FeatureConfig {

    static FeatureConfig fromFailWhenElementNotFound( boolean failWhenElementNotFound ) {
        return new FeatureConfig() {
            @Override
            public boolean isFailWhenElementNotFound() {
                return failWhenElementNotFound;
            }
        };
    }

    static final FeatureConfig DEFAULT = new FeatureConfig() {};

    default String getTableCheckIntegrity() {
        return TableIntegrityCheckConstants.TABLE_INTEGRITY_CHECK_DEFAULT;
    }

    default boolean isFailWhenElementNotFound() {
        return GenericConsts.FAIL_WHEN_ELEMENT_NOT_FOUND_DEFAULT;
    }

}
