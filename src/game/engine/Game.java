package game.engine;
import game.engine.exceptions.InvalidMoveException;
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

    public void playTurn() throws InvalidMoveException {
        Monster active = this.current;

        // 1. Check if the current monster is frozen
        if (active.isFrozen()) {
            // Skip turn and unfreeze for the next round
            active.setFrozen(false);
        } else {
            // 2. Roll the dice (1-6)
            int roll = this.rollDice();

            // 3. Move the monster on the board
            // This method also triggers onLand effects and checks for collisions
            this.board.moveMonster(active, roll, this.getCurrentOpponent());
        }

        // 4. Always switch the turn at the end
        this.switchTurn();
    }

    private void switchTurn(){
        this.current = this.getCurrentOpponent();
    }

    private boolean checkWinCondition(Monster monster) {
        // 1. Check if the monster has reached the final cell (index 99)
        boolean reachedEnd = monster.getPosition() >= 99;

        // 2. Check if the monster has the required winning energy
        boolean hasEnoughEnergy = monster.getEnergy() >= Constants.WINNING_ENERGY;

        // 3. Both conditions must be true for a win
        return reachedEnd && hasEnoughEnergy;
    }

    public Monster getWinner() {
        // Check if the primary player has won
        if (checkWinCondition(this.player)) {
            return this.player;
        }

        // Check if the opponent has won
        if (checkWinCondition(this.opponent)) {
            return this.opponent;
        }

        // If no one has met the conditions yet, return null
        return null;
    }
}


