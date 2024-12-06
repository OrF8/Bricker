package bricker.main;

import bricker.brick_strategies.StrategyFactory;
import bricker.gameobjects.*;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.ImageRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

/**
 * Represents the game manager for the Bricker game.
 */
public class BrickerGameManager extends GameManager {

    /** The tag for the game ball. */
    public static final String BALL_TAG = "Ball";
    /** The tag for the user paddle. */
    public static final String PADDLE_TAG = "Paddle";
    /** The tag for the extra heart. */
    public static final String EXTRA_HEART_TAG = "ExtraHeart";

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
    private static final int DOUBLE_FACTOR = 2; /* The factor to double the value by */
    private static final float THREE_QUARTER_FACTOR = 0.75f; /* The factor to multiply by 0.75 */
    private static final String PUCK_TAG = "BallPuck"; /* The tag for the puck ball */
    /* The path to the ball collision sound */
    private static final String BALL_COLLISION_SOUND_PATH = "assets/blop.wav";
    /* The path to the puck ball image */
    private static final String PUCK_IMAGE_PATH = "assets/mockBall.png";
    /* The path to the paddle image */
    private static final String PADDLE_IMAGE_PATH = "assets/paddle.png";
    /* The path to the heart image */
    private static final String HEART_IMAGE_PATH = "assets/heart.png";
    /* The tag for the additional paddle */
    private static final String ADDITIONAL_PADDLE_TAG = "AdditionalPaddle";
    /* The factor to multiply the speed by in super mode */
    private static final float TURBO_SPEED_FACTOR = 1.4f;
    private static final String BALL_IMAGE_PATH = "assets/ball.png"; /* The path to the ball image */
    /* The path to the red ball image */
    private static final String RED_BALL_IMAGE_PATH = "assets/redBall.png";
    /* The spacing between the bricks in the y direction */
    private static final float BRICK_Y_SPACING = 1.04f;
    private static final float EXTRA_HEART_SPEED = 100; /* The speed of the extra heart */
    private static final int MAX_NUM_OF_LIVES = 4; /* The maximum number of lives the user can have */

    /* Constants fields */
    private final float brickWidth; /* The width of the bricks */
    private final Vector2 windowDimensions; /* The dimensions of the window */
    private PlayerLife playerLife; /* A renderer to visualize the player's life */

