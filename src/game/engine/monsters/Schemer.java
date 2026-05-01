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
        
        total += this.stealEnergyFrom(opponentMonster);

        
        if (Board.getStationedMonsters() != null) {
            for(Monster stationed : Board.getStationedMonsters()){
                if (stationed != this) {
                    total += this.stealEnergyFrom(stationed);
                }
            }
        }

        this.alterEnergy(total);
    }

    private int stealEnergyFrom(Monster target){
        if (target == null) return 0;
        

        int stealAmount = Math.min(Constants.SCHEMER_STEAL, target.getEnergy());
        
        boolean hadShield = target.isShielded();
        target.setShielded(false);
        

        target.alterEnergy(-stealAmount);
        

        target.setShielded(hadShield);
        
        return stealAmount;
    }
}
