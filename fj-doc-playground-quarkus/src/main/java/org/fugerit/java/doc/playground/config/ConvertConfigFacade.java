package org.fugerit.java.doc.playground.config;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.doc.freemarker.tool.GenerateStub;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@Slf4j
@ApplicationScoped
public class ConvertConfigFacade {

    public String convertHelper(ConvertConfigInput input) throws Exception {
        String result = null;
        if (input.getDocContent() != null && input.getFreemarkerJsonData() != null) {
            try (StringReader paramsReader = new StringReader(input.getFreemarkerJsonData());
                    InputStream reader = new ByteArrayInputStream(input.getDocContent().getBytes(StandardCharsets.UTF_8));
                    Writer writer = new StringWriter()) {
                Properties params = new Properties();
                params.load(paramsReader);
                GenerateStub.generate(writer, params, reader);
                result = writer.toString();
            }
        }
        return result;
    }

}
