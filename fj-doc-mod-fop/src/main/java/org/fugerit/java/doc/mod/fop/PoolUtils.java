package org.fugerit.java.doc.mod.fop;

import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.function.UnsafeSupplier;

import java.util.List;

@Slf4j
public class PoolUtils {

    private PoolUtils() {}

    public static FopConfigWrap handleFopWrap(FopConfigWrap toRelease, List<FopConfigWrap> pool, int poolMin, int poolMax, UnsafeSupplier<FopConfigWrap, ConfigException> supplier) throws ConfigException {
        FopConfigWrap res = null;
        try {
            if ( toRelease == null ) {
                if ( pool.isEmpty() ) {
                    log.info("empty pool (size:{}, min:{}/max:{}): new fop env", pool.size(), poolMin, poolMax);
                    res = supplier.get();
                } else {
                    log.info("get fop env from pool (size:{}, min:{}/max:{})", pool.size(), poolMin, poolMax);
                    return pool.remove( 0 );
                }
            } else if ( pool.size() < poolMax ) {
                log.info("release fop env to pool (size:{}, min:{}/max:{})", pool.size(), poolMin, poolMax);
                pool.add(toRelease);
            }
        } catch (Exception e) {
            log.warn( "handleFopWrap error : {} -> newFopWrap()", e.getMessage() );
            res = supplier.get();
        }
        return res;
    }

}
