package it.polimi.ingsw.am01.tui;

import com.sun.jna.Platform;
import it.polimi.ingsw.am01.tui.terminal.Terminal;
import it.polimi.ingsw.am01.tui.terminal.UnixTerminal;
import it.polimi.ingsw.am01.tui.terminal.WindowsTerminal;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Terminal terminal = Platform.isWindows() ? new WindowsTerminal() : new UnixTerminal();

        CodexNaturalisTuiApplication app = new CodexNaturalisTuiApplication(terminal);
        Runtime.getRuntime().addShutdownHook(new Thread(app::shutdown));
    }
}
