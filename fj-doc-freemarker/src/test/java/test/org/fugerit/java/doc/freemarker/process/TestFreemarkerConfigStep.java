package test.org.fugerit.java.doc.freemarker.process;

import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.lang.helpers.BooleanUtils;
import org.fugerit.java.core.util.filterchain.MiniFilter;
import org.fugerit.java.doc.base.process.DocProcessContext;
import org.fugerit.java.doc.base.process.DocProcessData;
import org.fugerit.java.doc.freemarker.config.FreeMarkerConfigStep;
import org.junit.Assert;
import org.junit.Test;

import java.util.Properties;

public class TestFreemarkerConfigStep {

    @Test
    public void test() throws Exception {
        Properties params = new Properties();
        FreeMarkerConfigStep step = new FreeMarkerConfigStep();
        step.setCustomConfig( params );
        DocProcessContext context = new DocProcessContext();
        DocProcessData data = new DocProcessData();
        Assert.assertThrows( ConfigRuntimeException.class, () -> step.process( context, data ) ) ;
        params.setProperty( "id", "FJ_DOC_TEST_1" );
        params.setProperty( "version", "2.3.31" );
        params.setProperty( "mode", "class" );
        Assert.assertThrows( ConfigRuntimeException.class, () -> step.process( context, data ) ) ;
        params.setProperty( "id", "FJ_DOC_TEST_2" );
        params.setProperty( "path", "/fj_doc_test/template/" );
        params.setProperty( "class", "org.fugerit.java.doc.freemarker.fun.ImageBase64CLFun" );
        params.setProperty( "exception-handler", "RETHROW_HANDLER" );
        Assert.assertEquals(MiniFilter.CONTINUE, step.process( context, data ) );
        params.setProperty( "id", "FJ_DOC_TEST_3" );
        params.setProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_MODE, FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_MODE_FOLDER );
        params.setProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_PATH, "target" );
        Assert.assertEquals(MiniFilter.CONTINUE, step.process( context, data ) );
        params.setProperty( "id", "FJ_DOC_TEST_4" );
        params.remove( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_CLASS );
        Assert.assertEquals(MiniFilter.CONTINUE, step.process( context, data ) );
        params.setProperty( "id", "FJ_DOC_TEST_5" );
        params.setProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_LOAD_BUNDLED_FUN, BooleanUtils.BOOLEAN_TRUE);
        Assert.assertEquals(MiniFilter.CONTINUE, step.process( context, data ) );
    }

}
