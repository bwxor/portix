package com.bwxor.iport;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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

        stage.setWidth(500);
        stage.setHeight(500);

        stage.setMinWidth(500);
        stage.setMinHeight(500);

        stage.setMaxWidth(750);
        stage.setMaxHeight(750);

        stage.initStyle(StageStyle.TRANSPARENT);

        stage.setTitle("iport");

        Scene scene = new Scene(fxmlLoader.load());
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add(MainApplication.class.getResource("css/onyx.css").toExternalForm());
        stage.setScene(scene);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
