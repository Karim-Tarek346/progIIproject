package game.engine.cells;

import game.engine.Constants;
import game.engine.exceptions.InvalidMoveException;
import game.engine.interfaces.CanisterModifier;
import game.engine.monsters.Monster;

public class ContaminationSock extends TransportCell implements CanisterModifier {

    public ContaminationSock(String name, int effect){
        super(name,effect);
    }


    @Override
    public void transport(Monster monster) {
        super.transport(monster); // Use the parent's movement logic[cite: 5]
        if (monster.getPosition() < 0) monster.setPosition(0);
        monster.alterEnergy(-Constants.SLIP_PENALTY);
    }


    @Override
    public void modifyCanisterEnergy(Monster monster, int canisterValue) {
        monster.alterEnergy(canisterValue);
    }

}
