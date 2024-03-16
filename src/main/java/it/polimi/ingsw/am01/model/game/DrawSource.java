package it.polimi.ingsw.am01.model.game;

import it.polimi.ingsw.am01.model.card.Card;

import java.util.Optional;

public interface DrawSource {
    Optional<Card> draw();
}