package it.polimi.ingsw.am01.network.message;

import it.polimi.ingsw.am01.network.message.s2c.*;

public interface S2CMessageVisitor {
    void visit(BroadcastMessageSentS2C message);

    void visit(DirectMessageSentS2C message);

    void visit(DoubleChoiceS2C message);

    void visit(DoubleSideChoiceS2C message);

    void visit(EmptySourceS2C message);

    void visit(GameAlreadyStartedS2C message);

    void visit(GameFinishedS2C message);

    void visit(GameJoinedS2C message);

    void visit(GameNotFoundS2C message);

    void visit(InvalidCardS2C message);

    void visit(InvalidMaxPlayersS2C message);

    void visit(InvalidObjectiveSelectionS2C message);

    void visit(InvalidPlacementS2C message);

    void visit(InvalidRecipientS2C message);

    void visit(KickedFromGameS2C message);

    void visit(NameAlreadyTakenS2C message);

    void visit(NewMessageS2C message);

    void visit(NotEnoughPlayersS2C message);

    void visit(PingS2C message); //TODO: maybe remove this?

    void visit(PlayerDisconnectedS2C message);

    void visit(PlayerNotInGameS2C message);

    void visit(PlayerReconnectedS2C message);

    void visit(SetBoardAndHandS2C message);

    void visit(SetGamePauseS2C message);

    void visit(SetPlayablePositionsS2C message);

    void visit(SetPlayerNameS2C message);

    void visit(SetRecoverStatusS2C message);

    void visit(SetStartingCardS2C message);

    void visit(SetupAfterReconnectionS2C message);

    void visit(UpdateDeckStatusS2C message);

    void visit(UpdateFaceUpCardsS2C message);

    void visit(UpdateGameListS2C message);

    void visit(UpdateGameStatusAndSetupObjectiveS2C message);

    void visit(UpdateGameStatusAndTurnS2C message);

    void visit(UpdateGameStatusS2C message);

    void visit(UpdateObjectiveSelectedS2C message);

    void visit(UpdatePlayAreaAfterUndoS2C message);

    void visit(UpdatePlayAreaS2C message);

    void visit(UpdatePlayerColorS2C message);

    void visit(UpdatePlayerHandS2C message);

    void visit(UpdatePlayerListS2C message);
}
