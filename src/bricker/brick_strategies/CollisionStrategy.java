package bricker.brick_strategies;

import danogl.GameObject;

/**
 * Represents a strategy to handle collisions between GameObjects.
 */
public interface CollisionStrategy {

    /**
     * Called when a collision is detected between the GameObject and another GameObject.
     * Implements the desired strategy to handle the collision.
     * @param gameObject The GameObject that this strategy is associated with.
     * @param other The GameObject that the collision was detected with.
     */
    void onCollision(GameObject gameObject, GameObject other);

}
