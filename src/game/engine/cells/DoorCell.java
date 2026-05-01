package game.engine.cells;

import game.engine.Board;
import game.engine.Role;
import game.engine.exceptions.InvalidMoveException;
import game.engine.interfaces.CanisterModifier;
import game.engine.monsters.Monster;

import java.util.ArrayList;

public class DoorCell extends Cell implements CanisterModifier {
    private Role role; //Getter only
    private int energy; //getter only
    private boolean activated;

    public DoorCell(String name, Role role, int energy){
        super(name);
        this.role = role;
        this.energy = energy;
        this.activated = false;
    }

    @Override
    public void modifyCanisterEnergy(Monster monster, int canisterValue) {
        monster.alterEnergy(canisterValue); //
    }

// Inside src/game/engine/cells/DoorCell.java

    @Override
    public void onLand(Monster landingMonster, Monster opponentMonster) {
        super.onLand(landingMonster, opponentMonster);

        if (!this.isActivated()) {
            int effectValue = (landingMonster.getRole() == this.role) ? this.energy : -this.energy;

            int oldEnergy = landingMonster.getEnergy();
            this.modifyCanisterEnergy(landingMonster, effectValue);
            boolean energyChanged = (landingMonster.getEnergy() != oldEnergy);

            if (Board.getStationedMonsters() != null) {
                for (Monster m : Board.getStationedMonsters()) {
                    // Ensure we don't apply the effect to the landing monster twice
                    if (m != landingMonster && m.getRole() == landingMonster.getRole()) {
                        int mOldEnergy = m.getEnergy();
                        this.modifyCanisterEnergy(m, effectValue);
                        if (m.getEnergy() != mOldEnergy) {
                            energyChanged = true;
                        }
                    }
                }
            }

            // Activate if ANY monster gained or lost energy
            if (energyChanged) {
                this.setActivated(true);
            }
        }
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
