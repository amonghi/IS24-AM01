package it.polimi.ingsw.am01.tui;

import it.polimi.ingsw.am01.tui.terminal.Terminal;
import it.polimi.ingsw.am01.tui.terminal.UnixTerminal;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Terminal terminal = new UnixTerminal();
        new CodexNaturalisTuiApplication(terminal);
    }
}
