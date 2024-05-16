package it.polimi.ingsw.am01.network.message;

import it.polimi.ingsw.am01.model.exception.IllegalMoveException;
import it.polimi.ingsw.am01.network.NetworkException;
import it.polimi.ingsw.am01.network.message.c2s.*;

public interface MessageVisitor {
    void visit(AuthenticateC2S message) throws IllegalMoveException, NetworkException;

    void visit(CreateGameAndJoinC2S message) throws IllegalMoveException, NetworkException;

    void visit(DrawCardFromDeckC2S message) throws IllegalMoveException, NetworkException;

    void visit(DrawCardFromFaceUpCardsC2S message) throws IllegalMoveException, NetworkException;

    void visit(JoinGameC2S message) throws IllegalMoveException, NetworkException;

    void visit(PlaceCardC2S message) throws IllegalMoveException, NetworkException;

    void visit(SelectColorC2S message) throws IllegalMoveException, NetworkException;

    void visit(SelectSecretObjectiveC2S message) throws IllegalMoveException, NetworkException;

    void visit(SelectStartingCardSideC2S message) throws IllegalMoveException, NetworkException;

    void visit(StartGameC2S message) throws IllegalMoveException, NetworkException;

    void visit(SendBroadcastMessageC2S message) throws IllegalMoveException, NetworkException;

    void visit(SendDirectMessageC2S message) throws IllegalMoveException, NetworkException;
}
