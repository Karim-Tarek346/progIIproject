package game.engine.exceptions;

import java.io.IOException;

public class InvalidCSVFormat extends IOException{
    private static final String MSG = "Invalid input detected while reading csv file, input = \n";
    private String inputLine; //read and write

    public InvalidCSVFormat(String inputLine){
        super(inputLine);
        this.inputLine = inputLine;
    }

    public String getInputLine(){
        return this.inputLine;
    }

    public void setInputLine(String inputLine){
        this.inputLine = inputLine;
    }

    @Override
    public String getMessage(){
        return MSG + inputLine;
    }

}
