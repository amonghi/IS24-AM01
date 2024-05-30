package it.polimi.ingsw.am01.client.gui.event;


import it.polimi.ingsw.am01.client.gui.GUIView;

public record NewMessageEvent(GUIView.Message message) implements ViewEvent {
}
