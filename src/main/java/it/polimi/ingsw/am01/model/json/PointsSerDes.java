package it.polimi.ingsw.am01.model.json;

import com.google.gson.*;
import it.polimi.ingsw.am01.model.card.face.points.CornerCoverPoints;
import it.polimi.ingsw.am01.model.card.face.points.ItemPoints;
import it.polimi.ingsw.am01.model.card.face.points.Points;
import it.polimi.ingsw.am01.model.card.face.points.SimplePoints;
import it.polimi.ingsw.am01.model.collectible.Item;

import java.lang.reflect.Type;

public class PointsSerDes implements JsonDeserializer<Points>, JsonSerializer<Points> {
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

    @Override
    public JsonElement serialize(Points points, Type type, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        switch (points) {
            case SimplePoints simplePoints -> {
                jsonObject.addProperty("type", "simple");
                jsonObject.addProperty("number", simplePoints.getPoints());
            }
            case CornerCoverPoints cornerCoverPoints -> {
                jsonObject.addProperty("type", "corner");
                jsonObject.addProperty("number", cornerCoverPoints.getPointsPerCorner());
            }
            case ItemPoints itemPoints -> {
                jsonObject.addProperty("type", "item");
                jsonObject.addProperty("item", itemPoints.getItem().toString());
                jsonObject.addProperty("number", itemPoints.getPointsPerItem());
            }
            default -> throw new IllegalStateException("Unexpected value: " + points);
        }
        return jsonObject;
    }
}
