package it.polimi.ingsw.am01.model.json;

import com.google.gson.*;
import it.polimi.ingsw.am01.model.chat.BroadcastMessage;
import it.polimi.ingsw.am01.model.chat.DirectMessage;
import it.polimi.ingsw.am01.model.chat.Message;

import java.lang.reflect.Type;

public class ChatMessageSerDes implements JsonDeserializer<Message>, JsonSerializer<Message> {

    @Override
    public Message deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        String type = jsonObject.get("type").getAsString();
        JsonElement element = jsonObject.get("data");

        return switch (type) {
            case "DIRECT" -> context.deserialize(element, DirectMessage.class);
            case "BROADCAST" -> context.deserialize(element, BroadcastMessage.class);
            default -> throw new JsonParseException("Invalid message type: " + type);
        };
    }


    @Override
    public JsonElement serialize(Message msg, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("type", new JsonPrimitive(msg.getMessageType().toString()));
        result.add("data", context.serialize(msg, msg.getClass()));

        return result;
    }
}
