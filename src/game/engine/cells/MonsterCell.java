package game.engine.cells;

import game.engine.monsters.Monster;

public class MonsterCell extends Cell {
    private final Monster monsterCell;

    public MonsterCell(String name, Monster monsterCell){
        super(name);
        this.monsterCell = monsterCell;
    }

    public Monster getCellMonster() {
        return monsterCell;
    }
}
