package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A factory class that creates a random strategy for the bricks.
 */
public class StrategyFactory {

    private static final int MAX_STRATEGIES = 3;
    private static final int TWO = 2;
    private static final int THREE = 3;
    private static final int FOUR = 4;
    private static final int FIVE = 5;
    private static final int TEN = 10;
    private static final Random rand = new Random();

    /**
     * Construct a new StrategyFactory instance.
     */
    public StrategyFactory() {}

    /**
     * Creates a random strategy for the bricks.
     * 1/2 chance of creating a BasicCollisionStrategy.
     * 1/10 chance of creating a PuckBallStrategy.
     * 1/10 chance of creating an AdditionalPaddleStrategy.
     * 1/10 chance of creating a TurboStrategy.
     * 1/10 chance of creating a HeartStrategy.
     * 1/10 chance of creating a DoubleBehaviorStrategy with two random special strategies.
     * @param gameManager The game manager that manages the game.
     * @return A random strategy for the bricks.
     */
    public static CollisionStrategy createStrategy(BrickerGameManager gameManager) {
        List<CollisionStrategy> strategies = new ArrayList<>();
        CollisionStrategy strategy = new BasicCollisionStrategy(gameManager);
        int choice = rand.nextInt(TEN);
        strategy = switch (choice) {
            case 0 -> new PuckBallStrategy(gameManager, strategy);
            case 1 -> new AdditionalPaddleStrategy(gameManager, strategy);
            case TWO -> new TurboStrategy(gameManager, strategy);
            case THREE -> new HeartStrategy(gameManager, strategy);
            case FOUR -> new DoubleBehaviorStrategy(
                    createSpecialStrategy(gameManager, strategies),
                    createSpecialStrategy(gameManager, strategies)
            );
            default -> strategy;
        };
        return strategy;
    }

    /**
     * Creates a special strategy for the bricks.
     * @param gameManager The game manager that manages the game.
     * @param strategies The list of strategies that have already been created.
     * @return A special strategy for the bricks.
     */
    public static CollisionStrategy createSpecialStrategy(
            BrickerGameManager gameManager, List<CollisionStrategy> strategies
    ) {
        int choice = rand.nextInt(FIVE);
        CollisionStrategy strategy = switch (choice) {
            case 0 -> new PuckBallStrategy(gameManager, new BasicCollisionStrategy(gameManager));
            case 1 -> new AdditionalPaddleStrategy(gameManager, new BasicCollisionStrategy(gameManager));
            case TWO -> new TurboStrategy(gameManager, new BasicCollisionStrategy(gameManager));
            case THREE -> new HeartStrategy(gameManager, new BasicCollisionStrategy(gameManager));
            case FOUR -> new DoubleBehaviorStrategy(
                    createSpecialStrategy(gameManager, strategies),
                    createSpecialStrategy(gameManager, strategies)
            );
            default -> new BasicCollisionStrategy(gameManager);
        };
        if (choice != FOUR) {
            strategies.add(strategy);
        }
        return strategies.size() > MAX_STRATEGIES ? null : strategy;
    }
}
