package it.polimi.ingsw.am01.client.tui.command.parser;

import it.polimi.ingsw.am01.client.tui.command.CommandContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

enum TestEnum {
    FIRST,
    SECOND,
    THIRD
}

class EnumParserTest {
    @Test
    void parse() throws ParseException {
        EnumParser<TestEnum> parser = new EnumParser<>("name", TestEnum.class);
        CommandContext context;

        context = new CommandContext();
        assertEquals(new Parser.Result(5, true), parser.parse(context, "first"));
        assertEquals(TestEnum.FIRST, context.getEnum("name", TestEnum.class));

        context = new CommandContext();
        assertEquals(new Parser.Result(2, false), parser.parse(context, "fi"));

        context = new CommandContext();
        assertEquals(new Parser.Result(5, true), parser.parse(context, "first hello"));
        assertEquals(TestEnum.FIRST, context.getEnum("name", TestEnum.class));

        context = new CommandContext();
        assertEquals(new Parser.Result(0, false), parser.parse(context, ""));


        context = new CommandContext();
        assertEquals(new Parser.Result(0, false), parser.parse(context, "a"));
    }

    @Test
    void completeValid() throws ParseException {
        EnumParser<TestEnum> parser = new EnumParser<>("name", TestEnum.class);

        assertEquals(new Parser.Completion("<name>", 0), parser.complete(""));
        assertEquals(new Parser.Completion("irst", 4), parser.complete("f"));
        assertEquals(new Parser.Completion("rst", 3), parser.complete("fi"));
    }

    @Test
    void completeInvalid() throws ParseException {
        EnumParser<TestEnum> parser = new EnumParser<>("name", TestEnum.class);

        assertThrows(ParseException.class, () -> parser.complete("a"));
    }
}
