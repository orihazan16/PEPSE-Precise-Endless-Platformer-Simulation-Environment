package pepse.world;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.Constants;
import pepse.util.ColorSupplier;
import pepse.world.avatars.AvatarJumpListener;
import pepse.world.clouds.Raindrop;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import pepse.Constants;

/**
 * Cloud class, responsible for creating a cloud game object and generating raindrops.
 */
public class Cloud implements AvatarJumpListener {

    private Vector2 cloudStartPosition;
    private List<Block> cloudBlocks = new ArrayList<>();
    private final RectangleRenderable cloudBlockRenderable;

    private final Consumer<GameObject> addRaindropGameObject;
    private final Consumer<GameObject> removeRaindropGameObject;
    private final Vector2 windowDimensions;
    private final Random seed = new Random();

    /**
     * Constructs a Cloud object.
     * @param windowDimensions the dimensions of the window
     * @param addRaindropGameObject the consumer to add a raindrop game object
     * @param removeRaindropGameObject the consumer to remove a raindrop game object
     */
    public Cloud(Vector2 windowDimensions, Consumer<GameObject> addRaindropGameObject,
                 Consumer<GameObject> removeRaindropGameObject) {

        this.addRaindropGameObject = addRaindropGameObject;
        this.removeRaindropGameObject = removeRaindropGameObject;
        this.windowDimensions = windowDimensions;
        this.cloudBlockRenderable = new
                RectangleRenderable(ColorSupplier.approximateMonoColor(Constants.BASE_CLOUD_COLOR));
        this.cloudStartPosition = new Vector2(0f, windowDimensions.y() / 8);
    }

    /**
     * Creates a cloud game object.
     * @return the cloud game object
     */
    public List<Block> createCloud() {
        List<Block> blocks = new ArrayList<>();
        for (int i = 0; i < Constants.CLOUD_SHAPE.size(); i++) {
            for (int j = 0; j < Constants.CLOUD_SHAPE.get(i).size(); j++) {
                if (Constants.CLOUD_SHAPE.get(i).get(j) == 1) {
                    Vector2 blockPosition = new Vector2(
                            cloudStartPosition.x() + j * Constants.BLOCK_SIZE,
                            cloudStartPosition.y() + i * Constants.BLOCK_SIZE);
                    Block newCloudBlock = new Block(blockPosition, cloudBlockRenderable);
                    newCloudBlock.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
                    blocks.add(newCloudBlock);
                    newCloudBlock.setTag(Constants.CLOUD_TAG);

                    float initialX = newCloudBlock.getTopLeftCorner().x();
                    new Transition<Float>(
                            newCloudBlock,
                            (Float x) -> newCloudBlock.setTopLeftCorner(new Vector2(initialX + x,
                                    newCloudBlock.getTopLeftCorner().y())),
                            -Constants.CLOUD_POSITION_OFFSET,
                            windowDimensions.x() + Constants.CLOUD_POSITION_OFFSET,
                            Transition.LINEAR_INTERPOLATOR_FLOAT,
                            Constants.CLOUD_TRANSITION_TIME,
                            Transition.TransitionType.TRANSITION_LOOP,
                            null
                    );
                }
            }
        }
        cloudBlocks = blocks;
        return blocks;
    }

    @Override
    public void avatarJump() {
        generateRain();
    }

    /**
     * Generates raindrops.
     */
    private void generateRain() {
        for (Block cloudBlock : cloudBlocks) {
            if (seed.nextInt(100) < Constants.PERCENTAGE_RAIN) {
                Vector2 raindropPosition = new Vector2(
                        cloudBlock.getTopLeftCorner().x(),
                        cloudBlock.getTopLeftCorner().y()
                );
                Raindrop raindrop = new Raindrop(raindropPosition, Vector2.ONES.mult(Constants.RAINDROP_SIZE),
                        new RectangleRenderable(Color.BLUE), removeRaindropGameObject);
                raindrop.setTag(Constants.RAINDROP_TAG);
                raindrop.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
                raindrop.transform().setAccelerationY(Constants.GRAVITY_WORLD);
                addRaindropGameObject.accept(raindrop);
            }
        }
    }
}