package pl.apisnet.userUI;

import com.jfoenix.controls.JFXButton;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pl.apisnet.backEND.AddPZDocumentService;
import pl.apisnet.backEND.ImportItemsToOptimaService;
import pl.apisnet.backEND.ImportXmlService;
import pl.apisnet.backEND.Optima;
import pl.apisnet.backEND.XMLFiles.XMLExcelParser;
import pl.apisnet.backEND.XMLFiles.XMLIHurtParser;
import pl.apisnet.backEND.XMLFiles.XMLImporter;
import pl.apisnet.backEND.XMLFiles.XMLObjects.IHurtXMLPZPosition;
import pl.apisnet.backEND.XMLFiles.XMLObjects.XMLPZPosition;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ImportXMLFileScreenController implements Initializable {

    //Global optima objects
    OptimaHolder optimaHolderInstance = OptimaHolder.getInstance();
    Optima mainOptima;

    FileChooser fileChooser; //Global object for choosing File
    String fileToImportPath; //Path to selected file by User

    XMLImporter importer; //Abstract object for different importer types
    int missingEansCounter;
    List<String> correctJEWList = Arrays.asList("szt","godz","kg","litr","m","mkw","opak");

    boolean[] selectedFileType = new boolean[]{false,false,false}; // Array for storing logic for selected type of file
                                                 // 1 - iHurt | 2 - Subiekt | 3 - Excel

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainOptima = optimaHolderInstance.getMainOptima();
        optimaFirmName.setText(mainOptima.getOptimaCompanyName());
        processImportButton.setVisible(false);
        addMissingEansButton.setVisible(false);
        itemsTable.getColumns().clear();
        progressIn.setVisible(false);
        progressIn.setStyle(" -fx-progress-color: #d85c2c");
        loadLabel.setVisible(false);
        addPZDocumentButton.setVisible(false);
        addPZDocumentButton.setText("Utwórz dokument PZ");
    }


    @FXML
    private Label loadLabel;

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private TableView<XMLPZPosition> itemsTable;

    @FXML
    private JFXButton exitButton;

    @FXML
    private JFXButton addMissingEansButton;

    @FXML
    private Label tableDesc;

    @FXML
    private Label optimaFirmName;

    @FXML
    private JFXButton iHurtButton;

    @FXML
    private JFXButton subiektButton;

    @FXML
    private JFXButton excelButton;

    @FXML
    private JFXButton processImportButton;

    @FXML
    private ProgressIndicator progressIn;

    @FXML
    private JFXButton addPZDocumentButton;


    //Exiting from app
    @FXML
    void exit(ActionEvent event) {

    }

//Buttons controllers - responsible for importing actions
//Import from EXCEL
    @FXML
    void importExcel(ActionEvent event) {
        selectXmlFile();
        if (!fileToImportPath.isBlank()){
            importer = new XMLExcelParser(fileToImportPath,mainOptima);
            List<String> avliableSheets = ((XMLExcelParser)importer).getAvailableSheets();

            ChoiceDialog<String> dialog = new ChoiceDialog<>(avliableSheets.get(0), avliableSheets);
            dialog.setTitle("Wymagane działanie");
            dialog.setHeaderText("Wybierz arkusz, z którego chcesz zaimportować dane !");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()){
                ((XMLExcelParser) importer).setSheetName(result.get());
                processImportButton.setVisible(true);
            } else if (result.isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR,"  Nie wybrano żadnego arkusza !");
                alert.setHeaderText("  Brak pliku !");
                alert.show();
            }

        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR,"  Nie wybrano żadnego pliku xlsx !");
            alert.setHeaderText("  Brak pliku !");
            alert.show();
        }
    }

//Import from iHurt
    @FXML
    void importIHurt(ActionEvent event) {
        selectXmlFile();
        if(!fileToImportPath.isBlank()){
            processImportButton.setVisible(true);
            importer = new XMLIHurtParser(fileToImportPath, mainOptima);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR,"  Nie wybrano żadnego pliku XML !");
            alert.setHeaderText("  Brak pliku !");
            alert.show();
        }
    }

//Import from Subiekt
    @FXML
    void importSubiekt(ActionEvent event) {
        selectXmlFile();
    }


