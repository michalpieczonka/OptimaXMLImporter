package pl.apisnet.userUI;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class EditItemScreenController implements Initializable {

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private Label itemName;

    @FXML
    private Label itemEAN;

    @FXML
    private Label itemJM;

    @FXML
    private JFXComboBox<?> JMBox;

    @FXML
    private JFXButton confirmButton;

    @FXML
    private JFXButton cancelButton;

    @FXML
    void cancelAndGoNext(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void changeJMBox(ActionEvent event) {

    }

    @FXML
    void confirmChanges(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("jestem lae nie widze");
    }
}

