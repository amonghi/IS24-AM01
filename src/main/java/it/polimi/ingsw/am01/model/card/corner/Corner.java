package it.polimi.ingsw.am01.model.card.corner;

import it.polimi.ingsw.am01.model.collectible.Collectible;

import java.util.Optional;

public class Corner {
    private boolean socket;
    private Optional<Collectible> collectible;

    public Corner missing() {
        throw new UnsupportedOperationException("TODO");
    }

    public Corner empty() {
        throw new UnsupportedOperationException("TODO");
    }

    public Corner filled(Collectible collectible) {
        throw new UnsupportedOperationException("TODO");
    }

    public boolean isSocket() {
        throw new UnsupportedOperationException("TODO");
    }

    public Optional<Collectible> getCollectible() {
        throw new UnsupportedOperationException("TODO");
    }
}
