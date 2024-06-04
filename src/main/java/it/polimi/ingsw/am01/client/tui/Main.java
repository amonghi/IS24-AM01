package it.polimi.ingsw.am01.client.tui;

import it.polimi.ingsw.am01.client.tui.rendering.Renderer;
import it.polimi.ingsw.am01.client.tui.terminal.Terminal;
import it.polimi.ingsw.am01.client.tui.terminal.UnixTerminal;

public class Main {
    public static void main(String[] args) {
        Terminal terminal = new UnixTerminal();
        new Renderer(terminal, CodexNaturalisTuiApplication.builder());
    }
}
