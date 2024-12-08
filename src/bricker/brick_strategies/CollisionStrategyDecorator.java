package bricker.brick_strategies;

import danogl.GameObject;

/**
 * A decorator for collision strategies.
 */
abstract class CollisionStrategyDecorator implements CollisionStrategy {

    /** The strategy that is being decorated. */
    protected final CollisionStrategy decoratedStrategy;

    /**
     * Creates a new collision strategy decorator.
     * @param decoratedStrategy The strategy to decorate.
     */
    CollisionStrategyDecorator(CollisionStrategy decoratedStrategy) {
        this.decoratedStrategy = decoratedStrategy;
    }

    /**
     * Called when a collision is detected.
     * This method calls the decorated strategy's onCollision method.
     * @param gameObject The GameObject that this strategy is associated with.
     * @param other The GameObject that the collision was detected with.
     */
    @Override
    public void onCollision(GameObject gameObject, GameObject other) {
        decoratedStrategy.onCollision(gameObject, other);
    }
}