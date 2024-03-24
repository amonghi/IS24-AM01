package it.polimi.ingsw.am01.model.deserializers;

import com.google.gson.*;
import it.polimi.ingsw.am01.model.card.face.points.CornerCoverPoints;
import it.polimi.ingsw.am01.model.card.face.points.ItemPoints;
import it.polimi.ingsw.am01.model.card.face.points.Points;
import it.polimi.ingsw.am01.model.card.face.points.SimplePoints;
import it.polimi.ingsw.am01.model.collectible.Item;

import java.lang.reflect.Type;

public class PointsDeserializer implements JsonDeserializer<Points> {

    @Override
    public Points deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String pointsType = jsonObject.get("type").getAsString();
        int pointsAmount = jsonObject.get("number").getAsInt();
        switch (pointsType) {
            case "corner":
                return new CornerCoverPoints(pointsAmount);
            case "item":
                Item item = context.deserialize(jsonObject.get("item"), Item.class);
                return new ItemPoints(item, pointsAmount);
            case "simple":
                return new SimplePoints(pointsAmount);
            default:
                throw new JsonParseException("Unknown Points type: " + type);
        }

    }
}
