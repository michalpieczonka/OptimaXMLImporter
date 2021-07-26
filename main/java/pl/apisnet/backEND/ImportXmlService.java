package pl.apisnet.backEND;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import pl.apisnet.backEND.XMLFiles.XMLImporter;

public class ImportXmlService  extends Service<String> {

    private XMLImporter importer;
    public ImportXmlService(XMLImporter importer){
        this.importer = importer;
    }

    @Override
    protected Task<String> createTask() {
        return new Task<String>() {
            @Override
            protected String call() throws Exception {
                importer.readXmlFileHeaders();
                String res = "toto";
                Thread.sleep(1000);
                return res;
            }
        };
    }
}
