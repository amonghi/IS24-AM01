package it.polimi.ingsw.am01.model.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import it.polimi.ingsw.am01.model.game.PlayArea;

import java.lang.reflect.Type;

public class PositionSerializer implements JsonSerializer<PlayArea.Position> {
    @Override
    public JsonElement serialize(PlayArea.Position position, Type type, JsonSerializationContext context) {
        return new JsonPrimitive("(" + position.i() + ", " + position.j() + ")");
    }
}
