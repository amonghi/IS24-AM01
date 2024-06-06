package it.polimi.ingsw.am01.client.tui.command.parser;

import it.polimi.ingsw.am01.client.tui.command.CommandContext;

public interface Parser {
    Result parse(CommandContext context, String cmd) throws ParseException;

    Completion complete(String partial) throws ParseException;

    record Result(int consumed, boolean isFullMatch) {
    }

    record Completion(String text, int autoWritableChars) {
        public static Completion completable(String text) {
            return new Completion(text, text.length());
        }

        public static Completion nonCompletable(String text) {
            return new Completion(text, 0);
        }

        public Completion {
            if (autoWritableChars < 0 || autoWritableChars > text.length()) {
                throw new IndexOutOfBoundsException();
            }
        }

        public boolean isFullyCompletable() {
            return autoWritableChars == text.length();
        }

        public Completion concat(Completion next) {
            int i = this.autoWritableChars +
                    (this.isFullyCompletable() ? next.autoWritableChars : 0);

            return new Completion(this.text + next.text, i);
        }
    }
}
