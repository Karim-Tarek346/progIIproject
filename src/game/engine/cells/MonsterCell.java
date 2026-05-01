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
    public void onLand(Monster landingMonster, Monster opponentMonster) {
        super.onLand(landingMonster, opponentMonster);

        if (landingMonster.getRole().equals(cellMonster.getRole())) {
            landingMonster.executePowerupEffect(opponentMonster);
        } else if (landingMonster.getEnergy() > cellMonster.getEnergy()) {
            int landingOldEnergy = landingMonster.getEnergy();
            int cellOldEnergy = cellMonster.getEnergy();

            // Calculate differences for alterEnergy to handle passives/shields correctly
            int changeForLanding = cellOldEnergy - landingOldEnergy;
            int changeForCell = landingOldEnergy - cellOldEnergy;

            landingMonster.alterEnergy(changeForLanding);
            cellMonster.alterEnergy(changeForCell);
        }
    }
}
