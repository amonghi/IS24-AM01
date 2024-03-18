package it.polimi.ingsw.am01.model.player;
//TODO: javadoc
/**
 * ...
 */
public class PlayerProfile {
    private final String name;

    /**
     * ...
     * @param name
     */
    public PlayerProfile(String name) {
        this.name = name;
    }

    /**
     * ...
     * @return
     */
    public String getName(){
        return name;
    }

    /**
     * ...
     * @return
     */
    public boolean isConnected() {
        throw new UnsupportedOperationException("TODO");
    }
}