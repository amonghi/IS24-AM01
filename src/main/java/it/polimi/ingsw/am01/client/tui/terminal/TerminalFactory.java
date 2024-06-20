package it.polimi.ingsw.am01.client.tui.terminal;

import com.sun.jna.Platform;

/**
 * A factory class that creates an instance of {@link Terminal} based on the current {@link Platform}.
 */
public class TerminalFactory {
    private TerminalFactory() {
    }

    /**
     * Create an instance of {@link Terminal} based on the current {@link Platform}.
     *
     * @return an instance of {@link Terminal}
     * @throws UnsupportedPlatformException if the current platform is not supported
     */
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
