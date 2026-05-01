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
        int targetOldEnergy = target.getEnergy();

        boolean wasShielded = target.isShielded();
        target.setShielded(false); // Temporarily remove shield to bypass it

        target.alterEnergy(-Constants.SCHEMER_STEAL); // Route through alterEnergy for passives

        if (wasShielded) target.setShielded(true); // Restore shield if they had one

        int stolen = targetOldEnergy - target.getEnergy();
        return Math.max(0, stolen);
    }
}