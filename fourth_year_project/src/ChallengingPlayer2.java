import java.util.ArrayList;
import java.util.Collections;

public class ChallengingPlayer2 extends Player{

    private char color;
    private char opponentsColor;

    @Override
    public char getColor() {return color;}

    private ArrayList<Double> whitesMoves = new ArrayList<>();     //Used to rank all of white's moves
    private ArrayList<String> statesAfterMoves = new ArrayList<>(); //Used to determine what move white played

    private ArrayList<Double> allMoveScores = new ArrayList<>(); //Used to save all of white's move. To monitor how well they are playing
    private int numberOfMoves;
    private double lastAverage;

    public ChallengingPlayer2(char color){
        super();
        this.color = color;
        if(color == 'O'){
            opponentsColor = 'X';
        }else{
            opponentsColor = 'O';
        }
        while(whitesMoves.size() < 4){
            whitesMoves.add(100.0);
        }
        statesAfterMoves.add("---------------------------OOO-----XO---------------------------");
        statesAfterMoves.add("---------------------------OX-----OOO---------------------------");
        statesAfterMoves.add("---------------------------OX------OO------O--------------------");
        statesAfterMoves.add("--------------------O------OO------XO---------------------------");

        numberOfMoves = 0;
        lastAverage = 0;
    }

    public void rankOpponentsMove(Game game){
        ArrayList<Integer> possibleMoves = game.findPossibleMoves(opponentsColor);

        if (possibleMoves.size() > 0) {
            whitesMoves.clear();
            statesAfterMoves.clear();

            String currentState = game.getGameState();


            //Add all next states to nextStates array
            //But keep currentState the same
            for (Integer p : possibleMoves) {
                game.setGameState(new ArrayList<>(game.flipTiles(p, opponentsColor)), opponentsColor);
                statesAfterMoves.add(game.getGameState());
                game.resetGameState(currentState);
            }

            for (String s : statesAfterMoves) {
                whitesMoves.add(min(-100, 100, game, color, s, 1));
            }
            game.resetGameState(currentState);
        }
    }

    public void playMove(Game game){
        ArrayList<Integer> possibleMoves = game.findPossibleMoves(color);

        if(possibleMoves.size() == 1){
            game.setGameState(new ArrayList<>(game.flipTiles(possibleMoves.get(0), color)), color);
        }else {

            String gameState = game.getGameState();


            double whitesMoveScore = 0;
            for (int i = 0; i < statesAfterMoves.size(); i++) {
                if (statesAfterMoves.get(i).equals(gameState)) {
                    whitesMoveScore = whitesMoves.get(i);
                }
            }
            int whitesMoveRating = 0;
            for (double w : whitesMoves) {
                if (w < whitesMoveScore) {
                    whitesMoveRating++;
                }
            }

            allMoveScores.add((double)whitesMoveRating/(double)whitesMoves.size());
            numberOfMoves++;

            ArrayList<String> nextStates = new ArrayList<>();

            //Add all next states to nextStates array
            //But keep currentState the same
            for (Integer p : possibleMoves) {
                game.setGameState(new ArrayList<>(game.flipTiles(p, color)), color);
                nextStates.add(game.getGameState());
                game.resetGameState(gameState);
            }

            ArrayList<Double> scores = new ArrayList<>();
            for (String s : nextStates) {
                scores.add(min(-100, 100, game, 'O', s, 1));
            }

            ArrayList<Double> scoresOrdered = new ArrayList<Double>(scores);
            Collections.sort(scoresOrdered);

            double relativeValue = 0;
            if(numberOfMoves < 3){
                for (int i = 0; i < numberOfMoves; i++){
                    relativeValue = relativeValue + allMoveScores.get(i);
                }
                relativeValue = relativeValue/(double)numberOfMoves;
            }else{
                for (int i = numberOfMoves-3; i < numberOfMoves; i++){
                    relativeValue = relativeValue + allMoveScores.get(i);
                }
                relativeValue = relativeValue/(double)3;
            }

            Double relativeScore;
            lastAverage = relativeValue;

            if(relativeValue > lastAverage){
                if((int)( relativeValue*(double)scoresOrdered.size() + 0.5) == scoresOrdered.size()) {
                    relativeScore = scoresOrdered.get(((int) (relativeValue * (double) scoresOrdered.size() + 0.5))-1);
                } else{
                    relativeScore = scoresOrdered.get(((int) (relativeValue * (double) scoresOrdered.size() + 1.5))-1);
                }
            }else{
                if((int)( relativeValue*(double)scoresOrdered.size()) == 0){
                    relativeScore = scoresOrdered.get((int)( relativeValue*(double)scoresOrdered.size()));
                } else{
                    relativeScore = scoresOrdered.get((int)( relativeValue*(double)scoresOrdered.size()) - 1);
                }
            }

            int move = 0;
            for (int i = 0; i < scores.size(); i++) {
                if (scores.get(i) == relativeScore) {
                    move = i;
                    break;
                }
            }


            game.resetGameState(gameState);
            ArrayList<Integer> tilesToFlip = new ArrayList<>(game.flipTiles(possibleMoves.get(move), color));

            if (tilesToFlip.size() > 0) {
                game.setGameState(tilesToFlip, color);
            }
        }


    }

    private double max(double alpha, double beta, Game game, char color, String currentState, int depth){
        game.resetGameState(currentState);
        double bestMove = 0;

        //Add check to see if black can play 2 moves in a row

        //If there is no more moves return the number of black tiles
        if (depth == 6 || (game.findPossibleMoves(color).size() == 0)){
            return game.get_weighted_score(color, "Killer");
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
            }
            if(vprime >= beta){
                return v;
            }
            if (vprime > alpha){
                alpha = vprime;
            }
        }

        return v;
    }

    private double min(double alpha, double beta, Game game, char color, String currentState, int depth){

        game.resetGameState(currentState);

        //If there is no more moves return the number of black tiles
        if (depth == 6 || (game.findPossibleMoves(color).size() == 0)){
            //Add check to see if black can play 2 moves in a row
            return game.get_weighted_score(color, "Killer");
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

}
