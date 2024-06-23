package it.polimi.ingsw.am01.client.tui.command.parser;

import it.polimi.ingsw.am01.client.tui.command.CommandContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GreedyTextArgumentParserTest {
    @Test
    void parse() throws ParseException {
        GreedyTextArgumentParser parser = new GreedyTextArgumentParser("name");

        CommandContext c1 = new CommandContext();
        assertThrows(ParseException.class, () -> parser.parse(c1, ""));

        CommandContext c2 = new CommandContext();
        assertEquals(new Parser.Result(1, true), parser.parse(c2, " "));
        assertEquals(" ", c2.getString("name"));

        CommandContext c3 = new CommandContext();
        assertEquals(new Parser.Result(1, true), parser.parse(c3, "a"));
        assertEquals("a", c3.getString("name"));

        CommandContext c4 = new CommandContext();
        assertEquals(new Parser.Result(3, true), parser.parse(c4, "abc"));
        assertEquals("abc", c4.getString("name"));

        CommandContext c5 = new CommandContext();
        assertEquals(new Parser.Result(7, true), parser.parse(c5, "abc cde"));
        assertEquals("abc cde", c5.getString("name"));
    }

    @Test
    void complete() {
        GreedyTextArgumentParser parser = new GreedyTextArgumentParser("name");

        assertEquals(new Parser.Completion("<name>", 0), parser.complete(""));
        assertEquals(new Parser.Completion("", 0), parser.complete(" "));
        assertEquals(new Parser.Completion("", 0), parser.complete("a"));
        assertEquals(new Parser.Completion("", 0), parser.complete("abc"));
        assertEquals(new Parser.Completion("", 0), parser.complete("abc cde"));
    }
}
