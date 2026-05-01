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
        // Clamp the steal amount to what the opponent actually has
        int stealAmount = Math.min(this.energy, opponent.getEnergy());
        int opponentOldEnergy = opponent.getEnergy();

        modifyCanisterEnergy(opponent, -stealAmount);

        // Player only gets what was ACTUALLY lost by the opponent after passives/shields
        int actualStolen = opponentOldEnergy - opponent.getEnergy();
        if (actualStolen > 0) {
            modifyCanisterEnergy(player, actualStolen);
        }
    }

    @Override
    public void modifyCanisterEnergy(Monster monster, int canisterValue) {
        monster.alterEnergy(canisterValue);
    }
}