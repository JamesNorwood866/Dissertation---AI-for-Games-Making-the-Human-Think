import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Flow;


public class GUI implements ActionListener {


    private boolean AI_move = false;
    private boolean playerOneHasTwoMoves = false;
    private boolean gameOver = false;
    String tempState;   //Used incase player 1 has 2 moves
    JFrame frame;
    JPanel groupedPanel;
    JFrame rules;
    JPanel board;
    JPanel panel2;
    JPanel gameInfo;
    JPanel buttons;
    JLabel playersTurn;
    JLabel whiteTiles;
    JLabel blackTiles;
    JButton forfeitButton;
    JButton rulesButton;
    boolean GUIvisible;

    public GUI(Game game, Player player1, Player player2) {

        GUIvisible = true;

        frame = new JFrame();
        rules = new JFrame();
        rules.setSize(600, 500);
        rules.setVisible(false);
        rules.setLocationRelativeTo(null);

        // Create a panel to hold the components
        JPanel panel = new JPanel(new BorderLayout());
        panel.setSize(600,400);

        // Create a label for the title
        JLabel titleLabel = new JLabel("Othello Game Rules");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Create a text area for the paragraph
        JTextArea paragraphArea = new JTextArea("The objective of this game is to have more tiles than the opponent at the end of the game\n\nA player can place a tile on any of the squares with a green circle.\n\nYour tile must be placed adjacent to a tile of the opponent, such that the opponents piece is flanked on either side by your tiles.\n\nThe opponents tiles that are captured between your two tiles are then flipped to be become your tiles.\n\nAn example of a move can be shown below:\n");
        paragraphArea.setEditable(false);
        paragraphArea.setLineWrap(true);
        paragraphArea.setWrapStyleWord(true);
        paragraphArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add some padding
        paragraphArea.setFont(new Font("SansSerif", Font.PLAIN, 12)); // Change the font size
        paragraphArea.setForeground(Color.BLACK); // Change the font color
        paragraphArea.setBackground(Color.WHITE); // Change the background color
        paragraphArea.setAlignmentX(Component.CENTER_ALIGNMENT); // Align the text horizontally to center
        paragraphArea.setAlignmentY(Component.CENTER_ALIGNMENT); // Align the text vertically to center
        panel.add(paragraphArea, BorderLayout.CENTER);

        // Create a panel to hold the pictures
        JPanel picturePanel = new JPanel(new GridLayout(1, 2));
        BufferedImage image1 = null;
        BufferedImage image2 = null;
        try {
            image1 = ImageIO.read(new File("images/Rules1.png"));
            image2 = ImageIO.read(new File("images/Rules2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JLabel picLabel1 = new JLabel(new ImageIcon(image1.getScaledInstance(250, 250, Image.SCALE_SMOOTH)));
        JLabel picLabel2 = new JLabel(new ImageIcon(image2.getScaledInstance(250, 250, Image.SCALE_SMOOTH)));
        picturePanel.add(picLabel1);
        picturePanel.add(picLabel2);
        panel.add(picturePanel, BorderLayout.SOUTH);

        picturePanel.add(picLabel1);
        picturePanel.add(picLabel2);
        panel.add(picturePanel, BorderLayout.SOUTH);

        rules.getContentPane().add(panel);



        groupedPanel = new JPanel(new BorderLayout());

        board = new JPanel();
        gameInfo = new JPanel(new GridLayout(1,3));
        playersTurn = new JLabel("Black's Turn");
        whiteTiles = new JLabel("White: 2");
        blackTiles = new JLabel("Black: 2");
        forfeitButton = new JButton("Forfeit");
        rulesButton = new JButton("View Rules");
        rulesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rules.setVisible(true);
            }
        });

        groupedPanel.setPreferredSize(new Dimension(600, 700));

        board.setLayout(new GridLayout(8, 8));
        board.setPreferredSize(new Dimension(600,600));

        playersTurn.setHorizontalAlignment(JLabel.CENTER);
        whiteTiles.setHorizontalAlignment(JLabel.CENTER);
        blackTiles.setHorizontalAlignment(JLabel.CENTER);

        gameInfo.setPreferredSize(new Dimension(600,50));
        gameInfo.add(blackTiles);
        gameInfo.add(playersTurn);
        gameInfo.add(whiteTiles);

        buttons = new JPanel(new GridLayout(1, 2));
        buttons.setPreferredSize(new Dimension(600,50));
        buttons.add(forfeitButton);
        buttons.add(rulesButton);

        panel2 = new JPanel();
        panel2.setPreferredSize(new Dimension(600, 100));
        panel2.add(gameInfo, BorderLayout.NORTH);
        panel2.add(buttons, BorderLayout.SOUTH);

        groupedPanel.add(board, BorderLayout.CENTER);
        groupedPanel.add(panel2, BorderLayout.SOUTH);

        frame.add(groupedPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);


        forfeitButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                GUIvisible = false;
                frame.dispose();

            }
        });
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final char[] color = {'O'};

        JButton[] buttons = new JButton[64];

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



            int finalI = i;
            buttons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    if (!AI_move) {
                        System.out.println();
                        String oldState = game.getGameState();
                        player1.playMove(game, finalI);

                        //if the gameState changes after the player has moved
                        if (!Objects.equals(game.getGameState(), oldState)) {
                            setColors(buttons, oldState, game.getGameState(), new ArrayList<>(), true);

                            if (game.findPossibleMoves(player2.getColor()).size() != 0) {
                                playersTurn.setText("White's turn");
                                whiteTiles.setText("White: " + (int)game.countTiles('X'));
                                blackTiles.setText("Black: " + (int)game.countTiles('O'));
                                AI_move = true;
                            } else if (game.findPossibleMoves(player1.getColor()).size() == 0) {
                                playersTurn.setText(game.findWinner());
                                forfeitButton.setText("New Game");
                                whiteTiles.setText("White: " + (int)game.countTiles('X'));
                                blackTiles.setText("Black: " + (int)game.countTiles('O'));
                                gameOver = true;
                            } else {
                                setColors(buttons, oldState, game.getGameState(), game.findPossibleMoves(player1.getColor()),   true);
                                tempState = oldState;
                                playerOneHasTwoMoves = true;
                            }
                        }
                    }
                }

            });
            board.add(buttons[i]);
        }

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Othello");
        frame.pack();
        frame.setVisible(true);


        while (true){
            if(!GUIvisible){
                break;
            }
            synchronized (GUI.this) {
                try {
                    GUI.this.wait(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (gameOver){
                setColors(buttons, game.getGameState(), game.getGameState(), new ArrayList<>(), true);
            }
            if(playerOneHasTwoMoves){
                setColors(buttons, tempState, game.getGameState(), game.findPossibleMoves(player1.getColor()), false);
                playerOneHasTwoMoves = false;
            }
            while (AI_move){
                setColors(buttons, game.getGameState(), game.getGameState(), new ArrayList<>(), false);

                String oldState = game.getGameState();
                player2.playMove(game);
                player2.rankOpponentsMove(game);


                synchronized (GUI.this) {
                    try {
                        GUI.this.wait(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                //If white can play another move after black
                if(game.findPossibleMoves(player1.getColor()).size() != 0) {
                    setColors(buttons, oldState, game.getGameState(), game.findPossibleMoves(player1.getColor()), false);
                    playersTurn.setText("Black's turn");
                    whiteTiles.setText("White: " + (int)game.countTiles('X'));
                    blackTiles.setText("Black: " + (int)game.countTiles('O'));
                    AI_move = false;

                //If game is over
                } else if(game.findPossibleMoves(player2.getColor()).size() == 0){
                    setColors(buttons, oldState, game.getGameState(), new ArrayList<>(), false);
                    playersTurn.setText(game.findWinner());
                    whiteTiles.setText("White: " + (int)game.countTiles('X'));
                    blackTiles.setText("Black: " + (int)game.countTiles('O'));
                    AI_move = false;

                //If black has 2 moves in a row
                } else{
                    setColors(buttons, oldState, game.getGameState(), new ArrayList<>(), false);

                }
            }
        }

    }


    public void setColors(JButton[] buttons, String oldState, String gameState, ArrayList<Integer> possibleMoves, Boolean x) {

        if (x) {
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
                } else if (possibleMoves.contains(i + 1)) {
                    buttons[i].setIcon(new ImageIcon("images/PossibleMove.PNG"));
                }else{
                    buttons[i].setIcon(new ImageIcon("images/NoTile.PNG"));

                }
            }
        } else {
            synchronized (GUI.this) {

                for (int i = 0; i < 64; i++) {

                    if (oldState.charAt(i) != gameState.charAt(i)) {
                        if (gameState.charAt(i) == 'X') {
                            buttons[i].setIcon(new ImageIcon("images/BTW.GIF"));
                        } else if (gameState.charAt(i) == 'O') {
                            buttons[i].setIcon(new ImageIcon("images/WTB.GIF"));
                        }
                    } else if (possibleMoves.contains(i + 1)) {
                        buttons[i].setIcon(new ImageIcon("images/PossibleMove.PNG"));
                    }
                }

                try {
                    GUI.this.wait(1000);

                    for (int i = 0; i < 64; i++) {

                        if (gameState.charAt(i) == 'X') {
                            buttons[i].setIcon(new ImageIcon("images/WhiteTile.PNG"));
                        } else if (gameState.charAt(i) == 'O') {
                            buttons[i].setIcon(new ImageIcon("images/BlackTile.PNG"));
                        } else if (possibleMoves.contains(i + 1)) {
                            buttons[i].setIcon(new ImageIcon("images/PossibleMove.PNG"));
                        } else {
                            buttons[i].setIcon(new ImageIcon("images/NoTile.PNG"));
                        }

                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
