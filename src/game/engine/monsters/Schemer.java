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
        //opponent
        total += this.stealEnergyFrom(opponentMonster);
        //stationed monsters
        for(Monster stationed : Board.getStationedMonsters()){
            total += this.stealEnergyFrom(stationed);
        }
        int current = this.getEnergy();
        //adding energy
        this.alterEnergy(current + total);
    }

    private int stealEnergyFrom(Monster target){
        int stealAmount = Constants.SCHEMER_STEAL;
        int targetEnergy = target.getEnergy();
        int stolen = Math.min(stealAmount, targetEnergy);
        target.setEnergy(targetEnergy - stolen); //ignores shield
        return stolen;
    }
}