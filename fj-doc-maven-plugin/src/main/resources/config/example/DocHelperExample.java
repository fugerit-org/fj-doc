package [PACKAGE];

import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.process.DocProcessContext;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class DocHelperExample {

    public static void main(String[] args) {
        try ( ByteArrayOutputStream baos = new ByteArrayOutputStream() ) {
            DocHelper docHelper = new DocHelper();
            DocTypeHandler docTypeHandler = docHelper.getDocProcessConfig().getFacade().findHandler(DocConfig.TYPE_MD);
            List<People> listPeople = Arrays.asList( new DocHelperExample.People( "Luthien", "Tinuviel", "Queen" ), new DocHelperExample.People( "Thorin", "Oakshield", "King" ) );
            docHelper.getDocProcessConfig().fullProcess( "document", DocProcessContext.newContext( "listPeople", listPeople ), docTypeHandler, DocOutput.newOutput( baos ) );
            System.out.println( "html output : \n"+ new String( baos.toByteArray(), StandardCharsets.UTF_8 ) );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class People {

        private String name;

        private String surname;

        private String title;

        public People(String name, String surname, String title) {
            this.name = name;
            this.surname = surname;
            this.title = title;
        }

        public String getName() {
            return name;
        }

        public String getSurname() {
            return surname;
        }

        public String getTitle() {
            return title;
        }
    }

}
