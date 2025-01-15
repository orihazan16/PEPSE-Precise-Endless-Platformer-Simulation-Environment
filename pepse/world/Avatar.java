package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.avatars.AvatarJumpListener;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import pepse.Constants;

/**
 * A class representing the avatar in the game, which extends the GameObject class.
 * The avatar is the main character in the game, which the player controls.
 * The avatar can move left, right, and jump. The avatar has a certain amount of energy, which is used
 * when the avatar moves or jumps. The avatar can collect fruits to gain energy.
 * The avatar has animations for moving left, right, jumping, and idling.
 * The avatar can notify listeners when it jumps.
 */
public class Avatar extends GameObject{

    private UserInputListener inputListener;
    private ImageReader imageReader;

    private float avatarEnergy = Constants.MAX_ENERGY;
    private List<AvatarJumpListener> jumpListeners = new ArrayList<>();
    private Renderable[] idleSprites;
    private Renderable[] jumpSprites;
    private Renderable[] runSprites;
    private HashMap<String, Renderable> animationMap = new HashMap<>();
    private HashMap<String, Runnable> actionMap = new HashMap<>();

    /**
     * Constructs an Avatar object with the given parameters.
     * @param topLeftCorner the top left corner of the avatar
     * @param inputListener the input listener of the avatar
     * @param imageReader the image reader of the avatar
     */
    public Avatar(Vector2 topLeftCorner,
                  UserInputListener inputListener,
                  ImageReader imageReader) {
        super(topLeftCorner, Constants.AVATAR_SIZE, imageReader.readImage(Constants.IDLE_SPRITE_0,
                true));
        // physics setup
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(Constants.GRAVITY_WORLD);
        // class variables setup
        this.inputListener = inputListener;
        this.imageReader = imageReader;
        // initialize maps
        createSprites();
        initializeAvatarAnimations();
        initializeAvatarActions();
        setTag(Constants.AVATAR_TAG);
    }

    /**
     * Adds a listener to the avatar that listens for jumps.
     * @param listener the listener to add
     */
    public void addJumpListener(AvatarJumpListener listener) {
        jumpListeners.add(listener);
    }

    /**
     * Updates the avatar's movement (position, animation and energy) based on user input.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float xVel = 0;
        boolean isLeftPressed = inputListener.isKeyPressed(KeyEvent.VK_LEFT);
        boolean isRightPressed = inputListener.isKeyPressed(KeyEvent.VK_RIGHT);
        boolean isSpacePressed = inputListener.isKeyPressed(KeyEvent.VK_SPACE);

        // Handle jumping
        if ((avatarEnergy > Constants.JUMP_ENERGY_COST) && isSpacePressed && getVelocity().y() == 0) {
            transform().setVelocityY(Constants.VELOCITY_Y);
            actionMap.get(Constants.AVATAR_JUMP).run();
            notifyJumpListeners();
        }

        // Handle both left and right keys pressed
        if (isLeftPressed && isRightPressed) {
            // Idle when both keys are pressed
            if (getVelocity().y() == 0) { // Only idle on ground
                actionMap.get(Constants.AVATAR_STAY_IDLE).run();
            }
        } else {
            // Handle single key presses
            if ((avatarEnergy > Constants.MOVE_ENERGY_COST) && isLeftPressed) {
                xVel -= Constants.VELOCITY_X;
                actionMap.get(Constants.AVATAR_MOVE_LEFT).run();
            }
            if ((avatarEnergy > Constants.MOVE_ENERGY_COST) && isRightPressed) {
                xVel += Constants.VELOCITY_X;
                actionMap.get(Constants.AVATAR_MOVE_RIGHT).run();
            }
        }

        // If no keys are pressed and avatar is on the ground, idle
        if (xVel == 0 && getVelocity().y() == 0) {
            actionMap.get(Constants.AVATAR_STAY_IDLE).run();
        }

        // Update horizontal velocity
        transform().setVelocityX(xVel);
    }

    /**
     * Method to be called when the avatar collides with another object.
     * Handles collisions with the ground and fruits.
     * @param other the object the avatar collided with
     * @param collision the collision object
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if(other.getTag().equals(Constants.AVATAR_ANIMATION_IDLE)){
            this.transform().setVelocityY(0);
        }
        if (other.getTag().equals(Constants.FRUIT_TAG)) {
            other.setTag(Constants.EATEN_TAG);
            addEnergy(Constants.FRUIT_ENERGY);
        }
    }

    /**
     * Gets the energy of the avatar.
     * @return the energy of the avatar
     */
    public float getEnergy() {
        return avatarEnergy;
    }

    /**
     * Notifies all jump listeners that the avatar has jumped.
     */
    private void notifyJumpListeners() {
        for (AvatarJumpListener listener : jumpListeners) {
            listener.avatarJump();
        }
    }

