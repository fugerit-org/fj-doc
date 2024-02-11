package org.fugerit.java.doc.playground;

import jakarta.ws.rs.core.Response;
import org.fugerit.java.doc.playground.val.ValUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class ValUtilsTest {

    @Test
    public void testDoIfInTmpFolder401() throws IOException  {
        Response response = ValUtils.doIfInTmpFolder(new File("/"), () -> null);
        Assertions.assertEquals( Response.Status.UNAUTHORIZED.getStatusCode(),  response.getStatus() );
    }

    @Test
    public void testDoIfInTmpFolder200() throws IOException  {
        Response response = ValUtils.doIfInTmpFolder( new File( System.getProperty( "java.io.tmpdir" ), "test.txt" ), () -> null);
        Assertions.assertNull( response );
    }

}
