package it.polimi.ingsw.am01.model.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import it.polimi.ingsw.am01.model.collectible.Collectible;
import it.polimi.ingsw.am01.model.collectible.Item;
import it.polimi.ingsw.am01.model.collectible.Resource;

import java.lang.reflect.Type;
import java.util.Arrays;

public class CollectibleDeserializer implements JsonDeserializer<Collectible> {
    @Override
    public Collectible deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        String value = jsonElement.getAsString();
        if(Arrays.stream(Resource.values()).map(Enum::name).anyMatch(value::equals)){
            return context.deserialize(jsonElement, Resource.class);
        }
        else if(Arrays.stream(Item.values()).map(Enum::name).anyMatch(value::equals)){
            return context.deserialize(jsonElement, Item.class);
        } else {
            throw new JsonParseException("Illegal collectible name");
        }
    }
}
