package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.Constants;

import java.awt.*;

/**
 * Responsible for creating the sun game object.
 */
public class Sun {
    /**
     * Creates the sun game object.
     * @param windowDimensions the dimensions of the window
     * @param cycleLength the length of the cycle
     * @return the sun game object
     */
    public static GameObject create(Vector2 windowDimensions, float cycleLength) {

        Vector2 cycleCenter = new Vector2(windowDimensions.x()/2, windowDimensions.y()/2);
        Vector2 initialSunCenter = new Vector2(windowDimensions.x()/2, windowDimensions.y()/12);

        GameObject sun = new GameObject(
                initialSunCenter,
                Vector2.ONES.mult(Constants.SUN_SIZE),
                new OvalRenderable(Constants.SUN_COLOR));

        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag(Constants.SUN_TAG);

        new Transition<Float>(
                sun, // the game object being changed
                (Float angle) -> sun.setCenter(initialSunCenter.subtract(cycleCenter)
                        .rotated(angle)
                        .add(cycleCenter)), // the method to call
                Constants.INITIAL_CYCLE_ANGLE_SUN, // initial transition value
                Constants.FINAL_CYCLE_ANGLE_SUN, // final transition value
                Transition.LINEAR_INTERPOLATOR_FLOAT,// use a cubic interpolator
                cycleLength,  // transition fully over half a day
                Transition.TransitionType.TRANSITION_LOOP, // Choose appropriate ENUM value
                null);// nothing further to execute upon reaching final value
        return sun;
    }
}
