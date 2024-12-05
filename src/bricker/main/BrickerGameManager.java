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
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import bricker.gameObjects.Ball;

import java.awt.*;
import java.util.Random;

public class BrickerGameManager extends GameManager {

    /* Static constants */
    public static final int BRICK_LAYER = Layer.STATIC_OBJECTS; /* The layer that the bricks are placed in */

    /* Static constant fields */
    private static final float BALL_SPEED = 200; /* The speed of the game ball */
    private static final float BALL_DIMS = 20; /* The dimensions of the game ball (BALL_DIMS x BALL_DIMS)*/
    private static final float PADDLE_WIDTH = 100; /* The width of the user paddle */
    private static final float PADDLE_HEIGHT = 15; /* The height of the user paddle */
    private static final float PADDLE_Y_OFFSET = 30; /* The y offset of the user paddle */
    private static final float BORDER_WIDTH = 5; /* The width of the borders */
    private static final float DEFAULT_BRICK_HEIGHT = 15; /* The default height of the bricks */
    private static final float BRICK_SPACING = 1.5f; /* The spacing between the bricks */
    private static final float NO_INPUT_VALUE = -1; /* The value to use when no input is given */
    /* The factor to multiply the window dimensions by to center */
    private static final float MULT_FACTOR_TO_CENTER = 0.5f;
    private static final float FULL_INPUT_LENGTH = 2; /* The length of the full input */
    /* The default window dimensions */
    private static final Vector2 DEFAULT_WINDOW_DIMENSIONS = Vector2.of(700, 500);
    private static final int DEFAULT_NUM_OF_LIVES = 3; /* The number of lives the user has */
    /* The factor to divide the window dimensions by to center */
    private static final int DIV_FACTOR_TO_CENTER = 2;
    private static final float HEART_DIMS = 15; /* The dimensions of a heart (HEART_DIMS x HEART_DIMES)*/
    private static final float HEART_Y_OFFSET = 20; /* The y offset of the heart */
    private static final float HEART_X_OFFSET = 0.04f; /* The x offset of the heart */
    private static final float HEART_BORDER_OFFSET = 30; /* The border offset of the heart */
    private static final int HEARTS_LAYER = -150; /* The layer that the hearts are placed in */
    /* The y offset of the numeric representation of the lives */
    private static final float NUMERIC_Y_OFFSET = 27;
    private static final float NUMERIC_DIMS = 20; /* The dimensions of the numeric representation */

    /* Constants fields */
    private final float brickWidth; /* The width of the bricks */
    private final Vector2 windowDimensions; /* The dimensions of the window */

