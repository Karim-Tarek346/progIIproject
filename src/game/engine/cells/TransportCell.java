package game.engine.cells;

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
    public void onLand(Monster landingMonster, Monster opponentMonster){
        super.onLand(landingMonster, opponentMonster);
        transport(landingMonster);
    }

    public void transport(Monster monster){
        monster.move(effect);
        //if out of the board
        if(monster.getPosition() > 99) monster.setPosition(99);
        else if (monster.getPosition() < 0) monster.setPosition(0);
    }
}
