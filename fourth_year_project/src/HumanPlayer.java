import java.util.ArrayList;

public class HumanPlayer extends Player {

    char color;

    public HumanPlayer(char color){
        super();
        this.color = color;
    }

    public void playMove(Game game, int position){
        //(position + 1) is used here as the game keeps track of buttons from 1 instead of 0
        ArrayList<Integer> tilesToFlip = new ArrayList<>(game.flipTiles(position + 1, color));
        if (tilesToFlip.size() > 1) {
            game.setGameState(tilesToFlip, color);
        }

    }

    @Override
    public char getColor() {
        return color;
    }
}

