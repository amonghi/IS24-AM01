package it.polimi.ingsw.am01.tui.command;

import java.util.Optional;

public interface Parser {
    Result parse(CommandContext context, String cmd) throws ParseException;

    Optional<String> complete(String partial);

    record Result(int consumed, boolean isFullMatch) {
    }
}
