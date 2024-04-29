package it.polimi.ingsw.am01.network.message.json;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import it.polimi.ingsw.am01.network.message.NetworkMessage;
import it.polimi.ingsw.am01.network.message.c2s.AuthenticateC2S;
import it.polimi.ingsw.am01.network.message.s2c.NameAlreadyTakenS2C;
import it.polimi.ingsw.am01.network.message.s2c.SetPlayerNameS2C;

import java.io.IOException;
import java.util.Map;

public class NetworkMessageTypeAdapterFactory implements TypeAdapterFactory {

    Map<String, Class<? extends NetworkMessage>> idToType = Map.ofEntries(
            Map.entry(AuthenticateC2S.ID, AuthenticateC2S.class),
            Map.entry(SetPlayerNameS2C.ID, SetPlayerNameS2C.class),
            Map.entry(NameAlreadyTakenS2C.ID, NameAlreadyTakenS2C.class)
    );


    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        if (typeToken.getType() != NetworkMessage.class) {
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
                Class<? extends NetworkMessage> type = idToType.get(data.getId());

                jsonWriter.beginObject();
                jsonWriter.name("id").value(data.getId());
                System.out.println("A");
                jsonWriter.name("data");

                TypeAdapter<NetworkMessage> adapter = (TypeAdapter<NetworkMessage>) gson.getAdapter(TypeToken.get(type));
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
                String id = jsonReader.nextName();
                Object data = gson.getAdapter(TypeToken.get(idToType.get(id))).read(jsonReader);
                jsonReader.endObject();

                return (T) data;
            }
        };
    }
}
