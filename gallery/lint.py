#!/usr/bin/env python3
# SPDX-License-Identifier: MIT
"""Lint the bounded Ender IO staging gallery without starting Minecraft."""

from __future__ import annotations

from collections import Counter
import json
from pathlib import Path
import re


ROOT = Path(__file__).resolve().parent
FUNCTIONS = ROOT / "datapack/data/enderio_gallery/function"


def fail(message: str) -> None:
    raise ValueError(message)


def main() -> int:
    json.loads((ROOT / "datapack/pack.mcmeta").read_text(encoding="utf-8"))
    json.loads(
        (ROOT / "datapack/data/minecraft/tags/function/load.json").read_text(
            encoding="utf-8"
        )
    )

    build = (FUNCTIONS / "build.mcfunction").read_text(encoding="utf-8")
    verify = (FUNCTIONS / "verify.mcfunction").read_text(encoding="utf-8")
    clear = (FUNCTIONS / "clear.mcfunction").read_text(encoding="utf-8")
    all_functions = "\n".join(
        path.read_text(encoding="utf-8")
        for path in sorted(FUNCTIONS.glob("*.mcfunction"))
    )

    placements = re.findall(r"^setblock (\d+) (\d+) (\d+) (\S+)$", build, re.MULTILINE)
    if len(placements) != 6:
        fail("gallery must place exactly six target blocks")
    coordinates = [(int(x), int(y), int(z)) for x, y, z, _ in placements]
    if len(set(coordinates)) != 6:
        fail("target coordinates must be unique")
    if any(not (160 <= x <= 179 and 99 <= y <= 103 and 160 <= z <= 179)
           for x, y, z in coordinates):
        fail("target escaped the bounded gallery envelope")

    block_ids = Counter(
        state.split("{", 1)[0].split("[", 1)[0]
        for _, _, _, state in placements
    )
    if block_ids != Counter({
        "enderio:painted_redstone_block": 3,
        "minecraft:stone": 1,
        "minecraft:bricks": 1,
        "minecraft:oak_planks": 1,
    }):
        fail(f"unexpected block census: {block_ids}")

    paints = re.findall(r'Paint:"([a-z0-9_./:-]+)"', build)
    if paints != [
        "minecraft:stone",
        "minecraft:bricks",
        "minecraft:oak_planks",
    ]:
        fail(f"unexpected persisted paint sequence: {paints}")
    if build.count("scoreboard players add #builds enderio_gallery 1") != 1:
        fail("build must increment the persistent counter exactly once")
    if len(re.findall(
        r"^scoreboard players add #checked enderio_gallery 1$",
        verify,
        re.MULTILINE,
    )) != 10:
        fail("verify must contain exactly ten checks")
    if clear.strip().splitlines()[-1] != (
        "fill 160 99 160 179 103 179 minecraft:air"
    ):
        fail("clear must cover exactly the bounded envelope")

    lowered = all_functions.lower()
    for token in (
        "paint2",
        "conduit",
        "facade",
        "painted_stairs",
        "painted_slab",
        "athena",
        "summon ",
        "data merge",
    ):
        if token in lowered:
            fail(f"excluded prototype path present: {token}")
    if build.count("function enderio_gallery:verify_immediate") != 1:
        fail("build must run one immediate check")
    if build.count("schedule function enderio_gallery:verify_20t 20t replace") != 1:
        fail("build must schedule one retained 20-tick check")

    print("Ender IO gallery lint passed: 6 placements, 10 checks/phase")
    return 0


if __name__ == "__main__":
    try:
        raise SystemExit(main())
    except ValueError as error:
        raise SystemExit(f"lint failed: {error}") from error
