package it.polimi.ingsw.am01.model.json;

import com.google.gson.*;
import it.polimi.ingsw.am01.model.card.Card;
import it.polimi.ingsw.am01.model.game.GameAssets;
import it.polimi.ingsw.am01.model.objective.Objective;

import java.lang.reflect.Type;
import java.util.Optional;

public class IDObjectiveSerDes implements JsonDeserializer<Objective>, JsonSerializer<Objective> {
    @Override
    public Objective deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        Optional<Objective> objective = GameAssets.getInstance().getObjectiveById(jsonElement.getAsInt());
        return objective.orElseThrow();
    }

    @Override
    public JsonElement serialize(Objective objective, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(objective.getId());
    }
}
