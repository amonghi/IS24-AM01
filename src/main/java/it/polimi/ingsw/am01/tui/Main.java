package it.polimi.ingsw.am01.tui;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        TuiApplication app = new TuiApplication();
        Runtime.getRuntime().addShutdownHook(new Thread(app::shutdown));
        app.start();
    }
}
