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

    @Override
    public void onLand(Monster landingMonster, Monster opponentMonster) throws InvalidMoveException {
        super.onLand(landingMonster, opponentMonster); //

        if (!this.isActivated()) {
            // Determine if it's a bonus (+) or penalty (-)
            int effectValue = (landingMonster.getRole() == this.role) ? this.energy : -this.energy; //

            // Record energy before changes to check if shield was used
            int oldEnergy = landingMonster.getEnergy();

            // 1. Apply to the landing monster directly
            this.modifyCanisterEnergy(landingMonster, effectValue); //

            // 2. Apply to the TEAM (All monsters with the landing monster's role)
            // This includes the landing monster again, which explains the 320 vs 210 failure
            for (Monster m : Board.getStationedMonsters()) { //
                if (m.getRole() == landingMonster.getRole()) {
                    this.modifyCanisterEnergy(m, effectValue); //
                }
            }

            // 3. Activation Logic: Only activate if someone's energy actually changed
            // If a shield blocked the change, oldEnergy will equal newEnergy
            if (landingMonster.getEnergy() != oldEnergy) { //
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
