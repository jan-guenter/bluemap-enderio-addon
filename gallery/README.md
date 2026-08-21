# Ender IO staging gallery

This deliberately small datapack reviews the exact Ender IO `8.2.11-beta`
painted-redstone prototype. It places three supported painted full cubes beside
matching stock controls, one malformed host with no `Paint`, and one host whose
persisted paint targets the deliberately unsupported `minecraft:oak_stairs`.

The three supported cells are placed directly in Ender IO's natural persisted
block-entity form: a top-level `Paint` string on
`enderio:painted_redstone_block`. The malformed and unsupported cells must
remain whole-block stock fallbacks. The fixture stays within x/z `160..179`,
y `99..103` in a disposable staging world.

Run:

```text
PYTHONDONTWRITEBYTECODE=1 python3 gallery/lint.py
bash gallery/package.sh /tmp/bluemap-enderio-gallery.zip
```

Install the ZIP as a world datapack, then invoke:

```text
/function enderio_gallery:build
/function enderio_gallery:verify
/function enderio_gallery:clear
/function enderio_gallery:release
```

Each immediate and 20-tick phase requires 15 checked and zero failures: eight
block states, six block-entity facts, and one single-build counter. The gallery
bundles no Ender IO or BlueMap code or assets. Its deterministic archive is
2,941 bytes with SHA-256
`1379ec5a4f16d017a8304f57771370e02d572dea36293a3ae1756da67c8101e1`.
