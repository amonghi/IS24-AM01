package it.polimi.ingsw.am01.network.message.c2s;

import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;

public record ResumeGameC2S() implements C2SNetworkMessage {
    public static final String ID = "RESUME_GAME";

    @Override
    public String getId() {
        return ID;
    }
}
