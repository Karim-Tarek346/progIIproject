package game.engine;

import java.security.Key;
import java.security.PrivateKey;

public final class Constants {
    public int BOARD_SIZE = 100;
    public int BOARD_ROWS = 10;
    public int BOARD_COLS = 10;
    public int WINNING_POSITION = 99;
    public int STARTING_POSITION = 0;
    public int[] MONTER_CELL_INDICES = {2, 18, 34, 54, 82, 88};
    public int[] CONVEYOR_CELL_INDICES = {6, 22, 44, 52, 66};
    public int[] SOCK_CELL_INDICES = {32, 42, 74, 84, 98};
    public int[] CARD_CELL_INDICES = {4, 12, 28, 36, 48, 56, 60, 76, 86, 90};
    public int WINNING_ENERGY = 1000;
    public int MIN_ENERGY = 0;
    public int MULTITASKER_BONUS = 200;
    public int SCHEMER_STEAL = 10;
    public int SLIP_PENALTY = 100;
    public int POWERUP_COST = 500;

}
