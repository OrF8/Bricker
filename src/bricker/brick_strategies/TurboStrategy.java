package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;

/**
 * A collision strategy that switches to turbo mode on collision.
 */
public class TurboStrategy extends CollisionStrategyDecorator {

    private final BrickerGameManager gameManager; /* The game manager to use. */

    /**
     * Creates a new collision strategy decorator.
     * @param gameManager The game manager to use.
     * @param decoratedStrategy The strategy to decorate.
     */
    public TurboStrategy(BrickerGameManager gameManager, CollisionStrategy decoratedStrategy) {
        super(decoratedStrategy);
        this.gameManager = gameManager;
    }

    /**
     * Switch to turbo mode on collision.
     * Switches to turbo mode if and only if collided with the game ball.
     * @param gameObject The GameObject that this strategy is associated with.
     * @param other The GameObject that the collision was detected with.
     */
    @Override
    public void onCollision(GameObject gameObject, GameObject other) {
        super.onCollision(gameObject, other);
        if (other.getTag().equals(BrickerGameManager.BALL_TAG)) {
            gameManager.switchToTurboMode();
        }
    }

}
