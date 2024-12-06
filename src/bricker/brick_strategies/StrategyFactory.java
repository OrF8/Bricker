package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StrategyFactory {

    private static final int MAX_STRATEGIES = 3;
    private static final int TWO = 2;
    private static final int THREE = 3;
    private static final int FOUR = 4;
    private static final int FIVE = 5;
    private static final int TEN = 10;
    private static final Random rand = new Random();

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
