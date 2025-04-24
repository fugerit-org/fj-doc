package org.fugerit.java.doc.lib.direct.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import lombok.Getter;
import lombok.Setter;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.io.FileIO;

import java.util.LinkedHashMap;

public class VenusDirectConfigChain {

    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    private static final ObjectMapper YAML_MAPPER = new YAMLMapper();

    @Getter @Setter
    private String chainId;

    @Getter @Setter
    private LinkedHashMap<String, Object> dataModel;

    @Getter @Setter
    private String dataModelJson;

    @Getter @Setter
    private String dataModelYaml;

    public void setupDataModel() {
        if ( this.dataModel == null ) {
            SafeFunction.applyIfNotNull( this.getDataModelJson(), () ->
                    this.setDataModel( JSON_MAPPER.readValue( FileIO.readString( this.getDataModelJson() ), LinkedHashMap.class ) ) );
            SafeFunction.applyIfNotNull( this.getDataModelYaml(), () ->
                    this.setDataModel( YAML_MAPPER.readValue( FileIO.readString( this.getDataModelYaml() ), LinkedHashMap.class ) ) );
        }
    }

}
