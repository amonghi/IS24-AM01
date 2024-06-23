package it.polimi.ingsw.am01.client.tui;

import it.polimi.ingsw.am01.client.tui.terminal.Terminal;
import it.polimi.ingsw.am01.client.tui.terminal.TerminalFactory;
import it.polimi.ingsw.am01.client.tui.terminal.UnsupportedPlatformException;

public class Main {
    public static void main(String[] args) {
        Terminal terminal;
        try {
            terminal = TerminalFactory.createTerminal();
        } catch (UnsupportedPlatformException e) {
            e.printStackTrace();
            System.exit(1);
            return;
        }

        new TuiView(terminal);
    }
}
