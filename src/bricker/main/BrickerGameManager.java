package bricker.main;

import bricker.brick_strategies.BasicCollisionStrategy;
import bricker.gameObjects.Brick;
import bricker.gameObjects.Paddle;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import bricker.gameObjects.Ball;

import java.util.Random;

public class BrickerGameManager extends GameManager {

    /* Static constants */
    private static final float BALL_SPEED = 200;
    private static final float BALL_DIMS = 20;
    private static final float PADDLE_WIDTH = 100;
    private static final float PADDLE_HEIGHT = 15;
    private static final float PADDLE_Y_OFFSET = 30;
    private static final float BORDER_WIDTH = 3;
    private static final float DEFAULT_BRICK_HEIGHT = 15;

    /* Constants */
    private final float DEFAULT_NUM_OF_BRICK_ROWS;
    private final float DEFAULT_NUM_OF_BRICK_IN_ROW;
    private final Vector2 windowDimensions;

    /* Fields */

    /* Constructor */

    /**
     * Construct a new BrickerGameManager instance.
     * @param windowTitle The title of the window.
     * @param windowDimensions The dimensions of the window.
     * @param numOfBrickRows The number of brick rows to create.
     * @param numOfBrickInRow The number of bricks in each row.
     */
    public BrickerGameManager(
            String windowTitle, Vector2 windowDimensions, float numOfBrickRows, float numOfBrickInRow
    ) {
        super(windowTitle, windowDimensions);
        this.windowDimensions = windowDimensions;
        if (numOfBrickRows == -1) {
            DEFAULT_NUM_OF_BRICK_ROWS = 7;
        } else {
            DEFAULT_NUM_OF_BRICK_ROWS = numOfBrickRows;
        }
        if (numOfBrickInRow == -1) {
            DEFAULT_NUM_OF_BRICK_IN_ROW = 8;
        } else {
            DEFAULT_NUM_OF_BRICK_IN_ROW = numOfBrickInRow;
        }
    }

    /* Methods */

    /**
     * Create the borders of the game window.
     */
    private void createBorders() {
        @SuppressWarnings("SuspiciousNameCombination") Vector2[] borderLocations = {
                Vector2.ZERO, Vector2.of(BORDER_WIDTH, windowDimensions.y()),
                Vector2.ZERO, Vector2.of(windowDimensions.x(), BORDER_WIDTH),
                Vector2.of(windowDimensions.x() - BORDER_WIDTH, 0), Vector2.of(BORDER_WIDTH, windowDimensions.y())
        };
        for (int i = 0; i < borderLocations.length - 1; i++) {
            GameObject border = new GameObject(
                    borderLocations[i], borderLocations[i + 1], null
            );
            gameObjects().addGameObject(border, Layer.STATIC_OBJECTS);
        }
    }

    /**
     * Create the background of the game window.
     * @param imageReader The image reader to use.
     */
    private void createBackground(ImageReader imageReader) {
        Renderable background = imageReader.readImage("assets/DARK_BG2_small.jpeg", false);
        GameObject backgroundObject = new GameObject(
                Vector2.ZERO, Vector2.of(windowDimensions.x(), windowDimensions.y()), background

        );
        backgroundObject.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(backgroundObject, Layer.BACKGROUND);
    }

    /**
     * Create the ball GameObject.
     * @param imageReader The image reader to use.
     */
    private void createBall(ImageReader imageReader, SoundReader soundReader) {
        Renderable ballImage = imageReader.readImage("assets/ball.png", true);
        Sound collisionSound = soundReader.readSound("assets/blop.wav");
        GameObject ball = new Ball(Vector2.ZERO, new Vector2(BALL_DIMS, BALL_DIMS), ballImage, collisionSound);
        float ballVelY = BALL_SPEED;
        float ballVelX =BALL_SPEED;
        Random rand = new Random();
        if (rand.nextBoolean()) {
            ballVelX = -ballVelX;
        }
        if (rand.nextBoolean()) {
            ballVelY = -ballVelY;
        }
        ball.setVelocity(new Vector2(ballVelX, ballVelY));
        ball.setCenter(windowDimensions.mult(0.5f));
        gameObjects().addGameObject(ball);
    }

    /**
     * Create the user paddle GameObject.
     * @param imageReader The image reader to read paddleImage with.
     */
    private void createUserPaddle(
            ImageReader imageReader, UserInputListener inputListener
    ) {
        Renderable paddleImage = imageReader.readImage("assets/paddle.png", true);
        GameObject paddle = new Paddle(
                Vector2.ZERO, new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT), paddleImage, inputListener, windowDimensions
        );
        paddle.setCenter(new Vector2(windowDimensions.x() / 2, windowDimensions.y() - PADDLE_Y_OFFSET));
        gameObjects().addGameObject(paddle);
    }

    /**
     * Create a brick GameObject.
     * @param brickImage The image to use for the brick.
     */
    private void createBrick(Renderable brickImage) {
        Brick brick = new Brick(
                Vector2.ZERO,
                new Vector2(windowDimensions.x(), DEFAULT_BRICK_HEIGHT),
                brickImage,
                new BasicCollisionStrategy(this)
        );
        gameObjects().addGameObject(brick);
    }

    private void createBricks(ImageReader imageReader) {
        Renderable brickImage = imageReader.readImage("assets/brick.png", true);
    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        // windowController.setTargetFramerate(100);

        createBall(imageReader, soundReader);

        createUserPaddle(imageReader, inputListener);

        createBorders();

        createBackground(imageReader);

        createBricks(imageReader);
    }

    /**
     * Remove a GameObject from the game.
     * @param gameObject The GameObject to remove.
     */
    public void removeGameObject(GameObject gameObject) {
        gameObjects().removeGameObject(gameObject);
    }

    public static void main(String[] args) {
        BrickerGameManager gameManager = new BrickerGameManager(
                "Bricker", new Vector2(700, 500), -1, -1
        );
        gameManager.run();
    }

}
