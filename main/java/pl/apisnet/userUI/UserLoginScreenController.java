package pl.apisnet.userUI;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import pl.apisnet.backEND.Optima;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserLoginScreenController implements Initializable {

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private AnchorPane loginAnchorPane;

    @FXML
    private TextField userLoginField;

    @FXML
    private Label operatorLabel;

    @FXML
    private Label firmLabel;

    @FXML
    private JFXButton loginButton;

    @FXML
    private TextField userPasswordField;

    boolean isClientOk;

    private static final String IDLE_BUTTON_STYLE = "-fx-background-color:  #61a1c7; -fx-background-radius: 15px;";
    private static final String HOVERED_BUTTON_STYLE = "-fx-background-color: #ce5c2e; -fx-background-radius: 15px;";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginButton.setStyle(IDLE_BUTTON_STYLE);
        loginButton.setOnMouseEntered(e -> loginButton.setStyle(HOVERED_BUTTON_STYLE));
        loginButton.setOnMouseExited(e -> loginButton.setStyle(IDLE_BUTTON_STYLE));
    }

    @FXML
    void loginToImporter(ActionEvent event) {
        if (!userPasswordField.getText().isBlank()  && !userLoginField.getText().isBlank()){
            if(userLoginField.getText().equals("Admin"))
                isClientOk = true;
            if (isClientOk){
                try{
                    Stage stage = (Stage) mainAnchorPane.getScene().getWindow();
                    stage.close();
                    Parent root = FXMLLoader.load(getClass().getResource(("loginScreen.fxml")));
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                }catch(IOException e){
                    System.out.println(e);
                    Alert alert = new Alert(Alert.AlertType.ERROR,"  Skontaktuj się z dostawcą oprogramowania !");
                    alert.setHeaderText("  Wystąpił nieoczekiwany błąd !");
                    alert.show();
                }

            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR,"  Sprawdź poprawność wprowadzonych danych !");
                alert.setHeaderText("  Autoryzacja nie możliwa !");
                alert.show();
            }
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR,"  Uzupełnij wszystkie pola !");
            alert.setHeaderText("  Autoryzacja nie możliwa !");
            alert.show();
        }
    }


}
