package bricker.brick_strategies;

import danogl.GameObject;

public class DoubleBehaviorStrategy implements CollisionStrategy {

    private final CollisionStrategy strategy1;
    private final CollisionStrategy strategy2;

    public DoubleBehaviorStrategy(CollisionStrategy strategy1, CollisionStrategy strategy2) {
        this.strategy1 = strategy1;
        this.strategy2 = strategy2;
    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        System.out.println("DoubleBehaviorStrategy.onCollision");
        if (strategy1 != null) {
            System.out.println("Strategy1: " + strategy1.getClass().getSimpleName());
            strategy1.onCollision(thisObj, otherObj);
        }
        if (strategy2 != null) {
            System.out.println("Strategy2: " + strategy2.getClass().getSimpleName());
            strategy2.onCollision(thisObj, otherObj);
        }
    }
}
