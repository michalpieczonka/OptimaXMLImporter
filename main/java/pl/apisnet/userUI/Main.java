package pl.apisnet.userUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;


public class Main extends Application {

    private Stage loginStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.loginStage = primaryStage;
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        openLoginScreen();
    }


    public static void main(String[] args) {
        launch(args);
    }



    private void openLoginScreen() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(("mainLoginScreen.fxml")));
        loginStage.setTitle("AIMPORTER");
        loginStage.setScene(new Scene(root, 450, 460));
        loginStage.setResizable(false);
        loginStage.show();

    }
}
