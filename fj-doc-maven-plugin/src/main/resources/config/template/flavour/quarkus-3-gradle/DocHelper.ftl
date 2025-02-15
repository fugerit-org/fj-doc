<#import '../flavour-macro.ftl' as fhm>
package <@fhm.toProjectPackage context=context/>;

import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfig;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfigFacade;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DocHelper {

    private FreemarkerDocProcessConfig docProcessConfig = FreemarkerDocProcessConfigFacade.loadConfigSafe( "cl://${context.resourcePathFmConfigXml}" );

    public FreemarkerDocProcessConfig getDocProcessConfig() { return this.docProcessConfig; }

}
