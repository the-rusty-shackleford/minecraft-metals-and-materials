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
