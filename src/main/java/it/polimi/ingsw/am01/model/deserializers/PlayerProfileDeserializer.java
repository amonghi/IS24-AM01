package it.polimi.ingsw.am01.model.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import it.polimi.ingsw.am01.model.player.PlayerProfile;

import java.lang.reflect.Type;

public class PlayerProfileDeserializer implements JsonDeserializer<PlayerProfile> {
    @Override
    public PlayerProfile deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        return new PlayerProfile(jsonElement.getAsString());
    }
}
