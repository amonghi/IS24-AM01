package it.polimi.ingsw.am01.model.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import it.polimi.ingsw.am01.model.card.face.placement.PlacementConstraint;

import java.lang.reflect.Type;

public class PlacementConstraintSerializer implements JsonSerializer<PlacementConstraint> {
    @Override
    public JsonElement serialize(PlacementConstraint placementConstraint, Type type, JsonSerializationContext context) {
        return context.serialize(placementConstraint.getRequiredResources());
    }
}
