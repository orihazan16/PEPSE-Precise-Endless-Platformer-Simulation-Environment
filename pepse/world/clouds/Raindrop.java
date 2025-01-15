package pepse.world.clouds;

import danogl.GameObject;
import danogl.components.Transition;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.Constants;

import java.util.function.Consumer;

/**
 * Represents a raindrop in the game, extends the GameObject class.
 */
public class Raindrop extends GameObject {

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Raindrop(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                    Consumer<GameObject> removeGameObject) {
        super(topLeftCorner, dimensions, renderable);

        transform().setAccelerationY(Constants.GRAVITY_WORLD);

        new Transition<Float>(
                this,
                (Float newOpacity) -> renderer().setOpaqueness(newOpacity),
                renderer().getOpaqueness(),  // Starting opacity
                Constants.FINAL_RAINDROP_OPACITY,  // Target opacity (fade out completely)
                Transition.LINEAR_INTERPOLATOR_FLOAT,  // Interpolation type
                Constants.RAINDROP_FALLING_TIME,  // Duration of the transition (in seconds)
                Transition.TransitionType.TRANSITION_ONCE,  // Fade out only once
                () -> removeGameObject.accept(this)  // Remove the raindrop from the game;
        );

    }

}
