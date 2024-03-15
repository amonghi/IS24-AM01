package it.polimi.ingsw.am01.model;

import java.util.Optional;

public interface DrawSource {
    Optional<Card> draw();
}