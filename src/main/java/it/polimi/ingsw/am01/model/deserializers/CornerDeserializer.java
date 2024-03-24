package it.polimi.ingsw.am01.model.deserializers;

import it.polimi.ingsw.am01.model.card.face.corner.Corner;
import com.google.gson.*;
import it.polimi.ingsw.am01.model.collectible.Collectible;
import it.polimi.ingsw.am01.model.collectible.Item;
import it.polimi.ingsw.am01.model.collectible.Resource;

import java.lang.reflect.Type;
import java.util.Arrays;


public class CornerDeserializer implements JsonDeserializer<Corner>{
    @Override
    public Corner deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        String value = jsonElement.getAsString();

        if ("EMPTY".equals(value)) {
            return Corner.empty();
        } else if ("NONE".equals(value)) {
            return Corner.missing();
        }

        Collectible collectible;
        if(Arrays.stream(Resource.values()).map(Enum::name).anyMatch(value::equals)){
            collectible = context.deserialize(jsonElement, Resource.class);
        }
        else if(Arrays.stream(Item.values()).map(Enum::name).anyMatch(value::equals)){
            collectible = context.deserialize(jsonElement, Item.class);
        } else {
            throw new JsonParseException("Illegal collectible name");
        }
        return Corner.filled(collectible);

    }
}
