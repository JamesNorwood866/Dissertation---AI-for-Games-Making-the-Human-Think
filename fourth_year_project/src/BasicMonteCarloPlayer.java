import java.util.ArrayList;
import java.util.Random;

public class BasicMonteCarloPlayer extends Player{

    private char color;

    public BasicMonteCarloPlayer(char color) {
        this.color = color;
    }

    public void playMove(Game game){
        String currentState = game.getGameState();

        ArrayList<Integer> possibleMoves = new ArrayList<>(game.findPossibleMoves(color));

        ArrayList<String> nextStates = new ArrayList<>();

        for (Integer p : possibleMoves){
            game.setGameState(new ArrayList<>(game.flipTiles(p, color)), color);
            nextStates.add(game.getGameState());
            game.resetGameState(currentState);
        }

        ArrayList<Integer> moveScores = new ArrayList<>();

        for (String s: nextStates){
            int score = 0;
            for (int i = 0; i < 500; i++){
                game.resetGameState(s);
                score = score + rollout(game, s, this.color);
            }
            moveScores.add(score);
        }
        game.resetGameState(currentState);

        double bestScore = moveScores.get(0);
        int bestMove = possibleMoves.get(0);

        for (int i = 1; i < moveScores.size(); i++){
            if (moveScores.get(i) > bestScore){
                bestScore = moveScores.get(i);
                bestMove = possibleMoves.get(i);
            }
        }

        ArrayList<Integer> tilesToFlip = new ArrayList<>(game.flipTiles(bestMove, color));

        if (tilesToFlip.size() > 0) {
            game.setGameState(tilesToFlip, color);
        }

    }


    public char getColor(){
        return this.color;
    }

    private int rollout(Game game, String gameState, char color){

        while((game.findPossibleMoves('X').size() + game.findPossibleMoves('O').size()) != 0 ){
            if(game.findPossibleMoves(color).size() > 0) {
                ArrayList<Integer> possibleMoves = new ArrayList<>(game.findPossibleMoves(color));

                Random position = new Random();
                int move = possibleMoves.get(position.nextInt(possibleMoves.size()));

                ArrayList<Integer> tilesToFlip = new ArrayList<>(game.flipTiles(move, color));

                if (tilesToFlip.size() > 0) {
                    game.setGameState(tilesToFlip, color);
                }
            }

            if (color == 'O'){
                color = 'X';
            }else{
                color = 'O';
            }
        }


        String winner = game.findWinner();

        if(winner.equals("White Wins!")){
            if (this.color == 'O') {
                return 1;
            }else{
                return -1;
            }
        }else if (winner.equals("Black Wins!")){
            if (this.color == 'X') {
                return 1;
            }else{
                return -1;
            }
        }else{
            return 0;
        }


        /*
        if (this.color == 'X'){
            return game.get_weighted_score('X', "Killer");
        } else{
            return game.get_weighted_score('O', "Killer");
        }

         */

    }
}
