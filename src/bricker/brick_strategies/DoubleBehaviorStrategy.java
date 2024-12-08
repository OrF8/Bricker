package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;

/**
 * A strategy that combines two strategies.
 */
class DoubleBehaviorStrategy implements CollisionStrategy {

    private final CollisionStrategy strategy1;
    private final CollisionStrategy strategy2;
    private final BasicCollisionStrategy basicStrategy;

    /**
     * Creates a new DoubleBehaviorStrategy with the given strategies.
     * @param strategy1 The first strategy.
     * @param strategy2 The second strategy.
     */
    DoubleBehaviorStrategy(
            BrickerGameManager gameManager, CollisionStrategy strategy1, CollisionStrategy strategy2
    ) {
        this.basicStrategy = new BasicCollisionStrategy(gameManager);
        this.strategy1 = strategy1;
        this.strategy2 = strategy2;
    }

    /**
     * Calls the onCollision method of both strategies.
     * @param thisObj The GameObject that this strategy is associated with.
     * @param otherObj The GameObject that the collision was detected with.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        // Make sure to remove the object from the game if it is a brick in case of 3 doubleBehaviorStrategies
        basicStrategy.onCollision(thisObj, otherObj);
        if (strategy1 != null) {
            strategy1.onCollision(thisObj, otherObj);
        }
        if (strategy2 != null) {
            strategy2.onCollision(thisObj, otherObj);
        }
    }
}
