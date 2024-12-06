package bricker.brick_strategies;

import bricker.main.BrickerGameManager;

import java.util.Random;

/**
 * A factory for creating collision strategies.
 */
public class StrategyFactory {

    private static final Random rand = new Random();

    /**
     * Chooses randomly between the available collision strategies.
     * @param gameManager The game manager to use.
     * @param decoratedStrategy The strategy to decorate.
     * @return The new collision strategy.
     */
    private static CollisionStrategy chooseStrategy(
            BrickerGameManager gameManager, CollisionStrategy decoratedStrategy
    ) {
        CollisionStrategy strategy = null;
        int choice = rand.nextInt(10);
        switch (choice) {
            case 0:
                strategy = new PuckBallStrategy(decoratedStrategy, gameManager);
                break;
            case 1:
                strategy = new AdditionalPaddleStrategy(decoratedStrategy, gameManager);
                break;
        }
        return strategy;
    }

    /**
     * Chooses randomly between the available collision strategies and creates a new instance of it.
     * 0.5 probability for a regular brick, 1/10 for a puck brick, 1/10 for a turbo brick,
     * 1/10 for a heart-giving brick, 1/10 for a double-behavior brick.
     * @param gameManager The game manager to use.
     * @param isDouble Whether the brick is a double-behavior brick.
     * @return The new collision strategy.
     */
    private static CollisionStrategy createStrategy(BrickerGameManager gameManager, boolean isDouble) {
        CollisionStrategy strategy = null;
        if (isDouble) {
            // TODO: Implement double-behavior brick strategy
            int x = 0;
            x++;
        } else {
            BasicCollisionStrategy basicCollisionStrategy = new BasicCollisionStrategy(gameManager);
            if (rand.nextBoolean()) {
                strategy = basicCollisionStrategy;
            } else {
                strategy = chooseStrategy(gameManager, basicCollisionStrategy);
            }
        }
        return strategy;
    }

    /**
     * Chooses randomly between the available collision strategies and creates a new instance of it.
     * 0.5 probability for a regular brick, 1/10 for a puck brick, 1/10 for a turbo brick,
     * 1/10 for a heart-giving brick, 1/10 for a double-behavior brick.
     * @param gameManager The game manager to use.
     * @return The new collision strategy.
     */
    public static CollisionStrategy createStrategy(BrickerGameManager gameManager) {
        return createStrategy(gameManager, false);
    }

}
