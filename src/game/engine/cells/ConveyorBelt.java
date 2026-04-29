package game.engine.cells;

import game.engine.exceptions.InvalidMoveException;
import game.engine.monsters.Monster;

public class ConveyorBelt extends TransportCell {

    public ConveyorBelt(String name, int effect)
    {
        super(name,effect);
    }

    @Override
    public void onLand(Monster landingMonster, Monster opponentMonster) throws InvalidMoveException {
        // 1. Always call super to register the landing monster on the cell
        super.onLand(landingMonster, opponentMonster);

        // 2. Trigger the transport effect on the monster that just landed
        this.transport(landingMonster);
    }

}
