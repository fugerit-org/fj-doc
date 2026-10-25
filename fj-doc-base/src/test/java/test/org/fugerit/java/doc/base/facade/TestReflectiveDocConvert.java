package test.org.fugerit.java.doc.base.facade;

import org.fugerit.java.doc.base.facade.ReflectiveDocConvert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.io.StringWriter;

class TestReflectiveDocConvert {

    @Test
    void testConvertCopiesContent() throws Exception {
        String content = "<doc><body><para>Hello world</para></body></doc>";
        StringReader reader = new StringReader( content );
        StringWriter writer = new StringWriter();
        ReflectiveDocConvert convert = new ReflectiveDocConvert();
        convert.convert( reader, writer );
        Assertions.assertEquals( content, writer.toString() );
    }

    @Test
    void testConvertEmptyContent() throws Exception {
        StringReader reader = new StringReader( "" );
        StringWriter writer = new StringWriter();
        new ReflectiveDocConvert().convert( reader, writer );
        Assertions.assertEquals( "", writer.toString() );
    }
}
