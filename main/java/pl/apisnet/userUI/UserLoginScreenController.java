package pl.apisnet.userUI;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import pl.apisnet.backEND.DatabaseConf;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserLoginScreenController implements Initializable {

    @FXML
    private StackPane mainPane;

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
    private JFXButton leaveButton;

    @FXML
    private PasswordField userPasswordField;

    @FXML
    private CheckBox showPassword;

    private Tooltip passTip;
    private SimpleBooleanProperty showPass;
    private Stage stage;

    DatabaseConf dbCon; //Temporar dBCon object to check if given user input is correct to connect
    DatabaseHolder dBHolder; //Singletone -> GLOBAL instance of Optima class

    private static final String IDLE_BUTTON_STYLE = "-fx-background-color:  #fff; -fx-background-radius: 5px;";
    private static final String HOVERED_BUTTON_STYLE = "-fx-background-color: #f57500; -fx-background-radius: 5px;";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Node root = loginAnchorPane;
        setGlobalEventHandler(root);
        //Hovers for buttons
        loginButton.setStyle(IDLE_BUTTON_STYLE);
        loginButton.setOnMouseEntered(e -> loginButton.setStyle(HOVERED_BUTTON_STYLE));
        loginButton.setOnMouseExited(e -> loginButton.setStyle(IDLE_BUTTON_STYLE));

        leaveButton.setStyle(IDLE_BUTTON_STYLE);
        leaveButton.setOnMouseEntered(e -> leaveButton.setStyle(HOVERED_BUTTON_STYLE));
        leaveButton.setOnMouseExited(e -> leaveButton.setStyle(IDLE_BUTTON_STYLE));

        //Additional code to deal with showing password in ToolTip
        showPass = new SimpleBooleanProperty();
        showPass.addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
            if(newValue){
                showPasswordMethod();
            }else{
                hidePasswordMethod();
            }
        });

        passTip = new Tooltip();
        passTip.setShowDelay(Duration.ZERO);
        passTip.setAutoHide(false);
        passTip.setMinWidth(50);

        userPasswordField.setOnKeyTyped(e -> {
            if ( showPass.get() ) {
                showPasswordMethod();
            }
        });

        showPass.bind(showPassword.selectedProperty());
    }

    private void setGlobalEventHandler(Node root){
        root.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
            if (ev.getCode() == KeyCode.ENTER) {
                loginButton.setStyle(HOVERED_BUTTON_STYLE);
                loginButton.fire();
                ev.consume();
            }
            else if (ev.getCode() == KeyCode.ESCAPE){
                leaveButton.setStyle(HOVERED_BUTTON_STYLE);
                leaveButton.fire();
                ev.consume();
            }
        });
    }

    @FXML
    void loginToImporter(ActionEvent event) {
        hidePasswordMethod();
        if (!userPasswordField.getText().isBlank()  && !userLoginField.getText().isBlank()){
            dbCon = new DatabaseConf();
            if (dbCon.userAuthentication(userLoginField.getText().trim(), userPasswordField.getText().trim())){
                dBHolder = DatabaseHolder.getInstance();
                dBHolder.setDbConf(dbCon);
                try{
                    Stage stage = (Stage) mainAnchorPane.getScene().getWindow();
                    stage.close();
                    Stage newStage = new Stage();
                    Parent root = FXMLLoader.load(getClass().getResource(("loginScreen.fxml")));
                    Scene scene = new Scene(root,1024,768);
                    newStage.initStyle(StageStyle.DECORATED);
                    newStage.setTitle("AIMPORTER");
                    newStage.setScene(scene);
                    newStage.show();
                }catch(IOException e){
                    System.out.println(e);
                    Alert alert = new Alert(Alert.AlertType.ERROR,"  Skontaktuj si?? z dostawc?? oprogramowania !");
                    alert.setHeaderText("  Wyst??pi?? nieoczekiwany b????d !");
                    alert.show();
                }

            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR,"  Sprawd?? poprawno???? wprowadzonych danych !");
                alert.setTitle("Wyst??pi?? b????d !");
                alert.setHeaderText("  Autoryzacja nie mo??liwa !");
                alert.show();
            }
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR,"  Uzupe??nij wszystkie pola !");
            alert.setHeaderText("  Autoryzacja nie mo??liwa !");
            alert.show();
        }
    }

    private void showPasswordMethod(){
        stage = (Stage) userLoginField.getScene().getWindow();
        Point2D p = userPasswordField.localToScene(userPasswordField.getBoundsInLocal().getMaxX(), userPasswordField.getBoundsInLocal().getMaxY());
        passTip.setText(userPasswordField.getText());
        passTip.show(userPasswordField,
                p.getX() + stage.getScene().getX() + stage.getX() - 130 ,
                p.getY() + stage.getScene().getY() + stage.getY());
    }

    private void hidePasswordMethod(){
        passTip.setText("");
        passTip.hide();
    }

    @FXML
    void exit(ActionEvent event) {
        hidePasswordMethod();
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


}
