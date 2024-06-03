package it.polimi.ingsw.am01.client.gui.event;


import it.polimi.ingsw.am01.client.View;

public record NewMessageEvent(View.Message message) implements ViewEvent {
}
