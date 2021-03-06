package pl.apisnet.userUI;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXRadioButton;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pl.apisnet.backEND.Exceptions.FileStructureException;
import pl.apisnet.backEND.XMLFiles.Services.AddPZDocumentService;
import pl.apisnet.backEND.XMLFiles.Services.ImportItemsToOptimaService;
import pl.apisnet.backEND.XMLFiles.Services.ImportXmlService;
import pl.apisnet.backEND.Optima;
import pl.apisnet.backEND.XMLFiles.XMLExcelParser;
import pl.apisnet.backEND.XMLFiles.XMLIHurtParser;
import pl.apisnet.backEND.XMLFiles.XMLImporter;
import pl.apisnet.backEND.XMLFiles.XMLObjects.XMLPZPosition;
import pl.apisnet.backEND.XMLFiles.XMLSubiektParser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ImportXMLFileScreenController implements Initializable {

    //Global optima objects
    OptimaHolder optimaHolderInstance = OptimaHolder.getInstance();
    Optima mainOptima;

    FileChooser fileChooser; //Global object for choosing File
    String fileToImportPath; //Path to selected file by User

    XMLImporter importer; //Abstract object for different importer types
    int missingEansCounter, incorrectItemsCounter;
    List<String> correctJEWList = Arrays.asList("szt","godz","kg","litr","m","mkw","opak");

    boolean[] selectedFileType = new boolean[]{false,false,false}; // Array for storing logic for selected type of file
    // 1 - iHurt | 2 - Subiekt | 3 - Excel

    private static final String IDLE_BUTTON_STYLE = "-fx-background-color:  #fff; -fx-background-radius: 8px;";
    private static final String HOVERED_BUTTON_STYLE = "-fx-background-color: #f57500; -fx-background-radius: 8px;";
    private final String errorHeaderString = "Wyst??pi?? b????d";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainOptima = optimaHolderInstance.getMainOptima();
        optimaFirmName.setText(mainOptima.getOptimaCompanyName());
        makeDesign(); //Styling buttons,labels etc.

    }

    @FXML
    private StackPane mainPane;
    @FXML
    private JFXButton backButton;

    @FXML
    private Label loadLabel;

    @FXML
    private JFXRadioButton eanAsSymbolButton;

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

    @FXML
    private JFXButton resetAllButton;

    String fileName = "";

    //Exiting from app
    @FXML
    void exit(ActionEvent event) {
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        dialogLayout.setStyle("-fx-background-color: #525252");

        Text exitHeader = new Text("Wyj??cie");
        exitHeader.setStyle("-fx-font-weight: bold; -fx-font-size: 15;");
        exitHeader.setFill(Color.WHITE);
        Text exitConfirmation = new Text("Czy na pewno chcesz wyj???? z programu ?");
        exitConfirmation.setStyle("-fx-font-weight: bold; -fx-font-size: 13;");
        exitConfirmation.setFill(Color.WHITE);

        dialogLayout.setHeading(exitHeader);
        dialogLayout.setBody(exitConfirmation);

        JFXButton ok = new JFXButton("Tak");
        JFXButton cancel = new JFXButton ("Anuluj");

        ok.setStyle(IDLE_BUTTON_STYLE);
        ok.setOnMouseEntered(e -> ok.setStyle(HOVERED_BUTTON_STYLE));
        ok.setOnMouseExited(e -> ok.setStyle(IDLE_BUTTON_STYLE));

        cancel.setStyle(IDLE_BUTTON_STYLE);
        cancel.setOnMouseEntered(e -> cancel.setStyle(HOVERED_BUTTON_STYLE));
        cancel.setOnMouseExited(e -> cancel.setStyle(IDLE_BUTTON_STYLE));

        JFXDialog dialog = new JFXDialog(mainPane, dialogLayout, JFXDialog.DialogTransition.CENTER);
        ok.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent t) {
                mainOptima.disconnectFromOptima();
                System.exit(0);
            }
        });

        cancel.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent t) {
                dialog.close();
            }

        });

        dialogLayout.setActions(ok,cancel);
        dialog.show();

    }



    //Moving to UserDashboardScreen
    @FXML
    void moveBack(ActionEvent event) {
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        dialogLayout.setStyle("-fx-background-color: #525252");

        Text exitHeader = new Text("Powr??t");
        exitHeader.setStyle("-fx-font-weight: bold; -fx-font-size: 15;");
        exitHeader.setFill(Color.WHITE);
        Text exitConfirmation = new Text("Czy na pewno chcesz wr??ci?? do poprzedniego ekranu ?");
        exitConfirmation.setStyle("-fx-font-weight: bold; -fx-font-size: 13;");
        exitConfirmation.setFill(Color.WHITE);

        dialogLayout.setHeading(exitHeader);
        dialogLayout.setBody(exitConfirmation);

        JFXButton ok = new JFXButton("Tak");
        JFXButton cancel = new JFXButton ("Anuluj");

        ok.setStyle(IDLE_BUTTON_STYLE);
        ok.setOnMouseEntered(e -> ok.setStyle(HOVERED_BUTTON_STYLE));
        ok.setOnMouseExited(e -> ok.setStyle(IDLE_BUTTON_STYLE));

        cancel.setStyle(IDLE_BUTTON_STYLE);
        cancel.setOnMouseEntered(e -> cancel.setStyle(HOVERED_BUTTON_STYLE));
        cancel.setOnMouseExited(e -> cancel.setStyle(IDLE_BUTTON_STYLE));

        JFXDialog dialog = new JFXDialog(mainPane, dialogLayout, JFXDialog.DialogTransition.CENTER);
        ok.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent t) {
                mainOptima.disconnectFromOptima();
                try{
                    Stage stage = (Stage) mainAnchorPane.getScene().getWindow();
                    stage.close();
                    Stage newStage = new Stage();
                    Parent root = FXMLLoader.load(getClass().getResource(("loginScreen.fxml")));
                    Scene scene = new Scene(root);
                    newStage.initStyle(StageStyle.DECORATED);
                    newStage.setTitle("AIMPORTER");
                    newStage.setScene(scene);
                    newStage.show();
                }catch (IOException e){
                    System.exit(0);
                }
            }
        });

        cancel.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent t) {
                dialog.close();
            }

        });

        dialogLayout.setActions(ok,cancel);
        dialog.show();
    }

    //Buttons controllers - responsible for importing actions
