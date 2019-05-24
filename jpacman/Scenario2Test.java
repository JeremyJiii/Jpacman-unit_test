package nl.tudelft.jpacman;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import nl.tudelft.jpacman.Launcher;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.game.Game;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.Level.LevelObserver;
import nl.tudelft.jpacman.npc.Ghost;
import nl.tudelft.jpacman.level.Pellet;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.level.MapParser;
import nl.tudelft.jpacman.PacmanConfigurationException;

import org.junit.Before;
import org.junit.Test;

public class Scenario2Test {
    private static final long PELLET_VALUE = 10;

    private Launcher launcher;
    private Game game;
    private Player player;
    private Square myLocation;
    private Level level;


    @Before
    public void setUpPacman() {
        launcher = new Launcher();
        launcher.launch();

        game = launcher.getGame();
        player = game.getPlayers().get(0);
        myLocation = player.getSquare();
    }

    public void setUpSimplePacman() {
        launcher.dispose();
        launcher = new Launcher();
        launcher.withMapFile("/simple_board.txt");
        launcher.launch();

        game = launcher.getGame();
        level = game.getLevel();
        player = game.getPlayers().get(0);
        myLocation = player.getSquare();
    }


    /**
     * Scenario S2.1: The player consumes
     * Given the game has started,
     *  and  my Pacman is next to a square containing a pellet;
     * When  I press an arrow key towards that square;
     * Then  my Pacman can move to that square,
     *  and  I earn the points for the pellet,
     *  and  the pellet disappears from that square.
     */
    @Test
    public void consume(){
        Square nextLocation = myLocation.getSquareAt(Direction.EAST);
        /**
         * Find if there is a Pellet in the neighbor.
         */
        Boolean b = false;
        for(Unit unit:nextLocation.getOccupants()){
            if(unit instanceof Pellet)
                b = true;
        }
        assertTrue(b);

        game.start();
        //Move to next location.
        game.move(player, Direction.EAST);
        //test if the sprite moves
        assertEquals(nextLocation, player.getSquare());
        // test if the score added.
        assertEquals(PELLET_VALUE,player.getScore());
        // find if the pellet disappears
        Boolean c = false;
        for(Unit unit:nextLocation.getOccupants()){
            if(unit instanceof Pellet)
                c = true;
        }
        assertFalse(c);
    }

    /**
     * Scenario S2.2: The player moves on empty square
     * Given the game has started,
     *  and  my Pacman is next to an empty square;
     * When  I press an arrow key towards that square;
     * Then  my Pacman can move to that square
     *  and  my points remain the same.
     */
    @Test
    public void playerMoves(){
        game.start();

        // Move one square to the east
        game.move(player, Direction.EAST);

        // The square at our original location should have no Pellet
        assertTrue(myLocation.getOccupants().isEmpty());

        // The initial score
        int score = player.getScore();
        game.move(player, Direction.WEST);

        // find that we moves to our original location
        assertEquals(myLocation, player.getSquare());
        // Check our score has remained the same as original
        assertEquals(score, player.getScore());
    }

    /**
     * Scenario S2.3: The move fails
     * Given the game has started,
     *   and my Pacman is next to a cell containing a wall;
     * When  I press an arrow key towards that cell;
     * Then  the move is not conducted.
     */
    @Test
    public void moveFails(){
        game.start();

        Square myLocation, adjacent;
        do {
            // Move to the east
            game.move(player, Direction.EAST);

            // Update our current location and adjacent square
            myLocation = player.getSquare();
            adjacent = myLocation.getSquareAt(Direction.EAST);

            // Keep doing this until the adjacent square isn't accessible (ie. it's a wall)
        } while (adjacent.isAccessibleTo(player));

        // Check that it is indeed not accessible
        assertFalse(adjacent.isAccessibleTo(player));

        // Try to go there anyway
        game.move(player, Direction.EAST);

        // Check that we haven't moved since breaking out of the loop
        assertEquals(myLocation, player.getSquare());
    }

    /**
     * Scenario S2.4: The player dies
     * Given the game has started,
     *  and  my Pacman is next to a cell containing a ghost;
     * When  I press an arrow key towards that square;
     * Then  my Pacman dies,
     *  and  the game is over.
     */
    @Test
    public void playerDies() throws InterruptedException {
        game.start();
        assertTrue(player.isAlive());

        // Wait until there is a ghost next to us
        Direction ghostdir = null;
        while (ghostdir == null) {
            // find if the ghost is on the neighors.
            // Direction is an enumeration.
            for (Direction dir: Direction.values()) {
                Square adjacent = myLocation.getSquareAt(dir);
                for(Unit unit:adjacent.getOccupants()){
                    if (unit instanceof Ghost) {
                        ghostdir = dir;
                    }
                }
            }
        }

        // Now move towards the ghost
        game.move(player, ghostdir);

        // Check if the player died
        assertFalse(player.isAlive());
        // Check that the game is over
        assertFalse(game.isInProgress());
    }

    /**
     * Scenario S2.5: Player wins, extends S2.1
     * When  I have eaten the last pellet;
     * Then  I win the game.
     */
    @Test
    public void playerWins(){
        setUpSimplePacman();
        // Create a testobserver and register it with the level.
        TestObserver observer = new TestObserver();
        level.addObserver(observer);

        game.start();

        // Pick up the only pellet in the level.
        game.move(player, Direction.EAST);
        // Check whether the game has been won.
        assertTrue(level.isAnyPlayerAlive());
        assertFalse(level.isInProgress());
        assertTrue(observer.hasWon());
    }

    class TestObserver implements LevelObserver {
        private boolean hasWon = false;
        /**
         * Query whether the game has been won.
         * @return boolean that indicates whether the player has won.
         */
        public boolean hasWon() { return hasWon; }
        /**
         * levelWon is a Level listener and gets called when the last Pellet is picked up.
         */
        public void	levelWon() { hasWon = true; }
        /**
         * levelLost is a Level listener and gets called when the player dies.
         */
        public void levelLost() { hasWon = false; }
    };
}
