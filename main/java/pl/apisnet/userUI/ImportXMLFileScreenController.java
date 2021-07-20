package pl.apisnet.userUI;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import pl.apisnet.backEND.Optima;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;


import java.net.URL;
import java.util.ResourceBundle;

public class ImportXMLFileScreenController implements Initializable {

    OptimaHolder optimaHolderInstance = OptimaHolder.getInstance();
    Optima mainOptima;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainOptima = optimaHolderInstance.getMainOptima();
    }
    @FXML
    private TableView<?> itemsTable;

    @FXML
    private JFXButton exitButton;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label tableDesc;

    @FXML
    private JFXButton iHurtButton;

    @FXML
    private JFXButton subiektButton;

    @FXML
    private JFXButton excelButton;

    @FXML
    void exit(ActionEvent event) {

    }

    @FXML
    void importExcel(ActionEvent event) {

    }

    @FXML
    void importIHurt(ActionEvent event) {

    }

    @FXML
    void importSubiekt(ActionEvent event) {

    }
}
