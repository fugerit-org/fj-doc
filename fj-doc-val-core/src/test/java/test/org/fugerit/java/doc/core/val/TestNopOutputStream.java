package test.org.fugerit.java.doc.core.val;

import org.fugerit.java.doc.val.core.io.NopOutputStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TestNopOutputStream {

    @Test
    public void test1() throws IOException {
        try ( NopOutputStream os = new NopOutputStream() ) {
            os.write( 1 );
            byte[] data = "test".getBytes();
            os.write( data );
            os.write( data, 0, data.length );
            Assertions.assertNotNull( os );
        }
    }

}
