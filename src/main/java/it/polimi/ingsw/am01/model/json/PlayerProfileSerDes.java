package it.polimi.ingsw.am01.model.json;

import com.google.gson.*;
import it.polimi.ingsw.am01.model.player.PlayerProfile;

import java.lang.reflect.Type;

public class PlayerProfileSerDes implements JsonDeserializer<PlayerProfile>, JsonSerializer<PlayerProfile> {
    @Override
    public PlayerProfile deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        return new PlayerProfile(jsonElement.getAsString());
    }

    @Override
    public JsonElement serialize(PlayerProfile playerProfile, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(playerProfile.name());
    }
}
