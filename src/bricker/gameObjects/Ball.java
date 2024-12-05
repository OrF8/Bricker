package bricker.gameObjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents a ball in the game.
 */
public class Ball extends GameObject {

    private final Sound collisionSound;
    private int collisionCounter = 0;

    /**
     * Construct a new Ball instance.
     *
     * @param topLeftCorner Position of the ball, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param collisionSound The sound to play when a collision occurs.
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound collisionSound) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionSound = collisionSound;
    }

    /**
     * Updates the ball's position and velocity.
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);

        collisionSound.play();
        collisionCounter++;

        Vector2 newVel = getVelocity().flipped(collision.getNormal());
        setVelocity(newVel);
    }

    /**
     * Get the number of collisions that the ball has had.
     * @return The number of collisions.
     */
    public int getCollisionCounter() {
        return collisionCounter;
    }

}
