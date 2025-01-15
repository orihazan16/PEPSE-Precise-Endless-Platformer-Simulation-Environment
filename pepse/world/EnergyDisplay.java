package pepse.world;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import pepse.Constants;

/**
 * Responsible for creating the energy display object.
 */
public class EnergyDisplay {

    /**
     * Creates an energy display object.
     * @param energyText the text renderable object of the energy display
     * @return the energy display game object
     */
    public static GameObject create(TextRenderable energyText) {
        GameObject energyDisplay = new GameObject(
            Constants.ENERGY_DISPLAY_POSITION,
            Vector2.ONES.mult(Constants.ENERGY_DISPLAY_SIZE),
            energyText);
        energyDisplay.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        energyDisplay.setTag(Constants.ENERGY_DISPLAY_TAG);
        return energyDisplay;
    }
}
