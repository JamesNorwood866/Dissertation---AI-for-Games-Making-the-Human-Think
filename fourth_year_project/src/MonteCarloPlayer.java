import java.util.ArrayList;
import java.lang.Math;
import java.util.Random;

public class MonteCarloPlayer extends Player{

    private char color;

    public MonteCarloPlayer(char color) {
        this.color = color;
    }

    public char getColor(){
        return this.color;
    }

    private final ArrayList<int[]> nodes = new ArrayList<>();
    private final ArrayList<String> states = new ArrayList<>();
    private int bestMove = 0;

    public void playMove(Game game){

        if(game.findPossibleMoves(color).size() == 1){
            nodes.clear();
            states.clear();
            game.setGameState(game.flipTiles(game.findPossibleMoves(color).get(0), color), color);
        }else {
            int startingColor = 0;
            if (color == 'X') {
                startingColor = 1;
            }
            String startState = game.getGameState();
            //Node  = {current, parent, t, n, turn(0 = W, 1 = B)}
            int[] startingNode = {0, -1, 0, 0, startingColor};
            int startingIndex = startingNode[0];

            boolean found = false;

            if (nodes.size() == 0) {
                // Node = {current, parent, t, n, turn(0 = W, 1 = B}
                nodes.add(startingNode);
                states.add(game.getGameState());
                states.addAll(getNextStates(game, startingNode));
            } else {
                for (int i = 0; i < states.size(); i++) {
                    if (startState.equals(states.get(i)) && bestMove == nodes.get(i)[1]) {
                        found = true;
                        startingIndex = nodes.get(i)[0];
                        break;
                    }
                }
                if ((!found && nodes.size() > 5)) {
                    nodes.clear();
                    states.clear();

                    nodes.add(startingNode);

                    states.add(game.getGameState());
                    states.addAll(getNextStates(game, startingNode));
                }

            }


            for (int i = 0; i < 500; i++) {

                game.resetGameState(startState);
                int[] current = nodes.get(startingIndex);

                //is current a leaf node?
                while (!isLeafNode(current)) {
                    int currentIndex = current[0];
                    if (current[4] == 0) {
                        double bestUCB1 = -100000;
                        for (int[] n : nodes) {
                            if (n[1] == current[0]) {
                                if (getUCB1(n) > bestUCB1) {
                                    bestUCB1 = getUCB1(n);
                                    currentIndex = n[0];
                                }
                            }
                        }
                    } else {
                        double bestUCB1 = 100000;
                        for (int[] n : nodes) {
                            if (n[1] == current[0]) {
                                if (getUCB1(n) < bestUCB1) {
                                    bestUCB1 = getUCB1(n);
                                    currentIndex = n[0];
                                }
                            }
                        }
                    }
                    for (int[] n : nodes) {
                        if (n[0] == currentIndex) {
                            current = n;
                            break;
                        }
                    }
                }

                //update game state to current
                game.resetGameState(states.get(current[0]));

                //is n for current 0
                if (current[3] == 0) {

                    //rollout
                    nodes.set(current[0], new int[]{current[0], current[1], rollout(game, current[4]), 1, current[4]});

                    //back propagate
                    if (current[0] != startingIndex) {          // If current node is not the starting node
                        int[] parent = nodes.get(current[1]);   // Parent = parent node of current node
                        while (parent[0] != startingIndex) {    // Loop until starting node is reached
                            nodes.set(parent[0], new int[]{parent[0], parent[1], (parent[2] + current[2]), parent[3] + 1, parent[4]});
                            parent = nodes.get(parent[1]);
                        }
                        nodes.set(parent[0], new int[]{parent[0], parent[1], (parent[2] + current[2]), parent[3] + 1, parent[4]});
                    }


                } else {
                    int nodeSize = nodes.size();
                    states.addAll(getNextStates(game, current));
                    if (nodeSize != nodes.size()) {

                        current = nodes.get(nodeSize);

                        nodes.set(nodeSize, new int[]{current[0], current[1], rollout(game, current[4]), 1, current[4]});

                        //back propagate
                        if (current[0] != startingIndex) {
                            int[] parent = nodes.get(current[1]);
                            while (parent[0] != startingIndex) {
                                nodes.set(parent[0], new int[]{parent[0], parent[1], (parent[2] + current[2]), parent[3] + 1, parent[4]});
                                parent = nodes.get(parent[1]);
                            }
                            nodes.set(parent[0], new int[]{parent[0], parent[1], (parent[2] + current[2]), parent[3] + 1, parent[4]});

                        }
                    } else {
                        nodes.set(current[0], new int[]{current[0], current[1], rollout(game, current[4]), 1, current[4]});

                        //back propagate
                        if (current[0] != startingIndex) {
                            int[] parent = nodes.get(current[1]);
                            while (parent[0] != startingIndex) {
                                nodes.set(parent[0], new int[]{parent[0], parent[1], (parent[2] + current[2]), parent[3] + 1, parent[4]});
                                parent = nodes.get(parent[1]);
                            }
                            nodes.set(parent[0], new int[]{parent[0], parent[1], (parent[2] + current[2]), parent[3] + 1, parent[4]});

                        }
                    }
                }
            }


            double bestV = -100000;
            for (int[] n : nodes) {
                if (n[1] == startingIndex) {
                    //System.out.println("ucb: " + getUCB1(n));
                    if (n[2] > bestV) {
                        //System.out.println("best: " + getUCB1(n));
                        bestV = n[2];
                        bestMove = n[0];
                    }
                }
            }

            game.resetGameState(states.get(bestMove));
        }
    }


