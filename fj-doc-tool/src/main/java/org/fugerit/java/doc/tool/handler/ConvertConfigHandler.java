package org.fugerit.java.doc.tool.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.Writer;
import java.util.Properties;
import java.util.function.Consumer;

import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.doc.freemarker.tool.ConvertConfig;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConvertConfigHandler implements Consumer<Properties> {
	
	public static final String ARG_INPUT_FILE = "input";
	
	public static final String ARG_OUTPUT_FILE = "output";
	
	@Override
	public void accept(Properties t) {
		String input = t.getProperty( ARG_INPUT_FILE );
		String output = t.getProperty( ARG_OUTPUT_FILE );
		if ( StringUtils.isEmpty( input ) ) {
			throw new ConfigRuntimeException( "Required parameter : "+ARG_INPUT_FILE );
		}
		if ( StringUtils.isEmpty( output ) ) {
			throw new ConfigRuntimeException( "Required parameter : "+ARG_OUTPUT_FILE );
		}
		log.info( "input {} / {} output file path", input, output );
		try ( InputStream is = new FileInputStream( new File( input ) );
				Writer w = new FileWriter( new File( output ) ) ) {
			ConvertConfig.generate(is, w, t);
		} catch (Exception e) {
			throw new ConfigRuntimeException( "Error generating stub "+e, e );
		}
	} 

}
