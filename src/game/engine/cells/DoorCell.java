package game.engine.cells;

import game.engine.Board;
import game.engine.Role;
import game.engine.interfaces.CanisterModifier;
import game.engine.monsters.Monster;

public class DoorCell extends Cell implements CanisterModifier {
    private Role role;
    private int energy;
    private boolean activated;

    public DoorCell(String name, Role role, int energy){
        super(name);
        this.role = role;
        this.energy = energy;
        this.activated = false;
    }

    @Override
    public void modifyCanisterEnergy(Monster monster, int canisterValue) {
        if (monster.getRole() == this.role) {
            monster.alterEnergy(canisterValue);
        } else {
            monster.alterEnergy(-canisterValue);
        }
    }

    @Override
    public void onLand(Monster landingMonster, Monster opponentMonster) {
        super.onLand(landingMonster, opponentMonster);

        if (!this.isActivated()) {
            boolean isPenalty = (landingMonster.getRole() != this.role);
            boolean teamShielded = false;
            Monster shieldProvider = null;

            // Check if ANY member of the team can provide a shield against the penalty
            if (isPenalty) {
                if (landingMonster.isShielded()) {
                    teamShielded = true;
                    shieldProvider = landingMonster;
                } else if (Board.getStationedMonsters() != null) {
                    for (Monster m : Board.getStationedMonsters()) {
                        if (m.getRole() == landingMonster.getRole() && m.isShielded()) {
                            teamShielded = true;
                            shieldProvider = m;
                            break;
                        }
                    }
                }
            }

            if (teamShielded) {
                shieldProvider.setShielded(false); // Consume the shield
                return; // Penalty blocked, door remains unactivated
            }

            int oldEnergy = landingMonster.getEnergy();
            this.modifyCanisterEnergy(landingMonster, this.energy);
            boolean energyChanged = (landingMonster.getEnergy() != oldEnergy);

            if (Board.getStationedMonsters() != null) {
                for (Monster m : Board.getStationedMonsters()) {
                    // Ensure we don't apply the effect to the landing monster twice
                    if (m != landingMonster && m.getRole() == landingMonster.getRole()) {
                        int mOldEnergy = m.getEnergy();
                        this.modifyCanisterEnergy(m, this.energy);
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

    public int getEnergy() { return energy; }
    public Role getRole() { return role; }
    public boolean isActivated() { return activated; }
    public void setActivated(boolean activated) { this.activated = activated; }
}
