package game.engine;
import game.engine.monsters.Monster;
import game.engine.dataloader.DataLoader;

import java.io.IOException;
import java.util.ArrayList;

public class Game {
    private Board board; //Getter only
    private ArrayList<Monster> allMonsters ; //Getter only
    private Monster player; //Getter
    private Monster opponent; //Getter
    private Monster current; // Getter and setter

    public Game(Role playerRole) throws IOException {
        this.board = new Board(DataLoader.readCards());
        this.allMonsters = DataLoader.readMonsters();
        this.player = selectRandomMonsterByRole(playerRole);

        if(playerRole == Role.SCARER)
            this.opponent = selectRandomMonsterByRole(Role.LAUGHER);
        else
            this.opponent = selectRandomMonsterByRole(Role.SCARER);

        this.current = player;

    }

    private Monster selectRandomMonsterByRole(Role role){

        ArrayList<Monster> filtered = new ArrayList<>();

        for(Monster m : allMonsters){
            if(m.getRole() == role){
                filtered.add(m);
            }
        }

        return filtered.get((int)(Math.random() * filtered.size()));
    }
}