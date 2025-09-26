package com.bwxor.iport;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));

        stage.maximizedProperty().addListener((obs, wasMaximized, isNowMaximized) -> {
            if (isNowMaximized) {
                stage.setMaximized(false); // force back to normal
            }
        });

        stage.getIcons().add(new Image(MainApplication.class.getResourceAsStream("img/icon.png")));

        stage.setWidth(475);
        stage.setHeight(475);

        stage.setMinWidth(475);
        stage.setMinHeight(475);

        stage.setMaxWidth(750);
        stage.setMaxHeight(750);

        stage.setTitle("iport");

        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);

        stage.show();
    }
}
