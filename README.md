# BlueMap Ender IO Add-on

A narrow Java 21 BlueMap 5.22 add-on for Ender IO's persisted painted
redstone block.

The exact All the Mons 1.2.0 profile activates only for
`enderio-8.2.11-beta.jar` (6,592,813 bytes, SHA-256
`e01af48907781f2d5ccdfa8d71975b611c33f295be11b7021cb91be06ce8070c`).
It owns only `enderio:painted_redstone_block`, reads the block entity's exact
top-level `Paint` string, and reuses an admitted target block's six installed
face materials and UVs.

Targets must be nonrecursive, propertyless, ordinary, deterministic, static,
opaque, untinted canonical full cubes. Any malformed or unsupported state
uses an atomic whole-block stock fallback. Ender IO remains operator-installed;
this JAR packages none of its assets, classes, source, binaries, or meshes.
For a bounded propertyless variant family, every member must pass the same
proof and BlueMap's position-stable variant selection is retained.

Conduits, conduit facades, all other painted shapes, double-slab `Paint2`,
Athena models, machines, fluids, contents, activity and animation are outside
this first release.

## Build

```bash
gradle --no-daemon \
  -PbluemapSourcePath=../bluemap-backport \
  -PenderIoJar=/absolute/path/enderio-8.2.11-beta.jar \
  clean check build
```

The output JAR belongs in BlueMap's `packs` directory. Removing it and
restarting restores stock rendering without changing world data. Release
identity and verification are recorded in `provenance/release.json`.
