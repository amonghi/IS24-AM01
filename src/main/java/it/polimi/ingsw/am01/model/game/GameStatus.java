package it.polimi.ingsw.am01.model.game;


public enum GameStatus {
    AWAITING_PLAYERS,
    SETUP_STARTING_CARD_SIDE,
    SETUP_COLOR,
    SETUP_OBJECTIVE,
    PLAY_PLACING,
    PLAY_DRAWING,
    FINISHED,
    SUSPENDED
}