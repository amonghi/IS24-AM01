package it.polimi.ingsw.am01.client.tui.command;

import java.util.Optional;

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
    public Optional<String> complete(String partial) {
        if (partial.isBlank()) {
            return Optional.of("<" + this.name + ">");
        }

        for (E e : enumClass.getEnumConstants()) {
            Optional<String> completion = new LiteralParser(e.name().toLowerCase()).complete(partial);

            if (completion.isPresent()) {
                return completion;
            }
        }

        return Optional.empty();
    }
}
