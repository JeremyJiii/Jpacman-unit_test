package nl.tudelft.jpacman;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import nl.tudelft.jpacman.game.Game;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * This class tests all scenario for User Story 1: Startup.
 */
public class Scenario1Test {
    private Launcher launcher;
    private Game game;

    /**
     * Sets up the game and initializes some variables we use quite often.
     */
    @Before
    public void setUpPacman() {
        launcher = new Launcher();
        launcher.launch();

        game = launcher.getGame();
    }

    @After
    public void After(){}

    /**
     * Scenario S1.1: Start.
     * Given the user has launched the JPacman GUI;
     * When  the user presses the "Start" button;
     * Then  the game should start.
     */
    @Test
    public void start() {
        assertFalse(game.isInProgress());
        game.start();
        assertTrue(game.isInProgress());
    }
}
