package it.polimi.ingsw.am01.client.tui.command.parser;

import it.polimi.ingsw.am01.client.tui.command.CommandContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WordArgumentParserTest {
    @Test
    void parseValid() throws ParseException {
        WordArgumentParser parser = new WordArgumentParser("name");
        CommandContext context;

        context = new CommandContext();
        assertEquals(new Parser.Result(1, true), parser.parse(context, "a"));
        assertEquals("a", context.getString("name"));

        context = new CommandContext();
        assertEquals(new Parser.Result(3, true), parser.parse(context, "abc"));
        assertEquals("abc", context.getString("name"));

        context = new CommandContext();
        assertEquals(new Parser.Result(3, true), parser.parse(context, "abc cde"));
        assertEquals("abc", context.getString("name"));
    }

    @Test
    void parseInvalid() {
        WordArgumentParser parser = new WordArgumentParser("name");
        CommandContext context = new CommandContext();

        assertThrows(ParseException.class, () -> parser.parse(context, ""));
        assertThrows(ParseException.class, () -> parser.parse(context, " "));
    }

    @Test
    void complete() {
        WordArgumentParser parser = new WordArgumentParser("name");

        assertEquals(new Parser.Completion("<name>", 0), parser.complete(""));
        assertEquals(new Parser.Completion("", 0), parser.complete("a"));
        assertEquals(new Parser.Completion("", 0), parser.complete("abc"));
    }

}
