package bricker.main;

import danogl.GameManager;
import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class BrickerGameManager extends GameManager {

    public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
        super(windowTitle, windowDimensions);
    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        // Create the ball
        Renderable ballImage = imageReader.readImage("assets/ball.png", true);
        GameObject ball = new GameObject(Vector2.ZERO, new Vector2(20, 20), ballImage);
        ball.setVelocity(Vector2.DOWN.mult(100));

        Vector2 windowDimensions = windowController.getWindowDimensions();
        ball.setCenter(windowDimensions.mult(0.5f));
        gameObjects().addGameObject(ball);

        // Create the paddle
        Renderable paddleImage = imageReader.readImage("assets/paddle.png", true);
        GameObject paddle = new GameObject(Vector2.ZERO, new Vector2(100, 15), paddleImage);
        paddle.setCenter(new Vector2(windowDimensions.x() / 2, windowDimensions.y() - 30));
        gameObjects().addGameObject(paddle);
    }

    public static void main(String[] args) {
        BrickerGameManager gameManager = new BrickerGameManager("Bricker", new Vector2(700, 500));
        gameManager.run();
    }

}
