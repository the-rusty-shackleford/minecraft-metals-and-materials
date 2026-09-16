# Metals and Materials

The pack's one steel: an ingot, a nugget and a block, registered once and tagged the
common way so any mod's recipe can take it without naming this mod.

- **Steel Ingot** -- three iron ingots and a piece of coal (anything in `#minecraft:coals`)
  make three. Tagged `c:ingots/steel`, and `c:ingots` includes it.
- **Steel Nugget** -- an ingot makes nine; nine make an ingot. Tagged `c:nuggets/steel`.
- **Block of Steel** -- nine ingots; back to nine. Mined with a stone pickaxe or better,
  drops itself. Tagged `c:storage_blocks/steel` as an item and as a block.

They sit right after iron's ingot, nugget and block in the Ingredients and Building Blocks
tabs.

The held 1.0.1 artwork gives the ingot and nugget distinct bevelled faces and a cool
steel palette at the existing 16-pixel resolution. The block and crafting behavior
are unchanged. The generator is deterministic and passes `mypy --strict`.

## For other mods

Take `#c:ingots/steel` (or `#c:nuggets/steel`, `#c:storage_blocks/steel`) in recipes and
nest this jar Jar-in-Jar with a range such as `[1.0,2.0)`; the loader keeps one copy across
every mod that carries it. Until a Maven repository exists, `./gradlew publishToMavenLocal`
publishes `com.chunkworks.metalsandmaterials:metalsandmaterials` for consumers to build
against. The Ranged Weapons Mod is the first consumer: its steel moved here in its 2.3.0,
and it aliases its old item id to this one so nobody's ingots vanished.

## Layout

There is no pure layer: this mod decides nothing, it declares. `src/main` holds the two
registrations (`ModContent`) and the creative-tab placement (`MetalsAndMaterials`); the
recipes, tags, loot table and recipe-book unlocks are JSON under
`src/main/resources/data`. `src/gametest` is a mod of its own that asserts, on a real server,
that each grid finds exactly its recipe, the tags name every piece, the block drops for the
right tool, and the tab order holds. `devtools/art/build.py` draws the three textures and
writes the model JSON; run it from the repository root with
`uv run --no-project python devtools/art/build.py` and commit the output.

## Building

```
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 PATH="$JAVA_HOME/bin:$PATH"
./gradlew check            # the gametest server; ends "All N required tests passed"
./gradlew check -PskipGameTests   # compile only
```

## Licence

AGPL-3.0-or-later. Copyright 2026 Rusty Shackleford and nfx.
