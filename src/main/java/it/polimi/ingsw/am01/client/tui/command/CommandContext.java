package it.polimi.ingsw.am01.client.tui.command;

import java.util.HashMap;
import java.util.Map;

/**
 * A context that is used during command parsing and execution.
 * <p>
 * {@link it.polimi.ingsw.am01.client.tui.command.parser.Parser}s put arguments
 * in the {@link CommandContext} so
 * that {@link it.polimi.ingsw.am01.client.tui.command.validator.PostValidator}s can validate them
 * and finally the {@link java.util.function.Consumer} associated with a {@link CommandNode} can use them
 * to execute the command.
 */
public class CommandContext {
    private final Map<String, Object> arguments;

    public CommandContext() {
        arguments = new HashMap<>();
    }

    public void putArgument(String key, Object value) {
        arguments.put(key, value);
    }

    public String getString(String key) {
        return (String) arguments.get(key);
    }

    public int getInt(String key) {
        return (int) arguments.get(key);
    }

    public <E extends Enum<E>> E getEnum(String key, Class<E> enumClass) {
        return enumClass.cast(arguments.get(key));
    }
}
