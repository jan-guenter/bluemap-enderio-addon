# BlueMap Ender IO Add-on

A narrow Java 21 add-on for the exact BlueMap 5.23 feature backport and
Ender IO's persisted painted
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

Version `0.1.0-alpha.2` compiles the pinned Adapter-API source module's four
Java files into the add-on. They provide the exact feature-backport runtime
identity check, registry guards, resource-extension factory, and synthetic
dispatch validation. The standalone module JAR is not installed or nested.
The gitlink pins `v0.1.0-alpha.2` commit
`e81f08bc4bfbf02d810ec8949a019130e2e61634`
and Java source tree `2f974c9bb2ba13888d69682f86f30f58922d30eb`.
The settings preflight rejects an uninitialized, changed, or dirty checkout.

## Build

```bash
git submodule update --init --recursive -- \
  tooling/bluemap-addon-toolkit modules/bluemap-addon-adapter-api
gradle --no-daemon \
  -PbluemapSourcePath=/path/to/exact/feature-backport \
  -PenderIoJar=/absolute/path/enderio-8.2.11-beta.jar \
  clean check build
```

The output JAR belongs in BlueMap's `packs` directory. Removing it and
restarting restores stock rendering without changing world data. Release
identity and verification are recorded in `provenance/release.json`.
