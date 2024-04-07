package it.polimi.ingsw.am01.model.serializers;

import com.google.gson.*;
import it.polimi.ingsw.am01.model.card.CardColor;
import it.polimi.ingsw.am01.model.game.PlayArea;
import it.polimi.ingsw.am01.model.objective.DifferentCollectibleObjective;
import it.polimi.ingsw.am01.model.objective.Objective;
import it.polimi.ingsw.am01.model.objective.PatternObjective;
import it.polimi.ingsw.am01.model.objective.SameCollectibleObjective;

import java.lang.reflect.Type;
import java.util.Map;

public class ObjectiveSerializer implements JsonSerializer<Objective> {
    Map<PlayArea.Position, PlayArea.Position> positionConversionMap = Map.of(
            new PlayArea.Position(0, 1),   new PlayArea.Position(0, 0),
            new PlayArea.Position(1, 1),   new PlayArea.Position(0, 1),
            new PlayArea.Position(1, 0),   new PlayArea.Position(0, 2),
            new PlayArea.Position(-1, 1),  new PlayArea.Position(1, 0),
            new PlayArea.Position(0, 0),   new PlayArea.Position(1, 1),
            new PlayArea.Position(1, -1),  new PlayArea.Position(1, 2),
            new PlayArea.Position(-1, 0),  new PlayArea.Position(2, 0),
            new PlayArea.Position(-1, -1), new PlayArea.Position(2, 1),
            new PlayArea.Position(0, -1),  new PlayArea.Position(2, 2)
    );
    @Override
    public JsonElement serialize(Objective objective, Type type, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        JsonObject jsonObjective = new JsonObject();
        jsonObjective.addProperty("points", objective.getPointsPerMatch());
        switch (objective){
            case SameCollectibleObjective sameCollectibleObjective -> {
                result.addProperty("type", "same");
                jsonObjective.addProperty("requiredCollectible", sameCollectibleObjective.getRequiredCollectible().toString());
                jsonObjective.addProperty("requiredNumber", sameCollectibleObjective.getRequiredNumber());
            }
            case DifferentCollectibleObjective differentCollectibleObjective -> {
                result.addProperty("type", "different");
                jsonObjective.add("requiredItems", context.serialize(differentCollectibleObjective.getRequiredItems()));
            }
            case PatternObjective patternObjective -> {
                result.addProperty("type", "pattern");
                JsonArray pattern = new JsonArray();
                char[][] stringMatrix = {"   ".toCharArray(), "   ".toCharArray(), "   ".toCharArray()};
                for (Map.Entry<PlayArea.Position, CardColor> entry : patternObjective.getPattern().entrySet()) {
                    PlayArea.Position convertedPosition = positionConversionMap.get(entry.getKey());
                    stringMatrix[convertedPosition.i()][convertedPosition.j()] = entry.getValue().toString().charAt(0);
                }
                for(char[] row : stringMatrix){
                    pattern.add(String.valueOf(row));
                }
                jsonObjective.add("pattern", pattern);
            }
            default -> throw new IllegalStateException("Unexpected value: " + objective);
        }
        result.add("objective", jsonObjective);
        return result;
    }
}
