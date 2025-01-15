package pepse.world.trees;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Block;
import pepse.Constants;

/**
 * A class representing a trunk object in the game, which extends the GameObject class.
 */
public class Trunk extends GameObject {

    private static final int trunkWidth = Constants.BLOCK_SIZE;

    /**
     * Constructs a trunk object.
     * @param topLeftCorner the top left corner of the trunk
     * @param trunk_height the height of the trunk
     * @param renderable the renderable object of the trunk
     */
    public Trunk(Vector2 topLeftCorner, int trunk_height, Renderable renderable) {
        super(new Vector2(topLeftCorner.x(), topLeftCorner.y() - trunk_height), new Vector2(trunkWidth,
                trunk_height), renderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
    }
}
