package pepse.world.trees;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.GameObjectPhysics;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.Constants;

/**
 * Responsible for creating a Leaf game object in the game.
 */
public class Leaf {

    /**
     * Creates a leaf game object with the given parameters.
     * @param topLeftCorner the top left corner of the leaf
     * @param renderable the renderable object of the leaf
     * @return the leaf game object
     */
    public static GameObject create(Vector2 topLeftCorner, Renderable renderable) {
        GameObject leaf = new GameObject(
                topLeftCorner,
                Vector2.ONES.mult(Constants.LEAF_SIZE),
                renderable);

        // initiate random delay for leaf transitions
        new ScheduledTask(
                leaf,
                (float) (Math.random() * (Constants.LEAF_DELAY_MAX - Constants.LEAF_DELAY_MIN)
                        + Constants.LEAF_DELAY_MIN),
                false,
                () -> startAngleTransitions(leaf)
        );

        new ScheduledTask(
                leaf,
                (float) (Math.random() * (Constants.LEAF_DELAY_MAX - Constants.LEAF_DELAY_MIN)
                        + Constants.LEAF_DELAY_MIN),
                false,
                () -> startDimensionsTransitions(leaf)
        );

        return leaf;
    }

    /**
     * gives a leaf game object a transition to swing back and forth
     * @param leaf the leaf game object
     */
    private static void startAngleTransitions(GameObject leaf) {
        // Transition for leaf angle
        new Transition<>(
                leaf, // the game object being changed
                angle -> leaf.renderer().setRenderableAngle(angle),
                Constants.LEAF_INITIAL_CYCLE_ANGLE, // initial transition value
                Constants.LEAF_FINAL_CYCLE_ANGLE, // final transition value
                Transition.LINEAR_INTERPOLATOR_FLOAT,// use a cubic interpolator
                Constants.LEAF_SWING_TIME,  // transition fully over half a day
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);
    }

    /**
     * gives a leaf game object a transition to change its dimensions
     * @param leaf the leaf game object
     */
    private static void startDimensionsTransitions(GameObject leaf) {
        // Transition for leaf dimensions
        new Transition<>(
                leaf,
                scale -> leaf.setDimensions(Vector2.ONES.mult(Constants.LEAF_SIZE * scale)),
                Constants.LEAF_DIMENSION_INITIAL_SCALE_FACTOR,
                Constants.LEAF_DIMENSION_FINAL_SCALE_FACTOR,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                Constants.LEAF_SWING_TIME,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null
        );
    }
}
