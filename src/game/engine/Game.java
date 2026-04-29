package game.engine;
import game.engine.exceptions.OutOfEnergyException;
import game.engine.monsters.Monster;
import game.engine.dataloader.DataLoader;

import java.io.IOException;
import java.util.ArrayList;

public class Game {
    private Board board; //Getter only
    private ArrayList<Monster> allMonsters ; //Getter only
    private Monster player; //Getter
    private Monster opponent; //Getter
    private Monster current; // Getter and setter

    public Game(Role playerRole) throws IOException {
        this.board = new Board(DataLoader.readCards());
        this.allMonsters = DataLoader.readMonsters();
        this.player = selectRandomMonsterByRole(playerRole);

        if(playerRole == Role.SCARER)
            this.opponent = selectRandomMonsterByRole(Role.LAUGHER);
        else
            this.opponent = selectRandomMonsterByRole(Role.SCARER);

        this.current = player;

    }

    private Monster selectRandomMonsterByRole(Role role){

        ArrayList<Monster> filtered = new ArrayList<>();

        for(Monster m : allMonsters){
            if(m.getRole() == role){
                filtered.add(m);
            }
        }

        return filtered.get((int)(Math.random() * filtered.size()));
    }

    public void setCurrent(Monster current) {
        this.current = current;
    }

    public Board getBoard() {
        return board;
    }

    public ArrayList<Monster> getAllMonsters() {
        return allMonsters;
    }

    public Monster getPlayer() {
        return player;
    }

    public Monster getOpponent() {
        return opponent;
    }

    public Monster getCurrent() {
        return current;
    }

    private int rollDice() {
        return (int) (Math.random() * 6) + 1;

    }

    private Monster getCurrentOpponent(){
        if(current == player)
            return opponent;
        return player;
    }

    public void usePowerup() throws OutOfEnergyException {
        // 1. Get the current active monster and its opponent
        Monster current = this.current;
        Monster opponent = this.getCurrentOpponent();

        // 2. Define the energy cost (ensure this matches your Constants class)
        int powerupCost = Constants.POWERUP_COST;

        // 3. Check if the monster can afford the move
        if (current.getEnergy() < powerupCost) {
            // If not enough energy, throw the specific exception
            throw new OutOfEnergyException();
        }

        // 4. Subtract the energy cost
        current.setEnergy(current.getEnergy() - powerupCost);

        // 5. Trigger the specific powerup effect on the opponent
        current.executePowerupEffect(opponent);
    }
}


