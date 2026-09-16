"""The art, as code: the steel ingot and nugget icons, the block's texture, and their model JSON.

Run from the repository root:

    uv run --no-project python devtools/art/build.py

Everything it writes lands under src/main/resources/assets/metalsandmaterials/
and is committed; this script is the source of truth for those files. The
ingot and nugget use original bevelled pixel art in a cool steel palette. All
original work; nothing is derived from another mod's assets.
"""

from __future__ import annotations

import json
import struct
import zlib
from pathlib import Path

type RGB = tuple[int, int, int]
type Pixel = tuple[int, int, int, int]
type Raster = tuple[tuple[Pixel, ...], ...]
type JSON = str | dict[str, JSON]

ROOT = Path(__file__).resolve().parents[2]
MODID = "metalsandmaterials"
ASSETS = ROOT / "src/main/resources/assets" / MODID


# ---------------------------------------------------------------- PNG writing


def write_png(path: Path, width: int, height: int, pixels: Raster) -> None:
    """requires: dimensions match pixels. effects: writes deterministic RGBA PNG. throws: OSError on write failure."""
    if len(pixels) != height or any(len(row) != width for row in pixels):
        raise ValueError("PNG dimensions do not match the pixels")
    raw = b"".join(b"\x00" + b"".join(bytes(p) for p in row) for row in pixels)

    def chunk(kind: bytes, data: bytes) -> bytes:
        return (
            struct.pack(">I", len(data))
            + kind
            + data
            + struct.pack(">I", zlib.crc32(kind + data) & 0xFFFFFFFF)
        )

    png = (
        b"\x89PNG\r\n\x1a\n"
        + chunk(b"IHDR", struct.pack(">IIBBBBB", width, height, 8, 6, 0, 0, 0))
        + chunk(b"IDAT", zlib.compress(raw, 9))
        + chunk(b"IEND", b"")
    )
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_bytes(png)


class Noise:
    """Deterministic grain. AF: state is the last unsigned LCG value.

    RI: 0 <= state <= 2**32 - 1. Mutation is confined to this local generator.
    """

    def __init__(self, seed: int) -> None:
        """effects: starts the generator at the unsigned seed."""
        self.state = seed & 0xFFFFFFFF

    def next(self) -> float:
        """effects: advances state and returns its normalized value in [0, 1]."""
        self.state = (1664525 * self.state + 1013904223) & 0xFFFFFFFF
        return self.state / 0xFFFFFFFF


def shade(rgb: RGB, delta: int) -> RGB:
    """effects: offsets each channel, clamped to byte range."""
    return (
        max(0, min(255, rgb[0] + delta)),
        max(0, min(255, rgb[1] + delta)),
        max(0, min(255, rgb[2] + delta)),
    )


# Steel: cooler and darker than iron.
LIGHT, MID, DARK = (150, 158, 170), (104, 112, 124), (58, 64, 74)


def sprite(rows: tuple[str, ...]) -> Raster:
    """requires: sixteen rows of sixteen palette symbols.

    effects: expands original pixel art to immutable RGBA. throws: ValueError for malformed art.
    """
    palette: dict[str, Pixel] = {
        ".": (0, 0, 0, 0),
        "O": (39, 46, 58, 255),
        "D": (65, 75, 89, 255),
        "M": (98, 110, 126, 255),
        "L": (130, 145, 160, 255),
        "H": (182, 196, 207, 255),
    }
    if len(rows) != 16 or any(
        len(row) != 16 or any(c not in palette for c in row) for row in rows
    ):
        raise ValueError("Malformed steel sprite")
    return tuple(tuple(palette[c] for c in row) for row in rows)


def steel_ingot_icon() -> Raster:
    """effects: returns the bevelled steel ingot, with separate top, front and end faces."""
    return sprite(
        (
            "................",
            "................",
            "................",
            "................",
            ".....OOOOOOOO...",
            "....OHHHHHHHHO..",
            "...OHLLLLLLLLO..",
            "..OHLLLLLLLMMO..",
            ".OHLLLLLMMMMO...",
            ".OMMMMMMMMDDO...",
            "..ODDDDDDDDO....",
            "...OOOOOOOO.....",
            "................",
            "................",
            "................",
            "................",
        )
    )


def steel_nugget_icon() -> Raster:
    """effects: returns a compact irregular steel offcut sharing the ingot's light direction."""
    return sprite(
        (
            "................",
            "................",
            "................",
            "................",
            ".......OO.......",
            ".....OOHLO......",
            "....OHLLLMO.....",
            "....OLLMMDDO....",
            "....OMMMMDDO....",
            ".....OMDDDO.....",
            "......OOOO......",
            "................",
            "................",
            "................",
            "................",
            "................",
        )
    )


def steel_block_texture() -> Raster:
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
    for rx, ry in ((3, 3), (12, 3), (3, 12), (12, 12)):
        px[ry][rx] = (*LIGHT, 255)
        px[ry + 1][rx + 1] = (*DARK, 255)
    return tuple(tuple(row) for row in px)


# ------------------------------------------------------------------ the JSON


def write_json(path: Path, data: JSON) -> None:
    """effects: writes generated model JSON; throws: OSError on write failure."""
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(data, indent=2) + "\n", encoding="utf-8")


def main() -> None:
    """effects: regenerates the steel art and model files. throws: OSError on write failure."""
    write_png(ASSETS / "textures/item/steel_ingot.png", 16, 16, steel_ingot_icon())
    write_png(ASSETS / "textures/item/steel_nugget.png", 16, 16, steel_nugget_icon())
    write_png(ASSETS / "textures/block/steel_block.png", 16, 16, steel_block_texture())
    for name in ("steel_ingot", "steel_nugget"):
        write_json(
            ASSETS / f"models/item/{name}.json",
            {
                "parent": "minecraft:item/generated",
                "textures": {"layer0": f"{MODID}:item/{name}"},
            },
        )
    write_json(
        ASSETS / "models/block/steel_block.json",
        {
            "parent": "minecraft:block/cube_all",
            "textures": {"all": f"{MODID}:block/steel_block"},
        },
    )
    write_json(
        ASSETS / "models/item/steel_block.json",
        {"parent": f"{MODID}:block/steel_block"},
    )
    write_json(
        ASSETS / "blockstates/steel_block.json",
        {"variants": {"": {"model": f"{MODID}:block/steel_block"}}},
    )
    print("wrote the steel ingot, nugget and block")


if __name__ == "__main__":
    main()
