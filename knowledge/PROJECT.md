---
title: Metals and Materials — project
type: overview
layer: store
tags: [overview]
---

# Metals and Materials

## What this is

A NeoForge 1.21.1 mod that is the pack's steel: an ingot, a nugget and a block, with the
`c:` tags every recipe takes. Nested Jar-in-Jar by the mods that use steel.

## Why it exists

Steel was an item of the Ranged Weapons Mod, so a second mod wanting steel (Vanilla Wheels'
chassis and engines) would have had to depend on a gun mod or mint a second steel. One
materials mod, nested by both, is the fix; the gun mod's recipes already took the tag.

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
