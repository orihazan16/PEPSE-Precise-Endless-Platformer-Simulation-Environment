package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import pepse.world.*;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Flora;

import java.util.*;

/**
 * The main class for the Pepse game.
 */
public class  PepseGameManager extends GameManager {

    private int chunkSize;
    private int currentChunk;
    private Map<Integer, List<GameObject>> activeChunks = new HashMap<>();
    private Flora flora;
    private Terrain terrain;
    private GameObject avatar;
    private final Random seed;

    /**
     * Constructs a PepseGameManager object.
     */
    public PepseGameManager() {
        super();
        this.seed = new Random();
    }

    /**
     * The main method for the Pepse game, which creates a new PepseGameManager object and runs the game.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }

    /**
     * Updates the game state.
     * @param deltaTime the time since the last update
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        int currentPosition = (int) avatar.getCenter().x();
        if (currentChunk*chunkSize >= currentPosition || currentPosition >= (currentChunk+1)*chunkSize) {
            currentChunk = currentPosition/chunkSize;
            updateChunks(currentChunk);
        }
    }

    /**
     * Updates the chunks of the game, loading new chunks and unloading old chunks.
     * @param currentChunk the current chunk
     */
    private void updateChunks(int currentChunk) {
        int leftChunk = currentChunk - Constants.NUM_OF_CHUNKS;
        int rightChunk = currentChunk + Constants.NUM_OF_CHUNKS;

        // Unload chunks outside the range
        removeChunks(leftChunk, rightChunk);

        // Load chunks inside the range
        addChunks(leftChunk, rightChunk);
    }

    /**
     * Adds the chunks that are inside the range.
     * @param leftChunk the left chunk
     * @param rightChunk the right chunk
     */
    private void addChunks(int leftChunk, int rightChunk) {

        // check if current chunks are already loaded
        for (int i = leftChunk; i <= rightChunk; i++) {
            if (!activeChunks.containsKey(i)) {
                List<GameObject> newChunk = createChunk(i);
                activeChunks.put(i, newChunk);
            }
        }
    }

    /**
     * Removes the chunks that are outside the range.
     * @param leftChunk the left chunk
     * @param rightChunk the right chunk
     */
    private void removeChunks(int leftChunk, int rightChunk) {
        List<Integer> removeChunkPositions = new ArrayList<>();
        for (Integer chunkPosition : activeChunks.keySet()) {
            if (chunkPosition < leftChunk || chunkPosition > rightChunk) {
                removeChunkPositions.add(chunkPosition);
                removeGameObjects(activeChunks.get(chunkPosition));
            }
        }
        for (Integer chunkPosition : removeChunkPositions) {
            activeChunks.remove(chunkPosition);
        }
    }

    /**
     * Removes the game objects from the game.
     * @param gameObjects the game objects to remove
     */
    private void removeGameObjects(List<GameObject> gameObjects) {
        for (GameObject gameObject : gameObjects) {
            if (gameObject.getTag().equals(Constants.GROUND_TAG)) {
                gameObjects().removeGameObject(gameObject, Layer.STATIC_OBJECTS);

            } else if (gameObject.getTag().equals(Constants.TRUNK_TAG)) {
                gameObjects().removeGameObject(gameObject, Layer.STATIC_OBJECTS);

            } else if (gameObject.getTag().equals(Constants.FRUIT_TAG)) {
                gameObjects().removeGameObject(gameObject, Layer.STATIC_OBJECTS);

            } else if (gameObject.getTag().equals(Constants.EATEN_TAG)) {
                gameObjects().removeGameObject(gameObject, Layer.STATIC_OBJECTS);

            } else if (gameObject.getTag().equals(Constants.LEAF_TAG)) {
                gameObjects().removeGameObject(gameObject, Constants.LEAFS_LAYER);

            }
        }
    }

    /**
     * Creates a new chunk for the game.
     * @param i the chunk number
     * @return the list of game objects in the chunk
     */
    private List<GameObject> createChunk(int i) {

        int minX = i * chunkSize;
        int maxX = (i + 1) * chunkSize;
        List<GameObject> newGameObjects = new ArrayList<>();

        // create new ground
        newGameObjects.addAll(createGround(minX, maxX));

        // create new flora
        newGameObjects.addAll(createFlora(minX, maxX));

        return newGameObjects;
    }

    /**
     * Initializes the game and its game objects.
     * @param imageReader the image reader
     * @param soundReader the sound reader
     * @param inputListener the input listener
     * @param windowController the window controller
     */
    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader, UserInputListener inputListener,
                               WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        this.chunkSize =
                (int) windowController.getWindowDimensions().x() /3 -
                        (int) windowController.getWindowDimensions().x()/3 % Constants.BLOCK_SIZE;
        this.currentChunk = 0;

        initializeSky(windowController);

        initializeTerrain(windowController);

        initializeFlora();

        initializeNight(windowController);

        initializeSunAndSunHalo(windowController);

        updateChunks(currentChunk);

        initializeAvatar(imageReader, inputListener);

        initializeEnergyText(windowController);

        initializeCloud(windowController);

