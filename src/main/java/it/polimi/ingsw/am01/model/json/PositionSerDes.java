package it.polimi.ingsw.am01.model.json;

import com.google.gson.*;
import it.polimi.ingsw.am01.model.game.PlayArea;

import java.lang.reflect.Type;

/**
 * GSON serializer and deserializer for {@link PlayArea.Position}
 */
public class PositionSerDes implements JsonDeserializer<PlayArea.Position>, JsonSerializer<PlayArea.Position> {
    /**
     * {@inheritDoc}
     */
    @Override
    public PlayArea.Position deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        String[] stringPosition = jsonElement.getAsString().replaceAll("[()]", "").split(", ");
        return new PlayArea.Position(Integer.parseInt(stringPosition[0]), Integer.parseInt(stringPosition[1]));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonElement serialize(PlayArea.Position position, Type type, JsonSerializationContext context) {
        return new JsonPrimitive("(" + position.i() + ", " + position.j() + ")");
    }
}
