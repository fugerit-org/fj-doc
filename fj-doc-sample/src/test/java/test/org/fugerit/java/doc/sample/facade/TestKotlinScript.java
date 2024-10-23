package test.org.fugerit.java.doc.sample.facade;

import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.process.DocProcessContext;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;

public class TestKotlinScript extends BasicFacadeTest {

    @Test
    @Override
    public void produce() throws Exception {
        File baseFile = new File(BASIC_OUTPUT_PATH, "kotlin-test");
        if (!baseFile.exists()) {
            logger.info("Create base path : {} ({})", baseFile.mkdirs(), baseFile.getCanonicalPath());
        }
        String chaindId = "kotlin-01";
        String[] handlerIds = {DocConfig.TYPE_HTML, DocConfig.TYPE_PDF, DocConfig.TYPE_MD};
        DocProcessContext context = DocProcessContext.newContext();
        for (String handleID : handlerIds) {
            try (FileOutputStream os = new FileOutputStream(new File(baseFile, chaindId+"."+handleID))) {
                PROCESSCONFIG.fullProcess( chaindId, context, handleID, os );
            }
        }
        Assert.assertTrue(baseFile.exists());
    }
}
