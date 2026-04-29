package game.engine.cells;

import game.engine.Constants;
import game.engine.interfaces.CanisterModifier;
import game.engine.monsters.Monster;

public class ContaminationSock extends TransportCell implements CanisterModifier {

    public ContaminationSock(String name, int effect){
        super(name,effect);
    }

    @Override
    public void transport(Monster monster){
        monster.move(this.getEffect()); //Move in negative direction
        if (monster.getPosition() < 0) monster.setPosition(0);
    }

    @Override
    public void onLand(Monster landingMonster, Monster opponentMonster){
        super.onLand(landingMonster, opponentMonster);
        transport(landingMonster); // move back
        modifyCanisterEnergy(landingMonster, -(Constants.SLIP_PENALTY)); // minus energy
    }

    @Override
    public void modifyCanisterEnergy(Monster monster, int canisterValue) {
        monster.alterEnergy(canisterValue);
    }

}
