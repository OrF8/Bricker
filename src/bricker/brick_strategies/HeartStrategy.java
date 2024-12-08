package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;

/**
 * A strategy that gives the player an extra life on collision.
 */
class HeartStrategy extends CollisionStrategyDecorator{

    private final BrickerGameManager gameManager; /* The game manager to use. */

    /**
     * Creates a new collision strategy decorator.
     * @param gameManager The game manager to use.
     * @param decoratedStrategy The strategy to decorate.
     */
    HeartStrategy(BrickerGameManager gameManager, CollisionStrategy decoratedStrategy) {
        super(decoratedStrategy);
        this.gameManager = gameManager;
    }

    /**
     * Gives the player an extra life on collision.
     * @param gameObject The GameObject that this strategy is associated with.
     * @param other The GameObject that the collision was detected with.
     */
    @Override
    public void onCollision(GameObject gameObject, GameObject other) {
        super.onCollision(gameObject, other);
        gameManager.createFallingHeart(gameObject.getCenter());
    }
}
