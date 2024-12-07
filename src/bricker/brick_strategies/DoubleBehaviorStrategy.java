package bricker.brick_strategies;

import danogl.GameObject;

/**
 * A strategy that combines two strategies.
 */
public class DoubleBehaviorStrategy implements CollisionStrategy {

    private final CollisionStrategy strategy1;
    private final CollisionStrategy strategy2;

    /**
     * Creates a new DoubleBehaviorStrategy with the given strategies.
     * @param strategy1 The first strategy.
     * @param strategy2 The second strategy.
     */
    public DoubleBehaviorStrategy(CollisionStrategy strategy1, CollisionStrategy strategy2) {
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
        if (strategy1 != null) {
            strategy1.onCollision(thisObj, otherObj);
        }
        if (strategy2 != null) {
            strategy2.onCollision(thisObj, otherObj);
        }
    }
}
