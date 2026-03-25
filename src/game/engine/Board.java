package game.engine;
import game.engine.cells.Cell;
import game.engine.monsters.Monster;
import game.engine.cards.Card;
import game.engine.Constants;

import java.util.ArrayList;

import static game.engine.dataloader.DataLoader.readCards;

public class Board {
    private Cell[][] boardCells; // Getter only
    static ArrayList<Monster> stationedMonsters; //Getter and Setter
    static ArrayList<Card> originalCards; // Getter only
    static ArrayList<Card> cards; //Getter and Setter

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

    public Board(ArrayList<Card> readCards){
        stationedMonsters = new ArrayList<>();
        cards = new ArrayList<>();
        originalCards = readCards;
        this.boardCells = new Cell[Constants.BOARD_ROWS][Constants.BOARD_COLS];

    }


}