//2 additional buttons responsible for reading from XML file and Processing readed data into Optima
//ActionListener for importing XML file button -> When clicking import data from XML to App starts.
    @FXML
    void startImporting(ActionEvent event) {
        processImport();
    }

//ActionListener for importing readed data (into Lists) to Optima
    @FXML
    void addMissingEansToOptima(ActionEvent event) {
        addMissingEansButton.setDisable(true);
        for (XMLPZPosition item: importer.getPZItemsList()){
            if (!item.isIsjEWCorrect()){
                ChoiceDialog<String> dialog = new ChoiceDialog<>("szt", correctJEWList);
                dialog.setTitle("Wymagane działanie");
                dialog.setHeaderText("Towar "+item.getSymbol()+" posiada niepoprawnie zdefiniowaną jednostkę miary !\n"+item.getjEW()+" jest niepoprawna !");
                dialog.setContentText("Aby usunąć problem dostosuj jednostkę miary:");

                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()){
                    item.setjEW(result.get());
                    item.setIsjEWCorrect(true);
                } else if (result.isEmpty()){
                    System.out.println("Usunięto pozycje");
                    importer.getPZItemsList().remove(item);
                }
            }
        }
        //Using task to do not freez main JavaFX GUI thread
        loadLabel.setVisible(true);
        loadLabel.setText("Dodawanie brakujących pozycji");
        final ImportItemsToOptimaService imp = new ImportItemsToOptimaService(importer);
        progressIn.visibleProperty().bind(imp.runningProperty());
        addPZDocumentButton.setVisible(true);
        addPZDocumentButton.setDisable(true);
        //If items were added successfully to Optima
        imp.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                loadLabel.setVisible(false);
                insertDataIntoTable(importer.getPZItemsList());
                addPZDocumentButton.setDisable(false);
            }
        });

        //If items were not added successfully to optima
        imp.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                addPZDocumentButton.setDisable(true);
                loadLabel.setVisible(false);
                System.out.println("nie udalo sie dodac towaru");
            }
        });
        imp.restart();

    }


    @FXML
    void createPZDocument(ActionEvent event) {
        final AddPZDocumentService imp = new AddPZDocumentService(importer);
        progressIn.visibleProperty().bind(imp.runningProperty());
        loadLabel.setVisible(true);
        loadLabel.setText("Tworzenie dokumentu PZ");
        addMissingEansButton.setDisable(true);
        //If PZ document was successfully created
        imp.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                loadLabel.setVisible(false);
                System.out.println("superancko zaimportowane");
            }
        });

        //If PZ document was not successfully created
        imp.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                loadLabel.setVisible(false);
                System.out.println("Zjebalo sie i nic nie dziala");
            }
        });
        imp.restart();
    }



