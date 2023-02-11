package com.example.blackbook;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;

public class Blackbook extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Blackbook.class.getResource("title.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("BlackBook");

        URL url = Blackbook.class.getResource("leviathan.png");
        String str = url.toString();
        Image icon = new Image(str);
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}