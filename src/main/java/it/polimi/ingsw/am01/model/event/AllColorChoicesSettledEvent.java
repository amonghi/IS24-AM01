package it.polimi.ingsw.am01.model.event;

/**
 * This event is emitted when all players have chosen their color and there are no equal preferences.
 * {@code GameStatus} will be set to {@code SETUP_OBJECTIVE} ({@code SETUP_COLOR} phase is finished).
 * @see it.polimi.ingsw.am01.model.game.selectionphase.SelectionPhase
 */
public record AllColorChoicesSettledEvent() implements GameEvent {
}
