package game.engine.cells;

import game.engine.exceptions.InvalidMoveException;
import game.engine.monsters.Monster;

public class MonsterCell extends Cell {
    private final Monster cellMonster;

    public MonsterCell(String name, Monster cellMonster){
        super(name);
        this.cellMonster = cellMonster;
    }

    public Monster getCellMonster() {
        return cellMonster;
    }

    @Override
    public void onLand(Monster landingMonster, Monster opponentMonster){
        super.onLand(landingMonster, opponentMonster);
        if (landingMonster.getRole().equals(cellMonster.getRole()))
            landingMonster.executePowerupEffect(opponentMonster);

        else if (landingMonster.getEnergy() > cellMonster.getEnergy()) {
            int temp = landingMonster.getEnergy();
            landingMonster.alterEnergy(cellMonster.getEnergy());
            cellMonster.alterEnergy(temp);
        }
    }
}
