package it.polimi.ingsw.am01.model.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import it.polimi.ingsw.am01.model.player.PlayerProfile;

import java.lang.reflect.Type;

public class PlayerProfileSerializer implements JsonSerializer<PlayerProfile> {

    @Override
    public JsonElement serialize(PlayerProfile playerProfile, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(playerProfile.getName());
    }
}
