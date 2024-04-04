package it.polimi.ingsw.am01.model.game;

public class IllegalMoveException extends IllegalStateException{
    public IllegalMoveException(){
        super("This move is not allowed at the current stage of the game");
    }
}
