package it.polimi.ingsw.am01.client.tui.command.parser;

import it.polimi.ingsw.am01.client.tui.command.CommandContext;

/**
 * A parser for a command token.
 */
public interface Parser {
    /**
     * Receive a string that <i>should</i> begin with the token that this parser is supposed to parse.
     * <p>
     * This method is supposed to handle three scenarios,
     * and either return a {@link Result} or throw a {@link ParseException}
     *
     * <table>
     *     <tr>
     *         <th>Scenario</th>
     *         <th>{@link Result#isFullMatch}</th>
     *         <th>throws</th>
     *     </tr>
     *     <tr>
     *         <td>the token is valid and complete</td>
     *         <td>{@code true}</td>
     *         <td></td>
     *     </tr>
     *     <tr>
     *         <td>the token is valid but incomplete</td>
     *         <td>{@code false}</td>
     *         <td></td>
     *     </tr>
     *     <tr>
     *         <td>the token is invalid</td>
     *         <td></td>
     *         <td>{@link ParseException}</td>
     *     </tr>
     * </table>
     *
     * @param context the context derived from the previous tokens
     * @param cmd     the string to parse
     * @return a {@link Result} object
     * @throws ParseException if the token is not valid
     */
    Result parse(CommandContext context, String cmd) throws ParseException;

    /**
     * Provide a completion for the given partial token.
     *
     * @param partial the partial token
     * @return a {@link Completion} object
     * @throws ParseException if the partial token is not valid
     */
    Completion complete(String partial) throws ParseException;

    /**
     * Represent the result of parsing a token.
     *
     * @param consumed    the number of characters consumed
     * @param isFullMatch false if the token that has been read is incomplete
     */
    record Result(int consumed, boolean isFullMatch) {
    }

    /**
     * Represent a completion for a partial token.
     * <p>
     * A completion can be literal text to complete the token, or a hint for the user to complete the token (or both).
     *
     * @param text              the text to complete the token
     * @param autoWritableChars the number of characters that can be automatically written
     */
    record Completion(String text, int autoWritableChars) {
        /**
         * A shorthand for creating a completion that can be fully written automatically.
         *
         * @param text the text to complete the token
         */
        public static Completion completable(String text) {
            return new Completion(text, text.length());
        }

        /**
         * A shorthand for creating a completion that is only a hint to the user and cannot be written automatically.
         *
         * @param text the hint
         */
        public static Completion nonCompletable(String text) {
            return new Completion(text, 0);
        }

        public Completion {
            if (autoWritableChars < 0 || autoWritableChars > text.length()) {
                throw new IndexOutOfBoundsException();
            }
        }

        /**
         * @return true iff {@link #autoWritableChars} is equal to the length of {@link #text}
         */
        public boolean isFullyCompletable() {
            return autoWritableChars == text.length();
        }

        /**
         * Concatenate this completion with another one.
         * <p>
         * <ul>
         *     <li>The {@link #text} of the resulting completion is the concatenation of the two completions.</li>
         *     <li>The {@link #autoWritableChars} of the resulting completion is obtained by summing the two
         *     completions only if the first completion is fully completable.</li>
         * </ul>
         *
         * @param next the next completion
         * @return a new completion that is the concatenation of this and the next completion
         */
        public Completion concat(Completion next) {
            int i = this.autoWritableChars +
                    (this.isFullyCompletable() ? next.autoWritableChars : 0);

            return new Completion(this.text + next.text, i);
        }
    }
}
