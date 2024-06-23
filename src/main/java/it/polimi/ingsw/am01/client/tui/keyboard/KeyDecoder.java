package it.polimi.ingsw.am01.client.tui.keyboard;

import it.polimi.ingsw.am01.client.tui.rendering.ansi.Ansi;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class KeyDecoder {
    private final InputStreamReader reader;

    public KeyDecoder(InputStream inputStream) {
        this.reader = new InputStreamReader(inputStream);
    }

    public Key nextKey() throws IOException {
        Key k;

        do {
            k = this.tryNextKey();
        } while (k == null);

        return k;
    }

    private Key tryNextKey() throws IOException {
        char c1 = this.readChar();

        if (c1 == '\177') {
            return new Key.Backspace();
        }

        if (c1 == '\r') {
            return new Key.Enter();
        }

        if (c1 == '\t') {
            return new Key.Tab();
        }

        if (c1 == Ansi.ESC) {
            return parseEscapeSequence();
        }

        if (c1 < 32) {
            char c = (char) (c1 + 96);
            return new Key.Ctrl(c);
        }

        // ascii printable characters are between 32 and 127
        return new Key.Character(c1);
    }

    private Key parseEscapeSequence() throws IOException {
        char c2 = this.readChar();
        if (c2 != '[') {
            return new Key.Alt(c2);
        }

        char c3 = this.readChar();
        return switch (c3) {
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

    private char readChar() throws IOException {
        int in = this.reader.read();
        if (in == -1) {
            // as per the docs, read() will return "-1 if the end of the stream is reached"
            throw new EOFException();
        }

        return (char) in;
    }
}
