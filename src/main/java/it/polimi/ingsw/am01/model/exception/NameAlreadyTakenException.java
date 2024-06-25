package it.polimi.ingsw.am01.model.exception;

/**
 * @see it.polimi.ingsw.am01.model.player.PlayerManager#createProfile(String)
 */
public class NameAlreadyTakenException extends Exception {
    private final String refusedName;

    public NameAlreadyTakenException(String refusedName) {
        super();
        this.refusedName = refusedName;
    }

    /**
     * @return the refused name
     */
    public String getRefusedName() {
        return refusedName;
    }
}
