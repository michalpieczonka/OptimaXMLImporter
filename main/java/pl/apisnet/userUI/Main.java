package pl.apisnet.userUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private Stage loginStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.loginStage = primaryStage;
        openLoginScreen();
    }


    public static void main(String[] args) {
        launch(args);
    }

    private void openLoginScreen() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(("mainLoginScreen.fxml")));
        loginStage.setTitle("AXMLImporter");
        loginStage.setScene(new Scene(root, 450, 394));
        loginStage.setResizable(false);
        loginStage.show();

    }
}
