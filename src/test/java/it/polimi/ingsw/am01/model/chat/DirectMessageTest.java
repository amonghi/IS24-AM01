package it.polimi.ingsw.am01.model.chat;

import it.polimi.ingsw.am01.model.exception.MessageSentToThemselvesException;
import it.polimi.ingsw.am01.model.player.PlayerProfile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DirectMessageTest {

    @Test
    public void testDirectMessage() throws MessageSentToThemselvesException {
        PlayerProfile sender = new PlayerProfile("Darius");
        PlayerProfile otherPlayer = new PlayerProfile("George");
        PlayerProfile recipient = new PlayerProfile("Mattew");
        String content = "Hi Mattew, I'm Darius!";
        DirectMessage directMessage = new DirectMessage(sender, recipient, content);

        assertEquals(sender, directMessage.getSender());
        assertEquals(content, directMessage.getContent());

        assertTrue(directMessage.isRecipient(recipient));
        assertFalse(directMessage.isRecipient(otherPlayer));
        assertFalse(directMessage.isRecipient(sender));
    }
}