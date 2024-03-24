package it.polimi.ingsw.am01.model.deserializers;

import com.google.gson.*;
import it.polimi.ingsw.am01.model.card.face.FrontCardFace;
import it.polimi.ingsw.am01.model.card.face.corner.Corner;
import it.polimi.ingsw.am01.model.card.face.placement.PlacementConstraint;
import it.polimi.ingsw.am01.model.card.face.points.Points;
import it.polimi.ingsw.am01.model.collectible.Resource;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class FrontCardFaceDeserializer implements JsonDeserializer<FrontCardFace> {
    @Override
    public FrontCardFace deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Corner tr = context.deserialize(jsonObject.get("tr"), Corner.class);
        Corner tl = context.deserialize(jsonObject.get("tl"), Corner.class);
        Corner br = context.deserialize(jsonObject.get("br"), Corner.class);
        Corner bl = context.deserialize(jsonObject.get("bl"), Corner.class);
        if(jsonObject.has("points")){
            Points points = context.deserialize(jsonObject.get("points"), Points.class);
            if(jsonObject.has("constraint")){
                PlacementConstraint constraint = context.deserialize(jsonObject.get("constraint"), PlacementConstraint.class);
                return new FrontCardFace(tr, tl, br, bl, constraint, points);
            }
            return new FrontCardFace(tr, tl, br, bl, points);
        }
        return new FrontCardFace(tr, tl, br, bl);
    }
}
