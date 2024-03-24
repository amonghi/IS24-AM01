package it.polimi.ingsw.am01.model.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import it.polimi.ingsw.am01.model.collectible.Resource;

import java.lang.reflect.Type;

public class ResourceDeserializer implements JsonDeserializer<Resource> {
    @Override
    public Resource deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        String resourceName = jsonElement.getAsString();
        return Resource.valueOf(resourceName);
    }
}
