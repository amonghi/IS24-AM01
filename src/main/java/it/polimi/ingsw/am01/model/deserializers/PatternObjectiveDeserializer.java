package it.polimi.ingsw.am01.model.deserializers;

import com.google.gson.*;
import it.polimi.ingsw.am01.model.card.CardColor;
import it.polimi.ingsw.am01.model.game.PlayArea;
import it.polimi.ingsw.am01.model.objective.PatternObjective;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class PatternObjectiveDeserializer implements JsonDeserializer<PatternObjective> {
    @Override
    public PatternObjective deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        int points = jsonObject.get("points").getAsInt();
        String[] patternMatrix = context.deserialize(jsonObject.get("pattern"), String[].class);

        PlayArea.Position[][] positions = {
            {new PlayArea.Position(0, 1), new PlayArea.Position(1, 1), new PlayArea.Position(1, 0)},
            {new PlayArea.Position(-1, 1), new PlayArea.Position(0, 0), new PlayArea.Position(1, -1)},
            {new PlayArea.Position(-1, 0), new PlayArea.Position(-1, -1), new PlayArea.Position(0, -1)}
        };
        Map<PlayArea.Position, CardColor> patternMap = new HashMap<>();

        for(int i = 0; i < patternMatrix.length; i++){
            for(int j = 0; j < patternMatrix[0].length(); j++){
                char colorChar = patternMatrix[i].charAt(j);
                if(colorChar != ' ') {
                    CardColor color = parseColor(colorChar);
                    patternMap.put(positions[i][j], color);
                }
            }
        }
        return new PatternObjective(points, patternMap);
    }
    private static CardColor parseColor(char color){
        return switch (Character.toUpperCase(color)) {
            case 'R' -> CardColor.RED;
            case 'G' -> CardColor.GREEN;
            case 'B' -> CardColor.BLUE;
            case 'P' -> CardColor.PURPLE;
            default -> throw new JsonParseException("Illegal color character: " + color);
        };
    }
}
