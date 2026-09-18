# Separately installed materials — 2026-09-18

Local, unreleased versions: Metals and Materials 1.0.3, Vanilla Wheels 1.7.2,
Ranged Weapons Mod 2.4.1, Trailblazer 1.7.1, Trailer 2.3.1 and Farmer's Pickup 1.3.1.
Metals and Materials D-0003 supersedes only the former nested-packaging choice.

## Verification

| Module | JUnit | Real-server GameTests |
| --- | ---: | ---: |
| Metals and Materials | 0 (data/registration only) | 7 |
| Vanilla Wheels | 83 | 36 |
| Ranged Weapons Mod | 120 | 44 |
| Trailblazer | 0 (data-only) | 11 |
| Trailer | 0 (data-only) | 5 |
| Farmer's Pickup | 0 (data-only) | 7 |

Every build and listed test passed. Materials was published only to Maven Local,
followed by Vanilla Wheels and Trailblazer for dependent builds. Consumer builds
used `build -PskipBooth`; source code, assets and data are unchanged, and actual
full-pack client/server startup establishes the changed packaging's runtime path.
This is not a new driving/vehicle-art validation claim.

- A recursive [jar audit](separate-dependency-jars.json) found **no embedded
  Metals and Materials jar** in any of the five consumers, including nested
  Vanilla Wheels inside each vehicle add-on. Other bundled libraries remain.
- The six materials-related jars' class, asset and data entries are byte-identical
  to their previously deployed counterparts. See the [payload comparison](separate-dependency-payloads.json).
  Item IDs, steel recipes/tags, old gun-mod aliases, vehicle models and profiles remain.
- With the sole materials jar removed from the isolated server, the actual loader
  refused startup and named `vanillawheels` and `rangedweaponsmod` as requiring
  `metalsandmaterials` in `[1.0,2.0)`. The Gradle run task returned zero despite the
  loader refusal; the negative check used the actual loader error, not the exit code.
- The materials jar was restored. The complete client and dedicated-server pack
  started with all six expected versions, plus Backpacks+ 0.2.0 and Quick Slot 0.1.1.
  Native backpack interactions and H passed with Iris/Complementary active.

Host file-watcher exhaustion affected early test starts. Disabling NeoForge's
automatic config watcher only in disposable fixtures and waiting for resources to
free allowed the final complete-pack check. All pack mods were present in that final
check. No live pack, server or personal Prism instance was changed.

Publication and deployment remain held. Mod Hub must retain its explicit materials
entry and update all consumers together; Rusty pulls the client update into Prism.
Scratch evidence: `/tmp/codex-mounts-materials-20260918/`.
