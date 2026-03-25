package game.engine;
import game.engine.monsters.Monster;
import game.engine.dataloader.DataLoader;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Game {
    private Board board; //Getter only
    private ArrayList<Monster> allMonsters ; //Getter only
    private Monster player; //Getter
    private Monster opponent; //Getter
    private Monster current; // Getter and setter


    private Monster selectRandomMonstersByRole(Role role){

        ArrayList<Monster> filtered = new ArrayList<>();

        for(Monster m : allMonsters){
            if(m.getRole() == role){
                filtered.add(m);
            }
        }

        return filtered.get((int)Math.random() * filtered.size());
    }


    public Game(Role playerRole) throws IOException {
        this.board = new Board(DataLoader.readCards());
        this.allMonsters = DataLoader.readMonsters();
        this.player = selectRandomMonstersByRole(playerRole);

        if(playerRole == Role.SCARER)
            this.opponent = selectRandomMonstersByRole(Role.LAUGHER);
        else
            this.opponent = selectRandomMonstersByRole(Role.SCARER);

        this.current = playe;

    }

}