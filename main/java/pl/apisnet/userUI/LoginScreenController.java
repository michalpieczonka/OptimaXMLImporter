package pl.apisnet.userUI;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import pl.apisnet.backEND.Optima;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginScreenController implements Initializable {
    private Optima mainOptima; //Temporary Optima object to check if given user input is correct to connect
    private OptimaHolder optimaMain; //Singletone -> GLOBAL instance of Optima class

    private static final String IDLE_BUTTON_STYLE = "-fx-background-color:  #61a1c7; -fx-background-radius: 15px;";
    private static final String HOVERED_BUTTON_STYLE = "-fx-background-color: #ce5c2e; -fx-background-radius: 15px;";

    @Override
    public void initialize(URL url, ResourceBundle rb){
        loadCompanys(); //Loading companys from Optima

        System.out.println(DatabaseHolder.getInstance().getDbConf().getCustomer().getCustomerNIP());

        optimaLoginButton.setStyle(IDLE_BUTTON_STYLE);
        optimaLoginButton.setOnMouseEntered(e -> optimaLoginButton.setStyle(HOVERED_BUTTON_STYLE));
        optimaLoginButton.setOnMouseExited(e -> optimaLoginButton.setStyle(IDLE_BUTTON_STYLE));

        optimaDirButton.setStyle(IDLE_BUTTON_STYLE);
        optimaDirButton.setOnMouseEntered(e -> optimaDirButton.setStyle(HOVERED_BUTTON_STYLE));
        optimaDirButton.setOnMouseExited(e -> optimaDirButton.setStyle(IDLE_BUTTON_STYLE));
    }

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private AnchorPane loginAnchorPane;

    @FXML
    private JFXComboBox<String> optimaCompanyName;

    @FXML
    private TextField optimaOperatorField;

    @FXML
    private TextField optimaInstallationDir;

    @FXML
    private Label operatorLabel;

    @FXML
    private Label firmLabel;

    @FXML
    private JFXButton optimaDirButton;

    @FXML
    private JFXButton optimaLoginButton;

    /**
     * Method responsible for checking if User input data are correct to get connection to Optima
     * If data is correct, initializing GLOBAL Optima object (Located in OptimaHolder class)
     * Then moving to other Scene
     */
    @FXML
    void loginToOptima(ActionEvent event) {
        if (optimaInstallationDir.getText() != null && !optimaOperatorField.getText().isBlank() && optimaCompanyName.getSelectionModel().getSelectedItem() !=null ){
            mainOptima = new Optima(optimaOperatorField.getText(),"",optimaCompanyName.getSelectionModel().getSelectedItem(),optimaInstallationDir.getText());

            if (mainOptima.connectToOptima()){
                optimaMain = OptimaHolder.getInstance();
                optimaMain.setMainOptima(mainOptima);
                try{
                    Stage stage = (Stage) mainAnchorPane.getScene().getWindow();
                    stage.close();
                    Parent root = FXMLLoader.load(getClass().getResource(("importXMLFileScreen.fxml")));
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

    /**
     * Method responsible for geting from user Optima installation directory and setting it into variable optimaInstallationDir
     */
    @FXML
    void setOptimaDirectory(ActionEvent event) {

        final DirectoryChooser dirChooser = new DirectoryChooser();
        File file = dirChooser.showDialog(mainAnchorPane.getScene().getWindow());
        if (file !=null){
            optimaInstallationDir.setText(file.getAbsolutePath());
        }
    }


    /**
     * Method responsible for loading all Companys names from Optima, that are necessary for loggin into Optima.
     */
    private void loadCompanys(){
        optimaCompanyName.getItems().add(0,"Test3");
    }

}
