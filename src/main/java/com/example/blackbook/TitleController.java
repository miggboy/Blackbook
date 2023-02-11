package com.example.blackbook;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class TitleController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private SVGPath minimize;

    private double x = 0;
    private double y = 0;

    @FXML
    protected void onTwitterIconClick() throws URISyntaxException, IOException {
        Desktop desk = Desktop.getDesktop();
        desk.browse(new URI("https://twitter.com/miggould"));
    }

    @FXML
    protected void onGithubIconClick() throws URISyntaxException, IOException {
        Desktop desk = Desktop.getDesktop();
        desk.browse(new URI("https://github.com/miggboy"));
    }

    @FXML
    protected void onExitClick(){
        javafx.application.Platform.exit();
    }

    @FXML
    protected void onMinimizeClick(){
        Stage stage = (Stage) minimize.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    protected void onSetupClick(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("primary.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void panePressed(MouseEvent mouseEvent){
        x = mouseEvent.getSceneX();
        y = mouseEvent.getSceneY();
    }

    @FXML
    public void paneDragged(MouseEvent mouseEvent){
        Stage stage = (Stage) minimize.getScene().getWindow();
        stage.setY(mouseEvent.getScreenY() - y);
        stage.setX(mouseEvent.getScreenX() - x);
    }
}