//Aditional methods sections
    /**
     * Method responsible for reading path to XML-file selected by User.
     */
    private void selectXmlFile(){
        fileChooser = new FileChooser();
        fileChooser.setTitle("Wskaż plik XML: ");
        File file = fileChooser.showOpenDialog(mainAnchorPane.getScene().getWindow());
        if (file !=null){
            processImportButton.setText("Wczytaj plik\n"+file.getName()+"\n do programu");
            fileToImportPath = file.getAbsolutePath();
        }
    }

    /**
     * Method responsible for processing import given XML file into Optima
     */
    private void processImport(){
        if (mainOptima.checkOptimaConnection()){
            try{
                loadLabel.setVisible(true);
                loadLabel.setText("Przetwarzanie wczytanego pliku");
                ImportXmlService impXmlService = new ImportXmlService(importer);
                progressIn.visibleProperty().bind(impXmlService.runningProperty());
                //If file was read successfully
                impXmlService.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent workerStateEvent) {
                        //Disabling all other buttons to avoid creating PZ with missing EANS(Items)
                        processImportButton.setDisable(true);
                        subiektButton.setDisable(true);
                        iHurtButton.setDisable(true);
                        excelButton.setDisable(true);
                        loadLabel.setVisible(false);

                        //Inserting data into Table
                        insertDataIntoTable(importer.getPZItemsList());
                        //Checking if missing Items exists and showing appropriate notifications
                        checkIfItemListCointainsMissingEans();
                        addMissingEansButton.setVisible(true);
                        addMissingEansButton.setText("Dodaj brakujące towary\ndo Optima ");
                        if(missingEansCounter > 0){
                            Alert alert = new Alert(Alert.AlertType.ERROR,"  Znaleziono "+missingEansCounter+" brakujących towarów!\n  Brakujące towary zostaną dodane po uruchomieniu procedury!");
                            alert.setHeaderText("  Uwaga !");
                            alert.setTitle("Brakujace towary");
                            alert.show();
                        }else{
                            addMissingEansButton.setDisable(true);
                            addPZDocumentButton.setVisible(true);
                            Alert alert = new Alert(Alert.AlertType.ERROR,"  Nie znaleziono brakujących towarów. ");
                            alert.setHeaderText("  Uwaga !");
                            alert.setTitle("Brakujace towary");
                            alert.show();
                        }


                    }
                });

                //If file was read unsuccessfully
                impXmlService.setOnFailed(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent workerStateEvent) {
                        loadLabel.setVisible(false);
                        System.out.println("Zjebalo sie i nic nie dziala");
                    }
                });
                impXmlService.restart();



            } catch (Exception e){
                System.out.println(e);
                Alert alert = new Alert(Alert.AlertType.ERROR,"  Wybrany plik XML posiada niepoprawną składnie !");
                alert.setHeaderText("  Błąd krytyczny !");
                alert.show();
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR,"  Nie udało się połączyć z Optima \n  Sprawdz ustawienia !");
            alert.setHeaderText("  Błąd krytyczny !");
            alert.show();
        }

    }


    /**
     * Method responsible for writing data into Table, read from xml file
     */
    private void insertDataIntoTable(List<XMLPZPosition> itemsInXML){
        itemsTable.getColumns().clear();

        ObservableList<XMLPZPosition> itemsInTable = FXCollections.observableArrayList(itemsInXML);

        TableColumn<XMLPZPosition,String> eanColumn = new TableColumn<>("EAN");
        TableColumn<XMLPZPosition,String> symbolColumn = new TableColumn<>("Symbol");
        TableColumn<XMLPZPosition,String> nazwaColumn = new TableColumn<>("Nazwa towaru");
        TableColumn<XMLPZPosition,String> jEWColumn = new TableColumn<>("Jednostka miary");
        TableColumn<XMLPZPosition,String> cenaColumn = new TableColumn<>("Cena");
        TableColumn<XMLPZPosition,String> iloscColumn = new TableColumn<>("Ilość");

        itemsTable.getColumns().addAll(symbolColumn,nazwaColumn,eanColumn,jEWColumn,cenaColumn,iloscColumn);

        eanColumn.setCellValueFactory(data -> new SimpleStringProperty(((XMLPZPosition) data.getValue()).getEAN()));
        symbolColumn.setCellValueFactory(data -> new SimpleStringProperty(((XMLPZPosition) data.getValue()).getSymbol()));
        nazwaColumn.setCellValueFactory(data -> new SimpleStringProperty(((XMLPZPosition) data.getValue()).getNazwa()));
        jEWColumn.setCellValueFactory(data -> new SimpleStringProperty(((XMLPZPosition) data.getValue()).getjEW()));
        cenaColumn.setCellValueFactory(data -> new SimpleStringProperty(Double.toString(((XMLPZPosition) data.getValue()).getCena())));
        iloscColumn.setCellValueFactory(data -> new SimpleStringProperty(Integer.toString(((XMLPZPosition) data.getValue()).getIlosc())));
        itemsTable.setItems(itemsInTable);

        itemsTable.setRowFactory(tv -> new TableRow<XMLPZPosition>() {
            @Override
            protected void updateItem(XMLPZPosition item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || (item).isAlreadyInOptima())
                    setStyle("-fx-background-color: #baffba;");
                else
                    setStyle("-fx-background-color: #CD5C5C;");
            }
        });
    }

    /**
     * Method responsible for checking if in list exists any item that is not already in Optima
     */
    private void checkIfItemListCointainsMissingEans(){
        missingEansCounter = 0;
        for (int i=0; i<importer.getPZItemsList().size(); i++){
            if (!importer.getPZItemsList().get(i).isAlreadyInOptima()){
                missingEansCounter ++;
            }
        }
    }



}
