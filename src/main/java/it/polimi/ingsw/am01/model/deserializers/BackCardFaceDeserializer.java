package it.polimi.ingsw.am01.model.deserializers;

import com.google.gson.*;
import it.polimi.ingsw.am01.model.card.face.BackCardFace;
import it.polimi.ingsw.am01.model.card.face.corner.Corner;
import it.polimi.ingsw.am01.model.collectible.Resource;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class BackCardFaceDeserializer implements JsonDeserializer<BackCardFace> {

    @Override
    public BackCardFace deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Corner tr = context.deserialize(jsonObject.get("tr"), Corner.class);
        Corner tl = context.deserialize(jsonObject.get("tl"), Corner.class);
        Corner br = context.deserialize(jsonObject.get("br"), Corner.class);
        Corner bl = context.deserialize(jsonObject.get("bl"), Corner.class);

        Map<Resource, Integer> resources = new HashMap<>();
        JsonObject resourcesObject = jsonObject.getAsJsonObject("center");
        for (Map.Entry<String, JsonElement> entry : resourcesObject.entrySet()) {
            Resource resource = Resource.valueOf(entry.getKey());
            int value = entry.getValue().getAsInt();
            resources.put(resource, value);
        }

        return new BackCardFace(tr, tl, br, bl, resources);
    }
}
