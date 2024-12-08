package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;

/**
 * A collision strategy that gives the player an additional paddle.
 */
class AdditionalPaddleStrategy extends CollisionStrategyDecorator {

    private final BrickerGameManager gameManager;

    /**
     * Creates a new collision strategy decorator.
     * @param gameManager The game manager to use.
     * @param decoratedStrategy The strategy to decorate.
     */
    AdditionalPaddleStrategy(BrickerGameManager gameManager, CollisionStrategy decoratedStrategy) {
        super(decoratedStrategy);
        this.gameManager = gameManager;
    }

    /**
     * If a collision is detected, creates an additional paddle.
     * @param gameObject The GameObject that this strategy is associated with.
     * @param other The GameObject that the collision was detected with.
     */
    @Override
    public void onCollision(GameObject gameObject, GameObject other) {
        super.onCollision(gameObject, other);
        gameManager.createAdditionalPaddle();
    }
}
