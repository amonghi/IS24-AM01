package it.polimi.ingsw.am01.client.tui.command.parser;

import it.polimi.ingsw.am01.client.tui.command.CommandContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LiteralParserTest {
    @Test
    void parse() {
        LiteralParser literalParser = new LiteralParser("test");
        CommandContext context = new CommandContext();

        assertEquals(new Parser.Result(1, false), literalParser.parse(context, "t"));
        assertEquals(new Parser.Result(2, false), literalParser.parse(context, "te"));
        assertEquals(new Parser.Result(3, false), literalParser.parse(context, "tes"));
        assertEquals(new Parser.Result(4, true), literalParser.parse(context, "test"));
        assertEquals(new Parser.Result(4, true), literalParser.parse(context, "test "));
        assertEquals(new Parser.Result(4, true), literalParser.parse(context, "test  "));

        // this is valid because it begins with the literal
        assertEquals(new Parser.Result(4, true), literalParser.parse(context, "testing"));

        // event tough this does not begin with the literal, we read a partial match or length 0
        assertEquals(new Parser.Result(0, false), literalParser.parse(context, "best"));
    }

    @Test
    void completeValid() throws ParseException {
        LiteralParser literalParser = new LiteralParser("test");

        assertEquals(new Parser.Completion("test", 4), literalParser.complete(""));
        assertEquals(new Parser.Completion("est", 3), literalParser.complete("t"));
        assertEquals(new Parser.Completion("st", 2), literalParser.complete("te"));
        assertEquals(new Parser.Completion("t", 1), literalParser.complete("tes"));
        assertEquals(new Parser.Completion("", 0), literalParser.complete("test"));
    }

    @Test
    void completeInvalid() {
        LiteralParser literalParser = new LiteralParser("test");

        assertThrows(ParseException.class, () -> literalParser.complete("best"));
        assertThrows(ParseException.class, () -> literalParser.complete("testing"));
        assertThrows(ParseException.class, () -> literalParser.complete("t "));
        assertThrows(ParseException.class, () -> literalParser.complete("test "));
        assertThrows(ParseException.class, () -> literalParser.complete("test  "));
    }
}
