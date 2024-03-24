package it.polimi.ingsw.am01.model.deserializers;

import com.google.gson.*;
import it.polimi.ingsw.am01.model.card.face.placement.PlacementConstraint;
import it.polimi.ingsw.am01.model.collectible.Resource;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class PlacementConstraintDeserializer implements JsonDeserializer<PlacementConstraint> {
    @Override
    public PlacementConstraint deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        Map<Resource, Integer> resources = new HashMap<>();
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            Resource resource = Resource.valueOf(entry.getKey());
            int value = entry.getValue().getAsInt();
            resources.put(resource, value);
        }
        return new PlacementConstraint(resources);
    }
}
