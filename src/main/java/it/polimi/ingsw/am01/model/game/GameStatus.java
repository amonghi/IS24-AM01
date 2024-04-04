package it.polimi.ingsw.am01.model.game;


public enum GameStatus {
    AWAITING_PLAYERS,
    SETUP_STARTING_CARD_SIDE,
    SETUP_COLOR,
    SETUP_OBJECTIVE,
    AWAITING_START,
    PLAY,
    SECOND_LAST_TURN,
    LAST_TURN,
    FINISHED,
    SUSPENDED
}