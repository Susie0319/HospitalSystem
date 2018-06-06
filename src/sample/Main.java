package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.util.List;
import sample.MySQLConnector;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader login = new FXMLLoader();
        Parent root = login.load(getClass().getResourceAsStream("Login.fxml"));

        primaryStage.setTitle("医院简易挂号管理系统登陆");
        primaryStage.setScene(new Scene(root, 600, 400));

        LoginController controllerLogin = login.getController();
        controllerLogin.initView();
        primaryStage.show();


    }

    public static void main(String[] args) {
        launch(args);
    }
}


