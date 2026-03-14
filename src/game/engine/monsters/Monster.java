package game.engine.monsters;

import game.engine.Role;

abstract class Monster {
    String name; // getter only
    String description; // getter only
    Role role;
    Role originalRole; // getter only
    int energy;
    int position;
    boolean frozen;
    boolean shielded;
    int confusionTurns;

    public Monster(){

    }

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
        this.energy = energy;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
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
}
