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
        // 1. Separate the loaded CSV cells by their exact type
        ArrayList<DoorCell> doors = new ArrayList<>();
        ArrayList<Cell> belts = new ArrayList<>();
        ArrayList<Cell> socks = new ArrayList<>();

        for (Cell c : specialCells) {
            if (c instanceof DoorCell) {
                doors.add((DoorCell) c);
            } else if (c instanceof ConveyorBelt) {
                belts.add(c);
            } else if (c instanceof ContaminationSock) {
                socks.add(c);
            }
        }

        // 2. Build the Base Grid (Even = Rest Cell, Odd = DoorCell)
        int doorIndex = 0;
        for (int i = 0; i < Constants.BOARD_SIZE; i++) {
            if (i % 2 == 0) {
                // Creates a default cell with a string argument
                this.setCell(i, new Cell("Cell"));
            } else {
                // Places a DoorCell from the loaded CSV
                this.setCell(i, doors.get(doorIndex));
                doorIndex++;
            }
        }

        // 3. Overwrite specific indices with the separated Special Cells
        int beltIndex = 0;
        for (int index : Constants.CONVEYOR_CELL_INDICES) {
            this.setCell(index, belts.get(beltIndex));
            beltIndex++;
        }

        int sockIndex = 0;
        for (int index : Constants.SOCK_CELL_INDICES) {
            this.setCell(index, socks.get(sockIndex));
            sockIndex++;
        }

        for (int index : Constants.CARD_CELL_INDICES) {
            // Creates a CardCell with a string argument
            this.setCell(index, new CardCell("CardCell"));
        }

        // 4. Combine Monster setup and MonsterCell instantiation
        ArrayList<Monster> stationed = getStationedMonsters();

        for (int i = 0; i < stationed.size(); i++) {
            // Define 'm' by getting it from the stationed list
            Monster m = stationed.get(i);

            // Set the monster's position property
            int pos = Constants.MONSTER_CELL_INDICES[i];
            m.setPosition(pos);

            // Create the MonsterCell using both the string name and the valid 'm' object
            this.setCell(pos, new MonsterCell("MonsterCell", m));
        }
    }


    public void moveMonster(Monster currentMonster, int roll, Monster opponentMonster) throws InvalidMoveException{
        int originalPosition = currentMonster.getPosition();
        currentMonster.move(roll);

        if (currentMonster.getPosition() > Constants.WINNING_POSITION)
            currentMonster.setPosition(Constants.WINNING_POSITION);

        if (currentMonster.getPosition() == opponentMonster.getPosition()) {
            currentMonster.setPosition(originalPosition);
            throw new InvalidMoveException("Move invalid: Cannot land directly on the opponent.");
        }

        Cell landedCell = this.getCell(currentMonster.getPosition());
        landedCell.onLand(currentMonster, opponentMonster);

        if (currentMonster.getPosition() == opponentMonster.getPosition()) {
            currentMonster.setPosition(originalPosition);
            throw new InvalidMoveException("Move invalid: Cell effect caused a collision.");
        }

        if (currentMonster.getConfusionTurns() > 0)
            currentMonster.setConfusionTurns(currentMonster.getConfusionTurns() - 1);

        if (opponentMonster.getConfusionTurns() > 0)
            opponentMonster.setConfusionTurns(opponentMonster.getConfusionTurns() - 1);

        this.updateMonsterPositions(currentMonster, opponentMonster);
    }

    private void updateMonsterPositions(Monster player, Monster opponent) {
        // 1. Clear the entire board of the moving players
        for (int i = 0; i < Constants.BOARD_SIZE; i++) {
            Cell currentCell = this.getCell(i);

            // Wipe the moving monster from the cell
            // (Note: If your MonsterCell class uses this same variable for stationed
            // monsters, you might need an 'if !(currentCell instanceof MonsterCell)' here)
            currentCell.setMonster(null);
        }

        // 2. Place the primary player on their exact current position
        int playerPos = player.getPosition();
        Cell playerLandedCell = this.getCell(playerPos);
        playerLandedCell.setMonster(player);

        // 3. Place the opponent on their exact current position
        int opponentPos = opponent.getPosition();
        Cell opponentLandedCell = this.getCell(opponentPos);
        opponentLandedCell.setMonster(opponent);
    }

}