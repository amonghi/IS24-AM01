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
import it.polimi.ingsw.am01.network.message.c2s.*;
import it.polimi.ingsw.am01.network.message.s2c.*;

import java.io.IOException;
import java.util.Map;

public class NetworkMessageTypeAdapterFactory implements TypeAdapterFactory {

    Map<String, Class<? extends NetworkMessage>> idToType = Map.ofEntries(
            // C2S messages
            Map.entry(AuthenticateC2S.ID, AuthenticateC2S.class),
            Map.entry(CreateGameAndJoinC2S.ID, CreateGameAndJoinC2S.class),
            Map.entry(DrawCardFromDeckC2S.ID, DrawCardFromDeckC2S.class),
            Map.entry(DrawCardFromFaceUpCardsC2S.ID, DrawCardFromFaceUpCardsC2S.class),
            Map.entry(JoinGameC2S.ID, JoinGameC2S.class),
            Map.entry(PlaceCardC2S.ID, PlaceCardC2S.class),
            Map.entry(SelectColorC2S.ID, SelectColorC2S.class),
            Map.entry(SelectSecretObjectiveC2S.ID, SelectSecretObjectiveC2S.class),
            Map.entry(SelectStartingCardSideC2S.ID, SelectStartingCardSideC2S.class),
            Map.entry(SendBroadcastMessageC2S.ID, SendBroadcastMessageC2S.class),
            Map.entry(SendDirectMessageC2S.ID, SendDirectMessageC2S.class),
            Map.entry(StartGameC2S.ID, StartGameC2S.class),


            // S2C messages
            Map.entry(BroadcastMessageSentS2C.ID, BroadcastMessageSentS2C.class),
            Map.entry(DirectMessageSentS2C.ID, DirectMessageSentS2C.class),
            Map.entry(DoubleChoiceS2C.ID, DoubleChoiceS2C.class),
            Map.entry(DoubleSideChoiceS2C.ID, DoubleSideChoiceS2C.class),
            Map.entry(EmptySourceS2C.ID, EmptySourceS2C.class),
            Map.entry(GameAlreadyStartedS2C.ID, GameAlreadyStartedS2C.class),
            Map.entry(GameFinishedS2C.ID, GameFinishedS2C.class),
            Map.entry(GameJoinedS2C.ID, GameJoinedS2C.class),
            Map.entry(GameNotFoundS2C.ID, GameNotFoundS2C.class),
            Map.entry(InvalidCardS2C.ID, InvalidCardS2C.class),
            Map.entry(InvalidGameStateS2C.ID, InvalidGameStateS2C.class),
            Map.entry(InvalidMaxPlayersS2C.ID, InvalidMaxPlayersS2C.class),
            Map.entry(InvalidObjectiveSelectionS2C.ID, InvalidObjectiveSelectionS2C.class),
            Map.entry(InvalidPlacementS2C.ID, InvalidPlacementS2C.class),
            Map.entry(InvalidRecipientS2C.ID, InvalidRecipientS2C.class),
            Map.entry(NameAlreadyTakenS2C.ID, NameAlreadyTakenS2C.class),
            Map.entry(NewMessageS2C.ID, NewMessageS2C.class),
            Map.entry(NotEnoughPlayersS2C.ID, NotEnoughPlayersS2C.class),
            Map.entry(PlayerNotInGameS2C.ID, PlayerNotInGameS2C.class),
            Map.entry(SetBoardAndHandS2C.ID, SetBoardAndHandS2C.class),
            Map.entry(SetGamePauseS2C.ID, SetGamePauseS2C.class),
            Map.entry(SetPlayablePositionsS2C.ID, SetPlayablePositionsS2C.class),
            Map.entry(SetPlayerNameS2C.ID, SetPlayerNameS2C.class),
            Map.entry(SetRecoverStatusS2C.ID, SetRecoverStatusS2C.class),
            Map.entry(SetStartingCardS2C.ID, SetStartingCardS2C.class),
            Map.entry(UpdateDeckStatusS2C.ID, UpdateDeckStatusS2C.class),
            Map.entry(UpdateFaceUpCardsS2C.ID, UpdateFaceUpCardsS2C.class),
            Map.entry(UpdateGameListS2C.ID, UpdateGameListS2C.class),
            Map.entry(UpdateGameStatusAndSetupObjectiveS2C.ID, UpdateGameStatusAndSetupObjectiveS2C.class),
            Map.entry(UpdateGameStatusAndTurnS2C.ID, UpdateGameStatusAndTurnS2C.class),
            Map.entry(UpdateGameStatusS2C.ID, UpdateGameStatusS2C.class),
            Map.entry(UpdateObjectiveSelectedS2C.ID, UpdateObjectiveSelectedS2C.class),
            Map.entry(UpdatePlayAreaS2C.ID, UpdatePlayAreaS2C.class),
            Map.entry(UpdatePlayerColorS2C.ID, UpdatePlayerColorS2C.class),
            Map.entry(UpdatePlayerHandS2C.ID, UpdatePlayerHandS2C.class),
            Map.entry(UpdatePlayerListS2C.ID, UpdatePlayerListS2C.class)
    );


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
                if (!idToType.containsKey(data.getId())) {
                    throw new JsonParseException("Unknown class message: " + data.getId());
                }

                Class<? extends NetworkMessage> type = idToType.get(data.getId());

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

                if (!idToType.containsKey(id)) {
                    throw new JsonParseException("Unknown class message: " + id);
                }
                Class<? extends NetworkMessage> type = idToType.get(id);

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
