package test.org.fugerit.java.doc.freemarker.process;

import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.doc.base.process.DocProcessContext;
import org.fugerit.java.doc.base.process.DocProcessData;
import org.fugerit.java.doc.freemarker.config.FreeMarkerComplexProcessStep;
import org.fugerit.java.doc.freemarker.config.FreeMarkerConstants;
import org.fugerit.java.doc.freemarker.config.FreeMarkerKotlinStep;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Properties;

@Slf4j
public class TestFreemarkerKotlinStep {

    private static final String KTS_PATH_SAMPLE = "coverage/kts/sample-2-coverage.kts";

    @Test
    public void testKotlinStep() throws Exception {
        DocProcessContext context = DocProcessContext.newContext(FreeMarkerConstants.ATT_FREEMARKER_MAP, new HashMap<>() );
        DocProcessData data = new DocProcessData();
        // fail no parser
        FreeMarkerKotlinStep stepFailNoParser = new FreeMarkerKotlinStep( -1 );
        Assert.assertThrows( ConfigRuntimeException.class, () -> stepFailNoParser.process(context, data ) );
        // fail no kts
        FreeMarkerKotlinStep stepFailNoKtsPath = new FreeMarkerKotlinStep();
        stepFailNoKtsPath.setCustomConfig( new Properties() );
        Assert.assertThrows( ConfigRuntimeException.class, () -> stepFailNoKtsPath.process(context, data ) );
        // test ok
        FreeMarkerKotlinStep stepOk = new FreeMarkerKotlinStep();
        Properties customConfig = new Properties();
        // test using template-path
        customConfig.setProperty( FreeMarkerComplexProcessStep.ATT_TEMPLATE_PATH, KTS_PATH_SAMPLE );
        stepOk.setCustomConfig( customConfig );
        log.info( "customConfig >> : {}", stepOk.getCustomConfig() );
        stepOk.process( context, data );
        Assert.assertNotNull( data.getCurrentXmlData() );
        // test using kts-path
        customConfig.setProperty( FreeMarkerKotlinStep.ATT_KTS_PATH, KTS_PATH_SAMPLE );
        stepOk.process( context, data );
        Assert.assertNotNull( data.getCurrentXmlData() );
    }

}
