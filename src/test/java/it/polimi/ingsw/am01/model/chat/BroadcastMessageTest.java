package it.polimi.ingsw.am01.model.chat;

import it.polimi.ingsw.am01.model.player.PlayerProfile;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BroadcastMessageTest {

    @Test
    public void testBroadcastMessage() {
        PlayerProfile sender = new PlayerProfile("George");
        List<PlayerProfile> recipients = new ArrayList<>();
        recipients.add(new PlayerProfile("John"));
        recipients.add(new PlayerProfile("Mary"));
        recipients.add(new PlayerProfile("Jack"));
        recipients.add(new PlayerProfile("Jane"));
        String content = "Hi everyone! How are you?";

        BroadcastMessage broadcastMessage = new BroadcastMessage(sender, content);

        assertEquals(sender, broadcastMessage.getSender());
        assertEquals(content, broadcastMessage.getContent());

        for (PlayerProfile recipient : recipients) {
            assertTrue(broadcastMessage.isRecipient(recipient));
        }
        assertFalse(broadcastMessage.isRecipient(sender));
    }
}