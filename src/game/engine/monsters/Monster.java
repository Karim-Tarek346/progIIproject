package game.engine.monsters;

import game.engine.Constants;
import game.engine.Role;

public abstract class Monster implements Comparable<Monster>{
    private final String name; // getter only
    private final String description; // getter only
    private Role role;
    private final Role originalRole; // getter only
    private int energy;
    private int position;
    private boolean frozen;
    private boolean shielded;
    private int confusionTurns;

    public Monster(String name, String description, Role originalRole, int energy) {
        this.name = name;
        this.description = description;
        this.originalRole = originalRole;
        this.energy = energy;

        this.role = originalRole;

        this.position = 0;
        this.confusionTurns = 0;

        this.frozen = false;
        this.shielded = false;
    }

    public abstract int compareTo(Monster o);

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = Math.max(0, energy);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position % Constants.BOARD_SIZE;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    public boolean isShielded() {
        return shielded;
    }

    public void setShielded(boolean shielded) {
        this.shielded = shielded;
    }

    public int getConfusionTurns() {
        return confusionTurns;
    }

    public void setConfusionTurns(int confusionTurns) {
        this.confusionTurns = confusionTurns;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Role getOriginalRole() {
        return originalRole;
    }

    public abstract void executePowerupEffect(Monster opponentMonster);

    public boolean isConfused(){
        return this.getConfusionTurns() != 0;
    }

    public void move(int distance){
        this.setPosition(distance);
    }

    public final void alterEnergy(int energy){
        if (this.isShielded() && energy < this.getEnergy())
            this.setShielded(false);

        else if(this.isShielded() && energy >= this.getEnergy())
            this.setEnergy((this.getEnergy() + energy));

        else
            this.setEnergy(energy);
    }

    public void decrementConfusion(){
        if(this.getConfusionTurns() > 0)
            this.setConfusionTurns((this.getConfusionTurns() - 1));
        if(this.getConfusionTurns() == 0)
            this.setRole(this.getOriginalRole());
    }
}
