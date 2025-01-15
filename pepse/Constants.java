package pepse;

import danogl.collisions.Layer;
import danogl.util.Vector2;
import java.awt.*;
import java.util.List;

public class Constants {
    /**WORD PACKAGE*/
    public static final float GRAVITY_WORLD = 400;
    public static final int DAY_CYCLE_LENGTH = 30;
    public static final int NUM_OF_CHUNKS = 3;

    //Sizes
    public static final int BLOCK_SIZE = 30;
    public static final int ENERGY_DISPLAY_SIZE = 50;
    public static final int BLOCK_COLUMN_HEIGHT = 20;

    // Cloud and Rain
    public static final float CLOUD_TRANSITION_TIME = 10f;
    public static final float CLOUD_POSITION_OFFSET = 100f;
    public static final Color BASE_CLOUD_COLOR = new Color(255, 255, 255);
    public static final java.util.List<java.util.List<Integer>> CLOUD_SHAPE = java.util.List.of(
            java.util.List.of(0, 1, 1, 0, 0, 0),
            java.util.List.of(1, 1, 1, 0, 1, 0),
            java.util.List.of(1, 1, 1, 1, 1, 1),
            java.util.List.of(1, 1, 1, 1, 1, 1),
            java.util.List.of(0, 1, 1, 1, 0, 0),
            List.of(0, 0, 0, 0, 0, 0));
    public static final float RAINDROP_SIZE = 10f;
    public static final float FINAL_RAINDROP_OPACITY = 0f;
    public static final float RAINDROP_FALLING_TIME = 1.5f;
    public static final float PERCENTAGE_RAIN = 30f;
    public static final Vector2 ENERGY_DISPLAY_POSITION = Vector2.ZERO;
    public static final int TREES_PERCENTAGE = 5;

    //Colers
    public static final Color SKY_COLOR = Color.decode("#80C6E5");
    public static final Color BASE_GROUND_COLOR = new Color(212,123,74);

    // Layers Constants
    public static final int SKY_LAYER = Layer.BACKGROUND;
    public static final int ENERGY_DISPLAY_LAYER = Layer.UI;
    public static final int SUN_HALO_LAYER = -198;
    public static final int SUN_LAYER = -197;
    public static final int RAIN_LAYER = -196;
    public static final int CLOUD_LAYER = -195;
    public static final int GROUND_LAYER = -194;
    public static final int LEAFS_LAYER = -1;
    public static final int NIGHT_LAYER = 101;

    // Game Objects Tags
    public static final String GROUND_TAG = "ground";
    public static final String SKY_TAG = "sky";
    public static final String ENERGY_DISPLAY_TAG = "energyDisplay";
    public static final String CLOUD_TAG = "cloud";
    public static final String RAINDROP_TAG = "raindrop";
    public static final String TREE_TAG = "tree";
    public static final String AVATAR_TAG = "avatar";
    public static final String FRUIT_TAG = "fruit";
    public static final String EATEN_TAG = "eaten";
    public static final String TRUNK_TAG = "trunk";
    public static final String LEAF_TAG = "leaf";
    public static final String SUN_HALO_TAG = "sunHalo";
    public static final String SUN_TAG = "sun";
    public static final String NIGHT_TAG = "night";

    /**Avatar*/
    public static final Vector2 AVATAR_SIZE = new Vector2(50,50);

    //Avatar Actions
    public static final String AVATAR_MOVE_LEFT = "moveLeft";
    public static final String AVATAR_MOVE_RIGHT = "moveRight";
    public static final String AVATAR_JUMP = "jump";
    public static final String AVATAR_STAY_IDLE = "stayIdle";

    //Avatar Animation
    public static final String AVATAR_ANIMATION_RUNNING_LEFT = "runningLeft";
    public static final String AVATAR_ANIMATION_RUNNING_RIGHT = "runningRight";
    public static final String AVATAR_ANIMATION_JUMP = "jumping";
    public static final String AVATAR_ANIMATION_IDLE = "idle";

