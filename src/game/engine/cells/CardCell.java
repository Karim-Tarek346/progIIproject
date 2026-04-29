package game.engine.cells;
import game.engine.Board;
import game.engine.cards.Card;

public class CardCell extends Cell{

    public CardCell(String name){
        super(name);
        Card card = Board.drawCard();

    }

}
