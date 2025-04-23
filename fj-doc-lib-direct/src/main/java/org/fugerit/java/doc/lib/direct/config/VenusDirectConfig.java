package org.fugerit.java.doc.lib.direct.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfig;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfigFacade;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class VenusDirectConfig {

    @Getter
    private FreemarkerDocProcessConfig docProcessConfig;

    @Getter
    private Map<String, VenusDirectConfigChain> chainMap;

    @Getter
    private Map<String, VenusDirectConfigOutput> outputMap;

    public void setupFreemarkerDocProcessConfig() throws ConfigException {
        log.info( "configId: {}, init", this.getConfigId() );
        log.info( "templatePath: {}, templateMode: {}", this.getTemplatePath(), this.getTemplateMode() );
        this.docProcessConfig = FreemarkerDocProcessConfigFacade.newSimpleConfigMode(
                this.getConfigId(), this.getTemplatePath(), this.getTemplateMode() );
        // config handlers
        SafeFunction.applyIfNotNull( this.getHandlerList(), () -> {
            for ( VenusDirectConfigHandler handler : this.getHandlerList() ) {
                String handlerType = handler.getType();
                log.info( "configId: {}, handlerType: {}", this.getConfigId(), handlerType );
                ConfigException.apply( () ->
                        this.docProcessConfig.getFacade().registerHandler(
                                (DocTypeHandler) ClassHelper.newInstance( handlerType ) ) );
            }
        } );
        // config chain
        SafeFunction.applyIfNotNull( this.getChainList(),
                () -> this.chainMap = this.getChainList().stream().collect( Collectors.toMap( chain -> chain.getChainId(), chain -> chain ) ) );
        log.info( "chainMap ids: {}", this.chainMap.keySet() );
        // config output
        SafeFunction.applyIfNotNull( this.getOutputList(),
                () -> this.outputMap = this.getOutputList().stream().collect( Collectors.toMap( output -> output.getOutputId(), output -> output ) ) );
        log.info( "outputMap ids: {}", this.outputMap.keySet() );
    }

    @Getter @Setter
    private String configId;

    @Getter @Setter
    private String templatePath;

    @Getter @Setter
    private String templateMode;

    @Getter @Setter
    private List<VenusDirectConfigHandler> handlerList;

    @Getter @Setter
    private List<VenusDirectConfigChain> chainList;

    @Getter @Setter
    private List<VenusDirectConfigOutput> outputList;


}
