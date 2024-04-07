package it.polimi.ingsw.am01.model.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import it.polimi.ingsw.am01.model.card.face.points.CornerCoverPoints;
import it.polimi.ingsw.am01.model.card.face.points.ItemPoints;
import it.polimi.ingsw.am01.model.card.face.points.Points;
import it.polimi.ingsw.am01.model.card.face.points.SimplePoints;

import java.lang.reflect.Type;

public class PointsSerializer implements JsonSerializer<Points> {
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
