package it.polimi.ingsw.am01.client.tui;

import com.sun.jna.Platform;
import it.polimi.ingsw.am01.client.tui.terminal.Terminal;
import it.polimi.ingsw.am01.client.tui.terminal.UnixTerminal;
import it.polimi.ingsw.am01.client.tui.terminal.WindowsTerminal;

public class Main {
    public static void main(String[] args) {
        Terminal terminal = Platform.isWindows()
                ? new WindowsTerminal()
                : new UnixTerminal();

        new TuiView(terminal);
    }
}
