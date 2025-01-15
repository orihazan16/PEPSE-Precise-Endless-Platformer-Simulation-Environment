package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import pepse.Constants;

import java.awt.*;

/**
 * Responsible for creating a sun halo game object in the game.
 */
public class SunHalo {

    /**
     * Creates a sun halo game object based on the given sun game object.
     * @param sun the sun game object
     * @return the sun halo game object
     */
    public static GameObject create(GameObject sun) {

        GameObject sunHalo = new GameObject(
                sun.getCenter(),
                Vector2.ONES.mult(Constants.SUN_HALO_SIZE),
                new OvalRenderable(Constants.SUN_HALO_COLOR));

        sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sunHalo.setTag(Constants.SUN_HALO_TAG);
        sunHalo.addComponent(deltaTime -> sunHalo.setCenter(sun.getCenter()));

        return sunHalo;

    }
}
