package it.polimi.ingsw.am01.model.json;

import com.google.gson.*;
import it.polimi.ingsw.am01.model.card.Card;
import it.polimi.ingsw.am01.model.game.GameAssets;

import java.lang.reflect.Type;
import java.util.Optional;

/**
 * GSON serializer and deserializer for {@link Card} that uses the card's ID
 */
public class IDCardSerDes implements JsonDeserializer<Card>, JsonSerializer<Card> {
    /**
     * {@inheritDoc}
     */
    @Override
    public Card deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        Optional<Card> card = GameAssets.getInstance().getCardById(jsonElement.getAsInt());
        return card.orElseThrow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonElement serialize(Card card, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(card.id());
    }
}
