package it.polimi.ingsw.am01.client.tui.command;

import java.util.HashMap;
import java.util.Map;

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
}
