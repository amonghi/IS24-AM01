package it.polimi.ingsw.am01.model.exception;

import it.polimi.ingsw.am01.model.objective.Objective;
import it.polimi.ingsw.am01.model.player.PlayerProfile;

/**
 * This exception is thrown when a player specifies an invalid or nonexistent {@link Objective}.
 * @see it.polimi.ingsw.am01.model.game.Game#selectObjective(PlayerProfile, Objective)
 * @see it.polimi.ingsw.am01.controller.Controller
 */
public class InvalidObjectiveException extends Exception {
    public InvalidObjectiveException() {
        super();
    }
}
