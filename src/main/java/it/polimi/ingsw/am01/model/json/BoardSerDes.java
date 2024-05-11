package it.polimi.ingsw.am01.model.json;

import com.google.gson.*;
import it.polimi.ingsw.am01.model.game.Board;
import it.polimi.ingsw.am01.model.game.Deck;

import java.lang.reflect.Type;
import java.util.stream.Collectors;

public class BoardSerDes implements JsonSerializer<Board>, JsonDeserializer<Board> {
    @Override
    public JsonElement serialize(Board board, Type type, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();

        Deck resourceCardDeck = board.getResourceCardDeck().createMergedDeck(
                board.getFaceUpCards().stream()
                        .filter(faceUpCard -> faceUpCard.getCard().isPresent() && !faceUpCard.getCard().get().isGold())
                        .map(faceUpCard -> faceUpCard.getCard().get())
                        .collect(Collectors.toList())
        );

        Deck goldenCardDeck = board.getGoldenCardDeck().createMergedDeck(
                board.getFaceUpCards().stream()
                        .filter(faceUpCard -> faceUpCard.getCard().isPresent() && faceUpCard.getCard().get().isGold())
                        .map(faceUpCard -> faceUpCard.getCard().get())
                        .collect(Collectors.toList())
        );

        jsonObject.add("resourceCardDeck", context.serialize(resourceCardDeck));
        jsonObject.add("goldenCardDeck", context.serialize(goldenCardDeck));

        return jsonObject;
    }

    @Override
    public Board deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        return new Board(
                context.deserialize(jsonElement.getAsJsonObject().get("resourceCardDeck"), Deck.class),
                context.deserialize(jsonElement.getAsJsonObject().get("goldenCardDeck"), Deck.class));
    }
}
