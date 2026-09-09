"""The art, as code: the steel ingot and nugget icons, the block's texture, and their model JSON.

Run from the repository root:

    uv run --no-project python devtools/art/build.py

Everything it writes lands under src/main/resources/assets/metalsandmaterials/
and is committed; this script is the source of truth for those files. The
ingot is the one the Ranged Weapons Mod drew for its own steel (the same
authors), so a player's steel looks the same the day it moves here. All
original work; nothing is derived from another mod's assets.
"""
from __future__ import annotations

import json
import struct
import zlib
from pathlib import Path

ROOT = Path(__file__).resolve().parents[2]
MODID = "metalsandmaterials"
ASSETS = ROOT / "src/main/resources/assets" / MODID


# ---------------------------------------------------------------- PNG writing

def write_png(path: Path, width: int, height: int, pixels) -> None:
    """pixels: rows of (r, g, b, a) tuples, top row first."""
    raw = b"".join(b"\x00" + b"".join(bytes(p) for p in row) for row in pixels)

    def chunk(kind: bytes, data: bytes) -> bytes:
        return (struct.pack(">I", len(data)) + kind + data
                + struct.pack(">I", zlib.crc32(kind + data) & 0xFFFFFFFF))

    png = (b"\x89PNG\r\n\x1a\n"
           + chunk(b"IHDR", struct.pack(">IIBBBBB", width, height, 8, 6, 0, 0, 0))
           + chunk(b"IDAT", zlib.compress(raw, 9))
           + chunk(b"IEND", b""))
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_bytes(png)


class Noise:
    """A deterministic grain so flat colours read as material, not plastic."""

    def __init__(self, seed: int) -> None:
        self.state = seed & 0xFFFFFFFF

    def next(self) -> float:
        self.state = (1664525 * self.state + 1013904223) & 0xFFFFFFFF
        return self.state / 0xFFFFFFFF


def shade(rgb, delta):
    return tuple(max(0, min(255, c + delta)) for c in rgb)


def _canvas():
    """A transparent 16x16 and a bounded plotter for it."""
    px = [[(0, 0, 0, 0) for _ in range(16)] for _ in range(16)]

    def put(x, y, c):
        if 0 <= x < 16 and 0 <= y < 16:
            px[y][x] = (*c, 255)

    return px, put


# Steel: cooler and darker than iron.
LIGHT, MID, DARK = (150, 158, 170), (104, 112, 124), (58, 64, 74)


def steel_ingot_icon():
    """An ingot in steel, lit along the top face."""
    px, put = _canvas()
    for y in range(5, 12):
        for x in range(2, 14):
            slant = (11 - y) // 2
            xx = x + slant
            put(xx, y, LIGHT if y == 5 else DARK if y == 11 or xx >= 13 + slant - 1 else MID)
    for x in range(3, 15):
        put(x, 5, LIGHT)
    return px


def steel_nugget_icon():
    """A lump the size of an iron nugget's, in the same three tones."""
    px, put = _canvas()
    lump = [
        "....LLL.....",
        "...LMMML....",
        "..LMMMMML...",
        "..LMMMMMD...",
        "..DMMMMDD...",
        "...DMDDD....",
        "....DDD.....",
    ]
    tones = {"L": LIGHT, "M": MID, "D": DARK}
    for row, line in enumerate(lump):
        for col, ch in enumerate(line):
            if ch != ".":
                put(col + 2, row + 5, tones[ch])
    return px


def steel_block_texture():
    """The iron block's layout -- a bevelled slab with a rivet in each corner -- in steel."""
    noise = Noise(0x5733)
    px = [[(0, 0, 0, 0) for _ in range(16)] for _ in range(16)]
    for y in range(16):
        for x in range(16):
            edge = x == 0 or y == 0 or x == 15 or y == 15
            inner = 1 <= x <= 14 and 1 <= y <= 14
            if edge:
                c = DARK
            elif x == 1 or y == 1:
                c = LIGHT
            elif x == 14 or y == 14:
                c = shade(DARK, 12)
            elif inner:
                c = MID
            else:
                c = MID
            grain = int((noise.next() - 0.5) * 10)
            px[y][x] = (*shade(c, grain), 255)
    for (rx, ry) in ((3, 3), (12, 3), (3, 12), (12, 12)):
        px[ry][rx] = (*LIGHT, 255)
        px[ry + 1][rx + 1] = (*DARK, 255)
    return px


# ------------------------------------------------------------------ the JSON

def write_json(path: Path, data) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(data, indent=2) + "\n", encoding="utf-8")


def main() -> None:
    write_png(ASSETS / "textures/item/steel_ingot.png", 16, 16, steel_ingot_icon())
    write_png(ASSETS / "textures/item/steel_nugget.png", 16, 16, steel_nugget_icon())
    write_png(ASSETS / "textures/block/steel_block.png", 16, 16, steel_block_texture())
    for name in ("steel_ingot", "steel_nugget"):
        write_json(ASSETS / f"models/item/{name}.json",
                   {"parent": "minecraft:item/generated", "textures": {"layer0": f"{MODID}:item/{name}"}})
    write_json(ASSETS / "models/block/steel_block.json",
               {"parent": "minecraft:block/cube_all", "textures": {"all": f"{MODID}:block/steel_block"}})
    write_json(ASSETS / "models/item/steel_block.json", {"parent": f"{MODID}:block/steel_block"})
    write_json(ASSETS / "blockstates/steel_block.json", {"variants": {"": {"model": f"{MODID}:block/steel_block"}}})
    print("wrote the steel ingot, nugget and block")


if __name__ == "__main__":
    main()
