package game.engine;

import java.security.Key;
import java.security.PrivateKey;

public final class Constants {
    private int BOARD_SIZE = 100;
    private int BOARD_ROWS = 10;
    private int BOARD_COLS = 10;
    private int WINNING_POSITION = 99;
    private int STARTING_POSITION = 0;
    private int[] MONTER_CELL_INDICES = {2, 18, 34, 54, 82, 88};
    private int[] CONVEYOR_CELL_INDICES = {6, 22, 44, 52, 66};
    private int[] SOCK_CELL_INDICES = {32, 42, 74, 84, 98};
    private int[] CARD_CELL_INDICES = {4, 12, 28, 36, 48, 56, 60, 76, 86, 90};
    int WINNING_ENERGY = 1000;
    int MIN_ENERGY = 0;
    int MULTITASKER_BONUS = 200;
    int SCHEMER_STEAL = 10;
    int SLIP_PENALTY = 100;
    int POWERUP_COST = 500;

}
