package it.polimi.ingsw.am01.tui;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class KeyboardReader {
    private final InputStream inputStream;

    public KeyboardReader(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public Key nextSupportedKey() throws IOException {
        Optional<Key> k;

        do {
            k = this.nextKey();
        } while (k.isEmpty());

        return k.get();
    }

    public Optional<Key> nextKey() throws IOException {
        char c1 = this.readChar();
        if (c1 != '\033') {
            return Optional.of(new Key.Character(c1));
        }

        char c2 = this.readChar();
        if (c2 != '[') {
            return Optional.empty();
        }

        char c3 = this.readChar();
        Key arrow = switch (c3) {
            case 'A' -> new Key.Arrow(Key.Arrow.Direction.UP);
            case 'B' -> new Key.Arrow(Key.Arrow.Direction.DOWN);
            case 'C' -> new Key.Arrow(Key.Arrow.Direction.RIGHT);
            case 'D' -> new Key.Arrow(Key.Arrow.Direction.LEFT);
            default -> null;
        };
        return Optional.ofNullable(arrow);
    }

    private char readChar() throws IOException {
        int in = this.inputStream.read();
        if (in == -1) {
            // as per the docs, read() will return "-1 if the end of the stream is reached"
            throw new EOFException();
        }

        return (char) in;
    }
}
