package it.polimi.ingsw.am01.model.event;

/**
 * This event is emitted when a game starts (transit to {@code SETUP_STARTING_CARD_SIDE} status).
 */
public record GameStartedEvent() implements GameManagerEvent {
}
