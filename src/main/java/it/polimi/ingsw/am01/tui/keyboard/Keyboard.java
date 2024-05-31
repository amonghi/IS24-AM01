package it.polimi.ingsw.am01.tui.keyboard;

import it.polimi.ingsw.am01.eventemitter.EventEmitter;
import it.polimi.ingsw.am01.eventemitter.EventEmitterImpl;
import it.polimi.ingsw.am01.eventemitter.EventListener;

import java.io.IOException;

public class Keyboard implements EventEmitter<Key> {
    private static Keyboard instance;

    public static Keyboard getInstance() {
        if (instance == null) {
            instance = new Keyboard();
        }

        return instance;
    }

    private final EventEmitterImpl<Key> emitter;
    private final KeyDecoder decoder;

    private Keyboard() {
        emitter = new EventEmitterImpl<>();
        decoder = new KeyDecoder(System.in);
        new Thread(this::run).start();
    }

    private void run() {
        while (true) {
            try {
                Key key = this.decoder.nextSupportedKey();
                this.emitter.emit(key);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    @Override
    public Registration onAny(EventListener<Key> listener) {
        return emitter.onAny(listener);
    }

    @Override
    public <T extends Key> Registration on(Class<T> eventClass, EventListener<T> listener) {
        return emitter.on(eventClass, listener);
    }

    @Override
    public boolean unregister(Registration registration) {
        return emitter.unregister(registration);
    }
}
