package test.org.fugerit.java.doc.base.kotlin.model;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.util.PropsIO;
import org.fugerit.java.doc.base.kotlin.gen.GenerateKotlinConfig;
import org.fugerit.java.doc.base.kotlin.gen.GenerateKotlinFacade;
import org.fugerit.java.doc.lib.autodoc.facade.XsdParserFacade;
import org.fugerit.java.doc.lib.autodoc.parser.model.AutodocModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Properties;

class GenerateKotlinTest {

    static AutodocModel parseLast() throws ConfigException {
        String path = "../fj-doc-base/src/main/resources/config/doc-2-1.xsd";
        XsdParserFacade xsdParserFacade = new XsdParserFacade();
        AutodocModel autodocModel = xsdParserFacade.parse(path);
        autodocModel.setVersion("2.1.0");
        autodocModel.setTitle("Reference xsd documentation for Venus - Fugerit Document Generation Framework (fj-doc)");
        autodocModel.setXsdPrefix("xsd:");
        autodocModel.setAutodocPrefix("doc:");
        return autodocModel;
    }

    @Test
    void testGeneration() {
        Assertions.assertNotNull( SafeFunction.get( () -> {
            Properties props = PropsIO.loadFromClassLoader( "generate-kotlin/config.properties" );
            GenerateKotlinConfig config = new GenerateKotlinConfig(
                    props,
                    parseLast());
            GenerateKotlinFacade.generate( config );
            Assertions.assertNotNull( config );
            // additional tests
            Assertions.assertNotNull( config.toCheckTypeFun( "test" ) );
            props.setProperty( "source-output-folder", "target/gen-test" );
            GenerateKotlinFacade.generate( config );
            return config;
        } ) );
    }

    @Test
    void testConfig() {
        Properties props = new Properties();
        AutodocModel autodocModel = new AutodocModel( null );
        Assertions.assertThrows( NullPointerException.class, () -> new GenerateKotlinConfig( null, null ) );
        Assertions.assertThrows( NullPointerException.class, () -> new GenerateKotlinConfig( props, null ) );
        Assertions.assertThrows( NullPointerException.class, () -> new GenerateKotlinConfig( null, autodocModel ) );
    }

}
