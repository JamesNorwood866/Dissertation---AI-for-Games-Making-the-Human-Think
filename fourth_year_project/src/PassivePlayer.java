import java.util.ArrayList;
import java.util.Collections;

public class PassivePlayer extends Player{

    private char color;

    public PassivePlayer(char color){
        this.color = color;
    }

    public void playMove(Game game){
        String gameState = game.getGameState();

        //Max is trying to get the most of x color tiles
        //Min is trying to get the least of x color tiles

        double alpha = -100;
        double beta = 100;


        double move = max(alpha, beta, game, color, gameState, 0);
        game.resetGameState(gameState);

        ArrayList<Integer> possibleMoves = new ArrayList<>(game.findPossibleMoves(color));

        ArrayList<Integer> tilesToFlip = new ArrayList<>(game.flipTiles(possibleMoves.get((int) move), color));

        if (tilesToFlip.size() > 0) {
            game.setGameState(tilesToFlip, color);
        }

    }

    private double max(double alpha, double beta, Game game, char color, String currentState, int depth){
        game.resetGameState(currentState);
        double bestMove = 0;

        //Add check to see if black can play 2 moves in a row

        //If there is no more moves return the number of black tiles
        if (depth == 6 || (game.findPossibleMoves(color).size() == 0)){
            return 50 - Math.abs(50 - game.get_weighted_score(this.color, "Passive"));
        }

        double v = alpha;

        ArrayList<Integer> possibleMoves = game.findPossibleMoves(color);
        ArrayList<String> nextStates = new ArrayList<>();

        //Add all next states to nextStates array
        //But keep currentState the same
        for (Integer p: possibleMoves){
            game.setGameState(new ArrayList<>(game.flipTiles(p, color)), color);
            nextStates.add(game.getGameState());
            game.resetGameState(currentState);
        }

        if (color == 'X'){
            color = 'O';
        } else{
            color = 'X';
        } //Change color

        for (int n = 0;  n < nextStates.size(); n++){
            double vprime = min(alpha, beta, game, color, nextStates.get(n), depth+1);
            if (vprime > v){
                v = vprime;
                bestMove = n;
            }
            if(vprime >= beta){
                if (depth == 0){
                    return bestMove;
                }
                return v;
            }
            if (vprime > alpha){
                alpha = vprime;
            }
        }
        if (depth == 0){
            return bestMove;
        }
        return v;
    }

    private double min(double alpha, double beta, Game game, char color, String currentState, int depth){

        game.resetGameState(currentState);

        //If there is no more moves return the number of black tiles
        if (depth == 6 || (game.findPossibleMoves(color).size() == 0)){
            //Add check to see if black can play 2 moves in a row

            return 100 - Math.abs(50 - game.get_weighted_score(this.color, "Passive"));
        }

        double v = beta;

        ArrayList<Integer> possibleMoves = game.findPossibleMoves(color);
        ArrayList<String> nextStates = new ArrayList<>();


        //Add all next states to nextStates array
        //But keep currentState the same
        for (Integer p: possibleMoves){
            game.setGameState(new ArrayList<>(game.flipTiles(p, color)), color);
            nextStates.add(game.getGameState());
            game.resetGameState(currentState);
        }

        if (color == 'X'){
            color = 'O';
        } else{
            color = 'X';
        }

        for (int n = 0;  n < nextStates.size(); n++){
            double vprime = max(alpha, beta, game, color, nextStates.get(n), depth+1);
            if (vprime < v){
                v = vprime;
            }
            if(vprime <= alpha){
                return v;
            }
            if (vprime < beta){
                beta = vprime;
            }
        }

        return v;
    }

    public char getColor(){
        return this.color;
    }

}
