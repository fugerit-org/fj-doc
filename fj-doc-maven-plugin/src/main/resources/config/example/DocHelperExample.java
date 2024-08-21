package [PACKAGE];

import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.process.DocProcessContext;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * This is a basic example of Fugerit Venus Doc usage,
 * running this main the program will :
 * - creates data to be used in document model
 * - renders the 'document.ftl' template
 * - print the result in markdown format on the stanndard output
 *
 * For further documentation :
 * https://github.com/fugerit-org/fj-doc
 *
 * NOTE: This is a 'Hello World' style example, adapt it to your scenario, especially :
 *  - remove system out and system err with your logging system
 *  - change the doc handler and the output mode (here a ByteArrayOutputStream buffer is used)
 */
public class DocHelperExample {

    public static void main(String[] args) {
        try ( ByteArrayOutputStream baos = new ByteArrayOutputStream() ) {
            // creates the doc helper
            DocHelper docHelper = new DocHelper();
            // find the handler for the output :
            DocTypeHandler docTypeHandler = docHelper.getDocProcessConfig().getFacade().findHandler(DocConfig.TYPE_MD);
            // create custom data for the fremarker template 'document.ftl'
            List<People> listPeople = Arrays.asList( new DocHelperExample.People( "Luthien", "Tinuviel", "Queen" ), new DocHelperExample.People( "Thorin", "Oakshield", "King" ) );
            // output generation
            docHelper.getDocProcessConfig().fullProcess( "document", DocProcessContext.newContext( "listPeople", listPeople ), docTypeHandler, DocOutput.newOutput( baos ) );
            // print the output
            System.out.println( "html output : \n"+ new String( baos.toByteArray(), StandardCharsets.UTF_8 ) );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Class used to wrap data to be rendered in the document template
     */
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
