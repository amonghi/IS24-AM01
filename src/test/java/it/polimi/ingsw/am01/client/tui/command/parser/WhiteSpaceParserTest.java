package it.polimi.ingsw.am01.client.tui.command.parser;

import it.polimi.ingsw.am01.client.tui.command.CommandContext;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WhiteSpaceParserTest {
    @Nested
    class Required {
        @Test
        void parse() {
            WhiteSpaceParser whiteSpaceParser = new WhiteSpaceParser(true);
            CommandContext context = new CommandContext();

            assertEquals(new Parser.Result(0, false), whiteSpaceParser.parse(context, ""));
            assertEquals(new Parser.Result(1, true), whiteSpaceParser.parse(context, " "));
            assertEquals(new Parser.Result(2, true), whiteSpaceParser.parse(context, "  "));
        }

        @Test
        void completeValid() throws ParseException {
            WhiteSpaceParser whiteSpaceParser = new WhiteSpaceParser(true);

            assertEquals(new Parser.Completion(" ", 1), whiteSpaceParser.complete(""));
            assertEquals(new Parser.Completion("", 0), whiteSpaceParser.complete(" "));
            assertEquals(new Parser.Completion("", 0), whiteSpaceParser.complete("  "));
        }

        @Test
        void completeInvalid() {
            WhiteSpaceParser whiteSpaceParser = new WhiteSpaceParser(true);

            assertThrows(ParseException.class, () -> whiteSpaceParser.complete("a"));
        }
    }

    @Nested
    class NotRequired {
        @Test
        void parse() {
            WhiteSpaceParser whiteSpaceParser = new WhiteSpaceParser(false);
            CommandContext context = new CommandContext();

            assertEquals(new Parser.Result(0, true), whiteSpaceParser.parse(context, ""));
            assertEquals(new Parser.Result(1, true), whiteSpaceParser.parse(context, " "));
            assertEquals(new Parser.Result(2, true), whiteSpaceParser.parse(context, "  "));
        }

        @Test
        void completeValid() throws ParseException {
            WhiteSpaceParser whiteSpaceParser = new WhiteSpaceParser(false);

            // if the whitespace is not required then it is never suggested
            assertEquals(new Parser.Completion("", 0), whiteSpaceParser.complete(""));
            assertEquals(new Parser.Completion("", 0), whiteSpaceParser.complete(" "));
            assertEquals(new Parser.Completion("", 0), whiteSpaceParser.complete("  "));
        }

        @Test
        void completeInvalid() {
            WhiteSpaceParser whiteSpaceParser = new WhiteSpaceParser(false);

            assertThrows(ParseException.class, () -> whiteSpaceParser.complete("a"));
        }
    }
}
