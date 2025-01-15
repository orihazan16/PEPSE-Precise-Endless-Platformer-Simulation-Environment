package pepse.world;

import danogl.GameObject;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.Constants;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Terrain class, responsible for creating terrain in a given range and getting the ground height at
 * different locations.
 */
public class Terrain {
    private float groundHeightAtX0;
    private NoiseGenerator noiseGenerator;

    /**
     * Creates a terrain object.
     * @param windowDimensions the dimensions of the window
     * @param seed the seed for the noise generator
     */
    public Terrain(Vector2 windowDimensions, int seed) {
        this.groundHeightAtX0 = windowDimensions.y() * 2/3;
        this.noiseGenerator = new NoiseGenerator(seed, (int) groundHeightAtX0);
    }

    /**
     * Returns the ground height at a given x coordinate.
     * @param x the x coordinate
     * @return the ground height at the given x coordinate
     */
    public float groundHeightAt(float x) {
        float noise = (float) noiseGenerator.noise(x, Constants.BLOCK_SIZE*7);
        return groundHeightAtX0 + noise;
    }

    /**
     * Creates terrain in a given range.
     * @param minX the minimum x coordinate
     * @param maxX the maximum x coordinate
     * @return the terrain blocks
     */
    public List<Block> createInRange(int minX, int maxX) {

        List<Block> blockList = new ArrayList<>();
        Renderable renderable =
                new RectangleRenderable(ColorSupplier.approximateColor(Constants.BASE_GROUND_COLOR));

        for (int i=minX; i<=maxX; i+= Constants.BLOCK_SIZE) {
            // get the length of the ith block with noise function
            float height = (float) (Math.floor(groundHeightAt(i) / Constants.BLOCK_SIZE)
                    * Constants.BLOCK_SIZE);
            // create a block column
            for(int j=0; j<Constants.BLOCK_COLUMN_HEIGHT; j++){
                Block block = new Block(new Vector2(i, height + j * Constants.BLOCK_SIZE), renderable);
                block.setTag(Constants.GROUND_TAG);
                blockList.add(block);
            }
        }
        return blockList;
    }
}
