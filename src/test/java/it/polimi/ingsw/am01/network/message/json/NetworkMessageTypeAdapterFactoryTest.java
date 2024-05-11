package it.polimi.ingsw.am01.network.message.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.am01.network.message.NetworkMessage;
import it.polimi.ingsw.am01.network.message.c2s.AuthenticateC2S;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NetworkMessageTypeAdapterFactoryTest {
    @Test
    void networkMessageTypeAdapter() {
        Gson gson = new GsonBuilder().
                registerTypeAdapterFactory(new NetworkMessageTypeAdapterFactory())
                .create();
        NetworkMessage original = new AuthenticateC2S("Alice");

        String serialized = gson.toJson(original);
        NetworkMessage deserialized = gson.fromJson(serialized, NetworkMessage.class);

        assertEquals(original, deserialized);
    }
}
