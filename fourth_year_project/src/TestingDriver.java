import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class TestingDriver{

    public static void main(String[] args) throws InterruptedException {

        JFrame frame = new JFrame();
        JPanel board = new JPanel();
        JButton optimalPlayer = new JButton();
        JButton challengingPlayer = new JButton();

        Game game = new Game();
        final GUI[] gui = {null};
        Player player1 = new HumanPlayer('O');
        final Player[] player2 = new Player[1];
        final boolean[] playerSelected = {false};

        optimalPlayer = new JButton("Player A");
        challengingPlayer = new JButton("Player B");

        optimalPlayer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui[0] = null;
                player2[0] = new KillerPlayer('X');
                playerSelected[0] = true;
            }
        });

        challengingPlayer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui[0] = null;
                player2[0] = new ChallengingPlayer2('X');
                playerSelected[0] = true;
            }
        });

        board.add(optimalPlayer);
        board.add(challengingPlayer);


        frame.add(board);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        while(true) {
            System.out.print("");
            if (playerSelected[0]) {
                frame.setVisible(false);
                gui[0] = new GUI(game, player1, player2[0]);
                frame.setVisible(true);
                game = new Game();
                player2[0] = null;
                playerSelected[0] = false;
            }
        }
    }
}
