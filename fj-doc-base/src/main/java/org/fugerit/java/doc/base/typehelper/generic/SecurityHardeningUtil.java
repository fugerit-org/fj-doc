package org.fugerit.java.doc.base.typehelper.generic;

import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.doc.base.model.DocBase;

@Slf4j
public class SecurityHardeningUtil {

    private SecurityHardeningUtil() {}

    private static String resolveSecurityHardeningInfo( String securityHardeningInfo ) {
        if ( GenericConsts.SECURITY_HARDENING_MAX.equalsIgnoreCase( securityHardeningInfo ) ) {
            return GenericConsts.SECURITY_HARDENING_CURRENT_MAX;
        } else {
            return securityHardeningInfo;
        }
    }

    public static int findCurrentValue( DocBase docBase ) {
        return findCurrentValue( docBase.getStableInfo().getProperty(GenericConsts.SECURITY_HARDENING, GenericConsts.SECURITY_HARDENING_DEFAULT) );
    }

    public static int findCurrentValue( String securityHardeningInfo ) {
        String securityHardening = resolveSecurityHardeningInfo( securityHardeningInfo );
        log.debug( "securityHardening : {}", securityHardening );
        try {
            return Integer.parseInt(securityHardening);
        } catch (NumberFormatException nfe) {
            log.warn( "failed to check option {} - {}", GenericConsts.SECURITY_HARDENING, nfe.getMessage() );
        }
        return SecurityHardeningConsts.SECURITY_HARDENING_DISABLED;
    }

}
