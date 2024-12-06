package bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

/**
 * Represents a paddle in the game.
 */
public class Paddle extends GameObject {

    private static final float MOVEMENT_SPEED = 300;
    private final UserInputListener InputListener;
    private final Vector2 windowDimensions;

    /**
     * Construct a new Paddle instance.
     *
     * @param topLeftCorner Position of the paddle, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param inputListener The input listener to use.
     * @param windowDimensions The dimensions of the window, in window coordinates.
     */
    public Paddle(
            Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, UserInputListener inputListener,
            Vector2 windowDimensions
    ) {
        super(topLeftCorner, dimensions, renderable);
        this.InputListener = inputListener;
        this.windowDimensions = windowDimensions;
    }

    /**
     * Updates the paddle's position and velocity based on user input.
     * @param deltaTime The time elapsed, in seconds, since the last frame.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Vector2 movementDir = Vector2.ZERO;
        if (InputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            movementDir =movementDir.add(Vector2.LEFT);
        }
        if (InputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            movementDir = movementDir.add(Vector2.RIGHT);
        }
        float leftCornerX = this.getTopLeftCorner().x();
        float leftCornerY = this.getTopLeftCorner().y();
        float dimensionX = this.getDimensions().x();
        float windowWidth = windowDimensions.x();
        if (leftCornerX < 0) {
            this.setTopLeftCorner(Vector2.of(0, leftCornerY));
        } else if (leftCornerX + dimensionX > windowWidth) {
            this.setTopLeftCorner(Vector2.of(windowWidth - dimensionX, leftCornerY));
        }
        setVelocity(movementDir.mult(MOVEMENT_SPEED));
    }
}

