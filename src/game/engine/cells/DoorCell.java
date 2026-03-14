package game.engine.cells;

import game.engine.Role;

public class DoorCell extends Cell {
    private Role role; //Getter only
    private int energy; //getter only
    private boolean activated;

    public DoorCell(String name, Role role, int energy){
        super(name);
        this.role = role;
        this.energy = energy;
        this.activated = false;
    }

    public int getEnergy() {
        return energy;
    }

    public Role getRole() {
        return role;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }
}
