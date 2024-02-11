package org.fugerit.java.doc.playground.val;

import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.function.UnsafeSupplier;
import org.fugerit.java.core.io.helper.HelperIOException;
import org.fugerit.java.doc.val.core.DocTypeValidationResult;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.function.Supplier;

@Slf4j
public class ValUtils {

    private ValUtils() {}

    public static boolean isInTmpFolder( File tempFile ) throws IOException {
        File tempDir = new File( System.getProperty( "java.io.tmpdir" ) );
        log.info( "file -> {} (tmpdir : {})", tempFile, tempDir );
        return tempFile.getCanonicalPath().startsWith(tempDir.getCanonicalPath() );
    }

    public static Response doIfInTmpFolder(File tempFile,  UnsafeSupplier<Response, Exception> fun ) throws IOException {
        Response res = null;
        if ( isInTmpFolder( tempFile ) ) {
            res = HelperIOException.get( fun::get );
        } else {
            // no access
            res = Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return res;
    }

}
