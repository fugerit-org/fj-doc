package org.fugerit.java.doc.yaml.parse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.doc.base.parser.DocConvert;
import org.fugerit.java.doc.json.parse.JsonConstants;

import java.io.Reader;
import java.io.Writer;

public class DocYamlToJson implements DocConvert {

    private ObjectMapper mapperFrom;

    private ObjectMapper mapperTo;

    public DocYamlToJson() {
        this( YamlConstants.getDefaultMapper(), JsonConstants.getDefaultMapper() );
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
