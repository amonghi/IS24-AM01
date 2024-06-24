package it.polimi.ingsw.am01.model.card.face;

import it.polimi.ingsw.am01.model.card.face.corner.Corner;
import it.polimi.ingsw.am01.model.card.face.corner.CornerPosition;
import it.polimi.ingsw.am01.model.collectible.Resource;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BackCardFaceTest {
    @Test
    void createBackStarterCardFace() {
        Map<Resource, Integer> resources = new HashMap<>();
        resources.put(Resource.ANIMAL, 1);
        resources.put(Resource.PLANT, 1);
        resources.put(Resource.INSECT, 1);
        Corner tr = Corner.filled(Resource.INSECT);
        Corner tl = Corner.empty();
        Corner br = Corner.empty();
        Corner bl = Corner.filled(Resource.FUNGI);

        BaseCardFace back = new BackCardFace(tr, tl, br, bl, resources);

        assertEquals(tr, back.corner(CornerPosition.TOP_RIGHT));
        assertEquals(tl, back.corner(CornerPosition.TOP_LEFT));
        assertEquals(br, back.corner(CornerPosition.BOTTOM_RIGHT));
        assertEquals(bl, back.corner(CornerPosition.BOTTOM_LEFT));
        assertEquals(resources, back.getCenterResources());
        assertEquals(Optional.empty(), back.getPoints());
        assertEquals(Optional.empty(), back.getPlacementConstraint());
    }

    @Test
    void createBackResourceOrGoldenCardFace() {
        Map<Resource, Integer> resources = new HashMap<>();
        resources.put(Resource.ANIMAL, 1);
        Corner tr = Corner.empty();
        Corner tl = Corner.empty();
        Corner br = Corner.empty();
        Corner bl = Corner.empty();

        BaseCardFace back = new BackCardFace(tr, tl, br, bl, resources);

        assertEquals(tr, back.corner(CornerPosition.TOP_RIGHT));
        assertEquals(tl, back.corner(CornerPosition.TOP_LEFT));
        assertEquals(br, back.corner(CornerPosition.BOTTOM_RIGHT));
        assertEquals(bl, back.corner(CornerPosition.BOTTOM_LEFT));
        assertEquals(resources, back.getCenterResources());
        assertEquals(Optional.empty(), back.getPoints());
        assertEquals(Optional.empty(), back.getPlacementConstraint());
    }

    @Test
    void equality(){
        Map<Resource, Integer> resources = new HashMap<>();
        Corner tr = Corner.filled(Resource.INSECT);
        Corner tl = Corner.empty();
        Corner br = Corner.empty();
        Corner bl = Corner.filled(Resource.FUNGI);

        BaseCardFace back1 = new BackCardFace(tr, tl, br, bl, resources);
        BaseCardFace back2 = new BackCardFace(tr, tl, br, bl, resources);
        assertEquals(back1, back2);
    }

}