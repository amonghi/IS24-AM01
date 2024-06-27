package it.polimi.ingsw.am01.client.tui;

import it.polimi.ingsw.am01.client.tui.terminal.Terminal;
import it.polimi.ingsw.am01.client.tui.terminal.TerminalFactory;
import it.polimi.ingsw.am01.client.tui.terminal.UnsupportedPlatformException;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Main {

    private static final String LOG_FILE = "client.log";

    public static void main(String[] args) {
        Logger rootLogger = LogManager.getLogManager().getLogger("");

        Terminal terminal;
        try {
            terminal = TerminalFactory.createTerminal();
        } catch (UnsupportedPlatformException e) {
            rootLogger.log(Level.SEVERE, "Unsupported platform", e);
            System.exit(1);
            return;
        }

        // Since the application runs in the terminal, we can't use it to print logs.
        // So we need to configure the root logger to use a file handler and not the console.
        rootLogger.setUseParentHandlers(false);
        Arrays.stream(rootLogger.getHandlers()).forEach(rootLogger::removeHandler);
        try {
            rootLogger.addHandler(new FileHandler(LOG_FILE));
        } catch (IOException e) {
            // we cant use the logger if we fail to set it up
            // noinspection CallToPrintStackTrace
            e.printStackTrace();

            System.exit(1);
            return;
        }

        new TuiView(terminal);
    }
}
