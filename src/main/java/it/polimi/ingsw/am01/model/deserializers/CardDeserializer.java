package it.polimi.ingsw.am01.model.deserializers;

import com.google.gson.*;
import it.polimi.ingsw.am01.model.card.Card;
import it.polimi.ingsw.am01.model.card.CardColor;
import it.polimi.ingsw.am01.model.card.face.BackCardFace;
import it.polimi.ingsw.am01.model.card.face.FrontCardFace;
import it.polimi.ingsw.am01.model.collectible.Item;

import java.lang.reflect.Type;

public class CardDeserializer implements JsonDeserializer<Card> {
    @Override
    public Card deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        int id = jsonObject.get("id").getAsInt();
        CardColor baseColor = CardColor.valueOf(jsonObject.get("baseColor").getAsString());
        boolean isStarter = jsonObject.get("isStarter").getAsBoolean();
        boolean isGold = jsonObject.get("isGold").getAsBoolean();
        FrontCardFace front = context.deserialize(jsonObject.get("front"), FrontCardFace.class);
        BackCardFace back = context.deserialize(jsonObject.get("back"), BackCardFace.class);

        return new Card(id, baseColor, isStarter, isGold, front, back);
    }
}
