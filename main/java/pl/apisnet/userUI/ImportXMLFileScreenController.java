package pl.apisnet.userUI;

import com.jfoenix.controls.JFXButton;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import pl.apisnet.backEND.Optima;
import pl.apisnet.backEND.XMLFiles.XMLIHurtParser;
import pl.apisnet.backEND.XMLFiles.XMLInterface;
import pl.apisnet.backEND.XMLFiles.XMLObjects.IHurtXMLPZPosition;
import pl.apisnet.backEND.XMLFiles.XMLObjects.XMLPZPosition;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ImportXMLFileScreenController implements Initializable {

    //Global optima objects
    OptimaHolder optimaHolderInstance = OptimaHolder.getInstance();
    Optima mainOptima;

    FileChooser fileChooser; //Global object for choosing File
    String fileToImportPath; //Path to selected file by User

    XMLInterface importer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainOptima = optimaHolderInstance.getMainOptima();
        optimaFirmName.setText(mainOptima.getOptimaCompanyName());
        processImportButton.setVisible(false);
        processButton.setVisible(false);
        itemsTable.getColumns().clear();
    }



    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private TableView<XMLPZPosition> itemsTable;

    @FXML
    private JFXButton exitButton;

    @FXML
    private JFXButton processButton;

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

//Exiting from app
    @FXML
    void exit(ActionEvent event) {

    }

//Buttons controllers - responsible for importing actions
//Import from EXCEL
    @FXML
    void importExcel(ActionEvent event) {
        selectXmlFile();
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
    void processFileToOptima(ActionEvent event) {

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


    private void processImport(){
        if (mainOptima.checkOptimaConnection()){
            XMLIHurtParser iHurtImporter = new XMLIHurtParser(fileToImportPath, mainOptima);
            try{
                iHurtImporter.readXmlFileHeaders();
                insertDataIntoTable(iHurtImporter.getPZItemsList());

            } catch (Exception e){
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



    void insertDataIntoTable(List<XMLPZPosition> itemsInXML){
        itemsTable.getColumns().clear();

        ObservableList<XMLPZPosition> itemsInTable = FXCollections.observableArrayList(itemsInXML);

        TableColumn<XMLPZPosition,String> eanColumn = new TableColumn<>("EAN");
        TableColumn<XMLPZPosition,String> symbolColumn = new TableColumn<>("Symbol");
        TableColumn<XMLPZPosition,String> nazwaColumn = new TableColumn<>("Nazwa towaru");
        TableColumn<XMLPZPosition,String> cenaColumn = new TableColumn<>("Cena");
        TableColumn<XMLPZPosition,String> iloscColumn = new TableColumn<>("Ilość");

        itemsTable.getColumns().addAll(symbolColumn,nazwaColumn,eanColumn,cenaColumn,iloscColumn);

        eanColumn.setCellValueFactory(data -> new SimpleStringProperty(((IHurtXMLPZPosition) data.getValue()).getEAN()));
        symbolColumn.setCellValueFactory(data -> new SimpleStringProperty(((IHurtXMLPZPosition) data.getValue()).getSymbol()));
        nazwaColumn.setCellValueFactory(data -> new SimpleStringProperty(((IHurtXMLPZPosition) data.getValue()).getNazwa()));
        cenaColumn.setCellValueFactory(data -> new SimpleStringProperty(Double.toString(((IHurtXMLPZPosition) data.getValue()).getCena())));
        iloscColumn.setCellValueFactory(data -> new SimpleStringProperty(Integer.toString(((IHurtXMLPZPosition) data.getValue()).getIlosc())));
        itemsTable.setItems(itemsInTable);

        itemsTable.setRowFactory(tv -> new TableRow<XMLPZPosition>() {
            @Override
            protected void updateItem(XMLPZPosition item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || ((IHurtXMLPZPosition)(item)).getIsAlreadyInOptima())
                    setStyle("-fx-background-color: #baffba;");
                else
                    setStyle("-fx-background-color: #CD5C5C;");
            }
        });
    }



}
