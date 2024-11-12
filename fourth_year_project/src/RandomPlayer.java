import java.util.ArrayList;
import java.util.Random;


public class RandomPlayer extends Player{

    char color;

    public RandomPlayer(char color){
        this.color = color;
    }

    public void playMove(Game game){

        ArrayList<Integer> possibleMoves = new ArrayList<>(game.findPossibleMoves(color));

        Random position = new Random();
        int move = possibleMoves.get(position.nextInt(possibleMoves.size()));

        ArrayList<Integer> tilesToFlip = new ArrayList<>(game.flipTiles(move, color));

        if (tilesToFlip.size() > 0) {
            game.setGameState(tilesToFlip, color);
        }
    }

    public char getColor(){
        return this.color;
    }

}