        setCamera(new Camera(avatar, Vector2.ZERO,
                windowController.getWindowDimensions(),
                windowController.getWindowDimensions()));
    }

    /**
     * Initializes the cloud in the game.
     * @param windowController the window controller
     */
    private void initializeCloud(WindowController windowController) {
        Cloud cloud = new Cloud(windowController.getWindowDimensions(),
                raindropToAdd -> gameObjects().addGameObject(raindropToAdd, Constants.RAIN_LAYER),
                objectToDestroy -> gameObjects().removeGameObject(objectToDestroy));
        ((Avatar) avatar).addJumpListener(cloud);
        List<Block> cloudBLockList = cloud.createCloud();
        createCloud(cloudBLockList);
    }

    /**
     * Initializes the energy text in the game.
     * @param windowController the window controller
     */
    private void initializeEnergyText(WindowController windowController) {
        TextRenderable energyText = new TextRenderable(Float.toString(((Avatar) avatar).getEnergy()));
        GameObject energyDisplay = EnergyDisplay.create(energyText);
        gameObjects().addGameObject(energyDisplay, Constants.ENERGY_DISPLAY_LAYER);
        energyDisplay.addComponent((deltaTime) ->
                energyText.setString(Float.toString(((Avatar) avatar).getEnergy())));
    }

    /**
     * Initializes the avatar in the game.
     * @param imageReader the image reader
     * @param inputListener the input listener
     */
    private void initializeAvatar(ImageReader imageReader, UserInputListener inputListener) {
        GameObject avatar = new Avatar(new Vector2(0, 0), inputListener, imageReader);
        gameObjects().addGameObject(avatar);
        this.avatar = avatar;
    }

    /**
     * Initializes the sun and sun halo in the game.
     * @param windowController the window controller
     */
    private void initializeSunAndSunHalo(WindowController windowController) {

        GameObject sun = Sun.create(windowController.getWindowDimensions(), Constants.DAY_CYCLE_LENGTH);
        gameObjects().addGameObject(sun, Constants.SUN_LAYER);

        GameObject sunHalo = SunHalo.create(sun);
        gameObjects().addGameObject(sunHalo, Constants.SUN_HALO_LAYER);
    }

    /**
     * Initializes the night in the game.
     * @param windowController the window controller
     */
    private void initializeNight(WindowController windowController) {
        GameObject night = Night.create(windowController.getWindowDimensions(), Constants.DAY_CYCLE_LENGTH);
        gameObjects().addGameObject(night, Constants.NIGHT_LAYER);
    }

    /**
     * Initializes the flora in the game.
     */
    private void initializeFlora() {
        this.flora = new Flora(seed, terrain::groundHeightAt);
    }

    /**
     * Initializes the terrain in the game.
     * @param windowController the window controller
     */
    private void initializeTerrain(WindowController windowController) {
        this.terrain = new Terrain(windowController.getWindowDimensions(), 0);
    }

    /**
     * Initializes the sky in the game.
     * @param windowController the window controller
     */
    private void initializeSky(WindowController windowController) {
        GameObject sky = Sky.create(windowController.getWindowDimensions());
        gameObjects().addGameObject(sky, Constants.SKY_LAYER);
    }

    /**
     * Creates the cloud in the game.
     * @param cloudCLockList the list of cloud blocks
     */
    private void createCloud(List<Block> cloudCLockList) {
        for (int i=0; i<cloudCLockList.size(); i++) {
            Block block = cloudCLockList.get(i);
            gameObjects().addGameObject(block, Constants.CLOUD_LAYER);
        }
    }

    /**
     * Creates ground in the game.
     * @param minX the minimum x coordinate
     * @param maxX the maximum x coordinate
     * @return the list of game objects in the ground
     */
    private List<GameObject> createGround(int minX, int maxX) {
        List<Block> blockList = terrain.createInRange(minX, maxX);
        List<GameObject> gameObjectList = new ArrayList<>();
        for (int i=0; i<blockList.size(); i++) {
            Block block = blockList.get(i);
            if (i % Constants.BLOCK_COLUMN_HEIGHT < 5) {
                gameObjects().addGameObject(block, Layer.STATIC_OBJECTS);
            }
            else {
                gameObjects().addGameObject(block, Constants.GROUND_LAYER);
            }
            gameObjectList.add(block);
        }
        return gameObjectList;
    }

    /**
     * Creates flora in the game.
     * @param minX the minimum x coordinate
     * @param maxX the maximum x coordinate
     * @return the list of game objects in the flora
     */
    private List<GameObject> createFlora(int minX, int maxX) {
        List<List<GameObject>> trees = flora.createInRange(minX, maxX);
        List<GameObject> gameObjectList = new ArrayList<>();
        for (List<GameObject> tree : trees) {
            for (GameObject treePart : tree) {
                if(treePart.getTag().equals(Constants.TRUNK_TAG)) {
                    gameObjects().addGameObject(treePart, Layer.STATIC_OBJECTS);
                }
                if (treePart.getTag().equals(Constants.FRUIT_TAG)) {
                    gameObjects().addGameObject(treePart, Layer.STATIC_OBJECTS);
                }
                if (treePart.getTag().equals(Constants.LEAF_TAG)) {
                    gameObjects().addGameObject(treePart, Constants.LEAFS_LAYER);
                }
                gameObjectList.add(treePart);
            }
        }
        return gameObjectList;
    }
}
