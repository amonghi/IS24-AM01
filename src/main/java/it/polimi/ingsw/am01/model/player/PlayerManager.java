package it.polimi.ingsw.am01.model.player;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

public class PlayerManager {
    private final Map<String, PlayerProfile> profiles;

    public PlayerManager() {
        profiles = new HashMap<>();
    }

    /**
     * Create a new profile and save it in this {@link PlayerManager}
     *
     * @param name The name of the player
     * @return The created profile
     * @throws IllegalStateException if a profile with that same name already exists
     */
    public PlayerProfile createProfile(String name) {
        if (this.profiles.containsKey(name)) {
            throw new IllegalStateException("A player with that name already exists");
        }

        PlayerProfile newProfile = new PlayerProfile(name);
        this.profiles.put(name, newProfile);
        return newProfile;
    }

    /**
     * @param name the name of the profile to get
     * @return an {@link Optional} containing the {@link PlayerProfile} with that name if such a profile exists, {@link Optional#empty()} otherwise.
     */
    public Optional<PlayerProfile> getProfile(String name) {
        return Optional.ofNullable(this.profiles.get(name));
    }

    /**
     * Unregister a profile when a player leaves
     *
     * @param profile the profile to un-register
     */
    public void removeProfile(PlayerProfile profile) {
        if (!this.profiles.containsKey(profile.getName())) {
            throw new NoSuchElementException();
        }

        this.profiles.remove(profile.getName());
    }
}
