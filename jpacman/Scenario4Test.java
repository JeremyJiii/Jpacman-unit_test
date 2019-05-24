package nl.tudelft.jpacman;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.game.Game;
import nl.tudelft.jpacman.level.Player;

import nl.tudelft.jpacman.npc.Ghost;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class Scenario4Test {
    private static final long DEFAULT_INTERVAL = 100L;
    private Launcher launcher;
    private Game game;
    private Player player;


    @Before
    public void setUpPacman() {
        launcher = new Launcher();
        launcher.launch();

        game = launcher.getGame();
        player = game.getPlayers().get(0);
    }

    @After
    public void tearDown() {
        launcher.dispose();
    }

    /**
     * Scenario S4.1: Suspend the game.
     * Given the game has started;
     * When  the player clicks the "Stop" button;
     * Then  all moves from ghosts and the player are suspended.
     *
     */
    @Test
    public void suspend(){
        game.start();
        //Set the initial direction of the ghost
        player.setDirection(Direction.WEST);

        // stop the game
        game.stop();
        assertEquals(false, game.isInProgress());

        // change the direction of the player
        player.setDirection(Direction.EAST);
        assertEquals(Direction.EAST, player.getDirection());

        // move the player to another square
        // check if he is still at the same square
        Square nowLocation = player.getSquare();
        game.move(player, Direction.EAST);
        assertEquals(nowLocation, player.getSquare());

//        Square s =
    }

    /**
     * Scenario S4.2: Restart the game.
     * Given the game is suspended;
     * When  the player hits the "Start" button;
     * Then  the game is resumed.
     */
    @Test
    public void restart(){
        game.start();

        // stop the game
        game.stop();

        //Check if the game is still running
        assertFalse(game.isInProgress());

        // and start the game again
        game.start();
        assertTrue(game.isInProgress());
    }
}
