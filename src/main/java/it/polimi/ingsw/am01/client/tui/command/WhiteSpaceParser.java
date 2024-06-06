package it.polimi.ingsw.am01.client.tui.command;

import java.util.Optional;

public class WhiteSpaceParser implements Parser {
    private final boolean required;

    public WhiteSpaceParser(boolean required) {
        this.required = required;
    }

    @Override
    public Result parse(CommandContext context, String cmd) {
        int consumed = cmd.length() - cmd.stripLeading().length();
        if (this.required && consumed == 0) {
            return new Result(0, false);
        }

        return new Result(consumed, true);
    }

    @Override
    public Optional<String> complete(String partial) {
        if (partial.isEmpty()) {
            return Optional.of(" ");
        }

        if (partial.isBlank()) {
            return Optional.of("");
        }

        return Optional.empty();
    }
}
