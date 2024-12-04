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
    public static final int BRICK_LAYER = Layer.STATIC_OBJECTS;

    /* Static constant fields */
    private static final float BALL_SPEED = 200;
    private static final float BALL_DIMS = 20;
    private static final float PADDLE_WIDTH = 100;
    private static final float PADDLE_HEIGHT = 15;
    private static final float PADDLE_Y_OFFSET = 30;
    private static final float BORDER_WIDTH = 3;
    private static final float DEFAULT_BRICK_HEIGHT = 15;
    private static final float BRICK_SPACING = 1.5f;

    /* Constants fields */
    private final float NUM_OF_BRICK_ROWS;
    private final float NUM_OF_BRICKS_IN_ROW;
    private final Vector2 windowDimensions;
    private final float brickWidth;

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
            NUM_OF_BRICK_ROWS = 7;
        } else {
            NUM_OF_BRICK_ROWS = numOfBrickRows;
        }
        if (numOfBrickInRow == -1) {
            NUM_OF_BRICKS_IN_ROW = 8;
        } else {
            NUM_OF_BRICKS_IN_ROW = numOfBrickInRow;
        }
        brickWidth = (
                (windowDimensions.x() - (NUM_OF_BRICKS_IN_ROW - 1) * BRICK_SPACING) / NUM_OF_BRICKS_IN_ROW
        );
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
     * @param brickWidth The width of the brick.
     * @param i The row of the brick.
     * @param j The column of the brick.
     */
    private void createBrick(Renderable brickImage, float brickWidth, int i, int j) {
        float x = j * (brickWidth + BRICK_SPACING);
        float y = i * (DEFAULT_BRICK_HEIGHT + BRICK_SPACING);
        Brick brick = new Brick(
                Vector2.of(x, y),
                new Vector2(brickWidth, DEFAULT_BRICK_HEIGHT),
                brickImage,
                new BasicCollisionStrategy(this)
        );
        gameObjects().addGameObject(brick, BRICK_LAYER);
    }

    /**
     * Create the bricks for the game.
     * @param imageReader The image reader to use.
     */
    private void createBricks(ImageReader imageReader) {
        Renderable brickImage = imageReader.readImage("assets/brick.png", true);
        for (int i = 0; i < NUM_OF_BRICK_ROWS; i++) {
            for (int j = 0; j < NUM_OF_BRICKS_IN_ROW; j++) {
                createBrick(brickImage, brickWidth, i, j);
            }
        }
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
     * @param layer The layer to remove the GameObject from.
     */
    public void removeGameObject(GameObject gameObject, int layer) {
        gameObjects().removeGameObject(gameObject, layer);
    }

    public static void main(String[] args) {
        float numOfBrickRows = -1;
        float numOfBrickInRow = -1;
        if (args.length == 2) {
            numOfBrickInRow = Float.parseFloat(args[0]);
            numOfBrickRows = Float.parseFloat(args[1]);
        } else if (args.length == 1) {
            numOfBrickInRow = Float.parseFloat(args[0]);
        }
        BrickerGameManager gameManager = new BrickerGameManager(
                "Bricker", new Vector2(700, 500), numOfBrickRows, numOfBrickInRow
        );
        gameManager.run();
    }

}
