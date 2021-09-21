package pl.apisnet.userUI;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
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
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class LoginScreenController implements Initializable {
    private Optima mainOptima; //Temporary Optima object to check if given user input is correct to connect
    private OptimaHolder optimaMain; //Singletone -> GLOBAL instance of Optima class
    private String[] optimaCompanys; //Array to store avaliable companys downloaded from Optima


    private static final String IDLE_BUTTON_STYLE = "-fx-background-color:  #fff; -fx-background-radius: 8px;";
    private static final String HOVERED_BUTTON_STYLE = "-fx-background-color: #f57500; -fx-background-radius: 8px;";

    @Override
    public void initialize(URL url, ResourceBundle rb){
        loadCompanys(); //Loading companys from Optima
        loadLastUserLoginDetails(); //Loading data from last login




        //Settings labels color for last login data details
        optimaLastCompany.setTextFill(Color.rgb(245,117,0));
        usernameLabel.setTextFill(Color.rgb(245,117,0));
        optimaLastOperator.setTextFill(Color.rgb(245,117,0));
        optimaLastDir.setTextFill(Color.rgb(245,117,0));

        List<Button> buttonsList = Arrays.asList(optimaLoginButton,optimaDirButton,setLastSettingsButton,exitButton,logoutButton);
        for (Button b: buttonsList){
            b.setStyle(IDLE_BUTTON_STYLE);
            b.setOnMouseEntered(e -> b.setStyle(HOVERED_BUTTON_STYLE));
            b.setOnMouseExited(e -> b.setStyle(IDLE_BUTTON_STYLE));
        }


    }

    @FXML
    private StackPane mainPane;

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

    @FXML
    private JFXButton logoutButton;

    @FXML
    private JFXButton exitButton;

    /**
     * Method responsible for closing application
     */
    @FXML
    void exit(ActionEvent event) {
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        dialogLayout.setStyle("-fx-background-color: #525252");

        Text exitHeader = new Text("Wyjście");
        exitHeader.setStyle("-fx-font-weight: bold; -fx-font-size: 15;");
        exitHeader.setFill(Color.WHITE);
        Text exitConfirmation = new Text("Czy na pewno chcesz wyjść z programu ?");
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


    /**
     * Method responsible for logging out from Customer account
     */
    @FXML
    void logout(ActionEvent event) {
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        dialogLayout.setStyle("-fx-background-color: #525252");

        Text exitHeader = new Text("Wyjście");
        exitHeader.setStyle("-fx-font-weight: bold; -fx-font-size: 15;");
        exitHeader.setFill(Color.WHITE);
        Text exitConfirmation = new Text("Czy na pewno chcesz wylogować się z programu ?");
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
                try{
                    DatabaseHolder.getInstance().getDbConf().logoutCustomer();
                    Stage stage = (Stage) mainAnchorPane.getScene().getWindow();
                    stage.close();
                    Stage newStage = new Stage();
                    Parent root = FXMLLoader.load(getClass().getResource(("mainLoginScreen.fxml")));
                    Scene scene = new Scene(root);
                    newStage.initStyle(StageStyle.TRANSPARENT);
                    newStage.setTitle("AIMPORTER");
                    newStage.setScene(scene);
                    newStage.show();
                }catch (IOException e){

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

    /**
     * Method responsible for checking if User input data are correct to get connection to Optima
     * If data is correct, initializing GLOBAL Optima object (Located in OptimaHolder class)
     * Then moving to other Scene
     */
    @FXML
    void loginToOptima(ActionEvent event) {
        mainOptima = null;
        if (optimaInstallationDir.getText() != null && !optimaOperatorField.getText().isBlank() && optimaCompanyName.getSelectionModel().getSelectedItem() !=null ){
            mainOptima = new Optima(optimaOperatorField.getText(),"",optimaCompanyName.getSelectionModel().getSelectedItem(),optimaInstallationDir.getText());

            if (mainOptima.connectToOptima()){
                setLastSettingsButton.setDisable(true);
                optimaMain = OptimaHolder.getInstance();
                optimaMain.setMainOptima(mainOptima);

                //Checking wheter Optima Customer Version is up to date (with version stored in Global Database)
                //If Customer Optima version is up to date
                if(optimaMain.getMainOptima().getOptimaVersion().equals(DatabaseHolder.getInstance().getDbConf().getGlobalOptimaVersion())) {
                    //Update Customer Optima login details
                    DatabaseHolder.getInstance().getDbConf().updateUserOptimaDetails(optimaOperatorField.getText(), optimaOperatorPassField.getText(), optimaCompanyName.getSelectionModel().getSelectedItem(), optimaInstallationDir.getText());
                    try {
                        Stage stage = (Stage) mainAnchorPane.getScene().getWindow();
                        stage.close();
                        Stage newStage = new Stage();
                        Parent root = FXMLLoader.load(getClass().getResource(("importXMLFileScreen.fxml")));
                        Scene scene = new Scene(root);
                        newStage.initStyle(StageStyle.DECORATED);
                        newStage.setTitle("AIMPORTER");
                        newStage.setScene(scene);
                        newStage.show();
                    } catch (IOException e) {
                        System.out.println(e);
                        Alert alert = new Alert(Alert.AlertType.ERROR, "  Skontaktuj się z dostawcą oprogramowania !");
                        alert.setHeaderText("  Wystąpił nieoczekiwany błąd !");
                        alert.show();
                    }
                } //If Customer Optima version is not up to date
                else{
                    mainOptima.disconnectFromOptima();
                    optimaLoginButton.setDisable(true);
                    mainOptima = null;
                    setLastSettingsButton.setDisable(true);
                    Alert alert = new Alert(Alert.AlertType.ERROR,"Twoja wersja programu nie jest aktualna w stosunku do programu Comarch Optima !\nSkontaktuj się z dostawcą oprogramowania");
                    alert.setHeaderText("  Autoryzacja nie możliwa !");
                    alert.show();
                }  //If input for Optima details is incorrect and can not login to Optima
            }else{
                mainOptima.disconnectFromOptima();
                mainOptima = null;
                Alert alert = new Alert(Alert.AlertType.ERROR,"  Sprawdź poprawność wprowadzonych danych !");
                alert.setHeaderText("  Autoryzacja w Optima nie możliwa !");
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
        mainOptima = null;
        CustomerOptimaDetails optimaDetailsTmp = DatabaseHolder.getInstance().getDbConf().getCustomer().getCustomerOptimaDetails();
        mainOptima = new Optima(optimaDetailsTmp.getOperator(),optimaDetailsTmp.getOperatorPassword(),optimaDetailsTmp.getCompanyName(),optimaDetailsTmp.getOptimaPath());

        if (mainOptima.connectToOptima()){
            optimaLoginButton.setDisable(true);
            optimaMain = OptimaHolder.getInstance();
            optimaMain.setMainOptima(mainOptima);
            //Checking wheter Optima Customer Version is up to date (with version stored in Global Database)
            //If Customer Optima version is up to date
            if(optimaMain.getMainOptima().getOptimaVersion().equals(DatabaseHolder.getInstance().getDbConf().getGlobalOptimaVersion())) {
                try{
                    Stage stage = (Stage) mainAnchorPane.getScene().getWindow();
                    stage.close();
                    Stage newStage = new Stage();
                    Parent root = FXMLLoader.load(getClass().getResource(("importXMLFileScreen.fxml")));
                    Scene scene = new Scene(root, 1024, 768);
                    newStage.initStyle(StageStyle.DECORATED);
                    newStage.setTitle("AIMPORTER");
                    newStage.setScene(scene);
                    newStage.show();
                }catch(IOException e){
                    Alert alert = new Alert(Alert.AlertType.ERROR,"  Skontaktuj się z dostawcą oprogramowania !");
                    alert.setTitle("Wystąpił błąd !");
                    alert.setHeaderText("  Wystąpił nieoczekiwany błąd !");
                    alert.show();
                }

            } else{ //If Customer Optima version is not up to date
                optimaLoginButton.setDisable(true);
                mainOptima.disconnectFromOptima();
                mainOptima = null;
                setLastSettingsButton.setDisable(true);
                Alert alert = new Alert(Alert.AlertType.ERROR,"Twoja wersja programu nie jest aktualna w stosunku do programu Comarch Optima !\nSkontaktuj się z dostawcą oprogramowania");
                alert.setTitle("Wystąpił błąd !");
                alert.setHeaderText("  Autoryzacja nie możliwa !");alert.show();

            }
        }else{
            mainOptima.disconnectFromOptima();
            mainOptima = null;
            Alert alert = new Alert(Alert.AlertType.ERROR,"  Sprawdź poprawność wprowadzonych danych !");
            alert.setTitle("Wystąpił błąd !");
            alert.setHeaderText("  Autoryzacja w Optima nie możliwa !");
            alert.show();
        }
    }

    /**
     * Method responsible for loading all Companys names from Optima, that are necessary for loggin into Optima.
     */
    private void loadCompanys(){
        Optima optemp = new Optima();
        optimaCompanys = optemp.getCompanysFromOptima();
        for (int i=0;i<optimaCompanys.length; i++){
            optimaCompanyName.getItems().add(i,optimaCompanys[i]);
            //System.out.println(optimaCompanys[i]);
        }
    }

    /**
     * Method responsible for loading all last used data from login into Optima and stored into DataBase.
     */
    private void loadLastUserLoginDetails(){
        Customer customer = DatabaseHolder.getInstance().getDbConf().getCustomer();
        CustomerOptimaDetails customerOptimaDetails = customer.getCustomerOptimaDetails();

        usernameLabel.setText(customer.getCustomerLogin());

        if (customerOptimaDetails.getCompanyName().isBlank())
            optimaLastCompany.setText(" BRAK DANYCH");
        else
            optimaLastCompany.setText(" "+ customerOptimaDetails.getCompanyName());
        if (customerOptimaDetails.getOperator().isBlank())
            optimaLastOperator.setText(" BRAK DANYCH");
        else
            optimaLastOperator.setText(" "+ customerOptimaDetails.getOperator());
        if(customerOptimaDetails.getOptimaPath().isBlank())
            optimaLastDir.setText(" BRAK DANYCH");
        else
            optimaLastDir.setText(" "+ customerOptimaDetails.getOptimaPath());

        //If any of necessary fields are missed, then user can not just login to Optima
        if (customerOptimaDetails.getCompanyName().isBlank() || customerOptimaDetails.getOperator().isBlank() || customerOptimaDetails.getCompanyName().isBlank() ){
            setLastSettingsButton.setDisable(true);
        }
    }

}