    /**
     * Initializes the avatar actions by mapping key events to actions.
     */
    private void initializeAvatarActions() {
        actionMap.put(Constants.AVATAR_MOVE_LEFT, () ->
                handleMovement(-Constants.MOVE_ENERGY_COST, Constants.AVATAR_ANIMATION_RUNNING_LEFT));
        actionMap.put(Constants.AVATAR_MOVE_RIGHT, () ->
                handleMovement(-Constants.MOVE_ENERGY_COST, Constants.AVATAR_ANIMATION_RUNNING_RIGHT));
        actionMap.put(Constants.AVATAR_JUMP, () ->
                handleMovement(-Constants.JUMP_ENERGY_COST, Constants.AVATAR_ANIMATION_JUMP));
        actionMap.put(Constants.AVATAR_STAY_IDLE, () ->
                handleMovement(Constants.IDLE_ENERGY, Constants.AVATAR_ANIMATION_IDLE));
    }

    /**
     * Handles the avatar's movement by changing its energy and animation.
     * @param energy the energy to add to the avatar
     * @param state the state to change the animation to
     */
    private void handleMovement(float energy, String state) {
        addEnergy(energy);
        changeAnimation(state);
    }

    /**
     * Changes the avatar's animation to the given state.
     * @param state the state to change the animation to
     */
    private void changeAnimation(String state) {
        if (state.equals(Constants.AVATAR_ANIMATION_RUNNING_LEFT)) {
            renderer().setIsFlippedHorizontally(true);
        } else if (state.equals(Constants.AVATAR_ANIMATION_RUNNING_RIGHT)) {
            renderer().setIsFlippedHorizontally(false);
        }
        renderer().setRenderable(animationMap.get(state));
    }

    /**
     * Adds energy to the avatar, ensuring it stays within the energy bounds.
     * @param energy the energy to add to the avatar
     */
    private void addEnergy(float energy) {
        if ((avatarEnergy + energy >= Constants.MIN_ENERGY ) &&
                (avatarEnergy + energy <= Constants.MAX_ENERGY)) {
            avatarEnergy += energy;
        }
        else if (avatarEnergy + energy > Constants.MAX_ENERGY){
            avatarEnergy = Constants.MAX_ENERGY;
        }
    }

    /**
     * Creates the sprites for the avatar's animations.
     */
    private void createSprites() {
        idleSprites = new Renderable[4];
        idleSprites[0] = imageReader.readImage(Constants.IDLE_SPRITE_0, true);
        idleSprites[1] = imageReader.readImage(Constants.IDLE_SPRITE_1, true);
        idleSprites[2] = imageReader.readImage(Constants.IDLE_SPRITE_2, true);
        idleSprites[3] = imageReader.readImage(Constants.IDLE_SPRITE_3, true);

        jumpSprites = new Renderable[4];
        jumpSprites[0] = imageReader.readImage(Constants.JUMP_SPRITE_0, true);
        jumpSprites[1] = imageReader.readImage(Constants.JUMP_SPRITE_1, true);
        jumpSprites[2] = imageReader.readImage(Constants.JUMP_SPRITE_2, true);
        jumpSprites[3] = imageReader.readImage(Constants.JUMP_SPRITE_3, true);

        runSprites = new Renderable[6];
        runSprites[0] = imageReader.readImage(Constants.RUN_SPRITE_0, true);
        runSprites[1] = imageReader.readImage(Constants.RUN_SPRITE_1, true);
        runSprites[2] = imageReader.readImage(Constants.RUN_SPRITE_2, true);
        runSprites[3] = imageReader.readImage(Constants.RUN_SPRITE_3, true);
        runSprites[4] = imageReader.readImage(Constants.RUN_SPRITE_4, true);
        runSprites[5] = imageReader.readImage(Constants.RUN_SPRITE_5, true);
    }

    /**
     * Initializes the avatar animations by creating animation renderables for each animation state.
     */
    private void initializeAvatarAnimations() {
        Renderable idleAnimation = new AnimationRenderable(idleSprites, Constants.ANIMATION_DURATION);
        Renderable jumpAnimation = new AnimationRenderable(jumpSprites, Constants.ANIMATION_DURATION);
        Renderable runAnimation = new AnimationRenderable(runSprites, Constants.ANIMATION_DURATION);

        animationMap.put(Constants.AVATAR_ANIMATION_IDLE, idleAnimation);
        animationMap.put(Constants.AVATAR_ANIMATION_JUMP, jumpAnimation);
        animationMap.put(Constants.AVATAR_ANIMATION_RUNNING_LEFT, runAnimation);
        animationMap.put(Constants.AVATAR_ANIMATION_RUNNING_RIGHT, runAnimation);
    }
}