package bricker.main;

import bricker.brick_strategies.BasicCollisionStrategy;
import bricker.gameObjects.Brick;
import bricker.gameObjects.GameObjects;
import bricker.gameObjects.Paddle;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import bricker.gameObjects.Ball;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * Represents the game manager for the Bricker game.
 */
public class BrickerGameManager extends GameManager {

    /* Static constant fields */
    private static final float BALL_SPEED = 200; /* The speed of the game ball */
    private static final float BALL_DIMS = 20; /* The dimensions of the game ball (BALL_DIMS x BALL_DIMS)*/
    private static final float PADDLE_WIDTH = 100; /* The width of the user paddle */
    private static final float PADDLE_HEIGHT = 15; /* The height of the user paddle */
    private static final float PADDLE_Y_OFFSET = 30; /* The y offset of the user paddle */
    private static final float BORDER_WIDTH = 5; /* The width of the borders */
    private static final float DEFAULT_BRICK_HEIGHT = 15; /* The default height of the bricks */
    private static final float BRICK_SPACING = 1.5f; /* The spacing between the bricks */
    private static final int BRICK_LAYER = Layer.STATIC_OBJECTS; /* The layer the bricks are placed in */
    private static final int NO_INPUT_VALUE = -1; /* The value to use when no input is given */
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
    /* The y offset of the numeric representation of the lives */
    private static final float NUMERIC_Y_OFFSET = 27;
    private static final float NUMERIC_DIMS = 20; /* The dimensions of the numeric representation */
    private static final int DOUBLE_FACTOR = 2; /* The factor to double the value by */

    /* Constants fields */
    private final float brickWidth; /* The width of the bricks */
    private final Vector2 windowDimensions; /* The dimensions of the window */

    /* Fields */
    private Renderable heart; /* The heart image to show the user lives */
    private ImageReader imageReader; /* The image reader */
    private SoundReader soundReader; /* The sound reader */
    private UserInputListener inputListener; /* The input listener */
    private WindowController windowController; /* The window controller */
    private int numOfBrickRows = 7; /* The number of brick rows */
    private int numOfBricksInRow = 8; /* The number of bricks in each row */
    private Ball ball; /* The game ball */
    private int numOfLives; /* The number of lives the user has */
    private int lives; /* The number of lives the user has left */
    private Counter brickCounter; /* The counter for the bricks */
    private boolean[][] bricks; /* The bricks in the game */

    /* Constructor */
    /**
     * Construct a new BrickerGameManager instance.
     * @param windowTitle The title of the window.
     * @param windowDimensions The dimensions of the window.
     * @param numOfBrickRows The number of brick rows to create.
     * @param numOfBrickInRow The number of bricks in each row.
     */
    public BrickerGameManager(
            String windowTitle, Vector2 windowDimensions, int numOfBrickRows, int numOfBrickInRow
    ) {
        super(windowTitle, windowDimensions);
        this.windowDimensions = windowDimensions;
        if (numOfBrickRows != NO_INPUT_VALUE) { // If the number of brick rows is given
            this.numOfBrickRows = numOfBrickRows;
        }
        if (numOfBrickInRow != NO_INPUT_VALUE) { // If the number of bricks in a row is given
            this.numOfBricksInRow = numOfBrickInRow;
        }
        /* Calculate the width of the bricks based on the number of bricks in a row,
         * the brick spacing and the window dimensions.
         */
        this.brickWidth = (
                (windowDimensions.x() - DOUBLE_FACTOR * BORDER_WIDTH - (this.numOfBricksInRow - 1) *
                        BRICK_SPACING) / this.numOfBricksInRow
        );
        initializeBeforeGameInit();
    }

    /* Methods */

    /**
     * Initializes important fields before the game is initialized.
     */
    private void initializeBeforeGameInit() {
        this.bricks = new boolean[this.numOfBrickRows][this.numOfBricksInRow];
        this.numOfLives = DEFAULT_NUM_OF_LIVES;
        this.lives = DEFAULT_NUM_OF_LIVES;
        this.brickCounter = new Counter(this.numOfBrickRows * this.numOfBricksInRow);
    }

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
     */
    private void createBackground() {
        Renderable background = imageReader.readImage("assets/DARK_BG2_small.jpeg", false);
        GameObject backgroundObject = new GameObject(
                Vector2.ZERO, Vector2.of(windowDimensions.x(), windowDimensions.y()), background

        );
        backgroundObject.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(backgroundObject, Layer.BACKGROUND);
    }

