package it.polimi.ingsw.am01.model.json;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.am01.model.card.face.placement.PlacementConstraint;
import it.polimi.ingsw.am01.model.collectible.Resource;

import java.lang.reflect.Type;
import java.util.Map;

public class PlacementConstraintDeserializer implements JsonDeserializer<PlacementConstraint> {
    @Override
    public PlacementConstraint deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Map<Resource, Integer> resources = context.deserialize(jsonObject, new TypeToken<Map<Resource, Integer>>() {
        }.getType());
        return new PlacementConstraint(resources);
    }

}
