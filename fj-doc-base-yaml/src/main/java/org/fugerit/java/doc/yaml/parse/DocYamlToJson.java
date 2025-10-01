package org.fugerit.java.doc.yaml.parse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.doc.base.parser.DocConvert;

import java.io.Reader;
import java.io.Writer;

public class DocYamlToJson implements DocConvert {

    protected static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    protected static final ObjectMapper YAML_MAPPER = new YAMLMapper();

    private ObjectMapper mapperFrom;

    private ObjectMapper mapperTo;

    public DocYamlToJson() {
        this( YAML_MAPPER, JSON_MAPPER );
    }

    public DocYamlToJson(ObjectMapper mapperFrom, ObjectMapper mapperTo) {
        this.mapperFrom = mapperFrom;
        this.mapperTo = mapperTo;
    }

    @Override
    public void convert(Reader from, Writer to) throws ConfigException {
        ConfigException.apply( () -> {
            JsonNode node = this.mapperFrom.readTree( from );
            this.mapperTo.writerWithDefaultPrettyPrinter().writeValue(to, node);
        } );
    }

}
