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

    public PlayerProfile createProfile(String name) {
        if (this.profiles.containsKey(name)) {
            throw new IllegalArgumentException("A player with that name already exists");
        }

        PlayerProfile newProfile = new PlayerProfile(name);
        this.profiles.put(name, newProfile);
        return newProfile;
    }

    public Optional<PlayerProfile> getProfile(String name) {
        return Optional.ofNullable(this.profiles.get(name));
    }

    public void removeProfile(PlayerProfile profile) {
        if (!this.profiles.containsKey(profile.getName())) {
            throw new NoSuchElementException();
        }

        this.profiles.remove(profile.getName());
    }
}
