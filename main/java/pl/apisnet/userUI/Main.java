package pl.apisnet.userUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import pl.apisnet.backEND.Entities.Customer;

import java.io.IOException;

public class Main extends Application {

    private Stage loginStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.loginStage = primaryStage;
        primaryStage.initStyle(StageStyle.TRANSPARENT);
      //  hibernateLogin();
        openLoginScreen();
    }


    public static void main(String[] args) {
        launch(args);
    }

    private void hibernateLogin(){
        Configuration conf = new Configuration();
        conf.configure("hibernate.cfg.xml");
        conf.addAnnotatedClass(Customer.class);
        SessionFactory factory = conf.buildSessionFactory();
        Session session = factory.getCurrentSession();
        Customer admin = new Customer();
        admin.setLogin("Admin");
        admin.setPassword("Limanowa2021");
        admin.setNIP(123456789);
        session.beginTransaction();
        session.save(admin);
        session.getTransaction().commit();
        factory.close();
    }

    private void openLoginScreen() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(("mainLoginScreen.fxml")));
        loginStage.setTitle("AXMLImporter");
        loginStage.setScene(new Scene(root, 450, 460));
        loginStage.setResizable(false);
        loginStage.show();

    }
}
