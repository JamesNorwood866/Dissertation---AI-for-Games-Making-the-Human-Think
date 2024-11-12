import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Game {

    private String gameState;
    public Game() {
        gameState = "---------------------------XO------OX---------------------------";
    }



    public void setGameState(ArrayList<Integer> tilesToFlip, char color){

        for (Integer flippedTile : tilesToFlip) {
            gameState = gameState.substring(0, flippedTile-1) + color + gameState.substring(flippedTile);
        }

    }

    public String getGameState(){
        return gameState;
    }

    public void resetGameState(String gameState){this.gameState = gameState;}

    private char getColor(int position){
        return gameState.charAt(position - 1);
    }

    public ArrayList<Integer> flipTiles(int position, char color) {

        ArrayList<Integer> flippedTiles = new ArrayList<>();

        if (getColor(position) == '-') {

                //The exclusions ArrayList is used to avoid out of bounds errors
                //If the position of the tile is at an edge, the algorithm won't try to look beyond the edge
                ArrayList<Integer> exclusions = new ArrayList<>();
                if (position < 9)            {  exclusions.addAll(new ArrayList<>(Arrays.asList(-7,-8,-9)));}   // Top row
                if ((position - 1) % 8 == 0) {  exclusions.addAll(new ArrayList<>(Arrays.asList( 7, -1,-9)));}  // Left Column
                if (position % 8 == 0)       {  exclusions.addAll(new ArrayList<>(Arrays.asList(-7, 1, 9)));}   // Right Column
                if (position > 56)           {  exclusions.addAll(new ArrayList<>(Arrays.asList( 7, 8, 9)));}   // Bottom row

                flippedTiles.add(position);

                for (int i = 7; i < 10; i++) {
                    if (!exclusions.contains(i)) {
                        flippedTiles.addAll(findFlippedTiles(position, i, color, new ArrayList<>()));
                    }
                    if (!exclusions.contains(-i)) {
                        flippedTiles.addAll(findFlippedTiles(position, -i, color, new ArrayList<>()));
                    }
                }
                if (!exclusions.contains(1)) {
                    flippedTiles.addAll(findFlippedTiles(position, 1, color, new ArrayList<>()));
                }
                if (!exclusions.contains(-1)) {
                    flippedTiles.addAll(findFlippedTiles(position, -1, color, new ArrayList<>()));
                }
            }

        return flippedTiles;

    }

    public ArrayList<Integer> findFlippedTiles(int current, int direction, char color, ArrayList<Integer> tiles) {

        //If at the edge of board, return empty array
        if ((current < 9) && (direction == -7 || direction == -8 || direction == -9))           {return new ArrayList<>();}
        if (((current - 1) % 8 == 0) && (direction == -9 || direction == -1 || direction == 7)) {return new ArrayList<>();}
        if ((current % 8 == 0) && (direction == -7 || direction == 1 || direction == 9))        {return new ArrayList<>();}
        if ((current > 56) && (direction == 7 || direction == 8 || direction == 9))             {return new ArrayList<>();}

        if (getColor(current + direction) == '-') {    //If this direction ends on an empty tile
            //Return an empty array as no tiles need to be flipped
            return new ArrayList<>();

        } else if (getColor(current + direction) != color) {    //If the next tile is not the same as the players
            ArrayList<Integer> newTile = new ArrayList<>();
            newTile.add(current + direction);
            ArrayList<Integer> flippedTiles = findFlippedTiles(current + direction, direction, color, newTile);
            if (flippedTiles.size() == 0) {
                //if finFlippedTiles returns 0 tiles, no new tiles need flipped, an empty array is returned
                return new ArrayList<>();
            }
            tiles.addAll(flippedTiles);
            return tiles;

        } else{     //If the next tile is the same as the players
            //Return the input tiles. No new tiles must be flipped
            return tiles;
        }

    }

    public ArrayList<Integer> findPossibleMoves(char color){
        ArrayList<Integer> possibleMoves = new ArrayList<>();
        for(int i = 1; i < 65; i++){
            if (flipTiles(i, color).size() > 1){
                possibleMoves.add(i);
            }
        }
        return possibleMoves;
    }


    public String findWinner(){
        int white = 0;
        int black = 0;
        for(int i = 0; i < gameState.length(); i++){
            if (gameState.charAt(i) == 'X'){
                white++;
            } else if (gameState.charAt(i) == 'O'){
                black++;
            }
        }
        if (white > black){
            return "White Wins!";
        } else if (black > white) {
            return "Black Wins!";
        }
        return "It's a Draw";
    }

    public double countTiles(char color){
        double numberOfTiles = 0;
        for (int i = 0; i < gameState.length(); i++){
            if (gameState.charAt(i) == color){
                numberOfTiles++;
            }
        }
        return numberOfTiles;
    }

    public double countTotalTiles(){
        double numberOfTiles = 0;
        for (int i = 0; i < gameState.length(); i++){
            if (gameState.charAt(i) != '-'){
                numberOfTiles++;
            }
        }
        return numberOfTiles;
    }

    public double findNumberOfMovesForScore(){
        double possibleMoves = 0;
        for (int i = 1; i <= gameState.length(); i++){
            if (flipTiles(i, 'O').size() > 1){
                possibleMoves++;
            }
            if (flipTiles(i, 'X').size() > 1){
                possibleMoves++;
            }
        }
        if(possibleMoves == 0){
            return 1;
        }
        return possibleMoves;
    }

    public double getCorenerDifference(char color){
        double colorCorners = 0;
        double oppositeColorCorners = 0;

        if (gameState.charAt(0) == color){
            colorCorners++;
        }else if (gameState.charAt(0) != '-'){
            oppositeColorCorners++;
        }
        if (gameState.charAt(7) == color){
            colorCorners++;
        }else if (gameState.charAt(7) != '-'){
            oppositeColorCorners++;
        }
        if (gameState.charAt(55) == color){
            colorCorners++;
        }else if (gameState.charAt(55) != '-'){
            oppositeColorCorners++;
        }
        if (gameState.charAt(63) == color){
            colorCorners++;
        }else if (gameState.charAt(63) != '-'){
            oppositeColorCorners++;
        }

        if (colorCorners == 0){
            return 0;
        }
        return (colorCorners / (colorCorners + oppositeColorCorners)) * 100;

    }

    public double stableTokens(char color){
        double numberOfStableTokens = 0;

        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++) {
                int directions = 0;
                if(getColor((i*8+j)+1) == color) {
                    for (int k = 7; k < 10; k++) {
                        if (findFlippedTiles((i * 8 + j) + 1, -k, color, new ArrayList<>()).size() == i || findFlippedTiles((i * 8 + j) + 1, -k, color, new ArrayList<>()).size() == j) {
                            directions++;
                        } else if (findFlippedTiles((i * 8 + j) + 1, k, color, new ArrayList<>()).size() == i || findFlippedTiles((i * 8 + j) + 1, k, color, new ArrayList<>()).size() == j) {
                            directions++;
                        }
                    }

                    if (findFlippedTiles((i * 8 + j) + 1, -1, color, new ArrayList<>()).size() == i || findFlippedTiles((i * 8 + j) + 1, -1, color, new ArrayList<>()).size() == j) {
                        directions++;
                    } else if (findFlippedTiles((i * 8 + j) + 1, 1, color, new ArrayList<>()).size() == i || findFlippedTiles((i * 8 + j) + 1, 1, color, new ArrayList<>()).size() == j) {
                        directions++;
                    }

                    if (directions == 4) {
                        numberOfStableTokens++;
                    }
                }
            }
        }

        return numberOfStableTokens;
    }

    public double get_one_weighted_score(char color, String type){
        double score;

        double coinDiff = (countTiles(color)/countTotalTiles()) * 100;
        double choiceDiff = ((double) findPossibleMoves(color).size()/findNumberOfMovesForScore()) * 100;
        double cornerDiff = getCorenerDifference(color);
        double stability = stableTokens(color)/countTiles(color);

        if (type.equals("Killer")) {
            score = coinDiff * 0.15 + choiceDiff * 0.15 + cornerDiff * 0.4 + stability * 0.3;
        }else {
            score = coinDiff * 0.4 + choiceDiff * 0.15 + cornerDiff * 0.3 + stability * 0.15;
        }

        return score;
    }

    public double get_weighted_score(char color, String type){
        double first = get_one_weighted_score(color, type);
        if (color == 'O'){
            color = 'X';
        }else{
            color = 'O';
        }
        double second = get_one_weighted_score(color, type);

        return (first + (100 - second)) / 2;
    }

}
