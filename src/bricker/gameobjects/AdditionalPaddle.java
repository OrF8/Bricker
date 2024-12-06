package bricker.gameobjects;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * A singleton class representing an additional paddle in the game.
 */
public class AdditionalPaddle extends Paddle {

    /** The maximum number of hits the additional paddle can take. */
    public static final int MAX_HIT_COUNT = 4;

    private static AdditionalPaddle instance; /* The instance of the AdditionalPaddle. */

    private static int hitCount; /* The number of hits the additional paddle has taken. */

    /**
     * Construct a new Paddle instance.
     *
     * @param topLeftCorner    Position of the paddle, in window coordinates (pixels).
     *                         Note that (0,0) is the top-left corner of the window.
     * @param dimensions       Width and height in window coordinates.
     * @param paddleImage       The renderable representing the object. Can be null, in which case
     *                         the GameObject will not be rendered.
     * @param inputListener    The input listener to use.
     * @param windowDimensions The dimensions of the window, in window coordinates.
     * @param hitCount         The number of hits the additional paddle has taken.
     */
    private AdditionalPaddle(
            Vector2 topLeftCorner, Vector2 dimensions, Renderable paddleImage,
            UserInputListener inputListener, Vector2 windowDimensions, int hitCount
    ) {
        super(topLeftCorner, dimensions, paddleImage, inputListener, windowDimensions);
        AdditionalPaddle.hitCount = hitCount;
    }

    /**
     * Get the instance of the AdditionalPaddle.
     * @param topLeftCorner Position of the paddle, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions Width and height in window coordinates.
     * @param paddleImage The renderable representing the object. Can be null, in which case
     *                    the GameObject will not be rendered.
     * @param inputListener The input listener to use.
     * @param windowDimensions The dimensions of the window, in window coordinates.
     * @param hitCount The number of hits the additional paddle has taken.
     * @return The instance of the AdditionalPaddle.
     */
    public static AdditionalPaddle getInstance(
            Vector2 topLeftCorner, Vector2 dimensions, Renderable paddleImage,
            UserInputListener inputListener, Vector2 windowDimensions, int hitCount
    ) {
        if (instance == null) {
            instance = new AdditionalPaddle(
                    topLeftCorner, dimensions, paddleImage, inputListener, windowDimensions, hitCount
            );
        }
        return instance;
    }

    /**
     * Reset the instance of the AdditionalPaddle.
     * @return null.
     */
    public static AdditionalPaddle resetInstance() {
        instance = null;
        hitCount = 0;
        return null;
    }

    /**
     * Get the number of hits the additional paddle has taken.
     * @return The number of hits the additional paddle has taken.
     */
    public int getHitCount() {
        return hitCount;
    }

    /**
     * Called when a collision occurs with this GameObject.
     * Resets the instance of the AdditionalPaddle when the hit count reaches the maximum.
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        String otherTag = other.getTag();
        if (otherTag.startsWith(BrickerGameManager.BALL_TAG)) {
            hitCount++;
        }
    }
}
