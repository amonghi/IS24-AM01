package it.polimi.ingsw.am01.model.deserializers;

import com.google.gson.*;
import it.polimi.ingsw.am01.model.objective.DifferentCollectibleObjective;
import it.polimi.ingsw.am01.model.objective.Objective;
import it.polimi.ingsw.am01.model.objective.PatternObjective;
import it.polimi.ingsw.am01.model.objective.SameCollectibleObjective;

import java.lang.reflect.Type;

public class ObjectiveDeserializer implements JsonDeserializer<Objective> {
    @Override
    public Objective deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String objectiveType = jsonObject.get("type").getAsString();

        return switch (objectiveType) {
            case "same" -> context.deserialize(jsonObject.get("objective"), SameCollectibleObjective.class);
            case "different" -> context.deserialize(jsonObject.get("objective"), DifferentCollectibleObjective.class);
            case "pattern" -> context.deserialize(jsonObject.get("objective"), PatternObjective.class);
            default -> throw new JsonParseException("Unknown Points type: " + objectiveType);
        };

    }
}