//Import from EXCEL
    @FXML
    void importExcel(ActionEvent event) {
        selectXmlFile();
        if (!fileToImportPath.isBlank()){
            if(fileToImportPath.substring(fileToImportPath.length() - 3).equals("xls") || fileToImportPath.substring(fileToImportPath.length() - 4).equals("xlsx") ){
                eanAsSymbolButton.setVisible(true);
                importer = new XMLExcelParser(fileToImportPath,mainOptima);
                List<String> avliableSheets = ((XMLExcelParser)importer).getAvailableSheets();

                ChoiceDialog<String> dialog = new ChoiceDialog<>(avliableSheets.get(0), avliableSheets);
                dialog.setTitle("Wymagane dzia??anie");
                dialog.setHeaderText("Wybierz arkusz, z kt??rego chcesz zaimportowa?? dane !");

                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()){
                    ((XMLExcelParser) importer).setSheetName(result.get());
                    processImportButton.setVisible(true);
                } else if (result.isEmpty()){
                    Alert alert = new Alert(Alert.AlertType.ERROR,"  Nie wybrano ??adnego arkusza !");
                    alert.setTitle(errorHeaderString);
                    alert.setHeaderText("  Brak pliku !");
                    alert.show();
                }
            } else{
                Alert alert = new Alert(Alert.AlertType.ERROR,"  Wybrany plik posiada niepoprawn?? sk??adni?? !");
                alert.setTitle(errorHeaderString);
                alert.setHeaderText("  Niepoprawny typ pliku !");
                alert.show();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR,"  Nie wybrano ??adnego pliku XLSX!");
            alert.setTitle(errorHeaderString);
            alert.setHeaderText("  Brak pliku !");
            alert.show();
        }
    }

    //Import from iHurt
    @FXML
    void importIHurt(ActionEvent event) {
        selectXmlFile();
        if(!fileToImportPath.isBlank()){
            if(fileToImportPath.substring(fileToImportPath.length() - 3).equals("xml")){
                eanAsSymbolButton.setVisible(true);
                processImportButton.setVisible(true);
                importer = new XMLIHurtParser(fileToImportPath, mainOptima);
            } else{
                Alert alert = new Alert(Alert.AlertType.ERROR,"  Wybrany plik posiada niepoprawn?? sk??adni?? !");
                alert.setTitle(errorHeaderString);
                alert.setHeaderText("  Niepoprawny typ pliku !");
                alert.show();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR,"  Nie wybrano ??adnego pliku XML !");
            alert.setTitle(errorHeaderString);
            alert.setHeaderText("  Brak pliku !");
            alert.show();
        }
    }

    //Import from Subiekt
    @FXML
    void importSubiekt(ActionEvent event) {
        selectXmlFile();
        if(!fileToImportPath.isBlank()){
            if(fileToImportPath.substring(fileToImportPath.length() - 3).equals("epp")){
                eanAsSymbolButton.setVisible(true);
                processImportButton.setVisible(true);
                importer = new XMLSubiektParser(fileToImportPath, mainOptima);
            } else{
                Alert alert = new Alert(Alert.AlertType.ERROR,"  Wybrany plik posiada niepoprawn?? sk??adni?? !");
                alert.setTitle(errorHeaderString);
                alert.setHeaderText("  Niepoprawny typ pliku !");
                alert.show();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR,"  Nie wybrano ??adnego pliku EPP !");
            alert.setTitle(errorHeaderString);
            alert.setHeaderText("  Brak pliku !");
            alert.show();
        }
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
        boolean userMistake = true; //flag responsible for checking if user did not click cancel or exit button
        while (userMistake == true){
            for (XMLPZPosition item: importer.getPZItemsList()){
                if (!item.isIsjEWCorrect()){
                    ChoiceDialog<String> dialog = new ChoiceDialog<>("szt", correctJEWList);
                    dialog.setTitle("Wymagane dzia??anie");
                    dialog.setHeaderText("Towar "+item.getSymbol()+" posiada niepoprawnie zdefiniowan?? jednostk?? miary !\n"+item.getjEW()+" jest niepoprawna !");
                    dialog.setContentText("Aby usun???? problem dostosuj jednostk?? miary:");
                    Optional<String> result = dialog.showAndWait();
                    if (result.isPresent()){
                        item.setjEW(result.get());
                        item.setIsjEWCorrect(true);
                    }
                }
            }
            //If PZItemList still contains any item with wrong JEW while loop should run again
            userMistake = importer.getPZItemsList().stream().anyMatch(p->p.isIsjEWCorrect() == false);
        }

        //Using task to do not freez main JavaFX GUI thread
        loadLabel.setVisible(true);
        loadLabel.setText("Dodawanie brakuj??cych pozycji");
        final ImportItemsToOptimaService imp = new ImportItemsToOptimaService(importer);
        progressIn.visibleProperty().bind(imp.runningProperty());
        addPZDocumentButton.setVisible(true);
        addPZDocumentButton.setDisable(true);
        //If items were added successfully to Optima
        imp.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION,"Wszystkie towary zosta??y pomy??lnie dodane do Optima !");
                alert.setTitle("Potwierdzenie");
                alert.setHeaderText("  Sukces !");
                alert.show();
                loadLabel.setVisible(false);
                insertDataIntoTable(importer.getPZItemsList());
                addPZDocumentButton.setDisable(false);
            }
        });

        //If items were not added successfully to optima
        imp.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                Alert alert = new Alert(Alert.AlertType.ERROR,"Nie udalo sie dodac wszystkich brakuj??cych towar??w do Optima!\nPrawdopodobnie w Optima istniej?? ju?? duplikaty towar??w o takim samym symbolu !\nWykorzystaj opcje 'Ean jako symbol'!");
                alert.setTitle(errorHeaderString);
                alert.setHeaderText("  B????d !");
                alert.show();
                addPZDocumentButton.setDisable(true);
                loadLabel.setVisible(false);
                resetImportDetails();
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
        addPZDocumentButton.setDisable(true);
        //If PZ document was successfully created
        imp.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                loadLabel.setVisible(false);
                Alert alert = new Alert(Alert.AlertType.INFORMATION,"Dokument przyj??cia zewn??trznego zosta?? utworzony w Optima!");
                alert.setTitle("Potwierdzenie");
                alert.setHeaderText("  Sukces !");
                alert.show();
                //addPZDocumentButton.setDisable(true);
                resetAllButton.setText("Zresetuj i importuj\nkolejny plik");
                resetAllButton.setVisible(true);
            }
        });

        //If PZ document was not successfully created
        imp.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                loadLabel.setVisible(false);
                Alert alert = new Alert(Alert.AlertType.ERROR,"Dokument przyj??cia zewn??trznego nie zosta?? utworzony w Optima ! Wyst??pi?? b????d !");
                alert.setTitle(errorHeaderString);
                alert.setHeaderText("  B????d !");
                alert.show();
                resetImportDetails();
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
        fileChooser.setTitle("Wska?? plik XML: ");
        File file = fileChooser.showOpenDialog(mainAnchorPane.getScene().getWindow());
        if (file !=null){
            processImportButton.setText("Wczytaj plik\n"+file.getName()+"\n do programu");
            fileToImportPath = file.getAbsolutePath();
            fileName = file.getName();
        }
    }

    /**
     * Method responsible for processing import given XML file into Optima
     */
    private void processImport(){
        if (mainOptima.checkOptimaConnection()){
                //Checking if Customer wants EAN code to be Symbol of items
                eanAsSymbolButton.setVisible(false);
                importer.setItemSymbolAsEan(eanAsSymbolButton.isSelected());

                //Disabling all other buttons to avoid creating PZ with missing EANS(Items)
                processImportButton.setDisable(true);
                subiektButton.setDisable(true);
                iHurtButton.setDisable(true);
                excelButton.setDisable(true);

                loadLabel.setVisible(true);
                loadLabel.setText("Przetwarzanie wczytanego pliku");
                ImportXmlService impXmlService = new ImportXmlService(importer);
                progressIn.visibleProperty().bind(impXmlService.runningProperty());
                //If file was read successfully
                impXmlService.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent workerStateEvent) {

                        loadLabel.setVisible(false);

                        //Inserting data into Table
                        insertDataIntoTable(importer.getPZItemsList());
                        //Checking if missing Items exists and showing appropriate notifications
                        checkIfItemListCointainsMissingEans();
                        addMissingEansButton.setVisible(true);
                        addMissingEansButton.setText("Dodaj brakuj??ce towary\ndo Optima ");
                        if(missingEansCounter > 0){
                            Alert alert = new Alert(Alert.AlertType.ERROR,"  Znaleziono "+missingEansCounter+" brakuj??cych towar??w!\n  Brakuj??ce towary zostan?? dodane po uruchomieniu procedury!");
                            alert.setHeaderText("  Uwaga !");
                            alert.setTitle("Brakujace towary");
                            alert.show();
                        }
                        else{
                            addMissingEansButton.setDisable(true);
                            addPZDocumentButton.setVisible(true);
                            Alert alert = new Alert(Alert.AlertType.ERROR,"  Nie znaleziono brakuj??cych towar??w. ");
                            alert.setHeaderText("  Uwaga !");
                            alert.setTitle("Brakujace towary");
                            alert.show();
                        }

                        if (incorrectItemsCounter > 0){
                            Alert alert = new Alert(Alert.AlertType.ERROR,"Wczytany plik zawiera?? towary, kt??rych jednostki miary r????ni??y si?? od towar??w znajduj??cych si?? w Optimie !\n"+
                                    "Jednostki miary towar??w zosta??y ujednolicone zgodnie z danymi pobranymi z Optimy !\nNiezgodne towary zosta??y oznaczone ??????tym kolorem !");
                            alert.setHeaderText("  Uwaga !");
                            alert.show();
                        }


                    }
                });
                //If file was read unsuccessfully
                impXmlService.setOnFailed(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent workerStateEvent) {
                        loadLabel.setVisible(false);
                        Alert alert = new Alert(Alert.AlertType.ERROR," Wyst??pi?? problem z przetworzeniem wczytanego pliku !\n Upewnij si??, ??e jego sk??adnia jest poprawna !");
                        alert.setTitle(errorHeaderString);
                        alert.setHeaderText("  B????d krytyczny !");
                        alert.show();
                        resetImportDetails();
                    }
                });
                impXmlService.restart();

        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR,"  Nie uda??o si?? po????czy?? z Optima \n  Sprawdz ustawienia !");
            alert.setTitle(errorHeaderString);
            alert.setHeaderText("  B????d krytyczny !");
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
        TableColumn<XMLPZPosition,String> iloscColumn = new TableColumn<>("Ilo????");

        itemsTable.getColumns().addAll(symbolColumn,nazwaColumn,eanColumn,jEWColumn,cenaColumn,iloscColumn);

        eanColumn.setPrefWidth(110);
        symbolColumn.setPrefWidth(150);
        nazwaColumn.setPrefWidth(200);
        jEWColumn.setPrefWidth(120);
        cenaColumn.setPrefWidth(90);
        iloscColumn.setPrefWidth(83);

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

                if (item != null && (item).getjEWWasIncorrect())
                    setStyle("-fx-background-color: #CCCC33;");
            }
        });
    }

    /**
     * Method responsible for checking if in list exists any item that is not already in Optima
     */
    private void checkIfItemListCointainsMissingEans(){
        missingEansCounter = 0;
        incorrectItemsCounter = 0;
        for (int i=0; i<importer.getPZItemsList().size(); i++){
            if (!importer.getPZItemsList().get(i).isAlreadyInOptima()){
                missingEansCounter ++;
            }
            if (importer.getPZItemsList().get(i).getjEWWasIncorrect()){
                incorrectItemsCounter ++;
            }
        }
    }

    /**
     * Method responsible for hovering buttons
     */
    private void makeDesign(){
        eanAsSymbolButton.setVisible(false);

        optimaFirmName.setTextFill(Color.rgb(245,117,0));
        processImportButton.setVisible(false);
        addMissingEansButton.setVisible(false);
        itemsTable.getColumns().clear();
        progressIn.setVisible(false);
        progressIn.setStyle(" -fx-progress-color: #d85c2c");
        loadLabel.setVisible(false);
        addPZDocumentButton.setVisible(false);
        addPZDocumentButton.setText("Utw??rz dokument PZ");
        resetAllButton.setVisible(false);

        List<Button> buttonsList = Arrays.asList(iHurtButton,subiektButton,excelButton,processImportButton,addMissingEansButton,addPZDocumentButton,exitButton,backButton,resetAllButton);
        for (Button b: buttonsList){
            b.setStyle(IDLE_BUTTON_STYLE);
            b.setOnMouseEntered(e -> b.setStyle(HOVERED_BUTTON_STYLE));
            b.setOnMouseExited(e -> b.setStyle(IDLE_BUTTON_STYLE));
        }
    }

    /**
     * Method responsible for reseting all stored items to let customer import next file to Optima
     */
    @FXML
    void resetImportingDetails(ActionEvent event) {
        resetImportDetails();
    }

    private void resetImportDetails(){
        eanAsSymbolButton.setSelected(false);
        itemsTable.getColumns().clear();
        importer.getPZItemsList().clear();
        fileToImportPath = "";
        iHurtButton.setDisable(false);
        subiektButton.setDisable(false);
        excelButton.setDisable(false);

        processImportButton.setVisible(false);
        processImportButton.setDisable(false);

        addMissingEansButton.setVisible(false);
        addMissingEansButton.setDisable(false);

        addPZDocumentButton.setDisable(false);
        addPZDocumentButton.setVisible(false);
        resetAllButton.setVisible(false);
    }


}
