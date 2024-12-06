package bricker.gameobjects;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * A class that represents the player's lives.
 */
public class PlayerLife {

    /** The dimensions of a heart (HEART_DIMS x HEART_DIMES)*/
    public static final float HEART_DIMS = 15;

    /* Static constant fields */
    private static final float HEART_Y_OFFSET = -10; /* The y offset of the heart */
    private static final float HEART_X_OFFSET = 0.004f; /* The x offset of the heart */
    private static final float HEART_BORDER_OFFSET = 30; /* The border offset of the heart */
    /* The y offset of the numeric representation of the lives */
    private static final float NUMERIC_Y_OFFSET = 27;
    private static final float NUMERIC_DIMS = 20; /* The dimensions of the numeric representation */

    /* Constants fields */
    private final Vector2 windowDimensions; /* The dimensions of the window */
    private final Renderable heartImage; /* The image to use for the hearts */
    private final BrickerGameManager gameManager; /* The game manager that manages the game */

    /* Fields */
    private GameObject numericDisplay; /* The numeric display of the lives */

    /**
     * Construct a new PlayerLife instance.
     * @param windowDimensions The dimensions of the window, in window coordinates.
     * @param heartImage The image to use for the hearts.
     * @param gameManager The game manager that manages the game the player life is used in.
     */
    public PlayerLife(Vector2 windowDimensions, Renderable heartImage, BrickerGameManager gameManager) {
        this.windowDimensions = windowDimensions;
        this.heartImage = heartImage;
        this.gameManager = gameManager;
    }

    /**
     * Show the player's lives as hearts on the screen.
     * @param numOfLives The number of lives to show.
     */
    public void showHearts(int numOfLives) {
        for (int i = 0; i < numOfLives; i++) {
            Heart heartObject = new Heart(
                    Vector2.of(
                            HEART_BORDER_OFFSET + i * (HEART_DIMS + HEART_X_OFFSET * windowDimensions.y()),
                            windowDimensions.y() - HEART_Y_OFFSET - HEART_BORDER_OFFSET
                    ),
                    Vector2.of(HEART_DIMS, HEART_DIMS),
                    heartImage,
                    gameManager
            );
            gameManager.addGameObject(heartObject, Layer.UI);
        }
    }

    /**
     * Show the player's lives as a number on the screen.
     * @param numOfLives The number of lives to show.
     */
    public void showNumeric(int numOfLives) {
        if (numericDisplay != null) {
            gameManager.removeGameObject(numericDisplay, GameObjects.LIFE);
        }
        TextRenderable textRenderable = new TextRenderable(Integer.toString(numOfLives));
        switch (numOfLives) {
            case 1:
                textRenderable.setColor(Color.RED);
                break;
            case 2:
                textRenderable.setColor(Color.YELLOW);
                break;
            default:
                textRenderable.setColor(Color.GREEN);
                break;
        }
        numericDisplay = new GameObject(
                Vector2.of(HEART_BORDER_OFFSET - NUMERIC_DIMS, windowDimensions.y() - NUMERIC_Y_OFFSET),
                Vector2.of(NUMERIC_DIMS, NUMERIC_DIMS),
                textRenderable
        );
        gameManager.addGameObject(numericDisplay, Layer.UI);
    }
}