    /**
     * Create the ball GameObject.
     */
    private void createBall() {
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
     */
    private void createUserPaddle() {
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
        brick.setTag(Integer.toString(i) + ',' + j);
        gameObjects().addGameObject(brick, BRICK_LAYER);
    }

    /**
     * Create the bricks for the game.
     */
    private void createBricks() {
        Renderable brickImage = imageReader.readImage("assets/brick.png", true);
        for (int i = 0; i < numOfBrickRows; i++) {
            for (int j = 0; j < numOfBricksInRow; j++) {
                if (!bricks[i][j]) {
                    createBrick(brickImage, i, j);
                }
            }
        }
    }

    /**
     * Initialize the game.
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     *                    See its documentation for help.
     * @param soundReader Contains a single method: readSound, which reads a wav file from disk.
     *                    See its documentation for help.
     * @param inputListener Contains a single method: isKeyPressed, which returns whether
     *                      a given key is currently pressed by the user or not.
     *                      See its documentation for help.
     * @param windowController Contains an array of helpful, self-explanatory methods concerning the window.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.inputListener = inputListener;
        this.windowController = windowController;

        this.heart = imageReader.readImage("assets/heart.png", true);

        // windowController.setTargetFramerate(100);

        /* Create essential game objects */
        createBall();
        createUserPaddle();
        createBorders();
        createBackground();
        createBricks();
        showLifeData();
    }

    /**
     * Show the life data on the window.
     */
    private void showLifeData() {
        showHearts();
        showNumeric();
    }

    /**
     * Check if the user has lost.
     * @param ballHeight The height of the ball.
     * @return The number of lives the user has left.
     */
    private int userLives(float ballHeight) {
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
            gameObjects().addGameObject(heartObject, Layer.UI);
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
        gameObjects().addGameObject(textObject, Layer.UI);
    }

    /**
     * Checks if the game has ended.
     * If it ended, asks for the user if he wants to play again or not.
     */
    private void checkForGameEnd() {
        float ballHeight = ball.getCenter().y();
        String prompt = "";
        int userLives = userLives(ballHeight);
        if (userLives == 0) { // User has no life left
            prompt = "You Lose!";
        } else if (userLives != numOfLives) { // User has lost a life
            numOfLives--;
            showLifeData();
            windowController.resetGame();
        }
        if (brickCounter.value() == 0) { // User has won
            prompt = "You Win!";
        } else if (inputListener.isKeyPressed(KeyEvent.VK_W)) {
            prompt = "You Win!";
        }
        System.out.println(brickCounter.value());
        if (!prompt.isEmpty()) {
            prompt = prompt.concat(" Play again?");
            if (windowController.openYesNoDialog(prompt)) { // == true if the user wants to play again
                initializeBeforeGameInit();
                windowController.resetGame();
            } else {
                windowController.closeWindow();
            }
        }
    }

    /**
     * Update the game state.
     * @param deltaTime The time, in seconds, that's passed since
     *                  the last invocation of this method(i.e., since the last frame).
     */
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
    private void removeGameObject(GameObject gameObject, int layer) {
        gameObjects().removeGameObject(gameObject, layer);
    }

    /**
     * Remove a GameObject from the layer it is in.
     * @param gameObject The GameObject to remove.
     * @param objType The type of the GameObject.
     */
    public void removeGameObject(GameObject gameObject, GameObjects objType) {
        if (objType == GameObjects.BRICK) {
            removeGameObject(gameObject, BRICK_LAYER);

            /* Mark the brick as removed */
            String tag = gameObject.getTag();
            if (!tag.isEmpty()) {
                String[] tagParts = tag.split(",");
                int i = Integer.parseInt(tagParts[0]);
                int j = Integer.parseInt(tagParts[1]);
                bricks[i][j] = true;
            }

            brickCounter.decrement();
        } else {
            removeGameObject(gameObject, Layer.DEFAULT);
        }
    }

    /**
     * Runs the game with the user input.
     * @param args The arguments to run the game with.
     */
    public static void main(String[] args) {
        int numOfBrickRows = NO_INPUT_VALUE;
        int numOfBrickInRow = NO_INPUT_VALUE;
        if (args.length == FULL_INPUT_LENGTH) {
            numOfBrickInRow = Integer.parseInt(args[0]);
            numOfBrickRows = Integer.parseInt(args[1]);
        } else if (args.length == 1) {
            numOfBrickInRow = Integer.parseInt(args[0]);
        }
        BrickerGameManager gameManager = new BrickerGameManager(
                "Bricker", DEFAULT_WINDOW_DIMENSIONS, numOfBrickRows, numOfBrickInRow
        );
        gameManager.run();
    }

}
