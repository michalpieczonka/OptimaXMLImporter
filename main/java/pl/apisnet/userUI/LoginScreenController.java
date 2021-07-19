package pl.apisnet.userUI;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class LoginScreenController {

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private AnchorPane loginAnchorPane;

    @FXML
    private JFXComboBox<?> optimaCompanyName;

    @FXML
    private TextField optimaOperatorField;

    @FXML
    private Label operatorLabel;

    @FXML
    private Label firmLabel;

    @FXML
    private Label firmLabel1;

    @FXML
    private JFXButton optimaDirButton;

    @FXML
    private JFXButton optimaLoginButton;

    @FXML
    void loginToOptima(ActionEvent event) {

    }

    @FXML
    void setOptimaDirectory(ActionEvent event) {

    }

}
