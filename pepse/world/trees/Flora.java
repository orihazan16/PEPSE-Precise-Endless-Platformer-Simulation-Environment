package pepse.world.trees;

import danogl.GameObject;
import danogl.util.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;

import pepse.Constants;

/**
 * Flora class, responsible for creating trees in a given range.
 */
public class Flora {

    private final Random seed;
    private final Function<Float, Float> groundHeightCalculator;

    /**
     * Constructs a Flora object.
     * @param seed the seed for the random number generator
     * @param groundHeightAt the function that returns the ground height at a given x coordinate
     */
    public Flora(Random seed, Function<Float, Float> groundHeightAt) {
        this.seed = seed;
        this.groundHeightCalculator = groundHeightAt;
    }

    /**
     * Creates trees in a given range.
     * @param minX the minimum x coordinate
     * @param maxX the maximum x coordinate
     * @return the trees as a list of a list of game objects
     */
    public List<List<GameObject>> createInRange(int minX, int maxX) {
        List<List<GameObject>> trees = new ArrayList<>();
        for (int i = minX; i <= maxX; i += Constants.BLOCK_SIZE) {
            Random newRandom = new Random(Objects.hash(i, seed));
            if (newRandom.nextInt(100) < Constants.TREES_PERCENTAGE) {
                Tree tree = new Tree(newRandom);
                float height = groundHeightCalculator.apply((float) i);
                List<GameObject> treeParts = tree.createTree(new Vector2(i, height));
                trees.add(treeParts);
            }
        }
        return trees;
    }
}
