package bricker.brick_strategies;

import bricker.main.BrickerGameManager;

import java.util.Random;

/**
 * A factory for creating collision strategies.
 */
public class StrategyFactory {

    /**
     * Chooses randomly between the available collision strategies and creates a new instance of it.
     * 0.5 probability for a regular brick, 1/10 for a puck brick, 1/10 for a turbo brick,
     * 1/10 for a heart-giving brick, 1/10 for a double-behavior brick.
     * @return The new collision strategy.
     */
    public static CollisionStrategy createStrategy(BrickerGameManager gameManager) {
        BasicCollisionStrategy basicCollisionStrategy = new BasicCollisionStrategy(gameManager);
        Random rand = new Random();
    }

}
