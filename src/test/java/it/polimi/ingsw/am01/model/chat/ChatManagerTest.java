package it.polimi.ingsw.am01.model.chat;

import it.polimi.ingsw.am01.model.exception.MessageSentToThemselvesException;
import it.polimi.ingsw.am01.model.player.PlayerProfile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChatManagerTest {

    ChatManager manager = new ChatManager();
    PlayerProfile pp1 = new PlayerProfile("Alice");
    PlayerProfile pp2 = new PlayerProfile("Bob");
    PlayerProfile pp3 = new PlayerProfile("Charlie");
    PlayerProfile pp4 = new PlayerProfile("David");

    @Test
    void sendDirectMessage() throws MessageSentToThemselvesException {
        Message m = new DirectMessage(pp1, pp2, "Message from Alice to Bob");
        manager.send(m);
        assertTrue(manager.getMailbox(pp2).contains(m));
        assertTrue(manager.getMailbox(pp1).contains(m));
        assertFalse(manager.getMailbox(pp3).contains(m));
        assertFalse(manager.getMailbox(pp4).contains(m));
        assertEquals("Message from Alice to Bob", m.getContent());
    }

    @Test
    void sendBroadcastMessage() {
        Message m = new BroadcastMessage(pp4, "Message for everyone!");
        manager.send(m);
        assertTrue(manager.getMailbox(pp1).contains(m));
        assertTrue(manager.getMailbox(pp2).contains(m));
        assertTrue(manager.getMailbox(pp3).contains(m));
        assertTrue(manager.getMailbox(pp4).contains(m));
        assertEquals("Message for everyone!", m.getContent());
    }

    @Test
    void equality() throws MessageSentToThemselvesException {
        ChatManager chatManager1 = new ChatManager();
        ChatManager chatManager2 = new ChatManager();
        chatManager1.send(new BroadcastMessage(pp4, "Message for everyone!"));
        chatManager2.send(new BroadcastMessage(pp4, "Message for everyone!"));
        chatManager1.send(new DirectMessage(pp1, pp2, "Message from Alice to Bob"));
        chatManager2.send(new DirectMessage(pp1, pp2, "Message from Alice to Bob"));
        assertEquals(chatManager1, chatManager2);
    }
}