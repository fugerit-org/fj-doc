package org.fugerit.java.doc.mod.fop.utils;

import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.doc.mod.fop.PdfFopTypeHandler;

import java.util.function.Consumer;

@Slf4j
public class ConfigUtils {

    private ConfigUtils() {}

    public static final Consumer<Boolean> LEGACY_CLASS_LOADER_MODE_CONFIG_HANDLER = error -> {
        log.warn( "Deprecated legacy class loader mode : classloader-legacy" );
        if ( error ) {
            throw new ConfigRuntimeException( "Deprecated config mode, see github fugerit-org/fj-doc repository, issue 65" );
        }
    };

}
