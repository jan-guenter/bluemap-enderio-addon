# Ender IO staging gallery

This deliberately small datapack reviews the exact Ender IO `8.2.11-beta`
painted-redstone prototype. It places three supported painted full cubes beside
matching stock controls.

The three supported cells are placed directly in Ender IO's natural persisted
block-entity form: a top-level `Paint` string on
`enderio:painted_redstone_block`. Malformed or command-injected unsupported
snapshots remain covered by automated fallback tests instead of owner-facing
visual cells. The fixture stays within x/z `160..179`, y `99..103` in a
disposable staging world.

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

Each immediate and 20-tick phase requires 10 checked and zero failures: six
block states, three block-entity facts, and one single-build counter. The
gallery bundles no Ender IO or BlueMap code or assets.
Its deterministic archive is 2,732 bytes with SHA-256
`02e7cfe845645197911d3bf7e15334c4771b81d8450b0db7beda40abaac3d2e6`.
