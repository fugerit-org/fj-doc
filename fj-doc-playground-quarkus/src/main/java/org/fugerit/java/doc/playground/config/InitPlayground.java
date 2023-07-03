package org.fugerit.java.doc.playground.config;

import org.fugerit.java.doc.base.config.InitHandler;
import org.fugerit.java.doc.mod.fop.PdfFopTypeHandler;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class InitPlayground {

	public static final PdfFopTypeHandler PDF_FOP_TYPE_HANDLER = new PdfFopTypeHandler();
	
	void onStart(@Observes StartupEvent ev) {        
		log.info( "InitPlayground start" );
		InitHandler.initDocAsync( PDF_FOP_TYPE_HANDLER );
		log.info( "InitPlayground end" );
	}
	
}
