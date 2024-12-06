package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;

public class AdditionalPaddleStrategy extends CollisionStrategyDecorator {

    private final BrickerGameManager gameManager;

    /**
     * Creates a new collision strategy decorator.
     *
     * @param decoratedStrategy The strategy to decorate.
     */
    public AdditionalPaddleStrategy(CollisionStrategy decoratedStrategy, BrickerGameManager gameManager) {
        super(decoratedStrategy);
        this.gameManager = gameManager;
    }

    @Override
    public void onCollision(GameObject gameObject, GameObject other) {
        super.onCollision(gameObject, other);
        gameManager.createAdditionalPaddle();
    }
}