    private Boolean isLeafNode(int[] current){
        for (int[] n: nodes){
            if (current[0] == n[1]){
                return false;
            }
        }
        return true;
    }

    private double getUCB1(int[] current){
        if(current[3] == 0) //If it is the first time the node is visited
            if (current[4] == 0) {  //If it is blacks turn
                return -100000;
            }else {                 //If it is whites turn
                return 100000;
            }
        int i = current[2] / current[3];    //Average Value of the node
        if(current[4] == 1){
            return i - 2 * (Math.sqrt(Math.log(getN(current))/current[3]));
        } else{
            return i + 2 * (Math.sqrt(Math.log(getN(current))/current[3]));
        }
    }

    private int getN(int[] current){
        for (int[] n: nodes){
            if(current[1] == n[0]){
                return n[3];
            }
        }
        return 0;
    }

    private ArrayList<String> getNextStates(Game game, int[] current){

        String currentState = game.getGameState();

        int turn = 0;
        char color = 'X';
        if (nodes.get(current[0])[4] == 0){
            turn = 1;
            color = 'O';
        }

        ArrayList<Integer> possibleMoves = new ArrayList<>(game.findPossibleMoves(color));
        ArrayList<String> nextStates = new ArrayList<>();

        for (Integer p : possibleMoves){
            game.setGameState(new ArrayList<>(game.flipTiles(p, color)), color);
            nextStates.add(game.getGameState());
            game.resetGameState(currentState);
            nodes.add(new int[]{nodes.size(), current[0], 0, 0, turn});
        }

        return nextStates;
    }

    private int rollout(Game game, int turn){

        char color;
        char oppositeColor = 'O';

        if (turn == 1){
            color = 'O';
        }else{
            color = 'X';
        }

        if(this.color == 'X'){
            oppositeColor = 'O';
        }

        String startingState = game.getGameState();
        int totalScore = 0;

        for(int i = 0; i < 10; i++) {
            game.resetGameState(startingState);
            while ((game.findPossibleMoves('X').size() + game.findPossibleMoves('O').size()) != 0) {
                if (game.findPossibleMoves(color).size() > 0) {
                    ArrayList<Integer> possibleMoves = new ArrayList<>(game.findPossibleMoves(color));

                    Random position = new Random();
                    int move = possibleMoves.get(position.nextInt(possibleMoves.size()));

                    ArrayList<Integer> tilesToFlip = new ArrayList<>(game.flipTiles(move, color));

                    if (tilesToFlip.size() > 0) {
                        game.setGameState(tilesToFlip, color);
                    }
                }

                if (color == 'O') {
                    color = 'X';
                } else {
                    color = 'O';
                }
            }
            totalScore = totalScore + findWinner(game);
        }



        return totalScore;

    }

    private int findWinner(Game game){
        String winner = game.findWinner();

        if (winner.equals("White Wins!")) {
            if (this.color == 'O') {
                return 1;
            } else {
                return -1;
            }
        } else if (winner.equals("Black Wins!")) {
            if (this.color == 'X') {
                return 1;
            } else {
                return -1;
            }
        } else
            return 0;
    }
}
