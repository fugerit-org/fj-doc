package test.org.fugerit.java.doc.mod.fop;

import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.doc.mod.fop.utils.ApacheFopUtils;
import org.junit.jupiter.api.Test;

@Slf4j
class TestApacheFopVersion {

    @Test
    void testFopVersion() {
        String fopVersion = ApacheFopUtils.getApacheFOPVersion();
        log.info( "fop version : {}", fopVersion  );
    }

}
