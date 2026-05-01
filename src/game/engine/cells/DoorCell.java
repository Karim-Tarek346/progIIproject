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
    
    public int getEnergy() { return energy; }
    public Role getRole() { return role; }
    public boolean isActivated() { return activated; }
    public void setActivated(boolean activated) { this.activated = activated; }

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

            
            if (isPenalty) {
                Monster shieldProvider = null;
                if (landingMonster.isShielded()) {
                    shieldProvider = landingMonster;
                } else if (Board.getStationedMonsters() != null) {
                    for (Monster m : Board.getStationedMonsters()) {
                        if (m.getRole() == landingMonster.getRole() && m.isShielded()) {
                            shieldProvider = m;
                            break;
                        }
                    }
                }


                if (shieldProvider != null) {
                    shieldProvider.setShielded(false);
                    return; 
                }
            }

            
            int oldEnergy = landingMonster.getEnergy();
            this.modifyCanisterEnergy(landingMonster, this.energy); 
            boolean energyChanged = (landingMonster.getEnergy() != oldEnergy);

            if (Board.getStationedMonsters() != null) {
                for (Monster m : Board.getStationedMonsters()) {
                    if (m != landingMonster && m.getRole() == landingMonster.getRole()) {
                        int mOld = m.getEnergy();
                        this.modifyCanisterEnergy(m, this.energy); 
                        if (m.getEnergy() != mOld) {
                            energyChanged = true;
                        }
                    }
                }
            }


            if (energyChanged) {
                this.setActivated(true);
            }
        }
    }

    
}
