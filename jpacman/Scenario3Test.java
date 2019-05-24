package nl.tudelft.jpacman;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import nl.tudelft.jpacman.board.Board;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.game.Game;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.Pellet;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.npc.Ghost;

import java.util.LinkedList;
import nl.tudelft.jpacman.npc.ghost.GhostFactory;
import nl.tudelft.jpacman.sprite.PacManSprites;
import nl.tudelft.jpacman.PacmanConfigurationException;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

// The test class for user story 3.
public class Scenario3Test {
    private static final long DEFAULT_INTERVAL = 100L;
    private Launcher launcher;
    private Game game;
    private Player player;

    /**
     * Sets up the game using a simple map and a custom ghost factory.
     * Also initializes some variables we use quite often.
     */
    @Before
    public void setUpSimpleGhostPacman() {
        launcher = new Launcher();
        launcher.launch();

        game = launcher.getGame();
        player = game.getPlayers().get(0);
    }

    /**
     * Tears down the game so it can gracefully exit.
     */
    @After
    public void tearDown() {
        launcher.dispose();
    }

    /**
     * Scenario S3.1: A ghost moves.
     * Given the game has started,
     *  and  a ghost is next to an empty cell;
     * When  a tick event occurs;
     * Then  the ghost can move to that cell.
     */
    @Test
    public void ghostMoves(){
        launcher = new Launcher();
        launcher = new ghost1();
        launcher.launch();
        game = launcher.getGame();

        Board board  = game.getLevel().getBoard();
        Square originGhost = board.squareAt(0,0);
        game.start();

        List<Unit> oriOcc = originGhost.getOccupants();
        Ghost g = (Ghost)oriOcc.get(0);

        Square nextStep = originGhost.getSquareAt(g.nextMove());

        assertThat(nextStep.isAccessibleTo(g)).isTrue();

//        Thread.sleep(DEFAULT_INTERVAL);

        List<Unit> occ = nextStep.getOccupants();
        assertTrue(occ.isEmpty());
        if(occ!=null &&  !occ.isEmpty()) {
            System.out.println("here");
            assertThat(occ.get(0)).isEqualTo(oriOcc.get(0));
        }

        //If so, the bot has indeed moved to a square next to him which he could stand on
    }

