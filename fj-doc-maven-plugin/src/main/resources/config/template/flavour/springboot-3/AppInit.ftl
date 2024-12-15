<#import '../flavour-macro.ftl' as fhm>
package <@fhm.toProjectPackage context=context/>;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.fugerit.java.doc.base.config.InitHandler;
@Slf4j
public class AppInit implements InitializingBean {

    @Autowired
    DocHelper docHelper;

    @Override
    public void afterPropertiesSet() throws Exception {
        /*
         * This will initialize all the doc handlers using async mode.
         * (use method InitHandler.initDocAll() for synced startup)
         */
        InitHandler.initDocAllAsync(
        docHelper.getDocProcessConfig().getFacade().handlers() );
    }

}
