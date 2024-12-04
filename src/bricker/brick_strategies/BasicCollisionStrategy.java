package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;

public class BasicCollisionStrategy implements CollisionStrategy{

    private final BrickerGameManager gameManager;

    /**
     * Construct a new BasicCollisionStrategy instance.
     * @param gameManager The game manager that manages the game the collision strategy is used in.
     */
    public BasicCollisionStrategy(BrickerGameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public void onCollision(GameObject gameObject, GameObject other) {
        gameManager.removeGameObject(gameObject);
    }

}
