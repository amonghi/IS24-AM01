package it.polimi.ingsw.am01.client.gui.event;

import java.util.List;

public record UpdateSecretObjectiveChoiceEvent(List<String> playersChosen) implements ViewEvent{
}
