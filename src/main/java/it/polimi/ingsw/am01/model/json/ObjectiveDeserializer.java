package it.polimi.ingsw.am01.model.json;

import com.google.gson.*;
import it.polimi.ingsw.am01.model.game.PlayArea;
import it.polimi.ingsw.am01.model.objective.DifferentCollectibleObjective;
import it.polimi.ingsw.am01.model.objective.Objective;
import it.polimi.ingsw.am01.model.objective.PatternObjective;
import it.polimi.ingsw.am01.model.objective.SameCollectibleObjective;

import java.lang.reflect.Type;
import java.util.Map;

public class ObjectiveDeserializer implements JsonDeserializer<Objective> {
    Map<PlayArea.Position, PlayArea.Position> positionConversionMap = Map.of(
            new PlayArea.Position(0, 1), new PlayArea.Position(0, 0),
            new PlayArea.Position(1, 1), new PlayArea.Position(0, 1),
            new PlayArea.Position(1, 0), new PlayArea.Position(0, 2),
            new PlayArea.Position(-1, 1), new PlayArea.Position(1, 0),
            new PlayArea.Position(0, 0), new PlayArea.Position(1, 1),
            new PlayArea.Position(1, -1), new PlayArea.Position(1, 2),
            new PlayArea.Position(-1, 0), new PlayArea.Position(2, 0),
            new PlayArea.Position(-1, -1), new PlayArea.Position(2, 1),
            new PlayArea.Position(0, -1), new PlayArea.Position(2, 2)
    );

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
