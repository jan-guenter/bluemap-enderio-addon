# Agent guide for BlueMap Ender IO Add-on

Read `/root/work/allthemons/AGENTS.md` and this file before changing this
standalone repository. This is a plain BlueMap add-on, not a NeoForge mod.

## Exact baseline

| Component | Identity |
| --- | --- |
| All the Mons | `1.2.0`, commit `c7bb230f21d14d26859d0b92548f089b3a493ad9` |
| Minecraft / NeoForge / Java | `1.21.1` / `21.1.248` / `21` |
| BlueMap | `5.22-feature.backport-5.23-stateless-java-web-server-46`, commit `7e07f4e74ec1e92a6ead9aa1e66054af3e133aac` |
| Adapter API | `0.1.0-alpha.2`, commit `e81f08bc4bfbf02d810ec8949a019130e2e61634`, source tree `2f974c9bb2ba13888d69682f86f30f58922d30eb` |
| Ender IO | `enderio-8.2.11-beta.jar`, 6,592,813 bytes, SHA-256 `e01af48907781f2d5ccdfa8d71975b611c33f295be11b7021cb91be06ce8070c` |

## Release boundary

- Own only `enderio:painted_redstone_block` with block entity
  `enderio:single_painted`.
- Read only the exact top-level persisted string `Paint`.
- Accept only a non-Ender-IO, propertyless, deterministic ordinary resource
  model made of one opaque, static, unrotated full-cube element with six
  untinted faces. Render that exact model so its face textures and UVs survive.
- Missing, malformed, recursive, transformed, weighted, tinted, animated,
  translucent, multipart, non-cube, or unavailable targets fall back to the
  whole stock Ender IO host atomically.
- Conduits, facades, the other painted shapes, `Paint2`, Athena, machines,
  fluids, contents, activity and animation are excluded.
- Package no Ender IO asset, class, source, binary, or derived mesh.
- Compile the four pinned Adapter-API Java sources directly into this add-on.
  Never install or nest the standalone module JAR.

## Release gate

```bash
git submodule update --init --recursive -- \
  tooling/bluemap-addon-toolkit modules/bluemap-addon-adapter-api
gradle --no-daemon \
  -PbluemapSourcePath=/path/to/exact/feature-backport \
  -PenderIoJar=/absolute/path/enderio-8.2.11-beta.jar \
  -PreleaseTag=v0.1.0-alpha.3 \
  clean check build generatePomFileForAddonPublication \
  generateMetadataFileForAddonPublication verifyPublicationArtifacts \
  verifyReleaseCandidate
```

The release records the owner's completed visual acceptance separately from
the reproducible artifact gate. Publication never deploys to a Minecraft
server; production deployment remains a separate decision.
