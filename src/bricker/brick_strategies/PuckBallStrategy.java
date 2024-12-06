package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;

/**
 * A collision strategy that creates pucks on collision.
 */
public class PuckBallStrategy extends CollisionStrategyDecorator {

    /* The number of pucks to create on collision. */
    private static final int NUM_OF_PUCKS_TO_CREATE_ON_COLLISION = 2;

    private final BrickerGameManager gameManager; /* The game manager to use. */

    /**
     * Creates a new collision strategy decorator.
     * @param gameManager The game manager to use.
     * @param decoratedStrategy The strategy to decorate.
     */
    public PuckBallStrategy(BrickerGameManager gameManager, CollisionStrategy decoratedStrategy) {
        super(decoratedStrategy);
        this.gameManager = gameManager;
    }

    /**
     * Creates pucks on collision.
     * @param gameObject The GameObject that this strategy is associated with.
     * @param other The GameObject that the collision was detected with.
     */
    @Override
    public void onCollision(GameObject gameObject, GameObject other) {
        super.onCollision(gameObject, other);
        gameManager.createPucks(gameObject.getCenter(), NUM_OF_PUCKS_TO_CREATE_ON_COLLISION);
    }
}
