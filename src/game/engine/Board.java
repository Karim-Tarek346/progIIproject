package game.engine;
import game.engine.cells.Cell;
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

    public void initializeBoard(ArrayList<Cell> specialCells){

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

}