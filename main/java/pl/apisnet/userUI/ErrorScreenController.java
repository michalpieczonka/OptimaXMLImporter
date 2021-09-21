package pl.apisnet.userUI;

import com.jfoenix.controls.JFXButton;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class ErrorScreenController implements Initializable {

    private static final String IDLE_BUTTON_STYLE = "-fx-background-color:  #fff; -fx-background-radius: 5px;";
    private static final String HOVERED_BUTTON_STYLE = "-fx-background-color: #f57500; -fx-background-radius: 5px;";

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private JFXButton loginButton;

    @FXML
    private JFXButton exitButton;


    @FXML
    void loginToImporter(ActionEvent event) {
        try {
            Desktop.getDesktop().browse(new URL("https://apisnet.pl").toURI());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void exit(ActionEvent event){
        System.exit(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Hovers for buttons
        loginButton.setStyle(IDLE_BUTTON_STYLE);
        loginButton.setOnMouseEntered(e -> loginButton.setStyle(HOVERED_BUTTON_STYLE));
        loginButton.setOnMouseExited(e -> loginButton.setStyle(IDLE_BUTTON_STYLE));

        exitButton.setStyle(IDLE_BUTTON_STYLE);
        exitButton.setOnMouseEntered(e -> exitButton.setStyle(HOVERED_BUTTON_STYLE));
        exitButton.setOnMouseExited(e -> exitButton.setStyle(IDLE_BUTTON_STYLE));
    }
}