    //Avatar Energy
    public static final float MAX_ENERGY = 100f;
    public static final float MIN_ENERGY = 0f;
    public static final float MOVE_ENERGY_COST = 0.5f;
    public static final float JUMP_ENERGY_COST = 10f;
    public static final float IDLE_ENERGY = 1f;
    public static final float FRUIT_ENERGY = 10f;

    //Avatar Movement
    public static final float VELOCITY_X = 400;
    public static final float VELOCITY_Y = -400;
    //ANIMATION
    public static final float ANIMATION_DURATION = 0.1f;

    //Idle Animation
    public static final String IDLE_SPRITE_0 = "assets/idle_0.png";
    public static final String IDLE_SPRITE_1 = "assets/idle_1.png";
    public static final String IDLE_SPRITE_2 = "assets/idle_2.png";
    public static final String IDLE_SPRITE_3 = "assets/idle_3.png";

    //Jump Animation
    public static final String JUMP_SPRITE_0 = "assets/jump_0.png";
    public static final String JUMP_SPRITE_1 = "assets/jump_1.png";
    public static final String JUMP_SPRITE_2 = "assets/jump_2.png";
    public static final String JUMP_SPRITE_3 = "assets/jump_3.png";

    //Run Animation
    public static final String RUN_SPRITE_0 = "assets/run_0.png";
    public static final String RUN_SPRITE_1 = "assets/run_1.png";
    public static final String RUN_SPRITE_2 = "assets/run_2.png";
    public static final String RUN_SPRITE_3 = "assets/run_3.png";
    public static final String RUN_SPRITE_4 = "assets/run_4.png";
    public static final String RUN_SPRITE_5 = "assets/run_5.png";

    /**DAYNIGHT PACKAGE*/
    public static final float MIDNIGHT_OPACITY = 0.5f;
    public static final float INITIAL_CYCLE_ANGLE_SUN = 0f;
    public static final float FINAL_CYCLE_ANGLE_SUN = 360f;

    //Colors
    public static final Color NIGHT_COLOR = Color.BLACK;
    public static final Color SUN_COLOR = Color.YELLOW;
    public static final Color SUN_HALO_COLOR = new Color(255,255, 0, 20);

    //Sizes
    public static final float SUN_SIZE = 100;
    public static final float SUN_HALO_SIZE = 150;

    /**TREES PACKAGE*/
    public static final float FRUIT_DISAPPEARANCE_TIME = DAY_CYCLE_LENGTH;
    public static final float LEAF_INITIAL_CYCLE_ANGLE = 20f;
    public static final float LEAF_FINAL_CYCLE_ANGLE = -20f;
    public static final float LEAF_SWING_TIME = 2;
    public static final float LEAF_DIMENSION_INITIAL_SCALE_FACTOR = 0.8f;
    public static final float LEAF_DIMENSION_FINAL_SCALE_FACTOR = 1f;
    public static final float LEAF_DELAY_MAX = 2f;
    public static final float LEAF_DELAY_MIN = 0f;

    //Colors
    public static final Color TRUNK_COLOR = new Color(100, 50, 20); // Color for the trunk
    public static final Color LEAF_COLOR = new Color(50, 200, 30);
    public static final Color FRUIT_COLOR = new Color(220, 178, 40);

    //Sizes
    public static final int FRUIT_SIZE = 15;
    public static final int LEAF_SIZE = 20;
    public static final int TRUNK_MIN_HEIGHT = 100;
    public static final int TRUNK_MAX_HEIGHT = 150;
    public static final float LEAVES_RANGE_HEIGHT = 150;
    public static final float LEAVES_RANGE_WIDTH = 150;

    //Percentage Object
    public static final int LEAF_PERCENTAGE = 66;
    public static final int FRUIT_PERCENTAGE = 10;
}