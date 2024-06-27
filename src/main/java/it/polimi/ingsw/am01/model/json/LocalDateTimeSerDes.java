package it.polimi.ingsw.am01.model.json;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * GSON serializer and deserializer for {@link LocalDateTime}
 */
public class LocalDateTimeSerDes implements JsonDeserializer<LocalDateTime>, JsonSerializer<LocalDateTime> {
    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_DATE_TIME);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonElement serialize(LocalDateTime localDateTime, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(localDateTime.format(DateTimeFormatter.ISO_DATE_TIME));
    }
}
