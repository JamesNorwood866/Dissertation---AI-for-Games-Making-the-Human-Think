import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Driver implements ActionListener {


    public static void main(String[] args) throws InterruptedException {


        final boolean[] p2Made = {false};
        final Player[] player2 = {null};

        JFrame frame = new JFrame();
        JPanel board = new JPanel();
        JButton[] buttons = new JButton[8];

        Game game = new Game();
        final GUI[] gui = {null};
        Player player1 = new HumanPlayer('O');

        buttons[0] = new JButton("Challenging Player 1");
        buttons[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.print("j");
                gui[0] = null;
                player2[0] = new ChallengingPlayer1('X');
                p2Made[0] = true;
            }
        });
        board.add(buttons[0]);

        buttons[1] = new JButton("Challenging Player 2");
        buttons[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui[0] = null;

                player2[0] = new ChallengingPlayer2('X');
                p2Made[0] = true;
            }
        });
        board.add(buttons[1]);

        buttons[2] = new JButton("Basic Monte Carlo Player");
        buttons[2].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui[0] = null;

                player2[0] = new BasicMonteCarloPlayer('X');
                p2Made[0] = true;
            }
        });
        board.add(buttons[2]);

        buttons[3] = new JButton("Monte Carlo Player");
        buttons[3].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui[0] = null;

                player2[0] = new MonteCarloPlayer('X');
                p2Made[0] = true;
            }
        });
        board.add(buttons[3]);

        buttons[4] = new JButton("Killer Player");
        buttons[4].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui[0] = null;

                player2[0] = new KillerPlayer('X');
                p2Made[0] = true;
            }
        });
        board.add(buttons[4]);

        buttons[5] = new JButton("Passive Player");
        buttons[5].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui[0] = null;

                player2[0] = new PassivePlayer('X');
                p2Made[0] = true;
            }
        });
        board.add(buttons[5]);

        buttons[6] = new JButton("Random Player");
        buttons[6].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui[0] = null;

                player2[0] = new RandomPlayer('X');
                p2Made[0] = true;
            }
        });
        board.add(buttons[6]);



        frame.add(board);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


        while(true) {
            System.out.print("");
            if (p2Made[0]) {
                frame.setVisible(false);
                gui[0] = new GUI(game, player1, player2[0]);
                frame.setVisible(true);
                game = new Game();
                player2[0] = null;
                p2Made[0] = false;
            }
        }


    }








    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
