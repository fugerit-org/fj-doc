package test.org.fugerit.java.doc.freemarker.process;

import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.doc.base.kotlin.parse.DocKotlinParser;
import org.fugerit.java.doc.base.process.DocProcessContext;
import org.fugerit.java.doc.base.process.DocProcessData;
import org.fugerit.java.doc.freemarker.config.FreeMarkerComplexProcessStep;
import org.fugerit.java.doc.freemarker.config.FreeMarkerConstants;
import org.fugerit.java.doc.freemarker.config.FreeMarkerKotlinStep;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

@Slf4j
class TestFreemarkerKotlinStep {

    private static final String KTS_PATH_SAMPLE = "coverage/kts/document-kotlin.kts";

    @Test
    void testKotlinStep() throws Exception {
        DocProcessContext context = DocProcessContext.newContext(FreeMarkerConstants.ATT_FREEMARKER_MAP, new HashMap<>() );
        DocProcessData data = new DocProcessData();
        // fail no parser
        FreeMarkerKotlinStep stepFailNoParser = new FreeMarkerKotlinStep( -1 );
        stepFailNoParser.setCustomConfig( new Properties() );
        Assertions.assertThrows( ConfigRuntimeException.class, () -> stepFailNoParser.process(context, data ) );
        // fail no kts
        FreeMarkerKotlinStep stepFailNoKtsPath = new FreeMarkerKotlinStep();
        stepFailNoKtsPath.setCustomConfig( new Properties() );
        Assertions.assertThrows( ConfigRuntimeException.class, () -> stepFailNoKtsPath.process(context, data ) );
        // test ok
        FreeMarkerKotlinStep stepOk = new FreeMarkerKotlinStep();
        Properties customConfig = new Properties();
        // test using template-path
        customConfig.setProperty( FreeMarkerComplexProcessStep.ATT_TEMPLATE_PATH, KTS_PATH_SAMPLE );
        stepOk.setCustomConfig( customConfig );
        List<People> listPeople = Arrays.asList(new People("Luthien", "Tinuviel", "Queen"), new People("Thorin", "Oakshield", "King"));
        stepOk.getCustomConfig().setProperty( FreeMarkerComplexProcessStep.ATT_MAP_ATTS, "listPeople" );
        log.info( "customConfig >> : {}", stepOk.getCustomConfig() );
        stepOk.process( context.withAtt( "listPeople", listPeople ), data );
        Assertions.assertNotNull( data.getCurrentXmlData() );
        // test using kts-path
        customConfig.setProperty( FreeMarkerKotlinStep.ATT_KTS_PATH, KTS_PATH_SAMPLE );
        stepOk.process( context, data );
        Assertions.assertNotNull( data.getCurrentXmlData() );
        // convert test
        Assertions.assertThrows( ConfigRuntimeException.class, () -> FreeMarkerKotlinStep.convertOrExceptiopn( stepOk ) );
        Assertions.assertNotNull( FreeMarkerKotlinStep.convertOrExceptiopn( new DocKotlinParser() ) );
    }

}

