package game.engine.cards;

import game.engine.Constants;
import game.engine.monsters.Monster;

public class StartOverCard extends Card{
    public StartOverCard(String name, String description, int rarity, boolean lucky){
        super(name, description, rarity, lucky);
    }

    @Override
    public void performAction(Monster player, Monster opponent) {
        if(this.isLucky())
            opponent.setPosition(Constants.STARTING_POSITION);
        else
            player.setPosition(Constants.STARTING_POSITION);
    }
}
