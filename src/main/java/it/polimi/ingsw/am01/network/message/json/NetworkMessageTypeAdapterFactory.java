package it.polimi.ingsw.am01.network.message.json;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import it.polimi.ingsw.am01.network.message.NetworkMessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NetworkMessageTypeAdapterFactory implements TypeAdapterFactory {

    private static final Map<String, Class<? extends NetworkMessage>> ID_TO_TYPE = new HashMap<>();

    public static void register(String id, Class<? extends NetworkMessage> messageClass) {
        ID_TO_TYPE.put(id, messageClass);
    }

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        if (!NetworkMessage.class.isAssignableFrom(typeToken.getRawType())) {
            return null;
        }

        return new TypeAdapter<T>() {
            @Override
            public void write(JsonWriter jsonWriter, T value) throws IOException {
                if (value == null) {
                    jsonWriter.nullValue();
                    return;
                }

                NetworkMessage data = (NetworkMessage) value;
                Class<? extends NetworkMessage> type = ID_TO_TYPE.get(data.getId());

                jsonWriter.beginObject();
                jsonWriter.name("id").value(data.getId());
                jsonWriter.name("data");

                TypeAdapter<NetworkMessage> adapter = (TypeAdapter<NetworkMessage>) gson.getDelegateAdapter(NetworkMessageTypeAdapterFactory.this, TypeToken.get(type));
                adapter.write(jsonWriter, data);
                jsonWriter.endObject();
            }

            @Override
            public T read(JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }

                jsonReader.beginObject();

                // TODO: allow properties to be in different order
                String name = jsonReader.nextName();
                if (!name.equals("id")) {
                    throw new JsonParseException("Expected 'id' but found " + name);
                }

                String id = jsonReader.nextString();
                Class<? extends NetworkMessage> type = ID_TO_TYPE.get(id);

                name = jsonReader.nextName();
                if (!name.equals("data")) {
                    throw new JsonParseException("Expected 'data' but found " + name);
                }

                NetworkMessage data = gson.getDelegateAdapter(NetworkMessageTypeAdapterFactory.this, TypeToken.get(type)).read(jsonReader);
                jsonReader.endObject();

                return (T) data;
            }
        };
    }
}
