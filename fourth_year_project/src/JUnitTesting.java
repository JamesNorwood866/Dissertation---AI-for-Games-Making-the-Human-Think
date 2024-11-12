import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JUnitTesting {
    @Test
    public void testGetGameState() {
        Game game = new Game();
        String expectedGameState = "---------------------------XO------OX---------------------------";
        assertEquals(expectedGameState, game.getGameState());
    }

    @Test
    public void testResetGameState() {
        Game game = new Game();
        String expectedGameState = "XOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXOXO";
        game.resetGameState(expectedGameState);
        assertEquals(expectedGameState, game.getGameState());
    }

    @Test
    public void testFlipTiles() {
        Game game = new Game();
        ArrayList<Integer> expectedTiles = new ArrayList<>(List.of(27, 28));
        assertEquals(expectedTiles, game.flipTiles(27, 'O'));

        String expectedGameState = "--------------------------OOO------OX---------------------------";
        game.setGameState(game.flipTiles(27, 'O'), 'O');
        assertEquals(expectedGameState, game.getGameState());

        expectedTiles = new ArrayList<>(List.of(35, 36));
        assertEquals(expectedTiles, game.flipTiles(35, 'X'));

        expectedGameState = "--------------------------OOO-----XXX---------------------------";
        game.setGameState(game.flipTiles(35, 'X'), 'X');
        assertEquals(expectedGameState, game.getGameState());


        expectedTiles = new ArrayList<>(List.of(43, 36, 35));
        assertEquals(expectedTiles, game.flipTiles(43, 'O'));

        expectedGameState = "--------------------------OOO-----OOX-----O---------------------";
        game.setGameState(game.flipTiles(43, 'O'), 'O');
        assertEquals(expectedGameState, game.getGameState());

    }

    @Test
    public void testFindPossibleMoves() {
        Game game = new Game();
        ArrayList<Integer> expectedMoves = new ArrayList<>(List.of(20, 27, 38, 45));
        assertEquals(expectedMoves, game.findPossibleMoves('O'));

        game.resetGameState("--XXX-----OOO---------------------------------------------------");
        expectedMoves = new ArrayList<>(List.of(18, 19, 20, 21, 22));
        assertEquals(expectedMoves, game.findPossibleMoves('X'));

        game.resetGameState("----------------------------------------------------------------");
        expectedMoves = new ArrayList<>();
        assertEquals(expectedMoves, game.findPossibleMoves('X'));
    }

    @Test
    public void testFindWinner() {
        Game game = new Game();
        assertEquals("It's a Draw", game.findWinner());

        game.resetGameState("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        assertEquals("White Wins!", game.findWinner());

        game.resetGameState("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOXX");
        assertEquals("Black Wins!", game.findWinner());

        game.resetGameState("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
        assertEquals("It's a Draw", game.findWinner());
    }

    @Test
    public void testCountTiles() {
        Game game = new Game();
        assertEquals(2, game.countTiles('X'));
        assertEquals(2, game.countTiles('O'));

        game.resetGameState("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOXX");
        assertEquals(2, game.countTiles('X'));
        assertEquals(62, game.countTiles('O'));

        game.resetGameState("OOOOOOOOOOOOOOOOOOXOOOOOOOXOOOXOXXOOOOXXXOOOOOOOOOOOOOOOOOOOOOXX");
        assertEquals(10, game.countTiles('X'));
        assertEquals(54, game.countTiles('O'));
    }

    @Test
    public void testPlayMove(){
        Game game = new Game();
        Player player = new HumanPlayer('O');

        String expectedGameState = "--------------------------OOO------OX---------------------------";
        player.playMove(game, 26);
        assertEquals(expectedGameState, game.getGameState());

        expectedGameState = "--------------------------OOO------OOO--------------------------";
        player.playMove(game, 37);
        assertEquals(expectedGameState, game.getGameState());

        player.playMove(game, 2);
        assertEquals(expectedGameState, game.getGameState());

        player = new HumanPlayer('X');
        game.resetGameState("X--------O-----------------O--------X---------------------------");
        expectedGameState = "X--------X--------X--------X--------X---------------------------";
        player.playMove(game, 18);
        assertEquals(expectedGameState, game.getGameState());

    }
}
