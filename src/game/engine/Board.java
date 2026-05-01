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

        this.setCardsByRarity();


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

    public void moveMonster(Monster currentMonster, int roll, Monster opponentMonster) throws InvalidMoveException {
        int originalPosition = currentMonster.getPosition();
        currentMonster.move(roll);

        if (currentMonster.getPosition() > Constants.WINNING_POSITION)
            currentMonster.setPosition(Constants.WINNING_POSITION);

        if (currentMonster.getPosition() == opponentMonster.getPosition()) {
            currentMonster.setPosition(originalPosition);
            throw new InvalidMoveException("Cannot land on the opponent.");
        }


        Cell landedCell = this.getCell(currentMonster.getPosition());
        landedCell.onLand(currentMonster, opponentMonster);


        if (currentMonster.getPosition() == opponentMonster.getPosition()) {
            currentMonster.setPosition(originalPosition);
            throw new InvalidMoveException("Trap caused collision.");
        }

        // Only decrement the active monster's confusion
        currentMonster.decrementConfusion();

        this.updateMonsterPositions(currentMonster, opponentMonster);
    }



    public void initializeBoard(ArrayList<Cell> specialCells) {

        for(int i = 0; i <= 99; i++){
            if(i % 2 == 0) setCell(i, new Cell("Normal cell"));
            else {
                if((i / 2) % 2 == 0)
                    setCell(i, new DoorCell("Door", Role.SCARER, 0));
                else
                    setCell(i, new DoorCell("Door", Role.LAUGHER, 0));
            }
        }

        ArrayList<DoorCell> doorList = new ArrayList<>();
        ArrayList<ConveyorBelt> conveyorList = new ArrayList<>();
        ArrayList<ContaminationSock> sockList = new ArrayList<>();

        if (specialCells != null) {
            for(Cell c : specialCells){
                if(c instanceof DoorCell) doorList.add((DoorCell)c);
                else if(c instanceof ConveyorBelt) conveyorList.add((ConveyorBelt)c);
                else if(c instanceof ContaminationSock) sockList.add((ContaminationSock)c);
            }
        }

        // 2. Overwriting Doorcells
        int doorIdx = 0;
        for(int i = 1; i <= 99; i += 2) {
            if (doorIdx < doorList.size()) {
                setCell(i, doorList.get(doorIdx++));
            }
        }

        // 3. CardCell
        for(int i = 0; i < Constants.CARD_CELL_INDICES.length; i++){
            setCell(Constants.CARD_CELL_INDICES[i], new CardCell("Card Cell"));
        }

        // 4. ConveyorBelt
        for(int i = 0; i < Constants.CONVEYOR_CELL_INDICES.length && i < conveyorList.size(); i++){
            setCell(Constants.CONVEYOR_CELL_INDICES[i], conveyorList.get(i));
        }

        // 5. ContaminationSock
        for(int i = 0; i < Constants.SOCK_CELL_INDICES.length && i < sockList.size(); i++){
            setCell(Constants.SOCK_CELL_INDICES[i], sockList.get(i));
        }

        // 6. MonsterCell
        if (stationedMonsters != null) {
            for(int i = 0; i < Constants.MONSTER_CELL_INDICES.length && i < stationedMonsters.size(); i++){
                Monster m = stationedMonsters.get(i);
                if (m != null) {
                    m.setPosition(Constants.MONSTER_CELL_INDICES[i]);
                    setCell(Constants.MONSTER_CELL_INDICES[i], new MonsterCell(m.getName(), m));
                }
            }
        }
    }

    private void updateMonsterPositions(Monster player, Monster opponent) {
        for (int i = 0; i < Constants.BOARD_SIZE; i++) {
            Cell currentCell = this.getCell(i);

            if (!(currentCell instanceof MonsterCell)) {
                currentCell.setMonster(null);
            }
        }
        
        this.getCell(player.getPosition()).setMonster(player);
        this.getCell(opponent.getPosition()).setMonster(opponent);
    }

}