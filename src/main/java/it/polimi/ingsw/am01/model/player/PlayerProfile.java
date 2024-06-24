package it.polimi.ingsw.am01.model.player;

/**
 * PlayerProfile represents a player entity
 */
public record PlayerProfile(String name) {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerProfile that = (PlayerProfile) o;

        return name.equals(that.name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return name;
    }
}