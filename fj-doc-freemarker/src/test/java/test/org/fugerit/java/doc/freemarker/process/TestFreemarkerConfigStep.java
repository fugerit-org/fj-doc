package test.org.fugerit.java.doc.freemarker.process;

import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.lang.helpers.BooleanUtils;
import org.fugerit.java.core.util.filterchain.MiniFilter;
import org.fugerit.java.doc.base.process.DocProcessContext;
import org.fugerit.java.doc.base.process.DocProcessData;
import org.fugerit.java.doc.freemarker.config.FreeMarkerConfigStep;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Properties;

class TestFreemarkerConfigStep {

    @Test
    void test() throws Exception {
        Properties params = new Properties();
        FreeMarkerConfigStep step = new FreeMarkerConfigStep();
        step.setCustomConfig( params );
        DocProcessContext context = new DocProcessContext();
        DocProcessData data = new DocProcessData();
        Assertions.assertThrows( ConfigRuntimeException.class, () -> step.process( context, data ) ) ;
        params.setProperty( "id", "FJ_DOC_TEST_1" );
        params.setProperty( "version", "2.3.31" );
        params.setProperty( "mode", "class" );
        Assertions.assertThrows( ConfigRuntimeException.class, () -> step.process( context, data ) ) ;
        params.setProperty( "id", "FJ_DOC_TEST_2" );
        params.setProperty( "path", "/fj_doc_test/template/" );
        params.setProperty( "class", "org.fugerit.java.doc.freemarker.fun.ImageBase64CLFun" );
        params.setProperty( "exception-handler", "RETHROW_HANDLER" );
        Assertions.assertEquals(MiniFilter.CONTINUE, step.process( context, data ) );
        params.setProperty( "id", "FJ_DOC_TEST_3" );
        params.setProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_MODE, FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_MODE_FOLDER );
        params.setProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_PATH, "target" );
        Assertions.assertEquals(MiniFilter.CONTINUE, step.process( context, data ) );
        params.setProperty( "id", "FJ_DOC_TEST_4" );
        params.remove( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_CLASS );
        Assertions.assertEquals(MiniFilter.CONTINUE, step.process( context, data ) );
        params.setProperty( "id", "FJ_DOC_TEST_5" );
        params.setProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_LOAD_BUNDLED_FUN, BooleanUtils.BOOLEAN_TRUE);
        Assertions.assertEquals(MiniFilter.CONTINUE, step.process( context, data ) );
    }

}
