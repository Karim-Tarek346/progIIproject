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

        // creating the Board template
        for(int i = 0; i<=99; i++){
            if(i%2==0) setCell(i, new Cell("Normalcells"));
            // DoorCell takes name, role , energy
            else {
                if((i/2)%2==0)
                    setCell(i, new DoorCell("Doorcell",Role.SCARER,0));
                else setCell(i, new DoorCell("Doorcell",Role.LAUGHER,0));
            }
        }
        ArrayList<DoorCell> doorList = new ArrayList<>();
        ArrayList<ConveyorBelt> conveyorList = new ArrayList<>();
        ArrayList<ContaminationSock> sockList = new ArrayList<>();

        // extracts Doorcells, ContaminationSock, and ConveyorBelt separately
        for(Cell c: specialCells){
            if(c instanceof DoorCell) doorList.add((DoorCell)c);
            else if(c instanceof ConveyorBelt) conveyorList.add((ConveyorBelt)c);
            else if(c instanceof ContaminationSock) sockList.add((ContaminationSock)c);
        }

        //Overwriting Doorcells
        for(int i = 0; i< doorList.size(); i++){
            setCell((i*2) + 1, doorList.get(i));
        }
        //CardCell
        for(int i = 0; i<Constants.CARD_CELL_INDICES.length; i++){
            setCell(Constants.CARD_CELL_INDICES[i], new CardCell("Card Cell"));
        }
        //Overwriting ConveyorBelt
        for(int i = 0; i<Constants.CONVEYOR_CELL_INDICES.length && i<conveyorList.size(); i++){
            setCell(Constants.CONVEYOR_CELL_INDICES[i], conveyorList.get(i));
        }

        //ContaminationSock
        for(int i = 0; i < Constants.SOCK_CELL_INDICES.length && i<sockList.size(); i++){
            setCell(Constants.SOCK_CELL_INDICES[i], sockList.get(i));
        }


        //MonsterCell
        for(int i = 0; i<Constants.MONSTER_CELL_INDICES.length; i++){
            Monster m = stationedMonsters.get(i);
            m.setPosition(Constants.MONSTER_CELL_INDICES[i]);
            setCell(Constants.MONSTER_CELL_INDICES[i], new MonsterCell(m.getName(), m));
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