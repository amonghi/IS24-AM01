package it.polimi.ingsw.am01.model.game;

public class IllegalTurnException extends IllegalStateException{
    public IllegalTurnException(){
        super("It's not this player's turn");
    }
}
