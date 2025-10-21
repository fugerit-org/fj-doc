package test.org.fugerit.java.doc.project.facade;

import org.fugerit.java.doc.project.facade.FeatureFacade;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

class TestFeatureFacade {

    @Test
    void checkIfInBaseFolderTestOk() throws IOException {
        File baseFolder = new File( "." );
        File file = new File( "pom.xml" );
        FeatureFacade.checkIfInBaseFolder( baseFolder, file );
        Assertions.assertTrue( file.exists() );
    }

    @Test
    void checkIfInBaseFolderTestKo() {
        File baseFolder = new File( "fj-doc-base" );
        File file = new File( "pom.xml" );
        Assertions.assertThrows( IOException.class, () -> FeatureFacade.checkIfInBaseFolder( baseFolder, file ) );
    }

    @Test
    void insureParentTestNull1() throws IOException {
        File root = File.listRoots()[0];
        FeatureFacade.insureParent( root );
        Assertions.assertTrue( root.exists() );
    }

    @Test
    void insureParentTestNull2() throws IOException {
        File root = File.listRoots()[0];
        if ( root != null ) {
            File[] list = root.listFiles();
            if ( list != null && list.length > 0 ) {
                FeatureFacade.insureParent( list[0] );
                Assertions.assertTrue( root.exists() );
            }
        }
    }

}
