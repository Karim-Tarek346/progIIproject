package game.engine.monsters;

import game.engine.Role;

public class MultiTasker extends Monster{
    private int normalSpeedTurns;

    public MultiTasker(String name, String description, Role role, int energy){
        super(name, description, role, energy);
        this.normalSpeedTurns = 0;
    }

    public int getNormalSpeedTurns() {
        return normalSpeedTurns;
    }

    public void setNormalSpeedTurns(int normalSpeedTurns) {
        this.normalSpeedTurns = normalSpeedTurns;
    }

    @Override
    public int compareTo(Monster o){
        return this.getPosition() - o.getPosition();
    }

    @Override
    public void executePowerupEffect(Monster opponentMonster) {
        this.setNormalSpeedTurns(2);
    }

    @Override
    public void move(int distance){
        super.move(distance/2);
    }


}
