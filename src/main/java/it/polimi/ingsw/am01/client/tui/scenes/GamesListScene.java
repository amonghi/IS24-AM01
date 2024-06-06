package it.polimi.ingsw.am01.client.tui.scenes;

import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.component.elements.Composition;
import it.polimi.ingsw.am01.client.tui.component.elements.Text;
import it.polimi.ingsw.am01.client.tui.component.layout.Centered;
import it.polimi.ingsw.am01.client.tui.component.layout.Column;

import java.util.stream.Collectors;

public class GamesListScene implements Composition {

    private final TuiView view;

    public GamesListScene(TuiView view) {
        this.view = view;
    }

    @Override
    public Component compose() {
        if (view.getGames().isEmpty()) {
            return new Text("No game found");
        }

        return Centered.both(new Column(
                view.getGames().entrySet().stream()
                        .map(entry -> {
                            String t = "Game #%d (%d/%d)".formatted(
                                    entry.getKey(),
                                    entry.getValue().currentPlayersConnected(),
                                    entry.getValue().maxPlayers()
                            );
                            return new Text(t);
                        })
                        .collect(Collectors.toList())
        ));
    }
}
