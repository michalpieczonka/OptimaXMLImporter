package pl.apisnet.backEND.XMLFiles.Services;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import pl.apisnet.backEND.XMLFiles.XMLImporter;

public class ImportItemsToOptimaService extends Service<String> {
    private XMLImporter importer;
    public ImportItemsToOptimaService(XMLImporter importer){
        this.importer = importer;
    }
    @Override
    protected Task<String> createTask() {
        return new Task<String>() {
            @Override
            protected String call() throws Exception {
                importer.addMissingEans();
                String res = "toto";
                Thread.sleep(1000);
                return res;
            }
        };
    }
}
