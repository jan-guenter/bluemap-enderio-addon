# Changelog

## 0.1.0-alpha.3 - 2026-09-02

- Preserve painted target eligibility when another installed add-on wraps an
  originally default BlueMap block-state renderer.
- Refresh BlueNBT's shared resolver caches after registering the Ender IO
  block-entity projection and verify that the persisted `Paint` field survives.
- Add bounded fallback diagnostics and combined-pack regression tests.

## 0.1.0-alpha.2 - 2026-08-30

- Move the internal adapter boundary to the exact BlueMap 5.23 feature
  backport at commit `7e07f4e74ec1e92a6ead9aa1e66054af3e133aac`.
- Replace private runtime compatibility, registry, extension-type, and
  synthetic-dispatch helpers with four pinned Adapter-API sources compiled
  into this add-on.
- Keep the exact Ender IO profile, one block-entity registration, admission,
  rendering, diagnostics, and stock-fallback behavior unchanged.

## 0.1.0-alpha.1 - 2026-08-21

- Add an exact-artifact BlueMap renderer for Ender IO's painted redstone block.
- Read only the persisted top-level `Paint` resource ID and reuse admitted
  propertyless opaque full-cube face materials and UVs.
- Preserve BlueMap's position-stable model-variant selection.
- Fall back atomically for malformed, recursive, tinted, translucent,
  transformed, animated, weighted, multipart, or non-cube targets.
- Add focused Java 21 tests, exact artifact and resource validation, a
  deterministic six-cell staging gallery, and release-byte gates.
