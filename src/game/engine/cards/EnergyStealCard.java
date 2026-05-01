package game.engine.cards;

import game.engine.interfaces.CanisterModifier;
import game.engine.monsters.Monster;

public class EnergyStealCard extends Card implements CanisterModifier {
    private int energy;

    public EnergyStealCard(String name, String description, int rarity, int energy){
        super(name, description, rarity, true);
        this.energy = energy;
    }

    public int getEnergy() {
        return energy;
    }

    @Override
    public void performAction(Monster player, Monster opponent) {
        int opponentOldEnergy = opponent.getEnergy();

        // Pass the full card energy to alterEnergy to properly trigger the opponent's passives
        modifyCanisterEnergy(opponent, -this.energy);

        int lost = opponentOldEnergy - opponent.getEnergy();
        
        // Player only gets what was ACTUALLY lost by the opponent, capped at the card's energy limit
        if (lost > 0) {
            int actualStolen = Math.min(this.energy, lost);
            modifyCanisterEnergy(player, actualStolen);
        }
    }

    @Override
    public void modifyCanisterEnergy(Monster monster, int canisterValue) {
        monster.alterEnergy(canisterValue);
    }
}
