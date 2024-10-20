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
        SafeFunction.apply( () -> {
            GenerateKotlinConfig config = new GenerateKotlinConfig(
                    PropsIO.loadFromClassLoader( "generate-kotlin/config.properties" ),
                    parseLast());
            GenerateKotlinFacade.generate( config );
            Assertions.assertNotNull( config );
        } );
    }

}
