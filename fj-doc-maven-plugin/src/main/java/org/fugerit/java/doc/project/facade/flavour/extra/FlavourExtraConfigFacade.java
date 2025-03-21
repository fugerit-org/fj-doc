package org.fugerit.java.doc.project.facade.flavour.extra;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.fugerit.java.core.function.SafeFunction;

import java.io.InputStream;

public class FlavourExtraConfigFacade {

    private static final ObjectMapper MAPPER = new ObjectMapper( new YAMLFactory() );

    private FlavourExtraConfigFacade() {}

    public static FlavourExtraConfig readConfigWithDefault(InputStream is, FlavourExtraConfig defaultConfig) {
        return SafeFunction.get( () -> {
            if ( is != null ) {
                return MAPPER.readValue( is, FlavourExtraConfig.class );
            } else {
                return defaultConfig;
            }
        } );
    }

    public static FlavourExtraConfig readConfigBlankDefault(InputStream is) {
        return readConfigWithDefault( is, new FlavourExtraConfig() );
    }

    public static FlavourExtraConfig readConfig(InputStream is) {
        return readConfigWithDefault( is, null );
    }

}
