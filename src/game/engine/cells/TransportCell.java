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
    public void onLand(Monster landingMonster, Monster opponentMonster) {
        super.onLand(landingMonster, opponentMonster);
        this.transport(landingMonster);
    }
    //not monster.move(effect); cuz it implements the passive features twice
    public void transport(Monster monster){
        monster.setPosition(monster.getPosition() + effect);
    }
}
