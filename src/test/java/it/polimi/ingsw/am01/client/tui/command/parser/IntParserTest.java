package it.polimi.ingsw.am01.client.tui.command.parser;

import it.polimi.ingsw.am01.client.tui.command.CommandContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IntParserTest {
    @Test
    void parseValid() throws ParseException {
        IntParser parser = new IntParser("name");
        CommandContext context;

        context = new CommandContext();
        assertEquals(new Parser.Result(1, true), parser.parse(context, "1"));
        assertEquals(1, context.getInt("name"));

        context = new CommandContext();
        assertEquals(new Parser.Result(1, true), parser.parse(context, "1 hello"));
        assertEquals(1, context.getInt("name"));

        context = new CommandContext();
        assertEquals(new Parser.Result(2, true), parser.parse(context, "-1"));
        assertEquals(-1, context.getInt("name"));

        context = new CommandContext();
        assertEquals(new Parser.Result(2, true), parser.parse(context, "-1 hello"));
        assertEquals(-1, context.getInt("name"));
    }

    @Test
    void parseInvalid() {
        IntParser parser = new IntParser("name");
        CommandContext context = new CommandContext();

        assertThrows(ParseException.class, () -> parser.parse(context, ""));
        assertThrows(ParseException.class, () -> parser.parse(context, " "));
        assertThrows(ParseException.class, () -> parser.parse(context, "a"));
        assertThrows(ParseException.class, () -> parser.parse(context, "1a"));
        assertThrows(ParseException.class, () -> parser.parse(context, "-"));
    }

    @Test
    void complete() throws ParseException {
        IntParser parser = new IntParser("name");

        assertEquals(new Parser.Completion("<name>", 0), parser.complete(""));
        assertEquals(new Parser.Completion("", 0), parser.complete("1"));
        assertEquals(new Parser.Completion("", 0), parser.complete("-1"));

        assertThrows(ParseException.class, () -> parser.complete("a"));
        assertThrows(ParseException.class, () -> parser.complete("1a"));
        assertThrows(ParseException.class, () -> parser.complete("-"));
    }
}
