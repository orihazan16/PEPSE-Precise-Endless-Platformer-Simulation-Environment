package pepse.world.trees;

import danogl.GameObject;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import pepse.Constants;

/**
 * A class representing a tree in the world, which contains a trunk, leaves, and fruits.
 */
public class Tree {

    private final Random seed;

    /**
     * Constructs a Tree object with the given parameters.
     * @param seed the random seed to be used for generating the tree
     */
    public Tree(Random seed) {
        this.seed = seed;
    }

    /**
     * Creates a tree with a trunk, leaves, and fruits.
     * @param downLeftCorner the bottom left corner of the tree
     * @return a list of GameObjects representing the tree
     */
    public List<GameObject> createTree(Vector2 downLeftCorner) {

        List<GameObject> treeParts = new ArrayList<>();

        //Randomly choose the height of the trunk
        int trunkHeight = seed.nextInt(Constants.TRUNK_MAX_HEIGHT - Constants.TRUNK_MIN_HEIGHT + 1)
                + Constants.TRUNK_MAX_HEIGHT;

        //Create a trunk and add it to the list of objects
        GameObject trunk = new Trunk(new Vector2(downLeftCorner.x(), downLeftCorner.y()), trunkHeight,
                new RectangleRenderable(Constants.TRUNK_COLOR));
        trunk.setTag(Constants.TRUNK_TAG);
        treeParts.add(trunk);

        //create the leaves and fruits
        addLeafsAndFruit(treeParts, trunkHeight, downLeftCorner);
        return treeParts;
    }

    /**
     * Adds leaves and fruits to the tree.
     * @param treeParts the list of GameObjects representing the tree
     * @param trunkHeight the height of the trunk
     * @param downLeftCorner the bottom left corner of the tree
     */
    private void addLeafsAndFruit(List<GameObject> treeParts, int trunkHeight, Vector2 downLeftCorner) {
        for (int i = 0; i < Constants.LEAVES_RANGE_WIDTH; i += Constants.LEAF_SIZE) {
            for (int j = 0; j < Constants.LEAVES_RANGE_HEIGHT; j += Constants.LEAF_SIZE) {
                //Create random leaves and add them to the list of objects
                if (seed.nextInt(100) < Constants.LEAF_PERCENTAGE) {
                    GameObject leaf = Leaf.create(
                            new Vector2(downLeftCorner.x() - Constants.LEAVES_RANGE_WIDTH +
                                    Constants.LEAVES_RANGE_WIDTH/2+ i,downLeftCorner.y() - j - trunkHeight),
                            new RectangleRenderable(Constants.LEAF_COLOR)
                    );
                    leaf.setTag(Constants.LEAF_TAG);
                    treeParts.add(leaf);
                    //Create a fruit on top of the tree
                    if((j == Constants.LEAVES_RANGE_HEIGHT -
                            (Constants.LEAVES_RANGE_HEIGHT % Constants.LEAF_SIZE))&&
                            (seed.nextInt(100) < Constants.FRUIT_PERCENTAGE)) {
                        GameObject fruit = new Fruit( new Vector2(
                                downLeftCorner.x() - Constants.LEAVES_RANGE_HEIGHT +
                                        Constants.LEAVES_RANGE_HEIGHT / 2 + i,
                                downLeftCorner.y() - j - trunkHeight-Constants.LEAF_SIZE),
                                new OvalRenderable(Constants.FRUIT_COLOR));
                        fruit.setTag(Constants.FRUIT_TAG);
                        treeParts.add(fruit);
                    }
                }
                //Create a fruit if and not create a leaf and add it to the list of objects
                else if (seed.nextInt(100) < Constants.FRUIT_PERCENTAGE) {
                    GameObject fruit = new Fruit( new Vector2(
                            downLeftCorner.x() - Constants.LEAVES_RANGE_WIDTH +
                                    Constants.LEAVES_RANGE_WIDTH  / 2 + i,
                            downLeftCorner.y() - j - trunkHeight),
                            new OvalRenderable(Constants.FRUIT_COLOR));
                    fruit.setTag(Constants.FRUIT_TAG);
                    treeParts.add(fruit);
                }
            }
        }
    }
}
