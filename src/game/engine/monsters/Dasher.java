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
        if (this.getMomentumTurns() > 0) {
            // If powerup is active, move at 3x speed
            super.move(distance * 3);
            // Decrement the timer after moving
            this.setMomentumTurns(this.getMomentumTurns() - 1);
        } else {
            // Default Dasher passive: 2x speed
            super.move(distance * 2);
        }
    }
}

