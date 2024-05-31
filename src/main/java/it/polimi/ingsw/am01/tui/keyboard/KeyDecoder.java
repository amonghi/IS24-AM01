package it.polimi.ingsw.am01.tui.keyboard;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class KeyDecoder {
    private final InputStream inputStream;

    public KeyDecoder(InputStream inputStream) {
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

        if (c1 == '\177') {
            return Optional.of(new Key.Backspace());
        }

        if (c1 == '\r') {
            return Optional.of(new Key.Enter());
        }

        if (c1 == '\t') {
            return Optional.of(new Key.Tab());
        }

        if (c1 != '\033') {
            boolean ctrl = false;
            if (c1 <= 31) {
                ctrl = true;
                c1 = (char) (c1 + 96);
            }

            return Optional.of(new Key.Character(ctrl, c1));
        }

        char c2 = this.readChar();
        Key k = switch (c2) {
            case '[' -> {
                char c3 = this.readChar();
                yield switch (c3) {
                    case 'A' -> new Key.Arrow(Key.Arrow.Direction.UP);
                    case 'B' -> new Key.Arrow(Key.Arrow.Direction.DOWN);
                    case 'C' -> new Key.Arrow(Key.Arrow.Direction.RIGHT);
                    case 'D' -> new Key.Arrow(Key.Arrow.Direction.LEFT);
                    case 'H' -> new Key.Home();
                    case 'F' -> new Key.End();

                    case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
                        int c4 = System.in.read();
                        if (c4 != '~') {
                            yield null;
                        }

                        yield switch (c3) {
                            case '1', '7':
                                yield new Key.Home();
                            case '3':
                                yield new Key.Del();
                            case '4', '8':
                                yield new Key.End();
                            case '5':
                                yield new Key.PageUp();
                            case '6':
                                yield new Key.PageDown();
                            default:
                                yield null;
                        };
                    }

                    default -> null;
                };
            }
            case 'O' -> {
                char c3 = this.readChar();
                yield switch (c3) {
                    case 'H' -> new Key.Home();
                    case 'F' -> new Key.End();
                    default -> null;
                };
            }
            default -> null;
        };

        return Optional.ofNullable(k);
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
