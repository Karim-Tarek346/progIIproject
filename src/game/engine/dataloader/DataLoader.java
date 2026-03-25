package game.engine.dataloader;

import game.engine.Role;
import game.engine.cards.*;
import game.engine.cells.*;
import game.engine.exceptions.InvalidCSVFormat;
import game.engine.monsters.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DataLoader {

    public static final String CARDS_FILE_NAME   = "cards.csv";
    public static final String CELLS_FILE_NAME   = "cells.csv";
    public static final String MONSTERS_FILE_NAME = "monsters.csv";

    public static ArrayList<Card> readCards() throws IOException {
        ArrayList<Card> cards = new ArrayList<>();
        //tries the conditions before running them to avoid run-time errors
        try (BufferedReader br = new BufferedReader(new FileReader(CARDS_FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) { //as long as the line is not empty
                line = line.trim(); //trim() removes empty spaces around the whole line
                if (line.isEmpty()) continue; //skips this loop if line is empty

                String[] parts = line.split(","); //splits the line into an array of strings the moment a ',' is detected

                // Trim every token
                for (int i = 0; i < parts.length; i++) parts[i] = parts[i].trim();  //removes the empty spaces around a single string element

                String cardType = parts[0];
                String name = parts[1];
                String description = parts[2];
                int rarity = Integer.parseInt(parts[3]);

                switch (cardType) {
                    case "SWAPPER":
                        // (cardType, name, description, rarity)
                        if (parts.length != 4) throw new InvalidCSVFormat(line);
                        cards.add(new SwapperCard(name, description, rarity));
                        break;

                    case "SHIELD":
                        // (cardType, name, description, rarity)
                        if (parts.length != 4) throw new InvalidCSVFormat(line);
                        cards.add(new ShieldCard(name, description, rarity));
                        break;

                    case "ENERGYSTEAL":
                        // (cardType, name, description, rarity, energy)
                        if (parts.length != 5) throw new InvalidCSVFormat(line);
                        int energy = Integer.parseInt(parts[4]);
                        cards.add(new EnergyStealCard(name, description, rarity, energy));
                        break;

                    case "STARTOVER":
                        // (cardType, name, description, rarity, lucky)
                        if (parts.length != 5) throw new InvalidCSVFormat(line);
                        boolean lucky = Boolean.parseBoolean(parts[4]);
                        cards.add(new StartOverCard(name, description, rarity, lucky));
                        break;

                    case "CONFUSION":
                        // (cardType, name, description, rarity, duration)
                        if (parts.length != 5) throw new InvalidCSVFormat(line);
                        int duration = Integer.parseInt(parts[4]);
                        cards.add(new ConfusionCard(name, description, rarity, duration));
                        break;

                    default:
                        throw new InvalidCSVFormat(line);
                }
            }
        }
        return cards;
    }

    public static ArrayList<Cell> readCells() throws IOException {
        ArrayList<Cell> cells = new ArrayList<>();
        //tries the conditions before running them to avoid run-time errors
        try (BufferedReader br = new BufferedReader(new FileReader(CELLS_FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) { //as long as the line is not empty
                line = line.trim(); //trim() removes empty spaces around the whole line
                if (line.isEmpty()) continue; //skips this loop if line is empty

                String[] parts = line.split(","); //splits the line into an array of strings the moment a ',' is detected
                for (int i = 0; i < parts.length; i++) parts[i] = parts[i].trim();  //removes the empty spaces around a single string element

                if (parts.length == 3) {
                    // DoorCell: (name, role, energy)
                    String name = parts[0];
                    Role role = Role.valueOf(parts[1]);
                    int energy = Integer.parseInt(parts[2]);
                    cells.add(new DoorCell(name, role, energy));

                }
                else if (parts.length == 2) {
                    // TransportCell: (name, effect)
                    String name = parts[0];
                    int effect = Integer.parseInt(parts[1]);

                    if (effect >= 0) cells.add(new ConveyorBelt(name, effect));
                    else cells.add(new ContaminationSock(name, effect));

                } else {
                    throw new InvalidCSVFormat(line);
                }
            }
        }
        return cells;
    }

    public static ArrayList<Monster> readMonsters() throws IOException {
        ArrayList<Monster> monsters = new ArrayList<>();
        //tries the conditions before running them to avoid run-time errors
        try (BufferedReader br = new BufferedReader(new FileReader(MONSTERS_FILE_NAME))) {
            String line; 
            while ((line = br.readLine()) != null) { //as long as the line is not empty
                line = line.trim(); //trim() removes empty spaces around the whole line
                if (line.isEmpty()) continue; //skips this loop if line is empty

                String[] parts = line.split(","); //splits the line into an array of strings the moment a ',' is detected
                for (int i = 0; i < parts.length; i++) parts[i] = parts[i].trim(); //removes the empty spaces around a single string element

                if (parts.length != 5) throw new InvalidCSVFormat(line); //number of lements must be 5 for monsters

                String monsterType = parts[0];
                String name = parts[1];
                String description = parts[2];
                Role role = Role.valueOf(parts[3]);
                int energy = Integer.parseInt(parts[4]);

                switch (monsterType) {
                    case "DASHER":
                        monsters.add(new Dasher(name, description, role, energy));
                        break;
                    case "DYNAMO":
                        monsters.add(new Dynamo(name, description, role, energy));
                        break;
                    case "MULTITASKER":
                        monsters.add(new MultiTasker(name, description, role, energy));
                        break;
                    case "SCHEMER":
                        monsters.add(new Schemer(name, description, role, energy));
                        break;
                    default:
                        throw new InvalidCSVFormat(line);
                }
            }
        }
        return monsters;
    }
}