package it.polimi.ingsw.am01.model.json;

import com.google.gson.*;
import it.polimi.ingsw.am01.model.card.face.points.CornerCoverPoints;
import it.polimi.ingsw.am01.model.card.face.points.ItemPoints;
import it.polimi.ingsw.am01.model.card.face.points.Points;
import it.polimi.ingsw.am01.model.card.face.points.SimplePoints;
import it.polimi.ingsw.am01.model.collectible.Item;

import java.lang.reflect.Type;

/**
 * GSON deserializer for {@link Points}
 */
public class PointsDeserializer implements JsonDeserializer<Points> {
    /**
     * {@inheritDoc}
     */
    @Override
    public Points deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String pointsType = jsonObject.get("type").getAsString();
        int pointsAmount = jsonObject.get("number").getAsInt();
        return switch (pointsType) {
            case "corner" -> new CornerCoverPoints(pointsAmount);
            case "item" -> {
                Item item = context.deserialize(jsonObject.get("item"), Item.class);
                yield new ItemPoints(item, pointsAmount);
            }
            case "simple" -> new SimplePoints(pointsAmount);
            default -> throw new JsonParseException("Unknown Points type: " + type);
        };
    }
}
