package it.polimi.ingsw.am01.model.json;

import com.google.gson.*;
import it.polimi.ingsw.am01.model.player.PlayerProfile;

import java.lang.reflect.Type;

/**
 * GSON serializer and deserializer for {@link PlayerProfile}
 */
public class PlayerProfileSerDes implements JsonDeserializer<PlayerProfile>, JsonSerializer<PlayerProfile> {
    /**
     * {@inheritDoc}
     */
    @Override
    public PlayerProfile deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        return new PlayerProfile(jsonElement.getAsString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonElement serialize(PlayerProfile playerProfile, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(playerProfile.name());
    }
}
