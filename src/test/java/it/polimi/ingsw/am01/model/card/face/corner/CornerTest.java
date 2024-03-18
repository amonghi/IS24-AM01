package it.polimi.ingsw.am01.model.card.face.corner;

import it.polimi.ingsw.am01.model.collectible.Item;
import it.polimi.ingsw.am01.model.collectible.Resource;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CornerTest {
    @Test
    void createMissingCorner() {
        Corner corner = Corner.missing();

        assertFalse(corner.isSocket());
        assertEquals(corner.getCollectible(), Optional.empty());
    }
    @Test
    void createEmptyCorner() {
        Corner corner = Corner.empty();

        assertTrue(corner.isSocket());
        assertEquals(corner.getCollectible(), Optional.empty());

    }
    @Test
    void createFilledWithResourceCorner() {
        Corner corner = Corner.filled(Resource.FUNGI);

        assertTrue(corner.isSocket());
        assertEquals(corner.getCollectible(), Optional.of(Resource.FUNGI));
    }
    @Test
    void createFilledWithItemCorner() {
        Corner corner = Corner.filled(Item.QUILL);

        assertTrue(corner.isSocket());
        assertEquals(corner.getCollectible(), Optional.of(Item.QUILL));
    }

}