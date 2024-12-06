package bricker.brick_strategies;

import bricker.gameobjects.GameObjects;
import bricker.main.BrickerGameManager;
import danogl.GameObject;

/**
 * A basic collision strategy that removes a game object from the game when a collision is detected.
 */
public class BasicCollisionStrategy implements CollisionStrategy{

    private final BrickerGameManager gameManager;

    /**
     * Construct a new BasicCollisionStrategy instance.
     * @param gameManager The game manager that manages the game the collision strategy is used in.
     */
    public BasicCollisionStrategy(BrickerGameManager gameManager) {
        this.gameManager = gameManager;
    }

    /**
     * Remove a game object from the game when a collision is detected.
     * @param gameObject The GameObject that this strategy is associated with.
     * @param other The GameObject that the collision was detected with.
     */
    @Override
    public void onCollision(GameObject gameObject, GameObject other) {
        gameManager.removeGameObject(gameObject, GameObjects.BRICK);
    }

}
