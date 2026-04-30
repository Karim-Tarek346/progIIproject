package game.engine;
import game.engine.cells.*;
import game.engine.exceptions.InvalidMoveException;
import game.engine.monsters.Monster;
import game.engine.cards.Card;
import game.engine.Constants;

import java.util.*;

import static game.engine.dataloader.DataLoader.readCards;

public class Board {
    private Cell[][] boardCells; // Getter only
    private static ArrayList<Monster> stationedMonsters; //Getter and Setter
    private static ArrayList<Card> originalCards; // Getter only
    public static ArrayList<Card> cards; //Getter and Setter

    public Board(ArrayList<Card> readCards) {
        stationedMonsters = new ArrayList<>();
        cards = new ArrayList<>();
        originalCards = readCards;
        this.boardCells = new Cell[Constants.BOARD_ROWS][Constants.BOARD_COLS];

        // Expands the originalCards list based on the rarity of each card
        this.setCardsByRarity();

        // Copies the expanded originalCards into the active 'cards' deck and shuffles it
        Board.reloadCards();

    }

    public static ArrayList<Monster> getStationedMonsters() {
        return stationedMonsters;
    }

    public static ArrayList<Card> getCards() {
        return cards;
    }

    public Cell[][] getBoardCells() {
        return boardCells;
    }

    public static ArrayList<Card> getOriginalCards() {
        return originalCards;
    }

    public static void setCards(ArrayList<Card> cards) {
        Board.cards = cards;
    }

    public static void setStationedMonsters(ArrayList<Monster> stationedMonsters) {
        Board.stationedMonsters = stationedMonsters;
    }

    private int[] indexToRowCol(int index){
        int row = index / Constants.BOARD_ROWS;
        int col = (row % 2 == 0)? (index % Constants.BOARD_COLS) : Constants.BOARD_COLS - 1 - (index % Constants.BOARD_COLS);
        return new int[]{row, col};
    }

    private Cell getCell(int index){
        int[] split = indexToRowCol(index);
        return boardCells[split[0]][split[1]];
    }

    private void setCell(int index, Cell cell){
        int[] split = indexToRowCol(index);
        boardCells[split[0]][split[1]] = cell;
    }

    public static void reloadCards() {
        // Create a new list using the elements of the original
        cards = new ArrayList<>(originalCards);
        Collections.shuffle(cards);
    }

    public static Card drawCard() {
        if (getCards().isEmpty()) reloadCards();
        Card drawn = getCards().get(0);
        cards.remove(0);
        setCards(cards);
        return drawn;
    }

    private void setCardsByRarity(){
        ArrayList<Card> expandedDeck = new ArrayList<>();

        for(int i = 0; i < originalCards.size(); i++){
            Card currentCard = originalCards.get(i);

            int rarity = currentCard.getRarity();

            for(int j = 0; j < rarity; j++){
                expandedDeck.add(currentCard);
            }
        }
        originalCards = expandedDeck;

    }

    public void initializeBoard(ArrayList<Cell> specialCells) {
        // 1. Separate cells (Logic remains same)
        ArrayList<DoorCell> doors = new ArrayList<>();
        ArrayList<Cell> belts = new ArrayList<>();
        ArrayList<Cell> socks = new ArrayList<>();
        for (Cell c : specialCells) {
            if (c instanceof DoorCell) doors.add((DoorCell) c);
            else if (c instanceof ConveyorBelt) belts.add(c);
            else if (c instanceof ContaminationSock) socks.add(c);
        }

        // 2. Build Grid with Safety Checks
        int doorIndex = 0;
        for (int i = 0; i < Constants.BOARD_SIZE; i++) {
            if (i % 2 == 0) {
                this.setCell(i, new Cell("Rest Cell"));
            } else {
                if (doorIndex < doors.size()) {
                    this.setCell(i, doors.get(doorIndex));
                    doorIndex++;
                } else {
                    this.setCell(i, new Cell("Rest Cell"));
                }
            }
        }

        // 3. Overwrite with Specials using list size checks
        int beltIndex = 0;
        for (int index : Constants.CONVEYOR_CELL_INDICES) {
            if (beltIndex < belts.size()) {
                this.setCell(index, belts.get(beltIndex));
                beltIndex++;
            }
        }

        int sockIndex = 0;
        for (int index : Constants.SOCK_CELL_INDICES) {
            if (sockIndex < socks.size()) {
                this.setCell(index, socks.get(sockIndex));
                sockIndex++;
            }
        }

        for (int index : Constants.CARD_CELL_INDICES) {
            this.setCell(index, new CardCell("Card Cell"));
        }

        // 4. Station Monsters
        ArrayList<Monster> stationed = getStationedMonsters();
        for (int i = 0; i < stationed.size(); i++) {
            Monster m = stationed.get(i);
            int pos = Constants.MONSTER_CELL_INDICES[i];
            m.setPosition(pos);
            this.setCell(pos, new MonsterCell("Monster Cell", m));
        }
    }

    public void moveMonster(Monster currentMonster, int roll, Monster opponentMonster) throws InvalidMoveException {
        int originalPosition = currentMonster.getPosition();
        currentMonster.move(roll);

        if (currentMonster.getPosition() > Constants.WINNING_POSITION)
            currentMonster.setPosition(Constants.WINNING_POSITION);

        if (currentMonster.getPosition() == opponentMonster.getPosition()) {
            currentMonster.setPosition(originalPosition);
            throw new InvalidMoveException("Cannot land on the opponent.");
        }

        // Trigger cell effect
        Cell landedCell = this.getCell(currentMonster.getPosition());
        landedCell.onLand(currentMonster, opponentMonster);

        // Second collision check after traps/cards
        if (currentMonster.getPosition() == opponentMonster.getPosition()) {
            currentMonster.setPosition(originalPosition);
            throw new InvalidMoveException("Trap caused collision.");
        }

        // Only decrement the active monster's confusion
        currentMonster.decrementConfusion();

        this.updateMonsterPositions(currentMonster, opponentMonster);
    }

    private void updateMonsterPositions(Monster player, Monster opponent) {
        for (int i = 0; i < Constants.BOARD_SIZE; i++) {
            Cell currentCell = this.getCell(i);

            // PRESERVE STATIONED MONSTERS:
            // Do not clear cells that are MonsterCells
            if (!(currentCell instanceof MonsterCell)) {
                currentCell.setMonster(null);
            }
        }
        // Sync the players to the grid
        this.getCell(player.getPosition()).setMonster(player);
        this.getCell(opponent.getPosition()).setMonster(opponent);
    }

}