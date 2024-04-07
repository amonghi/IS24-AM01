package it.polimi.ingsw.am01.model.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import it.polimi.ingsw.am01.model.game.PlayArea;

import java.lang.reflect.Type;

public class PositionDeserializer implements JsonDeserializer<PlayArea.Position> {

    @Override
    public PlayArea.Position deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        String[] stringPosition = jsonElement.getAsString().replaceAll("[()]", "").split(", ");
        return new PlayArea.Position(Integer.parseInt(stringPosition[0]),Integer.parseInt(stringPosition[1]));
    }
}
