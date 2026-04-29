package game.engine.cells;

import game.engine.exceptions.InvalidMoveException;
import game.engine.monsters.Monster;

public abstract class TransportCell extends Cell{
    private int effect; //Getter only

    public TransportCell(String name, int effect){
        super(name);
        this.effect = effect;
    }

    public int getEffect(){
        return effect;
    }

    @Override
    public void onLand(Monster landingMonster, Monster opponentMonster) throws InvalidMoveException {
        super.onLand(landingMonster, opponentMonster);
        transport(landingMonster);
    }

    public void transport(Monster monster) throws InvalidMoveException{
        monster.move(effect);
        //if out of the board
        if(monster.getPosition() > 99) throw new InvalidMoveException("Exceed Maximum index");
        else if (monster.getPosition() < 0) throw new InvalidMoveException("Exceeded Minimum index");
    }
}
