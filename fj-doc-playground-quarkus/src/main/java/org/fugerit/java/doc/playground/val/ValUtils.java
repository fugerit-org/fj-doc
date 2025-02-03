package org.fugerit.java.doc.playground.val;

import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.function.UnsafeSupplier;
import org.fugerit.java.core.io.FileIO;
import org.fugerit.java.core.io.helper.HelperIOException;

import java.io.File;
import java.io.IOException;

@Slf4j
public class ValUtils {

    private ValUtils() {
    }

    public static Response doIfInTmpFolder(File tempFile, UnsafeSupplier<Response, Exception> fun) throws IOException {
        Response res = null;
        if (FileIO.isInTmpFolder(tempFile)) {
            res = HelperIOException.get(fun::get);
        } else {
            res = Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return res;
    }

}
