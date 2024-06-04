package it.polimi.ingsw.am01.client.gui;

import javafx.application.Application;
import javafx.stage.Stage;

public class GUIClient extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        new GUIView(stage);
    }
}
