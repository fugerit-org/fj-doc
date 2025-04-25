package org.fugerit.java.doc.playground;

import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.playground.doc.GenerateFacade;
import org.fugerit.java.doc.playground.doc.GenerateInput;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestGenerateFacade {

    private GenerateFacade facade = new GenerateFacade();

    @Test
    void testFacadeCsv() {
        GenerateInput input = new GenerateInput();
        input.setOutputFormat(DocConfig.TYPE_CSV);
        Assertions.assertNotNull(facade.findHandler(input));
    }

}
