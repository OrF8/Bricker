package bricker.gameobjects;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * A heart GameObject that gives the user an extra life when collected.
 */
public class Heart extends GameObject {

    private final BrickerGameManager gameManager;

    /**
     * Construct a new Heart instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param gameManager   The game manager that manages the game the heart is used in.
     */
    public Heart(
            Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, BrickerGameManager gameManager
    ) {
        super(topLeftCorner, dimensions, renderable);
        this.gameManager = gameManager;
    }

    /**
     * Returns true if this GameObject should collide with the other GameObject.
     * @param other The other GameObject.
     * @return True if this Heart should collide with the other GameObject.
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        if (this.getTag().equals(BrickerGameManager.EXTRA_HEART_TAG)) {
            return other.getTag().equals(BrickerGameManager.PADDLE_TAG);
        }
        return super.shouldCollideWith(other);
    }

    /**
     * Called when a collision occurs with the user paddle.
     * Adds a life to the user.
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (this.getTag().equals(BrickerGameManager.EXTRA_HEART_TAG)) {
            gameManager.addLife(this);
        }
    }
}