    /* Fields */
    private Renderable heartImage; /* The heart image to show the user lives */
    private ImageReader imageReader; /* The image reader */
    private UserInputListener inputListener; /* The input listener */
    private WindowController windowController; /* The window controller */
    private int numOfBrickRows = 7; /* The number of brick rows */
    private int numOfBricksInRow = 8; /* The number of bricks in each row */
    private Ball ball; /* The game ball */
    private int numOfLives; /* The number of lives the user has */
    private int lives; /* The number of lives the user has left */
    private Counter brickCounter; /* The counter for the bricks */
    private boolean[][] bricks; /* The bricks in the game */
    private Sound ballCollisionSound; /* The sound to play when a collision occurs */
    private Renderable puckImage; /* The image to use for the puck */
    private ImageRenderable paddleImage; /* The image to use for the paddle */
    private ArrayList<Ball> pucks; /* The puck balls */
    private AdditionalPaddle additionalPaddle; /* The additional paddle */
    private Vector2 additionalPaddleTopLeftCorner; /* The top left corner of the additional paddle */
    private int additionalPaddleHitCount; /* The number of times the additional paddle has been hit */
    private Renderable ballImage; /* The image to use for the ball */
    private Renderable redBallImage; /* The image to use for the red ball */
    private boolean turboMode; /* Whether the game is in turbo mode */
    private Counter turboCollisions; /* The counter for the turbo collisions */
    private int turboCollisionsCount; /* The number of turbo collisions */
    private ArrayList<Heart> hearts; /* The extra hearts */

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
        turboMode = false;
        additionalPaddle = AdditionalPaddle.resetInstance();
        additionalPaddleTopLeftCorner = null;
        additionalPaddleHitCount = 0;
        pucks = new ArrayList<>();
        hearts = new ArrayList<>();
        this.bricks = new boolean[this.numOfBrickRows][this.numOfBricksInRow];
        this.numOfLives = DEFAULT_NUM_OF_LIVES;
        this.lives = DEFAULT_NUM_OF_LIVES;
        this.brickCounter = new Counter(this.numOfBrickRows * this.numOfBricksInRow);
        this.turboCollisions = new Counter();
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
        Ball ball = new Ball(Vector2.ZERO, new Vector2(BALL_DIMS, BALL_DIMS), ballImage, ballCollisionSound);
        ball.setTag(BALL_TAG);
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
        Paddle paddle = new Paddle(
                Vector2.ZERO,
                Vector2.of(PADDLE_WIDTH, PADDLE_HEIGHT),
                paddleImage,
                inputListener,
                windowDimensions
        );
        paddle.setCenter(
                Vector2.of(
                        windowDimensions.x() / DIV_FACTOR_TO_CENTER,
                        windowDimensions.y() - PADDLE_Y_OFFSET
                )
            );
        paddle.setTag(PADDLE_TAG);
        gameObjects().addGameObject(paddle);
    }

    /**
     * Create a brick game object.
     * @param brickImage The image to use for the brick.
     * @param row The row of the brick.
     * @param col The column of the brick.
     */
    private void createBrick(Renderable brickImage, int row, int col) {
        float x = BORDER_WIDTH + col * (brickWidth + BRICK_SPACING);
        float y = BORDER_WIDTH + row * (DEFAULT_BRICK_HEIGHT + BRICK_SPACING) * BRICK_Y_SPACING;
        Brick brick = new Brick(
                Vector2.of(x, y),
                Vector2.of(brickWidth, DEFAULT_BRICK_HEIGHT),
                brickImage,
                StrategyFactory.createStrategy(this)
        );
        brick.setTag(Integer.toString(row) + ',' + col);
        gameObjects().addGameObject(brick, BRICK_LAYER);
    }

    /**
     * Create the bricks for the game.
     */
    private void createBricks() {
        Renderable brickImage = imageReader.readImage("assets/brick.png", true);
        for (int row = 0; row < numOfBrickRows; row++) {
            for (int col = 0; col < numOfBricksInRow; col++) {
                if (!bricks[row][col]) {
                    createBrick(brickImage, row, col);
                }
            }
        }
    }

    /**
     * Show the life data on the window.
     */
    private void showLifeData() {
        if (playerLife == null) {
            playerLife = new PlayerLife(windowDimensions, heartImage, this);
        }
        playerLife.showNumeric(numOfLives);
        playerLife.showHearts(numOfLives);
    }

    /**
     * Check if the user has lost a life and resets the game if he has.
     */
    private void checkLostLife() {
        if (lives != numOfLives) {
            numOfLives--;
            if (additionalPaddle != null) {
                additionalPaddleHitCount = additionalPaddle.getHitCount();
                additionalPaddleTopLeftCorner = additionalPaddle.getTopLeftCorner();
            } else {
                additionalPaddleHitCount = 0;
                additionalPaddleTopLeftCorner = null;
            }
            if (turboMode) {
                turboCollisions.reset();
                turboCollisions.increaseBy(turboCollisionsCount - ball.getCollisionCounter());
            }
            windowController.resetGame();
        }
    }

    /**
     * Return the number of lives the user has left.
     * @param ballHeight The height of the ball.
     * @return The number of lives the user has left.
     */
    private int userLives(float ballHeight) {
        return ballHeight > windowDimensions.y() ? --lives : lives;
    }

    /**
     * Checks if the game has ended.
     * If it ended, asks for the user if he wants to play again or not.
     */
    private void checkForGameEnd() {
        float ballHeight = ball.getCenter().y();
        String prompt = "";
        if (userLives(ballHeight) == 0) { // User has no life left
            prompt = "You Lose!";
        }
        checkLostLife();
        if (brickCounter.value() == 0) { // User has won
            prompt = "You Win!";
        } else if (inputListener.isKeyPressed(KeyEvent.VK_W)) { // User pressed W
            prompt = "You Win!";
        }
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
     * Check if a puck ball or an extra heart is out of the game window.
     * If it is, remove it from the game.
     */
    private void checkForObjectsOut() {
        for (GameObject gameObject : gameObjects()) {
            String tag = gameObject.getTag();
            if (tag.equals(PUCK_TAG)) {
                Vector2 center = gameObject.getCenter();
                if (center.y() > windowDimensions.y()) {
                    removeGameObject(gameObject, Layer.DEFAULT);
                    pucks.remove(gameObject);
                }
            } else if (tag.equals(EXTRA_HEART_TAG)) {
                Vector2 center = gameObject.getCenter();
                if (center.y() > windowDimensions.y()) {
                    removeGameObject(gameObject, Layer.DEFAULT);
                    hearts.remove(gameObject);
                }
            }
        }
    }

    /**
     * Check if the additional paddle has been hit four times.
     * If it has, remove it from the game.
     */
    private void checkAdditionalPaddle() {
        if (additionalPaddle != null && additionalPaddle.getHitCount() == AdditionalPaddle.MAX_HIT_COUNT) {
            removeGameObject(additionalPaddle, Layer.DEFAULT);
            additionalPaddle = AdditionalPaddle.resetInstance();
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
        checkForObjectsOut();
        checkAdditionalPaddle();
        checkTurboMode();
    }

    /**
     * Check if the game is in turboMode.
     * If it is, exit turboMode if ball has hit six objects in turbo mode.
     */
    private void checkTurboMode() {
        if (turboMode) {
            if (ball.getCollisionCounter() - turboCollisionsCount >= 6) {
                turboMode = false;
                ball.setVelocity(ball.getVelocity().mult(1 / TURBO_SPEED_FACTOR));
                ball.renderer().setRenderable(ballImage);
            }
        }
    }

    /**
     * Remove a GameObject from the game.
     * @param gameObject The GameObject to remove.
     * @param layer The layer to remove the GameObject from.
     */
    private boolean removeGameObject(GameObject gameObject, int layer) {
        return gameObjects().removeGameObject(gameObject, layer);
    }

    /**
     * Remove a GameObject from the layer it is in.
     * @param gameObject The GameObject to remove.
     * @param objType The type of the GameObject.
     */
    public void removeGameObject(GameObject gameObject, GameObjects objType) {
        if (objType == GameObjects.BRICK) {
            boolean result = removeGameObject(gameObject, BRICK_LAYER);
            if (result) { // If the brick was removed
                brickCounter.decrement();
                // Mark the brick as removed
                String tag = gameObject.getTag();
                String[] tagParts = tag.split(",");
                int i = Integer.parseInt(tagParts[0]);
                int j = Integer.parseInt(tagParts[1]);
                bricks[i][j] = true;
            }
        } else if (objType == GameObjects.LIFE) {
            removeGameObject(gameObject, Layer.UI);
        } else {
            removeGameObject(gameObject, Layer.DEFAULT);
        }
    }

    /**
     * Add a GameObject to the game.
     * @param gameObject The GameObject to add.
     * @param layer The layer to add the GameObject to.
     */
    public void addGameObject(GameObject gameObject, int layer) {
        gameObjects().addGameObject(gameObject, layer);
    }

    /**
     * Create two puck balls when a brick is removed.
     * @param brickCenter The center of the brick that was removed.
     * @param numOfPucks The number of pucks to create.
     */
    public void createPucks(Vector2 brickCenter, int numOfPucks) {
        Random rand = new Random();
        for (int i = 0; i < numOfPucks; i++) {
            Ball puck = new Ball(
                    Vector2.ZERO,
                    Vector2.of (BALL_DIMS * THREE_QUARTER_FACTOR, BALL_DIMS * THREE_QUARTER_FACTOR),
                    puckImage,
                    ballCollisionSound
            );
            puck.setTag(PUCK_TAG);
            puck.setCenter(brickCenter);

            // Randomize the direction of the ball
            double angle = rand.nextDouble() * Math.PI;
            float velocityX = (float) Math.cos(angle) * BALL_SPEED;
            float velocityY = (float) Math.sin(angle) * BALL_SPEED;
            puck.setVelocity(Vector2.of(velocityX, velocityY));

            pucks.add(puck);

            gameObjects().addGameObject(puck);
        }
    }

    /**
     * Creates a falling heart when a brick is removed.
     * The heart is falling from the center of the brick.
     * The user must collect the heart to gain an additional life.
     * @param brickCenter The center of the brick that was removed.
     */
    public void createFallingHeart(Vector2 brickCenter) {
        Heart heart = new Heart(
                brickCenter, Vector2.of(PlayerLife.HEART_DIMS, PlayerLife.HEART_DIMS), heartImage, this
        );
        heart.setTag(EXTRA_HEART_TAG);
        heart.setVelocity(Vector2.DOWN.mult(EXTRA_HEART_SPEED));
        hearts.add(heart);
        gameObjects().addGameObject(heart);
    }

    /**
     * Add a life to the user if the current number of lives is lower than the maximum number of lives.
     * @param heart The heart that collided with the paddle.
     */
    public void addLife(Heart heart) {
        if (numOfLives < MAX_NUM_OF_LIVES) {
            numOfLives++;
            lives++;
            showLifeData();
        }
        removeGameObject(heart, Layer.DEFAULT);
        hearts.remove(heart);
    }

    /**
     * Creates the additional paddle if and only if it does not already exist.
     * @param topLeftCorner The top left corner of the additional paddle.
     * @param hitCount The number of times the additional paddle has been hit.
     */
    private void createAdditionalPaddle(Vector2 topLeftCorner, int hitCount) {
        additionalPaddle = AdditionalPaddle.getInstance(
                topLeftCorner, Vector2.of(PADDLE_WIDTH, PADDLE_HEIGHT),
                paddleImage, inputListener, windowDimensions, hitCount
        );
        additionalPaddle.setTag(ADDITIONAL_PADDLE_TAG);
        // Since ModifiableList does not allow duplicates, we can safely add the paddle
        gameObjects().addGameObject(additionalPaddle);
    }


    /**
     * Creates an additional paddle when a brick is removed.
     */
    public void createAdditionalPaddle() {
        Vector2 topLeftCorner = Vector2.of(
                windowDimensions.x() / DIV_FACTOR_TO_CENTER,
                windowDimensions.y() / DIV_FACTOR_TO_CENTER
        );
        createAdditionalPaddle(topLeftCorner, 0);
    }

    /**
     * Initialize the pucks.
     */
    private void initPucks() {
        for (Ball puck : pucks) {
            gameObjects().addGameObject(puck);
        }
    }

    /**
     * Initialize the hearts.
     */
    private void initHearts() {
        for (Heart heart : hearts) {
            gameObjects().addGameObject(heart);
        }
    }

    /**
     * Switch to turbo mode.
     */
    public void switchToTurboMode() {
        if (!turboMode) {
            turboMode = true;
            // +1 to disregard to current collision
            turboCollisionsCount = ball.getCollisionCounter() + 1;
            ball.setVelocity(ball.getVelocity().mult(TURBO_SPEED_FACTOR));
            ball.renderer().setRenderable(redBallImage);
        }
    }

    /**
     * Initialize the additional paddle if it was rendered before the reset.
     */
    private void initAdditionalPaddle() {
        if (additionalPaddleTopLeftCorner != null) { // If the additional paddle was rendered before the reset
            additionalPaddle = AdditionalPaddle.resetInstance();
            createAdditionalPaddle(additionalPaddleTopLeftCorner, additionalPaddleHitCount);
        }
    }

    /**
     * Initialize turbo mode if it was active before the reset.
     */
    private void initTurboMode() {
        if (turboMode) {
            ball.setVelocity(ball.getVelocity().mult(TURBO_SPEED_FACTOR));
            ball.renderer().setRenderable(redBallImage);
            turboCollisionsCount = turboCollisions.value();
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
        this.inputListener = inputListener;
        this.windowController = windowController;

        this.ballCollisionSound = soundReader.readSound(BALL_COLLISION_SOUND_PATH);
        this.puckImage = imageReader.readImage(PUCK_IMAGE_PATH, true);
        this.paddleImage = imageReader.readImage(PADDLE_IMAGE_PATH, true);
        this.heartImage = imageReader.readImage(HEART_IMAGE_PATH, true);
        this.ballImage = imageReader.readImage(BALL_IMAGE_PATH, true);
        this.redBallImage = imageReader.readImage(RED_BALL_IMAGE_PATH, true);

        /* Create essential game objects */
        createBall();
        createUserPaddle();
        createBorders();
        createBackground();
        initPucks();
        initAdditionalPaddle();
        initTurboMode();
        initHearts();
        createBricks();
        showLifeData();
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
