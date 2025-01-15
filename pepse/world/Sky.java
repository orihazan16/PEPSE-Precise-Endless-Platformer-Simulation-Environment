package pepse.world;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import java.awt.*;
import pepse.Constants;
/**
 * Responsible for creating the sky object.
 */
public class Sky {

    /**
     * Creates a sky object.
     * @param windowDimensions the dimensions of the window
     * @return the sky object
     */
    public static GameObject create(Vector2 windowDimensions) {

        GameObject sky = new GameObject(Vector2.ZERO, windowDimensions,
                new RectangleRenderable(Constants.SKY_COLOR));
        sky.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sky.setTag(Constants.SKY_TAG);

        return sky;
    }
}
