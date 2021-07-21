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
import pl.apisnet.backEND.XMLFiles.XMLObjects.IHurtXMLPZPosition;
import pl.apisnet.backEND.XMLFiles.XMLObjects.XMLPZPosition;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ImportXMLFileScreenController implements Initializable {

    OptimaHolder optimaHolderInstance = OptimaHolder.getInstance();
    Optima mainOptima;

    FileChooser fileChooser;
    String fileToImportPath;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainOptima = optimaHolderInstance.getMainOptima();
        optimaFirmName.setText(mainOptima.getOptimaCompanyName());
        processImportButton.setVisible(false);
        itemsTable.getColumns().clear();
    }



    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private TableView<XMLPZPosition> itemsTable;

    @FXML
    private JFXButton exitButton;


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
    void exit(ActionEvent event) {

    }

    @FXML
    void startImporting(ActionEvent event) {
        processImport();
    }

    @FXML
    void importExcel(ActionEvent event) {
        selectXmlFile();
    }

    @FXML
    void importIHurt(ActionEvent event) {
        selectXmlFile();
        if(!fileToImportPath.isBlank()){
            processImportButton.setVisible(true);
            processImportButton.setText("Wygeneruj\n przyjęcie zewnętrzne");
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR,"  Nie wybrano żadnego pliku XML !");
            alert.setHeaderText("  Brak pliku !");
            alert.show();
        }
    }

    @FXML
    void importSubiekt(ActionEvent event) {
        selectXmlFile();
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

    /**
     * Method responsible for reading path to XML-file selected by User.
     */
    private void selectXmlFile(){
        fileChooser = new FileChooser();
        fileChooser.setTitle("Wskaż plik XML: ");
        File file = fileChooser.showOpenDialog(mainAnchorPane.getScene().getWindow());
        if (file !=null){
            fileToImportPath = file.getAbsolutePath();
        }
    }

    void insertDataIntoTable(List<XMLPZPosition> itemsInXML){
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
                if (item == null || ((IHurtXMLPZPosition)(item)).getEAN().equals("5904000044710"))
                    setStyle("-fx-background-color: #baffba;");
               // else if (item.getValue() > 0)
                //    setStyle("-fx-background-color: #baffba;");
             //   else if (item.getValue() < 0)
              //      setStyle("-fx-background-color: #ffd7d1;");
                else
                    setStyle("");
            }
        });
    }



}
