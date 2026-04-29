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
    public void onLand(Monster landingMonster, Monster opponentMonster) throws InvalidMoveException {
        // 1. Execute base cell logic to register the landing monster
        super.onLand(landingMonster, opponentMonster); // [cite: 73]

        // 2. Check if the door is not yet activated
        if (!this.isActivated()) {
            boolean energyChanged = false;

            // 3. Determine the energy modifier based on whose side the door is on
            int energyModifier;
            if (landingMonster.getRole() == this.getRole()) {
                energyModifier = this.getEnergy(); // Positive gain if roles match [cite: 81]
            } else {
                energyModifier = -this.getEnergy(); // Penalty if roles mismatch [cite: 81]
            }

            // 4. Modify the landing monster's energy
            int initialLandingEnergy = landingMonster.getEnergy();

            // Use alterEnergy to automatically respect the shield mechanic [cite: 43, 81]
            // Note: If you implemented CanisterModifier, you might call modifyCanisterEnergy() here instead
            landingMonster.alterEnergy(energyModifier);

            if (landingMonster.getEnergy() != initialLandingEnergy) {
                energyChanged = true;
            }

            // 5. Modify stationed monsters of the same role as the landing monster
            // Note: Adjust Board.getStationedMonsters() based on how you access your stationed monsters array/list
            for (Monster stationedMonster : Board.getStationedMonsters()) { // [cite: 81]
                if (stationedMonster.getRole() == landingMonster.getRole()) { // [cite: 81]
                    int initialStationedEnergy = stationedMonster.getEnergy();

                    stationedMonster.alterEnergy(energyModifier); // Respects shield [cite: 81]

                    if (stationedMonster.getEnergy() != initialStationedEnergy) {
                        energyChanged = true;
                    }
                }
            }

            // 6. Mark as activated ONLY if energy was gained or lost (e.g., not fully blocked by a shield)
            if (energyChanged) { // [cite: 81]
                this.setActivated(true); // [cite: 81]
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
