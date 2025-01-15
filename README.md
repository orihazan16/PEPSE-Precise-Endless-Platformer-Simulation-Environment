# PEPSE – Precise Endless Platformer Simulation Environment

A 2D side-scrolling world simulator built in Java on top of the [DanoGameLab](https://github.com/danoble/DanoGameLab) (`danogl`) engine: an infinite, procedurally generated terrain with a day/night cycle, trees that bear fruit, rain-dropping clouds, and a playable, animated avatar with an energy system.



## Features

- **Infinite procedural terrain** generated on the fly from Perlin-like noise, so the ground height varies smoothly and endlessly in both directions.
- **Chunk-based streaming world**: the world is split into chunks around the avatar; chunks that scroll out of range are unloaded and new ones are generated as the avatar moves, so memory use stays bounded regardless of how far the player travels.
- **Day/night cycle**: a sun that arcs across the sky with a soft halo, and a night overlay that fades in and out on a configurable cycle length.
- **Procedural flora**: trees are scattered probabilistically along the terrain, each with a randomly sized trunk, swaying leaves (animated via a periodic angle/scale transition), and fruit.
- **Avatar**: sprite-animated (idle / run / jump), responds to arrow keys and space, obeys gravity and collisions, and manages an **energy** resource that is spent on moving and jumping and replenished by eating fruit (fruit respawns after a day/night cycle).
- **Weather**: clouds drift across the screen, and jumping triggers a chance of rain, with raindrops that fall and fade out.
- **Camera** follows the avatar as it scrolls through the endless world.

## Project Structure

```
.
├── pepse/
│   ├── Constants.java              # All tunable constants (sizes, colors, energy costs, layers, …)
│   ├── PepseGameManager.java       # Entry point: wires up the world and drives chunk streaming
│   ├── util/
│   │   ├── NoiseGenerator.java     # Perlin-style noise used for terrain height
│   │   └── ColorSupplier.java      # Slight per-object color variation for a more natural look
│   └── world/
│       ├── Block.java              # Basic fixed-size world tile
│       ├── Terrain.java            # Procedural ground height + block generation
│       ├── Sky.java / Cloud.java   # Background sky and drifting, rain-generating clouds
│       ├── EnergyDisplay.java      # On-screen avatar energy readout
│       ├── Avatar.java             # Player-controlled character: movement, animation, energy, collisions
│       ├── avatars/
│       │   └── AvatarJumpListener.java  # Observer interface notified when the avatar jumps
│       ├── clouds/
│       │   └── Raindrop.java       # Falling, fading raindrop
│       ├── daynight/
│       │   ├── Sun.java            # Sun orbit animation
│       │   ├── SunHalo.java        # Halo that follows the sun
│       │   └── Night.java          # Day/night opacity overlay
│       └── trees/
│           ├── Flora.java          # Probabilistic tree placement along the terrain
│           ├── Tree.java           # Builds a trunk + leaves + fruit for one tree
│           ├── Trunk.java          # Tree trunk block
│           ├── Leaf.java           # Swaying leaf (angle + scale transition)
│           └── Fruit.java          # Collectible fruit (restores avatar energy)
└── assets/                          # Avatar sprite PNGs (idle/run/jump)
```

## Architecture Notes

- **Procedural, seeded generation**: both terrain height (`NoiseGenerator`) and tree placement (`Flora`) are deterministic given a seed, so the same world layout regenerates consistently as chunks are unloaded and reloaded.
- **Chunked world streaming**: `PepseGameManager` tracks the avatar's current chunk and keeps a fixed window of chunks (`Constants.NUM_OF_CHUNKS` on each side) loaded, creating and destroying ground/flora game objects as the avatar crosses chunk boundaries. This keeps the number of active game objects bounded in a world with no edges.
- **Observer pattern for rain**: `Cloud` implements `AvatarJumpListener`; the avatar notifies all registered listeners on every jump, decoupling "the avatar jumped" from "it started raining."
- **Declarative animation via `Transition`**: the sun's orbit, the night's opacity fade, and the leaves' swinging angle/scale are all expressed as looping `Transition` objects rather than manual per-frame updates.
- **Energy as the core gameplay loop**: moving and jumping cost energy, idling slowly regenerates it, and fruit restores it — actions are disabled once energy runs out, so avatar state, animation, and the UI energy display are all driven from the same value.

## Requirements

This project depends on the **DanoGameLab** (`danogl`) engine, provided as course material and not included in this repository. Add `danogl.jar` to the classpath/module path to build and run.

## Running the Game

```bash
javac -cp danogl.jar -d out $(find pepse -name "*.java")
java -cp out:danogl.jar:. pepse.PepseGameManager
```

(On Windows, replace `:` with `;` in the classpath.) Run the command from the project root so the `assets/` directory is found via its relative path.

**Controls:** Left/Right arrows to move, Space to jump. Watch the energy meter — once it runs out, the avatar can only idle until it regenerates or a fruit is eaten.

## Tech Stack

Java

