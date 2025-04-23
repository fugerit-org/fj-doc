package org.fugerit.java.doc.lib.direct;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.process.DocProcessContext;
import org.fugerit.java.doc.freemarker.config.FreeMarkerConfigStep;
import org.fugerit.java.doc.freemarker.html.FreeMarkerHtmlTypeHandlerUTF8;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfig;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfigFacade;
import org.fugerit.java.doc.lib.direct.config.VenusDirectConfig;
import org.fugerit.java.doc.lib.direct.config.VenusDirectConfigChain;
import org.fugerit.java.doc.lib.direct.config.VenusDirectConfigOutput;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Reader;

public class VenusDirectFacade {

    public static final String ATT_DATA_MODEL = "dataModel";

    private VenusDirectFacade() {}

    private static final ObjectMapper YAML_MAPPER = new YAMLMapper();

    public static VenusDirectConfig readConfig( Reader reader ) {
        return SafeFunction.get( () ->  {
            VenusDirectConfig config = YAML_MAPPER.readValue( reader, VenusDirectConfig.class );
            config.setupFreemarkerDocProcessConfig();
            return config;
        } );
    }

    public static void handleAllOutput(VenusDirectConfig config) {
        SafeFunction.applyIfNotNull( config.getOutputList(), () ->
                config.getOutputList().forEach( output -> handleOutput( config, output.getOutputId() ) ) );
    }

    public static void handleOutput(VenusDirectConfig config, String outputId) {
        SafeFunction.apply( () -> {
            VenusDirectConfigOutput output = config.getOutputMap().get( outputId );
            if ( output == null ) {
                throw new ConfigRuntimeException( String.format( "Output not found : %s", outputId ) );
            }
            VenusDirectConfigChain chain = config.getChainMap().get( output.getChainId() );
            if ( chain == null ) {
                throw new ConfigRuntimeException( String.format( "Chain not found : %s", output.getChainId() ) );
            }
            DocProcessContext context = DocProcessContext.newContext();
            SafeFunction.applyIfNotNull( chain.getDataModel(), () -> context.setAttribute( ATT_DATA_MODEL, chain.getDataModel() ) );
            File outputFile = new File( output.getFile() );
            try ( FileOutputStream fos = new FileOutputStream( outputFile ) ) {
                config.getDocProcessConfig().fullProcess(chain.getChainId(), context, output.getHandlerId(), fos );
            }
        } );

    }

}
