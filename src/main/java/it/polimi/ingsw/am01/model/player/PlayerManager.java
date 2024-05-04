package it.polimi.ingsw.am01.model.player;

import it.polimi.ingsw.am01.eventemitter.Event;
import it.polimi.ingsw.am01.eventemitter.EventEmitter;
import it.polimi.ingsw.am01.eventemitter.EventEmitterImpl;
import it.polimi.ingsw.am01.eventemitter.EventListener;
import it.polimi.ingsw.am01.model.event.PlayerAuthenitcatedEvent;
import it.polimi.ingsw.am01.model.exception.NameAlreadyTakenException;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

public class PlayerManager implements EventEmitter<Event> {
    private final EventEmitterImpl<Event> emitter;
    private final Map<String, PlayerProfile> profiles;

    public PlayerManager() {
        profiles = new HashMap<>();
        emitter = new EventEmitterImpl<>();
    }

    /**
     * Create a new profile and save it in this {@link PlayerManager}
     *
     * @param name The name of the player
     * @return The created profile
     * @throws IllegalArgumentException if a profile with that same name already exists
     */
    public synchronized PlayerProfile createProfile(String name) throws NameAlreadyTakenException {
        if (this.profiles.containsKey(name)) {
            throw new NameAlreadyTakenException(name);
        }

        PlayerProfile newProfile = new PlayerProfile(name);
        this.profiles.put(name, newProfile);

        emitter.emit(new PlayerAuthenitcatedEvent(newProfile));

        return newProfile;
    }

    /**
     * @param name the name of the profile to get
     * @return an {@link Optional} containing the {@link PlayerProfile} with that name if such a profile exists, {@link Optional#empty()} otherwise.
     */
    public synchronized Optional<PlayerProfile> getProfile(String name) {
        return Optional.ofNullable(this.profiles.get(name));
    }

    /**
     * Unregister a profile when a player leaves
     *
     * @param profile the profile to un-register
     * @throws NoSuchElementException if the specified profile is not registered in this {@link PlayerManager}
     */
    public synchronized void removeProfile(PlayerProfile profile) {
        if (!this.profiles.containsKey(profile.getName())) {
            throw new NoSuchElementException();
        }

        this.profiles.remove(profile.getName());
    }

    @Override
    public Registration onAny(EventListener<Event> listener) {
        return emitter.onAny(listener);
    }

    @Override
    public <T extends Event> Registration on(Class<T> eventClass, EventListener<T> listener) {
        return emitter.on(eventClass, listener);
    }

    @Override
    public boolean unregister(Registration registration) {
        return emitter.unregister(registration);
    }
}
