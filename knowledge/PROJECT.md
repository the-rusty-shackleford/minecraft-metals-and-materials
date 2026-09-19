---
title: Metals and Materials — project
type: overview
layer: store
tags: [overview]
---

# Metals and Materials

## What this is

A NeoForge 1.21.1 mod that is the pack's steel: an ingot, a nugget and a block, with the
`c:` tags every recipe takes. Installed separately and required by the mods that use steel (D-0003).

## Why it exists

Steel was an item of the Ranged Weapons Mod, so a second mod wanting steel (Vanilla Wheels'
chassis and engines) would have had to depend on a gun mod or mint a second steel. One
materials mod, required by both, is the fix; the gun mod's recipes already took the tag.

## Shape

`ModContent` registers the three things; everything else is data. No `domain` source set:
there is no decision here to test in isolation, so the gametests are the tests.

## How it is verified

`./gradlew check`: seven gametests on a headless server -- each of the five recipes found by
exactly its grid, a near miss finding nothing of ours, the specific and parent tags, the
block's tool and drop, the creative-tab order.

## Decisions

D-0001: one materials mod, nested by its consumers; the old gun-mod id is aliased by the gun
mod.

## Next

Ships as 1.0.0 (2026-09-09) with Ranged Weapons Mod 2.3.0 in one pack update. Later metals
(if any) join here rather than in the mod that first needs them.

2026-09-16 presentation polish: Held 1.0.1: D-0002 redraws the ingot/nugget with bevelled faces and strictly types
the deterministic generator. Seven real-server checks still pass; recipes, tags,
block appearance and IDs are unchanged. Release remains held.


## Release approval - 2026-09-16

Rusty approved the final review, completing their earlier conditional release go.
Version 1.0.1 was published on 2026-09-16 and deployed in pack 1.35.1
after the clean release build and asset verification. The deployed server matched
the published pack and ran at 20 TPS. This supersedes the earlier release holds
and pending presentation/listening review recorded above.


## Dedicated Creative tabs — 2026-09-18, unreleased

Rusty requested a separate Creative inventory page for each item-adding mod, then
explicitly chose to group all vehicles in Vanilla Wheels.
The dedicated Metals and Materials Creative tab groups the steel ingot, nugget and
block, alongside their existing vanilla categories and search.
No release or deployment is authorized by this follow-up.
Validation: 7 real-server GameTests and native full-pack Creative tab navigation/
item pickup passed; see [evidence](../devtools/verification/creative-tab.md).

## Release authorization — 2026-09-18

Rusty explicitly requested deployment: "Deploy it! I wanna play with it".
Version 1.0.2 is approved for publication and deployment in pack 1.39.1,
superseding the Creative-tab release hold above. Existing gameplay and world data
are preserved. Clean release builds and pack/hash verification gate deployment.

## Published release — 2026-09-18

Version 1.0.2 is published at
[GitHub Releases](https://github.com/the-rusty-shackleford/minecraft-metals-and-materials/releases/tag/v1.0.2)
and deployed to the server and Prism client in **pack 1.39.2**.
Clean release builds and real-server checks passed; the full-pack client verified
Creative tabs and item pickup. The downloaded release jar exactly matched the build.
The live server loaded the correct version and matched the published pack at 20 TPS.

Metals and Materials 1.0.2 is explicitly included in the pack: the first 1.39.1 startup
selected an older nested copy despite the updated Vanilla Wheels bundle. The 1.39.2
correction matches the directly installed materials jar used in full-pack testing.
Final startup verified all four updated mod versions; world, operators and DH settings
were preserved. This supersedes the historical release holds above.

## Shared dependency packaging — 2026-09-18, unreleased

D-0003 supersedes the nested packaging choice. Version 1.0.3 updates installation
guidance in mod metadata; item IDs, assets and gameplay are unchanged. Consumers
require a separately installed copy. Unit/server checks, recursive jar/payload audits and complete-pack startup passed; release is held.

Validation: see Metals and Materials `devtools/verification/separate-dependency.md`;
all six packaging builds and the complete-pack client/server check passed.

## Release authorization — 2026-09-19

Rusty explicitly requested: "Deploy it all so I can test that stuff."
Version 1.0.3 is authorized for public source/jar publication and deployment
in pack 1.47.0, superseding the earlier local-review and dependency-packaging holds.
Clean release builds, exact jar checks, staged pack comparison and an empty-server
restart gate deployment. The other vehicle mods are updated together for protocol 5.

## Published and deployed — 2026-09-19

Rusty explicitly approved public publication for all five coordinated releases.
Version 1.0.3 is published on GitHub and deployed in pack 1.47.0.
Clean release checks passed: 88 shared domain tests, 81 real-server GameTests
across the release set, and all four shader client booths. Downloaded release
assets match the tested builds; installed server jars match those assets.
All five loaded versions were confirmed after an empty-server restart; Mod Hub
reports pack/server parity and RCON measured 20 TPS. The client and server packs
change only the five mod downloads and version label; shared preferences are
preserved. Rusty imports the client update in Prism for multiplayer playtesting.
The earlier release holds above are superseded.
