package game.engine.monsters;

import game.engine.Role;

public class Dasher extends Monster{
    private int momentumTurns;

    public Dasher(String name, String description, Role role, int energy) {
        super(name, description, role, energy);
        this.momentumTurns = 0;
    }

    public int getMomentumTurns() {
        return momentumTurns;
    }

    public void setMomentumTurns(int momentumTurns) {
        this.momentumTurns = momentumTurns;
    }

    @Override
    public int compareTo(Monster o){
        return this.getPosition() - o.getPosition();
    }

    @Override
    public void executePowerupEffect(Monster opponentMonster) {
        this.setMomentumTurns(3);
    }

    @Override
    public void move(int distance) {
        // Dasher moves at 2x the provided distance (the dice roll)
        super.move(distance * 2);
    }
}

