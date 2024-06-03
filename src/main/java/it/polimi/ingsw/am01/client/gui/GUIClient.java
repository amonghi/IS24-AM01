package it.polimi.ingsw.am01.client.gui;

import it.polimi.ingsw.am01.client.View;
import javafx.application.Application;
import javafx.stage.Stage;

public class GUIClient extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        new View();
        new GUIView(stage);
    }
}
