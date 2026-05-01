package game.engine.monsters;

import game.engine.Board;
import game.engine.Constants;
import game.engine.Role;

public class Schemer extends Monster {
    public Schemer(String name, String description, Role role, int energy){
        super(name, description, role, energy);
    }

    @Override
    public int compareTo(Monster o){
        return this.getPosition() - o.getPosition();
    }

    @Override
    public void executePowerupEffect(Monster opponentMonster){
        int total = 0;
        
        // Steal from the opponent
        total += this.stealEnergyFrom(opponentMonster);

        // Steal from all stationed monsters
        if (Board.getStationedMonsters() != null) {
            for(Monster stationed : Board.getStationedMonsters()){
                if (stationed != this) {
                    total += this.stealEnergyFrom(stationed);
                }
            }
        }

        // Apply the accumulated total using alterEnergy so the Schemer's passive (+10) is added
        this.alterEnergy(total);
    }

    private int stealEnergyFrom(Monster target){
        if (target == null) return 0;
        
        // Strictly steal up to 10 energy, or the target's full remaining energy if less
        int stealAmount = Math.min(Constants.SCHEMER_STEAL, target.getEnergy());
        
        // Bypass the shield by temporarily disabling it
        boolean hadShield = target.isShielded();
        target.setShielded(false);
        
        // Apply the negative energy using alterEnergy to respect the target's passives
        target.alterEnergy(-stealAmount);
        
        // Restore the shield status
        target.setShielded(hadShield);
        
        return stealAmount;
    }
}
