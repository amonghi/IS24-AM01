package it.polimi.ingsw.am01.client.tui.terminal;

import com.sun.jna.Platform;

public class TerminalFactory {
    private TerminalFactory() {
    }

    public static Terminal createTerminal() throws UnsupportedPlatformException {
        if (Platform.isLinux()) {
            return new LinuxTerminal();
        }

        if (Platform.isWindows()) {
            return new WindowsTerminal();
        }

        throw new UnsupportedPlatformException();
    }
}
