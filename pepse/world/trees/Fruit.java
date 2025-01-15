package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.CoordinateSpace;
import danogl.components.ScheduledTask;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.Constants;

/**
 * A class representing a fruit object in the game, which extends the GameObject class.
 */
public class Fruit extends GameObject {

    private final Renderable fruitRenderable;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Fruit(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, Vector2.ONES.mult(Constants.FRUIT_SIZE), renderable);
        this.fruitRenderable = renderable;
        setTag(Constants.FRUIT_TAG);
    }

    /**
     * OnCollision, handel's the collision between the fruit and the avatar.
     * @param other the object the fruit collided with
     * @param collision the collision object
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other.getTag().equals(Constants.AVATAR_TAG)) {
            renderer().setRenderable(null);
            ScheduledTask task = new ScheduledTask(
                    this,
                    Constants.FRUIT_DISAPPEARANCE_TIME,
                    false,
                    () -> {
                setTag(Constants.FRUIT_TAG);
                renderer().setRenderable(fruitRenderable);
            });
        }
    }
}