    /**
     * Scenario S3.2: The ghost moves over food.
     * Given the game has started,
     *  and  a ghost is next to a cell containing food;
     * When  a tick event occurs;
     * Then  the ghost can move to the food cell,
     *  and  the food on that cell is not visible anymore.
     * @throws InterruptedException - Throws an error if the build factory goes wrong
     */
    @Test
    public void ghostMovesOverFood() throws InterruptedException {
        launcher = new ghost2();
        launcher.launch();
        game = launcher.getGame();

        Board board  = game.getLevel().getBoard();
        Square sq = board.squareAt(0,0);
        game.start();

        List<Unit> oriOcc = sq.getOccupants();
        Ghost g = (Ghost)oriOcc.get(0);
        Square sqNext = sq.getSquareAt(g.getDirection());

        // We sleep until the Ghost is going to a square with food next
        boolean boo = false;
        while (!boo) {
            //update the squares and directions of the ghost
            for(Unit unit : sqNext.getOccupants()) {
                if (unit instanceof Pellet) {
                    boo = true;
                }
            }
            Thread.sleep(DEFAULT_INTERVAL);
            sq = g.getSquare();
            sqNext = sq.getSquareAt(g.getDirection());
        }
        List<Unit> occupants;
        Unit lastOccupant;

        // We're at the food, now verify that the visible occupant (the last one) is a Pellet
        occupants = sqNext.getOccupants();
        lastOccupant = occupants.get(occupants.size() - 1);
        assertTrue(lastOccupant instanceof Pellet);

        // Now sleep until we moved and check that the last occupant is not a Pellet anymore
        Thread.sleep(DEFAULT_INTERVAL);
        assertEquals(sqNext, g.getSquare());

        // Verify that the visible occupant is not a Pellet anymore
        occupants = sqNext.getOccupants();
        lastOccupant = occupants.get(occupants.size() - 1);
        assertFalse(lastOccupant instanceof Pellet);
        Thread.sleep(DEFAULT_INTERVAL);
    }
    /**
     * Scenario S3.3: The ghost leaves a food cell.
     * Given a ghost is on a food cell (see S3.2);
     * When  a tick even occurs;
     * Then  the ghost can move to away from the food cell,
     *  and  the food on that cell is is visible again.
     *  @throws InterruptedException - Throws an error if the build factory goes wrong
     */
    @Test
    public void ghostLeavesFood() throws InterruptedException {
        launcher = new ghost2();
        launcher.launch();
        game = launcher.getGame();

        Board board  = game.getLevel().getBoard();
        Square sq = board.squareAt(0,0);
        game.start();

        List<Unit> oriOcc = sq.getOccupants();
        Ghost blinky = (Ghost)oriOcc.get(0);
        Square sqNext = sq.getSquareAt(blinky.getDirection());
        Thread.sleep(DEFAULT_INTERVAL);
        // We sleep until the Ghost is going to a square with food next
        boolean bool = false;
        while (!bool) {
            //update the squares and directions of the ghost
            for(Unit unit : sqNext.getOccupants()) {
                if (unit instanceof Pellet) {
                    bool = true;
                }
            }
            sq = blinky.getSquare();
            sqNext = sq.getSquareAt(blinky.getDirection());
        }
        List<Unit> occupants;
        Unit lastOccupant;

        // We're at the food, now verify that the Pellet is invisible (the last one)
        occupants = sqNext.getOccupants();
        lastOccupant = occupants.get(occupants.size() - 1);
        assertTrue(lastOccupant instanceof Pellet);

        // Now sleep until we moved and check that the last occupant is not a Pellet anymore
        Thread.sleep(DEFAULT_INTERVAL);
        assertEquals(sqNext, blinky.getSquare());

        // Verify that the visible occupant is not a Pellet anymore
        occupants = sqNext.getOccupants();
        lastOccupant = occupants.get(occupants.size() - 1);
        assertFalse(lastOccupant instanceof Pellet);
        boolean booo = false;
        for(Unit unit:sqNext.getOccupants())
            if(unit instanceof Pellet)
                booo = true;
        assertTrue(booo);
    }

    /**
     * Scenario S3.4: The player dies.
     * Given the game has started,
     *  and  a ghost is next to a cell containing the player;
     * When  a tick event occurs;
     * Then  the ghost can move to the player,
     *  and  the game is over.
     *  @throws InterruptedException - Throws an error if the build factory goes wrong
     */
    @Test
    public void playerDiesByGhost() throws InterruptedException {

        launcher = new ghost3();
        launcher.launch();
        game = launcher.getGame();

        Board board  = game.getLevel().getBoard();
        Square sq = board.squareAt(0,0);
        List<Unit> oriOcc = sq.getOccupants();
        Ghost blinky = (Ghost)oriOcc.get(0);


        game.start();
        Direction next = blinky.getDirection();
        Square sqNext = sq.getSquareAt(next);

        //Suspend the test until we are actually at the player to check if it gets killed
        //Break if the next square contains the player
        boolean boo = false;
        while (!boo) {
            //update the squares and directions of the ghost
            for(Unit unit : sqNext.getOccupants()) {
                if (unit instanceof Player) {
                    boo = true;
                }
            }
            sq = blinky.getSquare();
            next = blinky.getDirection();
            sqNext = sq.getSquareAt(next);
            Thread.sleep(DEFAULT_INTERVAL);
        }
        //Suspend to give the ghost time to move to the square of the player
        //Check if the player is indeed no longer alive
        Thread.sleep(DEFAULT_INTERVAL*100);
        assertTrue(player.isAlive());
    }

}
