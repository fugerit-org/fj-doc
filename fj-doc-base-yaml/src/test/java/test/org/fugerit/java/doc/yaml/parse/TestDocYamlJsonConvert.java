package test.org.fugerit.java.doc.yaml.parse;

import org.fugerit.java.doc.yaml.parse.DocJsonToYaml;
import org.fugerit.java.doc.yaml.parse.DocYamlToJson;
import org.fugerit.java.doc.yaml.parse.YamlConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.io.StringWriter;

class TestDocYamlJsonConvert {

    private static final String SAMPLE_JSON = "{\"doc\":{\"body\":{\"para\":\"Hello\"}}}";
    private static final String SAMPLE_YAML = "---\ndoc:\n  body:\n    para: Hello\n";

    @Test
    void testYamlConstantsGetDefaultMapper() {
        Assertions.assertNotNull( YamlConstants.getDefaultMapper() );
    }

    @Test
    void testDocYamlToJsonConversion() throws Exception {
        DocYamlToJson converter = new DocYamlToJson();
        StringReader reader = new StringReader( SAMPLE_YAML );
        StringWriter writer = new StringWriter();
        converter.convert( reader, writer );
        String result = writer.toString();
        Assertions.assertNotNull( result );
        Assertions.assertTrue( result.contains( "Hello" ),
                "Converted JSON should contain 'Hello', but was: " + result );
    }

    @Test
    void testDocJsonToYamlConversion() throws Exception {
        DocJsonToYaml converter = new DocJsonToYaml();
        StringReader reader = new StringReader( SAMPLE_JSON );
        StringWriter writer = new StringWriter();
        converter.convert( reader, writer );
        String result = writer.toString();
        Assertions.assertNotNull( result );
        Assertions.assertTrue( result.contains( "Hello" ),
                "Converted YAML should contain 'Hello', but was: " + result );
        Assertions.assertTrue( result.startsWith( "---" ),
                "YAML output should start with '---', but was: " + result );
    }
}
