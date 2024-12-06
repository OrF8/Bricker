package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;

public class PuckBallStrategy extends CollisionStrategyDecorator {
    private final BrickerGameManager gameManager;

    public PuckBallStrategy(CollisionStrategy decoratedStrategy, BrickerGameManager gameManager) {
        super(decoratedStrategy);
        this.gameManager = gameManager;
    }

    @Override
    public void onCollision(GameObject gameObject, GameObject other) {
        super.onCollision(gameObject, other);
        gameManager.createPucks(gameObject.getCenter());
    }
}
