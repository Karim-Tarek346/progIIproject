package game.engine;

import game.engine.exceptions.InvalidMoveException;
import game.engine.exceptions.OutOfEnergyException;
import game.engine.monsters.Monster;
import game.engine.dataloader.DataLoader;

import java.io.IOException;
import java.util.ArrayList;

public class Game {
    private Board board;
    private ArrayList<Monster> allMonsters;
    private Monster player;
    private Monster opponent;
    private Monster current;

    public Game(Role playerRole) throws IOException {
        // 1. Load the data using DataLoader
        // The Board constructor will handle setCardsByRarity and reloadCards[cite: 1]
        this.board = new Board(DataLoader.readCards());
        this.allMonsters = DataLoader.readMonsters();

        // 2. Select the player and opponent monsters
        this.player = selectRandomMonsterByRole(playerRole);
        if (playerRole == Role.SCARER) {
            this.opponent = selectRandomMonsterByRole(Role.LAUGHER);
        } else {
            this.opponent = selectRandomMonsterByRole(Role.SCARER);
        }

        // 3. Remove player and opponent from the list so they aren't used for MonsterCells
        // This ensures precisely 6 monsters remain for the 6 MonsterCell indices[cite: 2]
        this.allMonsters.remove(this.player);
        this.allMonsters.remove(this.opponent);

        // 4. Set the stationed monsters in the Board class before initializing the grid[cite: 1]
        // Since stationedMonsters is static in your Board, this makes it accessible to initializeBoard[cite: 1]
        Board.setStationedMonsters(this.allMonsters);

        // 5. Initialize the board grid with specialized cells from CSV[cite: 1]
        this.board.initializeBoard(DataLoader.readCells());

        // 6. Final turn and position setup[cite: 2]
        this.current = player;
        this.player.setPosition(0); // Constants.STARTING_POSITION[cite: 2]
        this.opponent.setPosition(0);
    }

    private Monster selectRandomMonsterByRole(Role role) {
        ArrayList<Monster> filtered = new ArrayList<>();
        for (Monster m : allMonsters) {
            if (m.getRole() == role) {
                filtered.add(m);
            }
        }
        if (filtered.isEmpty()) return null;
        return filtered.get((int) (Math.random() * filtered.size()));
    }

    private Monster getCurrentOpponent() {
        return (current == player) ? opponent : player;
    }

    public void usePowerup() throws OutOfEnergyException {
        if (current.getEnergy() < Constants.POWERUP_COST) {
            throw new OutOfEnergyException();
        }
        current.setEnergy(current.getEnergy() - Constants.POWERUP_COST);
        current.executePowerupEffect(getCurrentOpponent());
    }

    public void playTurn() throws InvalidMoveException {
        if (current.isFrozen()) {
            current.setFrozen(false);
        } else {
            int roll = rollDice();
            this.board.moveMonster(current, roll, getCurrentOpponent());
        }
        this.switchTurn();
    }

    private int rollDice() {
        return (int) (Math.random() * 6) + 1;
    }

    private void switchTurn() {
        this.current = getCurrentOpponent();
    }

    private boolean checkWinCondition(Monster monster) {
        // Monster must be at the final cell (99) with at least 1000 energy[cite: 2]
        return monster.getPosition() >= Constants.WINNING_POSITION &&
                monster.getEnergy() >= Constants.WINNING_ENERGY;
    }

    public Monster getWinner() {
        if (checkWinCondition(player)) return player;
        if (checkWinCondition(opponent)) return opponent;
        return null;
    }

    // Getters
    public Board getBoard() { return board; }
    public ArrayList<Monster> getAllMonsters() { return allMonsters; }
    public Monster getPlayer() { return player; }
    public Monster getOpponent() { return opponent; }
    public Monster getCurrent() { return current; }
    public void setCurrent(Monster current) { this.current = current; }
}