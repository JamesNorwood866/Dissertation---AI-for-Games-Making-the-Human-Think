import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class SimulationGUI {



    JFrame frame;
    JPanel groupedPanel;
    JPanel board;
    JPanel gameInfo;
    JLabel playersTurn;
    JLabel whiteTiles;
    JLabel blackTiles;
    JButton[] buttons = new JButton[64];


    public SimulationGUI(Game game, Player player1, Player player2){



        frame = new JFrame();
        groupedPanel = new JPanel();
        board = new JPanel();
        gameInfo = new JPanel();
        playersTurn = new JLabel("Blacks's Turn");
        whiteTiles = new JLabel("White: 2");
        blackTiles = new JLabel("Black: 2");

        groupedPanel.setPreferredSize(new Dimension(600, 700));

        board.setLayout(new GridLayout(8, 8));
        board.setPreferredSize(new Dimension(600,600));

        gameInfo.setPreferredSize(new Dimension(600,100));
        gameInfo.add(playersTurn);


        gameInfo.add(whiteTiles);
        gameInfo.add(blackTiles);

        groupedPanel.add(board);
        groupedPanel.add(gameInfo);

        frame.add(groupedPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);

        Color background = new Color(0, 153, 51);
        final char[] color = {'O'};


        ArrayList<Integer> possibleMoves = new ArrayList<>(game.findPossibleMoves(color[0]));

        for(int i = 0; i < 64; i++){

            if(possibleMoves.contains(i+1)){
                buttons[i] = new JButton(new ImageIcon("images/PossibleMove.PNG"));
            }else if(game.getGameState().charAt(i) == '-'){
                buttons[i] = new JButton(new ImageIcon("images/NoTile.PNG"));
            }else if(game.getGameState().charAt(i) == 'O') {
                buttons[i] = new JButton(new ImageIcon("images/BlackTile.PNG"));
            }else {
                buttons[i] = new JButton(new ImageIcon("images/WhiteTile.PNG"));
            }

            buttons[i].setBorder(BorderFactory.createLineBorder(new Color(0, 51, 0)));
            board.add(buttons[i]);
        }


        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Othello");
        frame.pack();
        frame.setVisible(true);


    }

    public void setColors(String oldState, Game game) throws InterruptedException {

        String gameState = game.getGameState();

        for (int i = 0; i < 64; i++) {

            if (oldState.charAt(i) != gameState.charAt(i)) {
                if (gameState.charAt(i) == 'X') {
                    buttons[i].setIcon(new ImageIcon("images/BTW.GIF"));
                } else if (gameState.charAt(i) == 'O') {
                    buttons[i].setIcon(new ImageIcon("images/WTB.GIF"));
                }
            }else if (gameState.charAt(i) == 'X') {
                buttons[i].setIcon(new ImageIcon("images/WhiteTile.PNG"));
            } else if (gameState.charAt(i) == 'O') {
                buttons[i].setIcon(new ImageIcon("images/BlackTile.PNG"));
            }else{
                buttons[i].setIcon(new ImageIcon("images/NoTile.PNG"));

            }
        }

        synchronized (SimulationGUI.this) {

            SimulationGUI.this.wait(1000);
        }

        for (int i = 0; i < 64; i++) {

            if (gameState.charAt(i) == 'X') {
                buttons[i].setIcon(new ImageIcon("images/WhiteTile.PNG"));
            } else if (gameState.charAt(i) == 'O') {
                buttons[i].setIcon(new ImageIcon("images/BlackTile.PNG"));
            } else {
                buttons[i].setIcon(new ImageIcon("images/NoTile.PNG"));
            }

        }

        whiteTiles.setText("White: " + (int)game.countTiles('X'));
        blackTiles.setText("Black: " + (int)game.countTiles('O'));

    }



}
