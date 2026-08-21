# Agent guide for BlueMap Ender IO Add-on

Read `/root/work/allthemons/AGENTS.md` and this file before changing this
standalone repository. This is a plain BlueMap add-on, not a NeoForge mod.

## Exact baseline

| Component | Identity |
| --- | --- |
| All the Mons | `1.2.0`, commit `c7bb230f21d14d26859d0b92548f089b3a493ad9` |
| Minecraft / NeoForge / Java | `1.21.1` / `21.1.248` / `21` |
| BlueMap | `5.22-agent.backport-5.22-mc1.21.1-2`, commit `9be321df995a1103808621d529eb72773e719d4d` |
| Ender IO | `enderio-8.2.11-beta.jar`, 6,592,813 bytes, SHA-256 `e01af48907781f2d5ccdfa8d71975b611c33f295be11b7021cb91be06ce8070c` |

## Prototype boundary

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

## Minimum gate

```bash
../bluemap-backport/gradlew --no-daemon \
  -PenderIoJar=/absolute/path/enderio-8.2.11-beta.jar \
  clean check build
```

This gate proves only a loadable staging candidate. Owner visual acceptance,
release, publication and production deployment remain separate decisions.
