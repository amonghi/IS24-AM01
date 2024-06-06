package it.polimi.ingsw.am01.client.tui;

import it.polimi.ingsw.am01.client.tui.terminal.Terminal;
import it.polimi.ingsw.am01.client.tui.terminal.UnixTerminal;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Terminal terminal = new UnixTerminal();
        new CodexNaturalisTuiApplication(terminal);
    }
}
