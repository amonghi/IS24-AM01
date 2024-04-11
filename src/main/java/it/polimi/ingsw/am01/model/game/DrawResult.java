package it.polimi.ingsw.am01.model.game;

/**
 * DrawResult represents all possible outcomes of a card draw
 */
public enum DrawResult {
    OK,
    /**
     * The {@link DrawSource} selected is empty
     */
    EMPTY
}