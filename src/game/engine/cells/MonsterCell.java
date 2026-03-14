package game.engine.cells;

import game.engine.monsters.Monster;

public class MonsterCell extends Cell {
    private Monster cellMonster;

    public MonsterCell(String name, Monster monsterCell){
        super(name);
        this.monsterCell = monsterCell;
    }

    public Monster getCellMonster() {
        return cellMonster;
    }
}
