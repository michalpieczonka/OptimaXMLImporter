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
import javafx.stage.StageStyle;
import org.hibernate.dialect.Database;
import pl.apisnet.backEND.Entities.Customer;
import pl.apisnet.backEND.Entities.CustomerOptimaDetails;
import pl.apisnet.backEND.Optima;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginScreenController implements Initializable {
    private Optima mainOptima; //Temporary Optima object to check if given user input is correct to connect
    private OptimaHolder optimaMain; //Singletone -> GLOBAL instance of Optima class

    private static final String IDLE_BUTTON_STYLE = "-fx-background-color:  #fff; -fx-background-radius: 8px;";
    private static final String HOVERED_BUTTON_STYLE = "-fx-background-color: #f57500; -fx-background-radius: 8px;";

    @Override
    public void initialize(URL url, ResourceBundle rb){
        loadCompanys(); //Loading companys from Optima
        loadLastUserLoginDetails(); //Loading data from last login


        optimaLoginButton.setStyle(IDLE_BUTTON_STYLE);
        optimaLoginButton.setOnMouseEntered(e -> optimaLoginButton.setStyle(HOVERED_BUTTON_STYLE));
        optimaLoginButton.setOnMouseExited(e -> optimaLoginButton.setStyle(IDLE_BUTTON_STYLE));

        optimaDirButton.setStyle(IDLE_BUTTON_STYLE);
        optimaDirButton.setOnMouseEntered(e -> optimaDirButton.setStyle(HOVERED_BUTTON_STYLE));
        optimaDirButton.setOnMouseExited(e -> optimaDirButton.setStyle(IDLE_BUTTON_STYLE));

        setLastSettingsButton.setStyle(IDLE_BUTTON_STYLE);
        setLastSettingsButton.setOnMouseEntered(e -> setLastSettingsButton.setStyle(HOVERED_BUTTON_STYLE));
        setLastSettingsButton.setOnMouseExited(e -> setLastSettingsButton.setStyle(IDLE_BUTTON_STYLE));
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

    @FXML
    private Label operatorLabel1;

    @FXML
    private TextField optimaOperatorPassField;

    @FXML
    private AnchorPane userAnchorPane;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label optimaLastOperator;

    @FXML
    private Label optimaLastCompany;

    @FXML
    private Label optimaLastDir;

    @FXML
    private JFXButton setLastSettingsButton;


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
                DatabaseHolder.getInstance().getDbConf().updateUserOptimaDetails("asd","abc","321asd","C:");
                try{
                    Stage stage = (Stage) mainAnchorPane.getScene().getWindow();
                    stage.close();
                    Stage newStage = new Stage();
                    Parent root = FXMLLoader.load(getClass().getResource(("importXMLFileScreen.fxml")));
                    Scene scene = new Scene(root);
                    newStage.initStyle(StageStyle.DECORATED);
                    newStage.setTitle("AIMPORTER");
                    newStage.setScene(scene);
                    newStage.show();
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
    @FXML
    void setLastOptimaSettings(ActionEvent event) {

    }

    /**
     * Method responsible for loading all Companys names from Optima, that are necessary for loggin into Optima.
     */
    private void loadCompanys(){
        optimaCompanyName.getItems().add(0,"Test3");
    }

    /**
     * Method responsible for loading all last used data from login into Optima and stored into DataBase.
     */
    private void loadLastUserLoginDetails(){
        Customer customer = DatabaseHolder.getInstance().getDbConf().getCustomer();
        CustomerOptimaDetails customerOptimaDetails = customer.getCustomerOptimaDetails();
        usernameLabel.setText(customer.getCustomerLogin());

        if (customerOptimaDetails.getCompanyName().isBlank())
            optimaLastCompany.setText("Firma: BRAK DANYCH");
        else
            optimaLastCompany.setText("Firma: "+ customerOptimaDetails.getCompanyName());
        if (customerOptimaDetails.getOperator().isBlank())
            optimaLastOperator.setText("Operator: BRAK DANYCH");
        else
            optimaLastOperator.setText("Operator: "+ customerOptimaDetails.getOperator());
        if(customerOptimaDetails.getOptimaPath().isBlank())
            optimaLastDir.setText("Katalog Optima: BRAK DANYCH");
        else
            optimaLastDir.setText("Katalog Optima: "+ customerOptimaDetails.getOptimaPath());

        //If any of necessary fields are missed, then user can not just login to Optima
        if (customerOptimaDetails.getCompanyName().isBlank() || customerOptimaDetails.getOperator().isBlank() || customerOptimaDetails.getCompanyName().isBlank() ){
            setLastSettingsButton.setDisable(true);
        }
    }

}