    /* Fields */
    private Renderable heart; /* The heart image to show the user lives */
    private WindowController windowController; /* The window controller */
    private float NUM_OF_BRICK_ROWS = 7; /* The number of brick rows */
    private float NUM_OF_BRICKS_IN_ROW = 8; /* The number of bricks in each row */
    private Ball ball; /* The game ball */
    private int numOfLives = DEFAULT_NUM_OF_LIVES; /* The number of lives the user has */
    private int lives = DEFAULT_NUM_OF_LIVES; /* The number of lives the user has left */

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
        if (numOfBrickRows != NO_INPUT_VALUE) { // If the number of brick rows is given
            NUM_OF_BRICK_ROWS = numOfBrickRows;
        }
        if (numOfBrickInRow != NO_INPUT_VALUE) { // If the number of bricks in a row is given
            NUM_OF_BRICKS_IN_ROW = numOfBrickInRow;
        }
        /* Calculate the width of the bricks based on the number of bricks in a row,
         * the brick spacing and the window dimensions.
         */
        this.brickWidth = (
                (windowDimensions.x() - 2 * BORDER_WIDTH - (NUM_OF_BRICKS_IN_ROW - 1) *
                        BRICK_SPACING) / NUM_OF_BRICKS_IN_ROW
        );
    }

    /* Methods */

    /**
     * Create the borders of the game window.
     */
    private void createBorders() {
        Vector2[] borderLocations = {
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
        Ball ball = new Ball(Vector2.ZERO, new Vector2(BALL_DIMS, BALL_DIMS), ballImage, collisionSound);
        this.ball = ball;
        float ballVelY = BALL_SPEED;
        float ballVelX =BALL_SPEED;
        Random rand = new Random();
        // Randomize the direction of the ball
        if (rand.nextBoolean()) {
            ballVelX = -ballVelX;
        }
        if (rand.nextBoolean()) {
            ballVelY = -ballVelY;
        }
        ball.setVelocity(Vector2.of(ballVelX, ballVelY));
        ball.setCenter(windowDimensions.mult(MULT_FACTOR_TO_CENTER));
        gameObjects().addGameObject(ball);
    }

    /**
     * Create the user paddle GameObject.
     * @param imageReader The image reader to read paddleImage with.
     */
    private void createUserPaddle(ImageReader imageReader, UserInputListener inputListener) {
        Renderable paddleImage = imageReader.readImage("assets/paddle.png", true);
        Paddle paddle = new Paddle(
                Vector2.ZERO,
                Vector2.of(PADDLE_WIDTH, PADDLE_HEIGHT),
                paddleImage,
                inputListener,
                windowDimensions
        );
        paddle.setCenter(Vector2.of(
                windowDimensions.x() / DIV_FACTOR_TO_CENTER, windowDimensions.y() - PADDLE_Y_OFFSET)
        );
        gameObjects().addGameObject(paddle);
    }

    /**
     * Create a brick GameObject.
     * @param brickImage The image to use for the brick.
     * @param i The row of the brick.
     * @param j The column of the brick.
     */
    private void createBrick(Renderable brickImage, int i, int j) {
        float x = BORDER_WIDTH + j * (brickWidth + BRICK_SPACING);
        float y = BORDER_WIDTH + i * (DEFAULT_BRICK_HEIGHT + BRICK_SPACING);
        Brick brick = new Brick(
                Vector2.of(x, y),
                Vector2.of(brickWidth, DEFAULT_BRICK_HEIGHT),
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
                createBrick(brickImage, i, j);
            }
        }
    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        this.heart = imageReader.readImage("assets/heart.png", true);
        this.windowController = windowController;

        // windowController.setTargetFramerate(100);

        createBall(imageReader, soundReader);

        createUserPaddle(imageReader, inputListener);

        createBorders();

        createBackground(imageReader);

        createBricks(imageReader);

        showHearts();

        showNumeric();
    }

    /**
     * Check if the user has lost.
     * @param ballHeight The height of the ball.
     * @return The number of lives the user has left.
     */
    private int hasUserLost(float ballHeight) {
        return ballHeight > windowDimensions.y() ? --lives : lives;
    }

    /**
     * Show numOfLives hearts on the window.
     */
    private void showHearts() {
        for (int i = 0; i < numOfLives; i++) {
            GameObject heartObject = new GameObject(
                    Vector2.of(
                            i * HEART_X_OFFSET * windowDimensions.y() + HEART_BORDER_OFFSET,
                            windowDimensions.y() - HEART_Y_OFFSET
                    ),
                    Vector2.of(HEART_DIMS, HEART_DIMS),
                    heart
            );
            gameObjects().addGameObject(heartObject, HEARTS_LAYER);
        }
    }

    /**
     * Show the numeric representation of the number of lives the user has left.
     */
    private void showNumeric() {
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
        GameObject textObject = new GameObject(
                Vector2.of(HEART_BORDER_OFFSET - NUMERIC_DIMS, windowDimensions.y() - NUMERIC_Y_OFFSET),
                Vector2.of(NUMERIC_DIMS, NUMERIC_DIMS),
                textRenderable
        );
        gameObjects().addGameObject(textObject, HEARTS_LAYER);
    }

    /**
     * If the user has lost a life, reset the game and show the correct number of hearts and lives left.
     */
    private void ifLostALife() {
        numOfLives--;
        showHearts();
        showNumeric();
        windowController.resetGame();
    }

    /**
     * Checks if the game has ended.
     * If it ended, asks for the user if he wants to play again or not.
     */
    private void checkForGameEnd() {
        float ballHeight = ball.getCenter().y();
        String prompt = "";
        int hasUserLost = hasUserLost(ballHeight);
        if (hasUserLost == 0) { // User has no life left
            prompt = "You Lose!";
        } else if (hasUserLost != numOfLives) { // User has lost a life
            ifLostALife();
        }
        if (!prompt.isEmpty()) {
            prompt = prompt.concat(" Play again?");
            if (windowController.openYesNoDialog(prompt)) { // == true if the user wants to play again
                lives = DEFAULT_NUM_OF_LIVES;
                numOfLives = DEFAULT_NUM_OF_LIVES;
                windowController.resetGame();
            } else {
                windowController.closeWindow();
            }
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkForGameEnd();
    }

    /**
     * Remove a GameObject from the game.
     * @param gameObject The GameObject to remove.
     * @param layer The layer to remove the GameObject from.
     */
    public void removeGameObject(GameObject gameObject, int layer) {
        gameObjects().removeGameObject(gameObject, layer);
    }

    /**
     * Remove a GameObject from the default layer of the game.
     * @param gameObject The GameObject to remove.
     */
    public void removeGameObject(GameObject gameObject) {
        removeGameObject(gameObject, Layer.DEFAULT);
    }

    public static void main(String[] args) {
        float numOfBrickRows = NO_INPUT_VALUE;
        float numOfBrickInRow = NO_INPUT_VALUE;
        if (args.length == FULL_INPUT_LENGTH) {
            numOfBrickInRow = Float.parseFloat(args[0]);
            numOfBrickRows = Float.parseFloat(args[1]);
        } else if (args.length == 1) {
            numOfBrickInRow = Float.parseFloat(args[0]);
        }
        BrickerGameManager gameManager = new BrickerGameManager(
                "Bricker", DEFAULT_WINDOW_DIMENSIONS, numOfBrickRows, numOfBrickInRow
        );
        gameManager.run();
    }

}
