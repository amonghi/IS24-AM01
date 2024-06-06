package it.polimi.ingsw.am01.client.tui.command.parser;

import it.polimi.ingsw.am01.client.tui.command.CommandContext;

public class EnumParser<E extends Enum<E>> implements Parser {
    private final String name;
    private final Class<E> enumClass;

    public EnumParser(String name, Class<E> enumClass) {
        this.name = name;
        this.enumClass = enumClass;
    }

    @Override
    public Result parse(CommandContext context, String cmd) throws ParseException {
        Result best = new Result(0, false);
        for (E e : enumClass.getEnumConstants()) {
            Result result = new LiteralParser(e.name().toLowerCase()).parse(context, cmd);
            if (result.isFullMatch()) {
                context.putArgument(this.name, e);
                return result;
            }

            if (result.consumed() > best.consumed()) {
                best = result;
            }
        }

        return best;
    }

    @Override
    public Completion complete(String partial) throws ParseException {
        if (partial.isBlank()) {
            return Completion.nonCompletable("<" + this.name + ">");
        }

        for (E e : enumClass.getEnumConstants()) {
            try {
                return new LiteralParser(e.name().toLowerCase()).complete(partial);
            } catch (ParseException ignored) {
            }
        }

        throw new ParseException();
    }
}
