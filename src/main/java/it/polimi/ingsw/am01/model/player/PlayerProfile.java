package it.polimi.ingsw.am01.model.player;

/**
 * PlayerProfile represents a player entity
 */
public class PlayerProfile {
    private final String name;

    /**
     * Constructs a PlayerProfile and sets name
     *
     * @param name The name of the player
     */
    public PlayerProfile(String name) {
        this.name = name;
    }

    /**
     * Provides the name of player
     *
     * @return The name of player
     */
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerProfile that = (PlayerProfile) o;

        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }
}