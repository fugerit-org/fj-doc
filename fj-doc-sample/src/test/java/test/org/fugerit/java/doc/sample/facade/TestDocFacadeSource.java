package test.org.fugerit.java.doc.sample.facade;

import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.doc.base.facade.DocFacadeSource;
import org.fugerit.java.doc.base.kotlin.parse.DocKotlinParser;
import org.fugerit.java.doc.base.parser.DocParser;
import org.junit.Assert;
import org.junit.Test;

@Slf4j
public class TestDocFacadeSource {

    @Test
    public void testKotlinYaml() {
        DocParser docParser = DocFacadeSource.getInstance().getParserForSource( DocFacadeSource.SOURCE_TYPE_YAML );
        log.info( "docParser yaml : {}", docParser );
        Assert.assertNotNull( docParser );
    }

    @Test
    public void testKotlinSource() {
        DocParser docParser = DocFacadeSource.getInstance().getParserForSource( DocFacadeSource.SOURCE_TYPE_KOTLIN );
        log.info( "docParser kotlin : {}", docParser );
        Assert.assertNotNull( docParser );
    }

}
