package org.fugerit.java.doc.tool.handler;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Properties;
import java.util.function.Consumer;

import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.doc.freemarker.tool.GenerateStub;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GenerateStubHandler implements Consumer<Properties> {
	
	public static final String ARG_OUTPUT_FILE = "output";
	
	@Override
	public void accept(Properties t) {
		String output = t.getProperty( ARG_OUTPUT_FILE );
		if ( StringUtils.isEmpty( output ) ) {
			throw new ConfigRuntimeException( "Required parameter : "+ARG_OUTPUT_FILE );
		}
		log.info( "output file path : {}", output );
		try ( Writer w = new FileWriter( new File( output ) ) ) {
			GenerateStub.generate( w, t );
		} catch (Exception e) {
			throw new ConfigRuntimeException( "Error generating stub "+e, e );
		}
	} 

}
