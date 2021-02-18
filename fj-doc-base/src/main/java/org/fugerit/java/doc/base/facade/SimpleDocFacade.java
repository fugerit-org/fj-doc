package org.fugerit.java.doc.base.facade;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;

import org.fugerit.java.core.io.helper.StreamHelper;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;

public class SimpleDocFacade {

	public static void produce( DocTypeHandler th, Reader xml, OutputStream dest ) throws Exception {
		DocInput input = DocInput.newInput( th.getType() , xml );
		DocOutput output = DocOutput.newOutput( dest );
		th.handle( input, output );
	}
	
	public static void produce( DocTypeHandler th, String xmlPath, File destFile ) throws Exception {
		try ( Reader reader = new InputStreamReader( StreamHelper.resolveStream( xmlPath ) );
				FileOutputStream dest = new FileOutputStream( destFile ) ) {
			produce(th, reader, dest);
		}
	}
	
}
