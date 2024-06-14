package it.polimi.ingsw.am01.client.tui.terminal;

import com.sun.jna.LastErrorException;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Structure;
import it.polimi.ingsw.am01.client.tui.rendering.Dimensions;
import it.polimi.ingsw.am01.eventemitter.EventEmitterImpl;
import it.polimi.ingsw.am01.eventemitter.EventListener;
import sun.misc.Signal;

public class LinuxTerminal implements Terminal {
    private LibC.Termios originalAttributes;
    private final EventEmitterImpl<ResizeEvent> emitter;

    public LinuxTerminal() {
        this.emitter = new EventEmitterImpl<>();

        Signal.handle(
                new Signal("WINCH"),
                sig -> this.emitter.emit(new ResizeEvent())
        );
    }

    @Override
    public Registration onAny(EventListener<ResizeEvent> listener) {
        return this.emitter.onAny(listener);
    }

    @Override
    public <T extends ResizeEvent> Registration on(Class<T> eventClass, EventListener<T> listener) {
        return this.emitter.on(eventClass, listener);
    }

    @Override
    public boolean unregister(Registration registration) {
        return this.emitter.unregister(registration);
    }

    @Override
    public void enableRawMode() {
        LibC.Termios termios = new LibC.Termios();
        int rc = LibC.INSTANCE.tcgetattr(LibC.SYSTEM_OUT_FD, termios);

        if (rc != 0) {
            throw new IllegalStateException("call to tcgetattr failed with return code " + rc);
        }

        originalAttributes = LibC.Termios.of(termios);

        LibC.INSTANCE.cfmakeraw(termios);
        LibC.INSTANCE.tcsetattr(LibC.SYSTEM_OUT_FD, LibC.TCSAFLUSH, termios);
    }

    @Override
    public void disableRawMode() {
        LibC.INSTANCE.tcsetattr(LibC.SYSTEM_OUT_FD, LibC.TCSAFLUSH, originalAttributes);
    }

    @Override
    public Dimensions getDimensions() {
        final LibC.Winsize winsize = new LibC.Winsize();
        final int rc = LibC.INSTANCE.ioctl(LibC.SYSTEM_OUT_FD, LibC.TIOCGWINSZ, winsize);

        if (rc != 0) {
            throw new IllegalStateException("call to ioctl failed with return code " + rc);
        }

        return Dimensions.of(winsize.ws_col, winsize.ws_row);
    }

    private interface LibC extends Library {

        int SYSTEM_OUT_FD = 0;
        int ISIG = 1, ICANON = 2, ECHO = 10, TCSAFLUSH = 2,
                IXON = 2000, ICRNL = 400, IEXTEN = 100000, OPOST = 1, VMIN = 6, VTIME = 5, TIOCGWINSZ = 0x5413;

        // we're loading the C standard library for POSIX systems
        LibC INSTANCE = Native.load("c", LibC.class);

        int tcgetattr(int fd, Termios termios);

        int tcsetattr(int fd, int optional_actions,
                      Termios termios);

        void cfmakeraw(Termios termios);

        int ioctl(int fd, int opt, Winsize winsize) throws LastErrorException;

        @Structure.FieldOrder(value = {"ws_row", "ws_col", "ws_xpixel", "ws_ypixel"})
        class Winsize extends Structure {
            public short ws_row, ws_col, ws_xpixel, ws_ypixel;
        }

        @Structure.FieldOrder({"c_iflag", "c_oflag", "c_cflag", "c_lflag", "c_line", "c_cc", "c_ispeed", "c_ospeed"})
        class Termios extends Structure {
            public int c_iflag;
            public int c_oflag;
            public int c_cflag;
            public int c_lflag;
            public byte c_line;
            public byte[] c_cc = new byte[19];
            public int c_ispeed;
            public int c_ospeed;

            public Termios() {
            }

            public static Termios of(Termios t) {
                Termios copy = new Termios();
                copy.c_iflag = t.c_iflag;
                copy.c_oflag = t.c_oflag;
                copy.c_cflag = t.c_cflag;
                copy.c_lflag = t.c_lflag;
                copy.c_cc = t.c_cc.clone();
                return copy;
            }

        }

    }

}
