package game.engine.monsters;

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

    }

    private int stealEnergyFrom(Monster target){
        if(target.getEnergy() < Constants.SCHEMER_STEAL)
            return target.getEnergy();
        return Constants.SCHEMER_STEAL;
    }
}