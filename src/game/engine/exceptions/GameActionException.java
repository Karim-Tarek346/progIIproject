package game.engine.exceptions;

abstract public class GameActionException extends Exception {
    public GameActionException(){
        super();
    }

    public GameActionException(String message){
        super(message);
    }
}